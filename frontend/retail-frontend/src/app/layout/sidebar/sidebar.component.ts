
import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, MatListModule, MatIconModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {
  readonly menuItems = [
    { label: 'Dashboard', icon: 'dashboard', route: '/dashboard' },
    { label: 'Products', icon: 'inventory_2', route: '/products' },
    { label: 'Inventory', icon: 'warehouse', route: '/inventory' },
    { label: 'Cart', icon: 'shopping_cart', route: '/cart' },
    { label: 'Orders', icon: 'receipt_long', route: '/orders' },
    { label: 'Notifications', icon: 'notifications', route: '/notifications' }
  ];
}
