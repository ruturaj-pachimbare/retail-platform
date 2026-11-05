export interface Product {
  id: number;
  name: string;
  description?: string;
  category: string;
  sku: string;
  price: number;
  active: boolean;
}

export interface ProductRequest {
  name: string;
  description: string;
  category: string;
  sku: string;
  price: number;
  active: boolean;
}

export interface PageResponse<T> {
  content: T[];
  totalElements?: number;
  totalPages?: number;
  number?: number;
  size?: number;
}
