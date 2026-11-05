export type OrderStatus = 'PENDING' | 'CONFIRMED' | 'CANCELLED' | 'FAILED' | string;

export interface RetailOrder {
  id: number;
  customerId: number;
  productId: number;
  quantity: number;
  unitPrice: number;
  totalAmount: number;
  status: OrderStatus;
  createdAt?: string;
}

export interface CreateOrderRequest {
  customerId: number;
  productId: number;
  quantity: number;
  unitPrice: number;
}
