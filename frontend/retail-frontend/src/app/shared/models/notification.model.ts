export interface RetailNotification {
  id: number;
  eventId?: string;
  eventType?: string;
  orderId: number;
  customerId: number;
  message?: string;
  status?: string;
  createdAt?: string;
}
