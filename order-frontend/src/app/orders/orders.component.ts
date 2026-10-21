import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { Order, Product, Stats, STATUS_CLASS } from '../core/models';
import { OrderService } from '../core/order.service';
import { ProductService } from '../core/product.service';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [CommonModule, FormsModule, MatCardModule, MatFormFieldModule, MatInputModule,
            MatSelectModule, MatButtonModule, MatIconModule, MatTableModule, MatSnackBarModule],
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit {

  products: Product[] = [];
  orders: Order[] = [];
  stats: Stats = { orderCount: 0, revenue: 0 };

  displayedColumns: string[] = ['id', 'customer', 'payment', 'discount', 'tax',
                                'shipping', 'fee', 'total', 'status', 'actions'];

  order: Order = this.emptyOrder();

  constructor(
    private orderService: OrderService,
    private productService: ProductService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.refresh();
  }

  refresh(): void {
    this.productService.getAll().subscribe(data => this.products = data);
    this.orderService.getAll().subscribe(data => this.orders = data);
    this.orderService.getStats().subscribe(data => this.stats = data);
  }

  addItem(): void {
    this.order.items.push({ productId: null, quantity: 1 });
  }

  removeItem(index: number): void {
    this.order.items.splice(index, 1);
  }

  submit(): void {
    this.orderService.create(this.order).subscribe({
      next: () => {
        this.snackBar.open('Commande créée', 'OK', { duration: 3000 });
        this.order.items = [{ productId: null, quantity: 1 }];
        this.refresh();
      },
      error: err => this.snackBar.open(err.error?.message || 'Erreur inconnue', 'OK', { duration: 5000 })
    });
  }

  cancel(id: number): void {
    this.orderService.cancel(id).subscribe({
      next: () => {
        this.snackBar.open('Commande annulée', 'OK', { duration: 3000 });
        this.refresh();
      },
      error: err => this.snackBar.open(err.error?.message || 'Erreur inconnue', 'OK', { duration: 5000 })
    });
  }

  // ✅ OCP : lookup dans une table au lieu d'un if/else
  getStatusClass(status: string | undefined): string {
    return STATUS_CLASS[status ?? ''] ?? '';
  }

  private emptyOrder(): Order {
    return {
      customerName: '',
      customerEmail: '',
      customerPhone: '',
      customerType: 'REGULAR',
      paymentMethod: 'CREDIT_CARD',
      notificationType: 'EMAIL',
      items: [{ productId: null, quantity: 1 }]
    };
  }
}