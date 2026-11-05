import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { InventoryCreateRequest, InventoryItem } from '../../shared/models/inventory.model';

@Injectable({ providedIn: 'root' })
export class InventoryService {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/api/v1/inventory`;

  list(): Observable<InventoryItem[]> {
    return this.http.get<InventoryItem[]>(this.url);
  }

  getByProduct(productId: number): Observable<InventoryItem> {
    return this.http.get<InventoryItem>(`${this.url}/${productId}`);
  }

  checkAvailability(productId: number, quantity: number): Observable<{ available: boolean }> {
    const params = new HttpParams().set('quantity', quantity);
    return this.http.get<{ available: boolean }>(
      `${this.url}/${productId}/availability`, { params }
    );
  }

  create(request: InventoryCreateRequest): Observable<InventoryItem> {
    return this.http.post<InventoryItem>(this.url, request);
  }

  adjustStock(productId: number, quantityDelta: number): Observable<InventoryItem> {
    return this.http.patch<InventoryItem>(`${this.url}/${productId}/stock`, { quantityDelta });
  }
}
