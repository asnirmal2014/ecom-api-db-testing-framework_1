package com.webprj.ecom.tests.client;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * OrderClient.java
 * This class contains all HTTP methods to interact with Order endpoints.
 * It uses RestAssured to make API calls.
 */
public class OrderClient
{
    private static final String BASE_PATH = "/api/orders";

    /**
     * CREATE ORDER - POST /api/orders
     */
    public Response createOrder(Object orderPayload)
    {
        System.out.println("✅ orderPayload: " + orderPayload + " - createOrder()");

        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(orderPayload)
                .when()
                .post(BASE_PATH);
    }

    /**
     * GET ORDER BY ID - GET /api/orders/{id}
     */
    public Response getOrderById(Long orderId)
    {
        return RestAssured
                .given()
                .when()
                .get(BASE_PATH + "/" + orderId);
    }

    /**
     * GET ALL ORDERS - GET /api/orders
     */
    public Response getAllOrders()
    {
        return RestAssured
                .given()
                .when()
                .get(BASE_PATH);
    }

    /**
     * FULL UPDATE (PUT) - PUT /api/orders/{id}
     */
    public Response updateOrder(Long orderId, Object payload)
    {
        System.out.println("✅ Payload: " + payload + " - updateOrder()");

        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .put(BASE_PATH + "/" + orderId);
    }

    /**
     * PARTIAL UPDATE (PATCH) - PATCH /api/orders/{id}
     */
    public Response patchOrder(Long orderId, Object payload)
    {
        System.out.println("✅ Payload: " + payload + " - patchOrder()");

        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .patch(BASE_PATH + "/" + orderId);
    }

    /**
     * CANCEL ORDER - PATCH /api/orders/{id}/cancel
     */
    public Response cancelOrder(Long orderId)
    {
        System.out.println("✅ Cancelling order ID: " + orderId);

        return RestAssured
                .given()
                .when()
                .patch(BASE_PATH + "/" + orderId + "/cancel");
    }

    /**
     * DELETE ORDER - DELETE /api/orders/{id}
     */
    public Response deleteOrder(Long orderId)
    {
        return RestAssured
                .given()
                .when()
                .delete(BASE_PATH + "/" + orderId);
    }
}