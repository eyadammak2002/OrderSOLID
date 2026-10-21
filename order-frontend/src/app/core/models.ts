export interface Product {
  id: number;
  name: string;
  price: number;
  stock: number;
}

export interface OrderItem {
  productId: number | null;
  quantity: number;
}

export interface Order {
  id?: number;
  customerName: string;
  customerEmail: string;
  customerPhone: string;
  customerType: string;
  paymentMethod: string;
  notificationType: string;
  status?: string;
  discount?: number;
  tax?: number;
  shipping?: number;
  paymentFee?: number;
  total?: number;
  items: OrderItem[];
}

export interface Stats {
  orderCount: number;
  revenue: number;
}

// ✅ OCP : un nouveau statut = une ligne, aucun if/else à modifier
export const STATUS_CLASS: Record<string, string> = {
  PAID: 'status-paid',
  PENDING: 'status-pending',
  CANCELLED: 'status-cancelled'
};