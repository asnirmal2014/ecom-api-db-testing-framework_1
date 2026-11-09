package com.webprj.ecom.tests.services;

import com.webprj.ecom.tests.client.ProductClient;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the service layer for Product API testing.
 * It prepares request payloads and calls the ProductClient methods.
 */
public class ProductService
{
    private final ProductClient productClient = new ProductClient();

    /**
     * CREATE PRODUCT with dynamic name (Recommended for most tests)
     */
    public Response createProduct(String name, BigDecimal price, Integer stock)
    {
        String description = "Description for " + name;
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", name);
        payload.put("description", description);
        payload.put("price", price);
        payload.put("stock", stock);
        
        return productClient.createProduct(payload);
    }

    /**
     * CREATE PRODUCT with full custom data
     */
    public Response createProduct(String name, String description, BigDecimal price, Integer stock)
    {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", name);
        payload.put("description", description);
        payload.put("price", price);
        payload.put("stock", stock);
        
        return productClient.createProduct(payload);
    }

    /**
     * GET PRODUCT BY ID
     */
    public Response getProductById(Long productId)
    {
        return productClient.getProductById(productId);
    }

    /**
     * GET ALL PRODUCTS
     */
    public Response getAllProducts()
    {
        return productClient.getAllProducts();
    }

    /**
     * FULL UPDATE (PUT)
     */
    public Response updateProduct(Long productId, String name, String description,
                                  BigDecimal price, Integer stock)
    {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", name);
        payload.put("description", description);
        payload.put("price", price);
        payload.put("stock", stock);
        
        return productClient.updateProduct(productId, payload);
    }

    /**
     * PARTIAL UPDATE (PATCH) - e.g., only price and stock
     */
    public Response patchProductPriceAndStock(Long productId, BigDecimal price, Integer stock)
    {
        Map<String, Object> payload = new HashMap<>();
        payload.put("price", price);
        payload.put("stock", stock);
        
        return productClient.patchProduct(productId, payload);
    }

    /**
     * DELETE PRODUCT
     */
    public Response deleteProduct(Long productId)
    {
        return productClient.deleteProduct(productId);
    }
}