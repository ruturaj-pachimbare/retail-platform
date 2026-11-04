package com.retail.product.exception;

public class DuplicateSkuException extends RuntimeException {
    public DuplicateSkuException(String sku) {
        super("A product already exists with SKU: " + sku);
    }
}
