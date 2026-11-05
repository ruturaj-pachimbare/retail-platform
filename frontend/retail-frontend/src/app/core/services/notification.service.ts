import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { RetailNotification } from '../../shared/models/notification.model';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/api/v1/notifications`;

  list(): Observable<RetailNotification[]> {
    return this.http.get<RetailNotification[]>(this.url);
  }

  get(id: number): Observable<RetailNotification> {
    return this.http.get<RetailNotification>(`${this.url}/${id}`);
  }

  byOrder(orderId: number): Observable<RetailNotification[]> {
    return this.http.get<RetailNotification[]>(`${this.url}/order/${orderId}`);
  }

  byCustomer(customerId: number): Observable<RetailNotification[]> {
    return this.http.get<RetailNotification[]>(`${this.url}/customer/${customerId}`);
  }
}
