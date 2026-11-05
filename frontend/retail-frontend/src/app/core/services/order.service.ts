import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CreateOrderRequest, RetailOrder } from '../../shared/models/order.model';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/api/v1/orders`;

  list(): Observable<RetailOrder[]> {
    return this.http.get<RetailOrder[]>(this.url);
  }

  get(id: number): Observable<RetailOrder> {
    return this.http.get<RetailOrder>(`${this.url}/${id}`);
  }

  byCustomer(customerId: number): Observable<RetailOrder[]> {
    return this.http.get<RetailOrder[]>(`${this.url}/customer/${customerId}`);
  }

  create(request: CreateOrderRequest): Observable<RetailOrder> {
    return this.http.post<RetailOrder>(this.url, request);
  }

  cancel(id: number): Observable<RetailOrder> {
    return this.http.post<RetailOrder>(`${this.url}/${id}/cancel`, {});
  }
}
