import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { NotificationService } from '../../core/services/notification.service';
import { RetailNotification } from '../../shared/models/notification.model';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [CommonModule, DatePipe],
  templateUrl: './notifications.component.html',
  styleUrl: './notifications.component.scss'
})
export class NotificationsComponent implements OnInit {
  private readonly api = inject(NotificationService);
  notifications: RetailNotification[] = [];
  loading = false;
  error = '';

  ngOnInit(): void {
    this.loading = true;
    this.api.byCustomer(environment.demoCustomerId).subscribe({
      next: data => { this.notifications = data; this.loading = false; },
      error: err => { this.error = err?.error?.message || 'Unable to load notifications.'; this.loading = false; }
    });
  }
}
