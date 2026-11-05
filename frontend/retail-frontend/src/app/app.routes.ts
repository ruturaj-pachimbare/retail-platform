import { Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login.component')
      .then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./features/auth/register/register.component')
      .then(m => m.RegisterComponent)
  },
  {
    path: 'auth/callback',
    loadComponent: () => import('./features/auth/auth-callback/auth-callback.component')
      .then(m => m.AuthCallbackComponent)
  },
  {
    path: '',
    canActivate: [AuthGuard],
    loadComponent: () => import('./layout/main-layout/main-layout.component')
      .then(m => m.MainLayoutComponent),
    children: [
      { path: 'dashboard', loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent) },
      { path: 'products', loadComponent: () => import('./features/products/product-list/product-list.component').then(m => m.ProductListComponent) },
      { path: 'products/:id', loadComponent: () => import('./features/products/product-details/product-details.component').then(m => m.ProductDetailsComponent) },
      { path: 'inventory', loadComponent: () => import('./features/inventory/inventory-list/inventory-list.component').then(m => m.InventoryListComponent) },
      { path: 'cart', loadComponent: () => import('./features/cart/cart.component').then(m => m.CartComponent) },
      { path: 'orders', loadComponent: () => import('./features/orders/order-list/order-list.component').then(m => m.OrderListComponent) },
      { path: 'orders/:id', loadComponent: () => import('./features/orders/order-details/order-details.component').then(m => m.OrderDetailsComponent) },
      { path: 'notifications', loadComponent: () => import('./features/notifications/notifications.component').then(m => m.NotificationsComponent) },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: 'dashboard' }
];
