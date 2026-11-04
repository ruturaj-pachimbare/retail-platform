package com.retail.inventory.service;

import com.retail.inventory.dto.*;
import com.retail.inventory.entity.InventoryItem;
import com.retail.inventory.exception.*;
import com.retail.inventory.repository.InventoryRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InventoryService {

    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    public InventoryResponse create(CreateInventoryRequest request) {
        if (repository.existsByProductId(request.productId())) {
            throw new DuplicateInventoryException("Inventory already exists for productId: " + request.productId());
        }
        if (repository.existsBySkuIgnoreCase(request.sku())) {
            throw new DuplicateInventoryException("Inventory already exists for SKU: " + request.sku());
        }
        try {
            InventoryItem item = new InventoryItem(request.productId(), request.sku().trim(), request.initialQuantity());
            return toResponse(repository.saveAndFlush(item));
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateInventoryException("Inventory already exists for this productId or SKU");
        }
    }

    @Transactional(readOnly = true)
    public InventoryResponse getByProductId(Long productId) {
        return toResponse(findByProductId(productId));
    }

    @Transactional(readOnly = true)
    public List<InventoryResponse> getAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public InventoryResponse adjustStock(Long productId, StockAdjustmentRequest request) {
        InventoryItem item = findByProductId(productId);
        try {
            item.adjustStock(request.quantityDelta());
        } catch (IllegalArgumentException ex) {
            throw new InvalidStockOperationException(ex.getMessage());
        }
        return toResponse(repository.saveAndFlush(item));
    }

    public InventoryResponse reserveStock(Long productId, StockQuantityRequest request) {
        InventoryItem item = findByProductId(productId);
        try {
            item.reserve(request.quantity());
        } catch (IllegalArgumentException ex) {
            throw new InsufficientStockException(ex.getMessage());
        }
        return toResponse(repository.saveAndFlush(item));
    }

    public InventoryResponse releaseStock(Long productId, StockQuantityRequest request) {
        InventoryItem item = findByProductId(productId);
        try {
            item.release(request.quantity());
        } catch (IllegalArgumentException ex) {
            throw new InvalidStockOperationException(ex.getMessage());
        }
        return toResponse(repository.saveAndFlush(item));
    }

    @Transactional(readOnly = true)
    public AvailabilityResponse checkAvailability(Long productId, int requestedQuantity) {
        if (requestedQuantity <= 0) {
            throw new InvalidStockOperationException("requestedQuantity must be greater than zero");
        }
        InventoryItem item = findByProductId(productId);
        int available = item.getAvailableQuantity();
        return new AvailabilityResponse(productId, item.getSku(), requestedQuantity,
                available, available >= requestedQuantity);
    }

    private InventoryItem findByProductId(Long productId) {
        return repository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException(productId));
    }

    private InventoryResponse toResponse(InventoryItem item) {
        return new InventoryResponse(item.getId(), item.getProductId(), item.getSku(),
                item.getQuantityOnHand(), item.getQuantityReserved(), item.getAvailableQuantity(),
                item.getVersion(), item.getCreatedAt(), item.getUpdatedAt());
    }
}
