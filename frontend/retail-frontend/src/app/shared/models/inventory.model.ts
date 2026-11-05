export interface InventoryItem {
  id?: number;
  productId: number;
  sku?: string;
  quantityOnHand?: number;
  quantityReserved?: number;
  availableQuantity: number;
}

export interface InventoryCreateRequest {
  productId: number;
  sku: string;
  initialQuantity: number;
}
