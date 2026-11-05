import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ProductService } from '../../../core/services/product.service';
import { CartService } from '../../../core/services/cart.service';
import { Product } from '../../../shared/models/product.model';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, CurrencyPipe],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.scss'
})
export class ProductListComponent implements OnInit {
  private readonly productsApi = inject(ProductService);
  private readonly cart = inject(CartService);
  products: Product[] = [];
  search = '';
  loading = false;
  error = '';

  get cartCount(): number { return this.cart.itemCount(); }

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.error = '';
    this.productsApi.list(0, 50, this.search).subscribe({
      next: data => { this.products = data; this.loading = false; },
      error: err => { this.error = err?.error?.message || 'Unable to load products. Check services and permissions.'; this.loading = false; }
    });
  }

  addToCart(product: Product): void {
    this.cart.add(product);
    alert(`${product.name} added to cart`);
  }
}
