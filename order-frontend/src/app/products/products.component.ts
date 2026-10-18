import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { Product } from '../core/models';
import { ProductService } from '../core/product.service';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [CommonModule, FormsModule, MatCardModule, MatFormFieldModule, MatInputModule,
            MatButtonModule, MatIconModule, MatTableModule, MatSnackBarModule],
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {

  products: Product[] = [];
  newProduct: Partial<Product> = { name: '', price: undefined, stock: undefined };
  displayedColumns: string[] = ['id', 'name', 'price', 'stock'];

  // ✅ le composant dépend d'un service, plus de HttpClient ni d'URL
  constructor(private productService: ProductService, private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.productService.getAll().subscribe(data => this.products = data);
  }

  add(): void {
    this.productService.create(this.newProduct).subscribe({
      next: () => {
        this.newProduct = { name: '', price: undefined, stock: undefined };
        this.snackBar.open('Produit ajouté', 'OK', { duration: 3000 });
        this.load();
      },
      error: () => this.snackBar.open("Erreur lors de l'ajout", 'OK', { duration: 4000 })
    });
  }

  getStockClass(stock: number): string {
    if (stock === 0) return 'stock-out';
    if (stock < 5) return 'stock-low';
    return 'stock-ok';
  }
}