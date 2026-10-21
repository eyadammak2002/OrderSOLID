import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Order, Stats } from './models';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private url = `${environment.apiUrl}/orders`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Order[]> {
    return this.http.get<Order[]>(this.url);
  }

  getStats(): Observable<Stats> {
    return this.http.get<Stats>(`${this.url}/stats`);
  }

  create(order: Order): Observable<Order> {
    return this.http.post<Order>(this.url, order);
  }

  cancel(id: number): Observable<Order> {
    return this.http.put<Order>(`${this.url}/${id}/cancel`, {});
  }
}