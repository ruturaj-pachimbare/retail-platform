import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { ProductService } from '../../core/services/product.service';
import { OrderService } from '../../core/services/order.service';
import { InventoryService } from '../../core/services/inventory.service';

import { Product } from '../../shared/models/product.model';
import { RetailOrder } from '../../shared/models/order.model';
import { InventoryItem } from '../../shared/models/inventory.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, CurrencyPipe, DatePipe, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {

  private readonly productService = inject(ProductService);
  private readonly orderService = inject(OrderService);
  private readonly inventoryService = inject(InventoryService);

  totalProducts = 0;
  totalOrders = 0;
  pendingOrders = 0;
  lowStockItems = 0;

  recentOrders: RetailOrder[] = [];

  loading = true;
  error = '';

  ngOnInit(): void {
    this.loadDashboard();
  }

  loadDashboard(): void {
    this.loading = true;
    this.error = '';

    forkJoin({
      products: this.productService.list(0, 1000).pipe(
        catchError(() => of([] as Product[]))
      ),

      orders: this.orderService.list().pipe(
        catchError(() => of([] as RetailOrder[]))
      ),

      inventory: this.inventoryService.list().pipe(
        catchError(() => of([] as InventoryItem[]))
      )
    }).subscribe({
      next: data => {
        this.totalProducts = data.products.length;
        this.totalOrders = data.orders.length;

        this.pendingOrders = data.orders.filter(
          order => order.status?.toUpperCase() === 'PENDING'
        ).length;

        this.lowStockItems = data.inventory.filter(
          item => item.availableQuantity <= 5
        ).length;

        this.recentOrders = [...data.orders]
          .sort((a, b) => {
            if (a.createdAt && b.createdAt) {
              return new Date(b.createdAt).getTime()
                - new Date(a.createdAt).getTime();
            }

            return b.id - a.id;
          })
          .slice(0, 5);

        this.loading = false;
      },

      error: () => {
        this.error = 'Unable to load dashboard data.';
        this.loading = false;
      }
    });
  }
}
