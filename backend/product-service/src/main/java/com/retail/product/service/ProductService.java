package com.retail.product.service;

import com.retail.product.dto.ProductRequest;
import com.retail.product.dto.ProductResponse;
import com.retail.product.entity.Product;
import com.retail.product.exception.DuplicateSkuException;
import com.retail.product.exception.ProductNotFoundException;
import com.retail.product.repository.ProductRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(ProductRequest request) {
        String sku = request.sku().trim();
        if (productRepository.existsBySkuIgnoreCase(sku)) {
            throw new DuplicateSkuException(sku);
        }

        Product product = new Product(
                request.name().trim(),
                request.description(),
                request.category().trim(),
                sku,
                request.price(),
                request.active() == null || request.active()
        );

        return toResponse(save(product, sku));
    }

    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        return toResponse(findProduct(id));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getAll(String search, Pageable pageable) {
        Page<Product> products;
        if (search == null || search.isBlank()) {
            products = productRepository.findAll(pageable);
        } else {
            String term = search.trim();
            products = productRepository
                    .findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                            term, term, pageable);
        }
        return products.map(this::toResponse);
    }

    public ProductResponse update(Long id, ProductRequest request) {
        Product product = findProduct(id);
        String sku = request.sku().trim();

        if (productRepository.existsBySkuIgnoreCaseAndIdNot(sku, id)) {
            throw new DuplicateSkuException(sku);
        }

        product.update(
                request.name().trim(),
                request.description(),
                request.category().trim(),
                sku,
                request.price(),
                request.active() == null || request.active()
        );

        return toResponse(save(product, sku));
    }

    public void delete(Long id) {
        Product product = findProduct(id);
        productRepository.delete(product);
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    private Product save(Product product, String sku) {
        try {
            return productRepository.saveAndFlush(product);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateSkuException(sku);
        }
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getSku(),
                product.getPrice(),
                product.isActive(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
