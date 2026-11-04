package com.retail.order.service;
import com.retail.order.client.InventoryClient; import com.retail.order.dto.*;
import com.retail.order.repository.OrderRepository;
import org.junit.jupiter.api.Test; import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*; import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import static org.mockito.ArgumentMatchers.*; import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
 @Mock OrderRepository orders; @Mock InventoryClient inventory; @Mock OrderPersistenceService persistence;
 @InjectMocks OrderService service;
 @Test void createReservesStockThenPersists(){
  CreateOrderRequest r=new CreateOrderRequest(1L,2L,3,new BigDecimal("10.00"));
  OrderResponse expected=new OrderResponse(5L,1L,2L,3,new BigDecimal("10.00"),new BigDecimal("30.00"),null,null,null);
  when(persistence.create(r)).thenReturn(expected);
  assertEquals(5L,service.create(r).id());
  verify(inventory).reserve(eq(2L),any(StockQuantityRequest.class)); verify(persistence).create(r);
 }
}
