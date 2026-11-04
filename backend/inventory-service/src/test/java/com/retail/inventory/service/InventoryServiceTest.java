package com.retail.inventory.service;

import com.retail.inventory.dto.*;
import com.retail.inventory.entity.InventoryItem;
import com.retail.inventory.exception.*;
import com.retail.inventory.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository repository;

    private InventoryService service;

    @BeforeEach
    void setUp() {
        service = new InventoryService(repository);
    }

    @Test
    void createShouldSaveInventory() {
        CreateInventoryRequest request = new CreateInventoryRequest(10L, "SKU-10", 25);
        when(repository.existsByProductId(10L)).thenReturn(false);
        when(repository.existsBySkuIgnoreCase("SKU-10")).thenReturn(false);
        when(repository.saveAndFlush(any(InventoryItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InventoryResponse response = service.create(request);

        assertEquals(10L, response.productId());
        assertEquals("SKU-10", response.sku());
        assertEquals(25, response.availableQuantity());
        verify(repository).saveAndFlush(any(InventoryItem.class));
    }

    @Test
    void createShouldRejectDuplicateProductId() {
        when(repository.existsByProductId(10L)).thenReturn(true);
        CreateInventoryRequest request = new CreateInventoryRequest(10L, "SKU-10", 25);

        assertThrows(DuplicateInventoryException.class, () -> service.create(request));
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    void reserveShouldReduceAvailableStock() {
        InventoryItem item = new InventoryItem(10L, "SKU-10", 20);
        when(repository.findByProductId(10L)).thenReturn(Optional.of(item));
        when(repository.saveAndFlush(item)).thenReturn(item);

        InventoryResponse response = service.reserveStock(10L, new StockQuantityRequest(7));

        assertEquals(20, response.quantityOnHand());
        assertEquals(7, response.quantityReserved());
        assertEquals(13, response.availableQuantity());
    }

    @Test
    void reserveShouldRejectInsufficientStock() {
        InventoryItem item = new InventoryItem(10L, "SKU-10", 5);
        when(repository.findByProductId(10L)).thenReturn(Optional.of(item));

        assertThrows(InsufficientStockException.class,
                () -> service.reserveStock(10L, new StockQuantityRequest(6)));
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    void releaseShouldReturnStockToAvailable() {
        InventoryItem item = new InventoryItem(10L, "SKU-10", 20);
        item.reserve(8);
        when(repository.findByProductId(10L)).thenReturn(Optional.of(item));
        when(repository.saveAndFlush(item)).thenReturn(item);

        InventoryResponse response = service.releaseStock(10L, new StockQuantityRequest(3));

        assertEquals(5, response.quantityReserved());
        assertEquals(15, response.availableQuantity());
    }

    @Test
    void adjustShouldNotReduceBelowReservedQuantity() {
        InventoryItem item = new InventoryItem(10L, "SKU-10", 10);
        item.reserve(7);
        when(repository.findByProductId(10L)).thenReturn(Optional.of(item));

        assertThrows(InvalidStockOperationException.class,
                () -> service.adjustStock(10L, new StockAdjustmentRequest(-4)));
    }

    @Test
    void getShouldThrowWhenInventoryMissing() {
        when(repository.findByProductId(99L)).thenReturn(Optional.empty());

        assertThrows(InventoryNotFoundException.class, () -> service.getByProductId(99L));
    }

    @Test
    void availabilityShouldReportNotAvailableWhenQuantityTooHigh() {
        InventoryItem item = new InventoryItem(10L, "SKU-10", 10);
        item.reserve(4);
        when(repository.findByProductId(10L)).thenReturn(Optional.of(item));

        AvailabilityResponse response = service.checkAvailability(10L, 7);

        assertFalse(response.available());
        assertEquals(6, response.availableQuantity());
    }
}
