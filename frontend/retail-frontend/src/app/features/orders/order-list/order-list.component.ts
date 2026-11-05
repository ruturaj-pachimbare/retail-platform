import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { OrderService } from '../../../core/services/order.service';
import { CartService } from '../../../core/services/cart.service';
import { environment } from '../../../../environments/environment';
import { RetailOrder } from '../../../shared/models/order.model';

@Component({
  selector: 'app-order-list',
  standalone: true,
  imports: [CommonModule, CurrencyPipe, DatePipe, RouterLink],
  templateUrl: './order-list.component.html',
  styleUrl: './order-list.component.scss'
})
export class OrderListComponent implements OnInit {
  private readonly api = inject(OrderService);
  private readonly cart = inject(CartService);
  orders: RetailOrder[] = [];
  loading = false;
  error = '';
  success = '';

  get cartEmpty(): boolean { return this.cart.lines().length === 0; }

  ngOnInit(): void { this.load(); }
  load(): void {
    this.loading = true;
    this.api.list().subscribe({
      next: data => { this.orders = data; this.loading = false; },
      error: err => { this.error = err?.error?.message || 'Unable to load orders.'; this.loading = false; }
    });
  }

  checkout(): void {
    const line = this.cart.lines()[0];
    if (!line) { this.error = 'Add a product to your cart first.'; return; }
    if (this.cart.lines().length > 1) {
      this.error = 'This backend create-order API accepts one product per order. Please checkout one product at a time.';
      return;
    }
    this.error = '';
    this.success = '';
    this.api.create({
      customerId: environment.demoCustomerId,
      productId: line.product.id,
      quantity: line.quantity,
      unitPrice: line.product.price
    }).subscribe({
      next: order => {
        this.success = `Order #${order.id} created successfully.`;
        this.cart.clear();
        this.load();
      },
      error: err => this.error = err?.error?.message || 'Order creation failed. Check inventory availability and backend logs.'
    });
  }

  cancel(order: RetailOrder): void {
    if (!confirm(`Cancel order #${order.id}?`)) return;
    this.api.cancel(order.id).subscribe({
      next: () => this.load(),
      error: err => this.error = err?.error?.message || 'Unable to cancel order.'
    });
  }
}
