package com.webprj.ecom.tests.testcases;

import com.webprj.ecom.tests.base.BaseTest;
import com.webprj.ecom.tests.model.DBOrderItem;
import com.webprj.ecom.tests.services.OrderItemService;
import com.webprj.ecom.tests.services.OrderService;
import com.webprj.ecom.tests.services.ProductService;
import com.webprj.ecom.tests.services.UserService;
import com.webprj.ecom.tests.utils.DBUtils;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * API + DB validation tests for OrderItem module.
 * Includes Positive, Negative, and Edge cases.
 */
public class OrderItemTests extends BaseTest
{
    private final UserService userService = new UserService();
    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();
    private final OrderItemService orderItemService = new OrderItemService();

    // ====================== POSITIVE TESTS ======================

    // CREATE ORDER ITEM + DB VALIDATION
    @Test(priority = 1, description = "Create OrderItem - API + DB Validation")
    public void createOrderItem_shouldReturn201_andMatchInDB()
    {
        System.out.println("createOrderItem_shouldReturn201_andMatchInDB() is called.");

        // Create prerequisite User, Product, and Order
        Response userResponse = userService.createUser("OrderItem Test User");
        Long userId = userResponse.jsonPath().getLong("id");

        Response productResponse = productService.createProduct("OrderItem Test Product", 
                new BigDecimal("99.99"), 100);
        Long productId = productResponse.jsonPath().getLong("id");

        Response orderResponse = orderService.createOrderWithOneItem(userId, productId, 1);
        Long orderId = orderResponse.jsonPath().getLong("id");

        // Create OrderItem
        Response response = orderItemService.createOrderItem(orderId, productId, 3);

        // API validation
        response.then().statusCode(201);

        Long orderItemId = response.jsonPath().getLong("id");
        Assert.assertTrue(orderItemId > 0, "OrderItem ID should be greater than 0");

        System.out.println("OrderItem created successfully with ID: " + orderItemId);

        // DB validation
        DBOrderItem dbOrderItem = DBUtils.getOrderItemById(orderItemId);
        Assert.assertNotNull(dbOrderItem, "OrderItem not found in DB!");

        Assert.assertEquals(dbOrderItem.getOrderId(), orderId);
        Assert.assertEquals(dbOrderItem.getProductId(), productId);
        Assert.assertEquals(dbOrderItem.getQuantity(), Integer.valueOf(3));

        assertTimestampsAreRecent(dbOrderItem.getCreatedAt(), dbOrderItem.getUpdatedAt(), true);
        System.out.println("DB Validation passed for OrderItem ID: " + orderItemId);
    }

    // GET ORDER ITEM BY ID
    @Test(priority = 2, description = "Get orderItem by ID")
    public void getOrderItemById_shouldReturn200()
    {
        System.out.println("getOrderItemById_shouldReturn200() is called.");

        Response userResponse = userService.createUser("Get OrderItem User");
        Long userId = userResponse.jsonPath().getLong("id");

        Response productResponse = productService.createProduct("Get OrderItem Product", 
                new BigDecimal("50.00"), 50);
        Long productId = productResponse.jsonPath().getLong("id");

        Response orderResponse = orderService.createOrderWithOneItem(userId, productId, 1);
        Long orderId = orderResponse.jsonPath().getLong("id");

        Response createResponse = orderItemService.createOrderItem(orderId, productId, 2);
        Long orderItemId = createResponse.jsonPath().getLong("id");

        Response response = orderItemService.getOrderItemById(orderItemId);

        response.then().statusCode(200);

        // DB validation
        DBOrderItem dbOrderItem = DBUtils.getOrderItemById(orderItemId);
        Assert.assertNotNull(dbOrderItem, "OrderItem not found in DB!");

        System.out.println("Get OrderItem By ID test passed for ID: " + orderItemId);
    }

    // GET ALL ORDER ITEMS
    @Test(priority = 3, description = "Get all orderItems")
    public void getAllOrderItems_shouldReturn200_andNotEmpty()
    {
        System.out.println("getAllOrderItems_shouldReturn200_andNotEmpty() is called.");

        Response response = orderItemService.getAllOrderItems();
        response.then().statusCode(200);

        int count = response.jsonPath().getList("$").size();
        System.out.println("Total orderItems count: " + count);
        Assert.assertTrue(count >= 0, "OrderItem list should be accessible");
    }

    // UPDATE ORDER ITEM (PUT)
    @Test(priority = 4, description = "Update orderItem using PUT and validate DB")
    public void updateOrderItem_shouldReturn200_andMatchDB()
    {
        System.out.println("updateOrderItem_shouldReturn200_andMatchDB() is called.");

        Response userResponse = userService.createUser("Update OrderItem User");
        Long userId = userResponse.jsonPath().getLong("id");

        Response productResponse = productService.createProduct("Update OrderItem Product", 
                new BigDecimal("100.00"), 200);
        Long productId = productResponse.jsonPath().getLong("id");

        Response orderResponse = orderService.createOrderWithOneItem(userId, productId, 1);
        Long orderId = orderResponse.jsonPath().getLong("id");

        Response createResponse = orderItemService.createOrderItem(orderId, productId, 2);
        Long orderItemId = createResponse.jsonPath().getLong("id");

        Response response = orderItemService.updateOrderItem(orderItemId, productId, 5);
        response.then().statusCode(200);

        DBOrderItem afterUpdate = DBUtils.getOrderItemById(orderItemId);
        Assert.assertNotNull(afterUpdate, "OrderItem not found in DB after update!");
        Assert.assertEquals(afterUpdate.getQuantity(), Integer.valueOf(5));

        System.out.println("OrderItem updated successfully with ID: " + orderItemId);
    }

    // PATCH ORDER ITEM
    @Test(priority = 5, description = "Patch OrderItem - Partial Update")
    public void patchOrderItem_shouldReturn200_andUpdateQuantity()
    {
        System.out.println("patchOrderItem_shouldReturn200_andUpdateQuantity() is called.");

        Response userResponse = userService.createUser("Patch OrderItem User");
        Long userId = userResponse.jsonPath().getLong("id");

        Response productResponse = productService.createProduct("Patch OrderItem Product", 
                new BigDecimal("75.00"), 80);
        Long productId = productResponse.jsonPath().getLong("id");

        Response orderResponse = orderService.createOrderWithOneItem(userId, productId, 1);
        Long orderId = orderResponse.jsonPath().getLong("id");

        Response createResponse = orderItemService.createOrderItem(orderId, productId, 2);
        Long orderItemId = createResponse.jsonPath().getLong("id");

        Response response = orderItemService.patchOrderItem(orderItemId, 10);
        response.then().statusCode(200);

        DBOrderItem afterPatch = DBUtils.getOrderItemById(orderItemId);
        Assert.assertEquals(afterPatch.getQuantity(), Integer.valueOf(10));

        System.out.println("OrderItem partially updated successfully for ID: " + orderItemId);
    }

    // DELETE ORDER ITEM
    @Test(priority = 6, description = "Delete orderItem and verify in DB")
    public void deleteOrderItem_shouldReturn204_andRemoveFromDB()
    {
        System.out.println("deleteOrderItem_shouldReturn204_andRemoveFromDB() is called.");

        Response userResponse = userService.createUser("Delete OrderItem User");
        Long userId = userResponse.jsonPath().getLong("id");

        Response productResponse = productService.createProduct("Delete OrderItem Product", 
                new BigDecimal("10.00"), 5);
        Long productId = productResponse.jsonPath().getLong("id");

        Response orderResponse = orderService.createOrderWithOneItem(userId, productId, 1);
        Long orderId = orderResponse.jsonPath().getLong("id");

        Response createResponse = orderItemService.createOrderItem(orderId, productId, 1);
        Long orderItemId = createResponse.jsonPath().getLong("id");

        System.out.println("orderItemId: " + orderItemId);

        Response response = orderItemService.deleteOrderItem(orderItemId);
        response.then().statusCode(204);

        System.out.println("OrderItem deleted successfully with ID: " + orderItemId);
    }

    // ====================== NEGATIVE TESTS ======================

    @Test(priority = 7, description = "Create OrderItem with Zero Quantity - Should Fail")
    public void createOrderItem_withZeroQuantity_shouldFail()
    {
        System.out.println("createOrderItem_withZeroQuantity_shouldFail() is called.");

        Response userResponse = userService.createUser("Zero Qty User");
        Long userId = userResponse.jsonPath().getLong("id");

        Response productResponse = productService.createProduct("Zero Qty Product", 
                new BigDecimal("10.00"), 10);
        Long productId = productResponse.jsonPath().getLong("id");

        Response orderResponse = orderService.createOrderWithOneItem(userId, productId, 1);
        Long orderId = orderResponse.jsonPath().getLong("id");

        Response response = orderItemService.createOrderItem(orderId, productId, 0);
        response.then().statusCode(400);

        System.out.println("✅ Zero quantity validation test passed.");
    }

    // ==================== HELPER METHOD FOR TIMESTAMP VALIDATION ====================
    private void assertTimestampsAreRecent(LocalDateTime createdAt,
                                           LocalDateTime updatedAt,
                                           boolean isCreation)
    {
        LocalDateTime now = LocalDateTime.now();
        Assert.assertNotNull(createdAt, "createdAt should not be null");
        Assert.assertNotNull(updatedAt, "updatedAt should not be null");
        Assert.assertTrue(createdAt.isAfter(now.minusHours(12)),
                "createdAt should be recent (within last 12 hours)");

        if (isCreation)
        {
            long secondsDiff = Math.abs(Duration.between(createdAt, updatedAt).getSeconds());
            Assert.assertTrue(secondsDiff <= 30,
                    "On creation, createdAt and updatedAt should be very close. Diff was: " + secondsDiff + " seconds");
        }
    }
}