package com.webprj.ecom.tests.client;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * ProductClient.java
 * This class contains all HTTP methods to interact with Product endpoints.
 * It uses RestAssured to make API calls.
 */
public class ProductClient
{
    private static final String BASE_PATH = "/api/products";

    /**
     * CREATE PRODUCT - POST /api/products
     */
    public Response createProduct(Object productPayload)
    {
        System.out.println("✅ productPayload: " + productPayload + " - createProduct()");

        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(productPayload)
                .when()
                .post(BASE_PATH);
    }

    /**
     * GET PRODUCT BY ID - GET /api/products/{id}
     */
    public Response getProductById(Long productId)
    {
        return RestAssured
                .given()
                .when()
                .get(BASE_PATH + "/" + productId);
    }

    /**
     * GET ALL PRODUCTS - GET /api/products
     */
    public Response getAllProducts()
    {
        return RestAssured
                .given()
                .when()
                .get(BASE_PATH);
    }

    /**
     * FULL UPDATE (PUT) - PUT /api/products/{id}
     */
    public Response updateProduct(Long productId, Object payload)
    {
        System.out.println("✅ Payload: " + payload + " - updateProduct()");

        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .put(BASE_PATH + "/" + productId);
    }

    /**
     * PARTIAL UPDATE (PATCH) - PATCH /api/products/{id}
     */
    public Response patchProduct(Long productId, Object payload)
    {
        System.out.println("✅ Payload: " + payload + " - patchProduct()");

        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .patch(BASE_PATH + "/" + productId);
    }

    /**
     * DELETE PRODUCT - DELETE /api/products/{id}
     */
    public Response deleteProduct(Long productId)
    {
        return RestAssured
                .given()
                .when()
                .delete(BASE_PATH + "/" + productId);
    }
}