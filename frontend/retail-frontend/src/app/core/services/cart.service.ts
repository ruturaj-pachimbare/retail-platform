import { Injectable, computed, signal } from '@angular/core';
import { Product } from '../../shared/models/product.model';

export interface CartLine {
  product: Product;
  quantity: number;
}

@Injectable({ providedIn: 'root' })
export class CartService {
  private readonly storageKey = 'techzone-cart';
  private readonly linesState = signal<CartLine[]>(this.read());
  readonly lines = this.linesState.asReadonly();
  readonly itemCount = computed(() => this.linesState().reduce((sum, x) => sum + x.quantity, 0));
  readonly total = computed(() => this.linesState().reduce((sum, x) => sum + x.quantity * x.product.price, 0));

  add(product: Product): void {
    const current = [...this.linesState()];
    const found = current.find(x => x.product.id === product.id);
    if (found) found.quantity += 1;
    else current.push({ product, quantity: 1 });
    this.save(current);
  }

  changeQuantity(productId: number, quantity: number): void {
    if (quantity <= 0) return this.remove(productId);
    this.save(this.linesState().map(x => x.product.id === productId ? { ...x, quantity } : x));
  }

  remove(productId: number): void {
    this.save(this.linesState().filter(x => x.product.id !== productId));
  }

  clear(): void {
    this.save([]);
  }

  private save(lines: CartLine[]): void {
    this.linesState.set(lines);
    localStorage.setItem(this.storageKey, JSON.stringify(lines));
  }

  private read(): CartLine[] {
    try {
      const value = localStorage.getItem(this.storageKey);
      return value ? JSON.parse(value) as CartLine[] : [];
    } catch {
      return [];
    }
  }
}
