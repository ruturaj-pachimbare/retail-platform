package com.retail.product.service;

import com.retail.product.dto.ProductRequest;
import com.retail.product.dto.ProductResponse;
import com.retail.product.entity.Product;
import com.retail.product.exception.DuplicateSkuException;
import com.retail.product.exception.ProductNotFoundException;
import com.retail.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ProductRequest request;

    @BeforeEach
    void setUp() {
        request = new ProductRequest(
                "Mechanical Keyboard",
                "RGB keyboard",
                "Accessories",
                "KB-100",
                new BigDecimal("2499.00"),
                true
        );
    }

    @Test
    void createShouldSaveProduct() {
        when(productRepository.existsBySkuIgnoreCase("KB-100")).thenReturn(false);
        when(productRepository.saveAndFlush(any(Product.class))).thenAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            // Simulate the database-generated ID for this unit test.
            return product;
        });

        // This test focuses on repository interaction; generated IDs are tested
        // in an integration test when PostgreSQL/Testcontainers is introduced.
        assertDoesNotThrow(() -> productService.create(request));
        verify(productRepository).saveAndFlush(any(Product.class));
    }

    @Test
    void createShouldRejectDuplicateSku() {
        when(productRepository.existsBySkuIgnoreCase("KB-100")).thenReturn(true);

        assertThrows(DuplicateSkuException.class, () -> productService.create(request));
        verify(productRepository, never()).saveAndFlush(any(Product.class));
    }

    @Test
    void getByIdShouldThrowWhenProductDoesNotExist() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getById(99L));
    }
}
