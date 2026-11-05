import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InventoryService } from '../../../core/services/inventory.service';
import { InventoryItem } from '../../../shared/models/inventory.model';

@Component({
  selector: 'app-inventory-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './inventory-list.component.html',
  styleUrl: './inventory-list.component.scss'
})
export class InventoryListComponent implements OnInit {
  private readonly api = inject(InventoryService);
  items: InventoryItem[] = [];
  loading = false;
  error = '';

  ngOnInit(): void {
    this.loading = true;
    this.api.list().subscribe({
      next: data => { this.items = data; this.loading = false; },
      error: err => { this.error = err?.error?.message || 'Unable to load inventory.'; this.loading = false; }
    });
  }
}
