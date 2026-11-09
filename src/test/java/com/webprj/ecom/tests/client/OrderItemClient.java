package com.webprj.ecom.tests.client;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * OrderItemClient.java
 * This class contains all HTTP methods to interact with OrderItem endpoints.
 * It uses RestAssured to make API calls.
 */
public class OrderItemClient
{
    private static final String BASE_PATH = "/api/order-items";

    /**
     * CREATE ORDER ITEM - POST /api/order-items
     */
    public Response createOrderItem(Object orderItemPayload)
    {
        System.out.println("✅ orderItemPayload: " + orderItemPayload + " - createOrderItem()");

        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(orderItemPayload)
                .when()
                .post(BASE_PATH);
    }

    /**
     * GET ORDER ITEM BY ID - GET /api/order-items/{id}
     */
    public Response getOrderItemById(Long orderItemId)
    {
        return RestAssured
                .given()
                .when()
                .get(BASE_PATH + "/" + orderItemId);
    }

    /**
     * GET ALL ORDER ITEMS - GET /api/order-items
     */
    public Response getAllOrderItems()
    {
        return RestAssured
                .given()
                .when()
                .get(BASE_PATH);
    }

    /**
     * FULL UPDATE (PUT) - PUT /api/order-items/{id}
     */
    public Response updateOrderItem(Long orderItemId, Object payload)
    {
        System.out.println("✅ Payload: " + payload + " - updateOrderItem()");

        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .put(BASE_PATH + "/" + orderItemId);
    }

    /**
     * PARTIAL UPDATE (PATCH) - PATCH /api/order-items/{id}
     */
    public Response patchOrderItem(Long orderItemId, Object payload)
    {
        System.out.println("✅ Payload: " + payload + " - patchOrderItem()");

        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .patch(BASE_PATH + "/" + orderItemId);
    }
    
    /*
    public Response cancelOrderItems(Long orderId)
    {
        // OrderItems typically don't have a "cancel" endpoint (cancellation is usually done at Order level).
    }
    */

    /**
     * DELETE ORDER ITEM - DELETE /api/order-items/{id}
     */
    public Response deleteOrderItem(Long orderItemId)
    {
        return RestAssured
                .given()
                .when()
                .delete(BASE_PATH + "/" + orderItemId);
    }
}