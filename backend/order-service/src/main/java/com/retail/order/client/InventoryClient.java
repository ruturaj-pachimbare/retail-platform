package com.retail.order.client;
import com.retail.order.config.InventoryClientConfig;
import com.retail.order.dto.StockQuantityRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
@FeignClient(name="inventory-service",url="${inventory.service.url}",configuration=InventoryClientConfig.class)
public interface InventoryClient {
 @PostMapping("${inventory.service.reserve-path}") void reserve(@PathVariable("productId") Long productId,@RequestBody StockQuantityRequest request);
 @PostMapping("${inventory.service.release-path}") void release(@PathVariable("productId") Long productId,@RequestBody StockQuantityRequest request);
}
