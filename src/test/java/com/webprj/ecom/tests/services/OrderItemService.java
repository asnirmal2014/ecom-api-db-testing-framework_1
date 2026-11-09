package com.webprj.ecom.tests.services;

import com.webprj.ecom.tests.client.OrderItemClient;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the service layer for OrderItem API testing.
 * Since no database triggers are used, all business logic is handled here.
 */
public class OrderItemService
{
    private final OrderItemClient orderItemClient = new OrderItemClient();

    /**
     * CREATE ORDER ITEM
     */
    public Response createOrderItem(Long orderId, Long productId, Integer quantity)
    {
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", orderId);
        payload.put("productId", productId);
        payload.put("quantity", quantity);

        System.out.println("✅ Creating orderItem for orderId: " + orderId + ", productId: " + productId);
        return orderItemClient.createOrderItem(payload);
    }

    /**
     * GET ORDER ITEM BY ID
     */
    public Response getOrderItemById(Long orderItemId)
    {
        return orderItemClient.getOrderItemById(orderItemId);
    }

    /**
     * GET ALL ORDER ITEMS
     */
    public Response getAllOrderItems()
    {
        return orderItemClient.getAllOrderItems();
    }

    /**
     * FULL UPDATE (PUT)
     */
    public Response updateOrderItem(Long orderItemId, Long productId, Integer quantity)
    {
        Map<String, Object> payload = new HashMap<>();
        payload.put("productId", productId);
        payload.put("quantity", quantity);

        System.out.println("✅ Updating orderItem ID: " + orderItemId);
        return orderItemClient.updateOrderItem(orderItemId, payload);
    }

    /**
     * PARTIAL UPDATE (PATCH)
     */
    public Response patchOrderItem(Long orderItemId, Integer quantity)
    {
        Map<String, Object> payload = new HashMap<>();
        payload.put("quantity", quantity);

        System.out.println("✅ Patching orderItem ID: " + orderItemId + " with quantity: " + quantity);
        return orderItemClient.patchOrderItem(orderItemId, payload);
    }

    /**
     * DELETE ORDER ITEM
     */
    public Response deleteOrderItem(Long orderItemId)
    {
        return orderItemClient.deleteOrderItem(orderItemId);
    }
}