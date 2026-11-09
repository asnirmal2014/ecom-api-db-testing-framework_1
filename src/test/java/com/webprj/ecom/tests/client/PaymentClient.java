package com.webprj.ecom.tests.client;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * PaymentClient.java
 * This class contains all HTTP methods to interact with Payment endpoints.
 * It uses RestAssured to make API calls.
 */
public class PaymentClient
{
    private static final String BASE_PATH = "/api/payments";

    /**
     * CREATE PAYMENT - POST /api/payments
     */
    public Response createPayment(Object paymentPayload)
    {
        System.out.println("✅ paymentPayload: " + paymentPayload + " - createPayment()");

        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(paymentPayload)
                .when()
                .post(BASE_PATH);
    }

    /**
     * GET PAYMENT BY ID - GET /api/payments/{id}
     */
    public Response getPaymentById(Long paymentId)
    {
        return RestAssured
                .given()
                .when()
                .get(BASE_PATH + "/" + paymentId);
    }

    /**
     * GET ALL PAYMENTS - GET /api/payments
     */
    public Response getAllPayments()
    {
        return RestAssured
                .given()
                .when()
                .get(BASE_PATH);
    }

    /**
     * FULL UPDATE (PUT) - PUT /api/payments/{id}
     */
    public Response updatePayment(Long paymentId, Object payload)
    {
        System.out.println("✅ Payload: " + payload + " - updatePayment()");

        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .put(BASE_PATH + "/" + paymentId);
    }

    /**
     * PARTIAL UPDATE (PATCH) - PATCH /api/payments/{id}
     */
    public Response patchPayment(Long paymentId, Object payload)
    {
        System.out.println("✅ Payload: " + payload + " - patchPayment()");

        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .patch(BASE_PATH + "/" + paymentId);
    }

    /**
     * DELETE PAYMENT - DELETE /api/payments/{id}
     */
    public Response deletePayment(Long paymentId)
    {
        return RestAssured
                .given()
                .when()
                .delete(BASE_PATH + "/" + paymentId);
    }
}