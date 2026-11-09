package com.webprj.ecom.tests.services;

import com.webprj.ecom.tests.client.OrderClient;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the service layer for Order API testing.
 * Since no database triggers are used, all business logic 
 * (total amount calculation, order items handling, etc.) is done here.
 */
public class OrderService
{
    private final OrderClient orderClient = new OrderClient();

    /**
     * CREATE ORDER with multiple items - Main method
     */
    public Response createOrder(Long userId, List<Map<String, Object>> orderItems)
    {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userId);
        payload.put("orderItems", orderItems);

        System.out.println("✅ Creating order for userId: " + userId + " with " + orderItems.size() + " items");
        return orderClient.createOrder(payload);
    }

    /**
     * CREATE ORDER with one product (Convenience method - Recommended for most tests)
     */
    public Response createOrderWithOneItem(Long userId, Long productId, Integer quantity)
    {
        List<Map<String, Object>> orderItems = new ArrayList<>();
        
        Map<String, Object> item = new HashMap<>();
        item.put("productId", productId);
        item.put("quantity", quantity);
        
        orderItems.add(item);

        return createOrder(userId, orderItems);
    }

    /**
     * GET ORDER BY ID
     */
    public Response getOrderById(Long orderId)
    {
        return orderClient.getOrderById(orderId);
    }

    /**
     * GET ALL ORDERS
     */
    public Response getAllOrders()
    {
        return orderClient.getAllOrders();
    }

    /**
     * FULL UPDATE (PUT)
     */
    public Response updateOrder(Long orderId, Long userId, List<Map<String, Object>> orderItems)
    {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userId);
        payload.put("orderItems", orderItems);

        System.out.println("✅ Updating order ID: " + orderId);
        return orderClient.updateOrder(orderId, payload);
    }

    /**
     * PARTIAL UPDATE (PATCH) - Simple version (most commonly used)
     * PARTIAL UPDATE (PATCH) - Simple version with status
     */
    public Response patchOrder(Long orderId, String status)
    {
        Map<String, Object> payload = new HashMap<>();
        payload.put("status", status);

        System.out.println("✅ Patching order ID: " + orderId + " with status: " + status);
        return orderClient.patchOrder(orderId, payload);
    }
    
    /**
     * PARTIAL UPDATE (PATCH) - Flexible version (any fields)
     */
    public Response patchOrder(Long orderId, Map<String, Object> payload)
    {
        System.out.println("✅ Patching order ID: " + orderId + " with payload: " + payload);
        return orderClient.patchOrder(orderId, payload);
    }

    /**
     * CANCEL ORDER
     */
    public Response cancelOrder(Long orderId)
    {
        System.out.println("✅ Cancelling order ID: " + orderId);
        return orderClient.cancelOrder(orderId);
    }

    /**
     * DELETE ORDER
     */
    public Response deleteOrder(Long orderId)
    {
        return orderClient.deleteOrder(orderId);
    }
}