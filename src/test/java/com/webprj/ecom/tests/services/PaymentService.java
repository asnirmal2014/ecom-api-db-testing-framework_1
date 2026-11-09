package com.webprj.ecom.tests.services;

import com.webprj.ecom.tests.client.PaymentClient;
import io.restassured.response.Response;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the service layer for Payment API testing.
 * Since no database triggers are used, all business logic is handled here.
 */
public class PaymentService
{
    private final PaymentClient paymentClient = new PaymentClient();

    /**
     * CREATE PAYMENT
     */
    public Response createPayment(Long orderId, BigDecimal amount, String paymentMethod)
    {
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", orderId);
        payload.put("amount", amount);
        payload.put("paymentMethod", paymentMethod);

        System.out.println("✅ Creating payment for orderId: " + orderId + " with amount: " + amount);
        return paymentClient.createPayment(payload);
    }

    /**
     * GET PAYMENT BY ID
     */
    public Response getPaymentById(Long paymentId)
    {
        return paymentClient.getPaymentById(paymentId);
    }

    /**
     * GET ALL PAYMENTS
     */
    public Response getAllPayments()
    {
        return paymentClient.getAllPayments();
    }

    /**
     * FULL UPDATE (PUT)
     */
    public Response updatePayment(Long paymentId, BigDecimal amount, String paymentMethod)
    {
        Map<String, Object> payload = new HashMap<>();
        payload.put("amount", amount);
        payload.put("paymentMethod", paymentMethod);

        System.out.println("✅ Updating payment ID: " + paymentId);
        return paymentClient.updatePayment(paymentId, payload);
    }

    /**
     * PARTIAL UPDATE (PATCH)
     */
    public Response patchPayment(Long paymentId, String status)
    {
        Map<String, Object> payload = new HashMap<>();
        payload.put("status", status);

        System.out.println("✅ Patching payment ID: " + paymentId + " with status: " + status);
        return paymentClient.patchPayment(paymentId, payload);
    }

    /**
     * DELETE PAYMENT
     */
    public Response deletePayment(Long paymentId)
    {
        return paymentClient.deletePayment(paymentId);
    }
}