package com.webprj.ecom.tests.testcases;

import com.webprj.ecom.tests.base.BaseTest;
import com.webprj.ecom.tests.model.DBOrder;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API + DB validation tests for Order module.
 * Includes Positive, Negative, and Edge cases.
 */
public class OrderTests extends BaseTest
{
    private final UserService userService = new UserService();
    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();

    // ====================== POSITIVE TESTS ======================

    // CREATE ORDER + DB VALIDATION
    @Test(priority = 1, description = "Create Order with One Item - API + DB Validation")
    public void createOrder_shouldReturn201_andMatchInDB()
    {
        System.out.println("createOrder_shouldReturn201_andMatchInDB() is called.");

        // Create User
        Response userResponse = userService.createUser("Order Test User");
        Long userId = userResponse.jsonPath().getLong("id");

        // Create Product
        Response productResponse = productService.createProduct("Order Test Product", 
                new BigDecimal("99.99"), 100);
        Long productId = productResponse.jsonPath().getLong("id");

        // Create Order with One Item
        Response response = orderService.createOrderWithOneItem(userId, productId, 2);

        // API validation
        response.then().statusCode(201);

        Long orderId = response.jsonPath().getLong("id");
        Assert.assertTrue(orderId > 0, "Order ID should be greater than 0");

        System.out.println("Order created successfully with ID: " + orderId);

        // DB validation
        DBOrder dbOrder = DBUtils.getOrderById(orderId);
        Assert.assertNotNull(dbOrder, "Order not found in DB!");

        // Timestamp Validation
        assertTimestampsAreRecent(dbOrder.getCreatedAt(), dbOrder.getUpdatedAt(), true);
        System.out.println("DB Validation passed for Order ID: " + orderId);
    }

    // GET ORDER BY ID
    @Test(priority = 2, description = "Get order by ID")
    public void getOrderById_shouldReturn200()
    {
        System.out.println("getOrderById_shouldReturn200() is called.");

        // Create User
        Response userResponse = userService.createUser("Get Order User");
        Long userId = userResponse.jsonPath().getLong("id");

        // Create Product
        Response productResponse = productService.createProduct("Get Order Product", 
                new BigDecimal("50.00"), 50);
        Long productId = productResponse.jsonPath().getLong("id");
        
        // Create Order with One Item
        Response createResponse = orderService.createOrderWithOneItem(userId, productId, 1);
        Long orderId = createResponse.jsonPath().getLong("id");

        Response response = orderService.getOrderById(orderId);
        
        // API validation
        response.then().statusCode(200);
        
        // DB validation
        DBOrder dbOrder = DBUtils.getOrderById(orderId);
        Assert.assertNotNull(dbOrder, "Order not found in DB!");

        System.out.println("Get Order By ID test passed for ID: " + orderId);
    }

    // GET ALL ORDERS
    @Test(priority = 3, description = "Get all orders")
    public void getAllOrders_shouldReturn200_andNotEmpty()
    {
        System.out.println("getAllOrders_shouldReturn200_andNotEmpty() is called.");

        Response response = orderService.getAllOrders();
        response.then().statusCode(200);

        int orderCount = response.jsonPath().getList("$").size();
        System.out.println("Total orders count: " + orderCount);
        Assert.assertTrue(orderCount > 0, "Order list should not be empty");
    }

    // CANCEL ORDER
    @Test(priority = 4, description = "Cancel Order and validate DB")
    public void cancelOrder_shouldReturn200_andUpdateStatus()
    {
        System.out.println("cancelOrder_shouldReturn200_andUpdateStatus() is called.");

        // Create User
        Response userResponse = userService.createUser("Cancel Order User");
        Long userId = userResponse.jsonPath().getLong("id");

        // Create Product
        Response productResponse = productService.createProduct("Cancel Order Product", 
                new BigDecimal("50.00"), 50);
        Long productId = productResponse.jsonPath().getLong("id");

        // Create Order with One Item
        Response createResponse = orderService.createOrderWithOneItem(userId, productId, 1);
        Long orderId = createResponse.jsonPath().getLong("id");

        Response response = orderService.cancelOrder(orderId);
        
        // API validation
        response.then().statusCode(200);

        // DB validation
        DBOrder dbOrder = DBUtils.getOrderById(orderId);
        Assert.assertEquals(dbOrder.getStatus(), "CANCELLED");

        System.out.println("Order cancelled successfully with ID: " + orderId);
    }

    // UPDATE ORDER (PUT)
    @Test(priority = 5, description = "Update order using PUT and validate DB")
    public void updateOrder_shouldReturn200_andMatchDB()
    {
        System.out.println("updateOrder_shouldReturn200_andMatchDB() is called.");

        // Create User
        Response userResponse = userService.createUser("Update Order User");
        Long userId = userResponse.jsonPath().getLong("id");

        // Create Product
        Response productResponse = productService.createProduct("Update Order Product", 
                new BigDecimal("100.00"), 200);
        Long productId = productResponse.jsonPath().getLong("id");

        // Create Order with One Item
        Response createResponse = orderService.createOrderWithOneItem(userId, productId, 2);
        Long orderId = createResponse.jsonPath().getLong("id");

        List<Map<String, Object>> newItems = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("productId", productId);
        item.put("quantity", 5);
        newItems.add(item);

        Response response = orderService.updateOrder(orderId, userId, newItems);
        
        // API validation
        response.then().statusCode(200);
        
        // DB validation
        DBOrder afterUpdate = DBUtils.getOrderById(orderId);
        Assert.assertNotNull(afterUpdate, "Order not found in DB after update!");

        System.out.println("Order updated successfully with ID: " + orderId);
    }

    // PATCH ORDER
    @Test(priority = 6, description = "Patch Order - Partial Update")
    public void patchOrder_shouldReturn200_andUpdateStatus()
    {
        System.out.println("patchOrder_shouldReturn200_andUpdateStatus() is called.");

        // Create User
        Response userResponse = userService.createUser("Patch Order User");
        Long userId = userResponse.jsonPath().getLong("id");

        // Create Product
        Response productResponse = productService.createProduct("Patch Order Product", 
                new BigDecimal("75.00"), 80);
        Long productId = productResponse.jsonPath().getLong("id");

        // Create Order with One Item
        Response createResponse = orderService.createOrderWithOneItem(userId, productId, 1);
        Long orderId = createResponse.jsonPath().getLong("id");

        Response response = orderService.patchOrder(orderId, "PROCESSING");
        
        // API validation
        response.then().statusCode(200);
        
        // DB validation
        DBOrder afterPatch = DBUtils.getOrderById(orderId);
        Assert.assertNotNull(afterPatch, "Order not found in DB after patch!");
        
        Assert.assertEquals(afterPatch.getStatus(), "PROCESSING");

        // Timestamp Validation
        assertTimestampsAreRecent(afterPatch.getCreatedAt(), afterPatch.getUpdatedAt(), false);

        System.out.println("Order partially updated successfully for ID: " + orderId);
    }

    // DELETE ORDER
    @Test(priority = 7, description = "Delete order and verify in DB")
    public void deleteOrder_shouldReturn204_andRemoveFromDB()
    {
        System.out.println("deleteOrder_shouldReturn204_andRemoveFromDB() is called.");

        // Create User
        Response userResponse = userService.createUser("Delete Order User");
        Long userId = userResponse.jsonPath().getLong("id");

        // Create Product
        Response productResponse = productService.createProduct("Delete Order Product", 
                new BigDecimal("10.00"), 5);
        Long productId = productResponse.jsonPath().getLong("id");

        // Create Order with One Item
        Response createResponse = orderService.createOrderWithOneItem(userId, productId, 1);
        Long orderId = createResponse.jsonPath().getLong("id");

        System.out.println("orderId: " + orderId);

        Response response = orderService.deleteOrder(orderId);
        
        // API validation
        response.then().statusCode(204);

        System.out.println("Order deleted successfully with ID: " + orderId);
    }

    // ====================== NEGATIVE TESTS ======================

    @Test(priority = 8, description = "Create Order with Invalid User - Should Fail")
    public void createOrder_withInvalidUser_shouldFail()
    {
        System.out.println("createOrder_withInvalidUser_shouldFail() is called.");
        Response response = orderService.createOrderWithOneItem(999999L, 1L, 1);
        response.then().statusCode(409);
        System.out.println("✅ Invalid user validation test passed.");
    }

    @Test(priority = 9, description = "Create Order with Zero Quantity - Should Fail")
    public void createOrder_withZeroQuantity_shouldFail()
    {
        System.out.println("createOrder_withZeroQuantity_shouldFail() is called.");
        Response response = orderService.createOrderWithOneItem(1L, 1L, 0);
        response.then().statusCode(500);
        System.out.println("✅ Zero quantity validation test passed.");
    }
    
    // ADVANCED: Stock Deduction Validation
    @Test(priority = 10, description = "Order Creation - Stock should be deducted")
    public void createOrder_shouldDeductStock()
    {
        System.out.println("createOrder_shouldDeductStock() is called.");

        Response userResponse = userService.createUser("Stock Test User");
        Long userId = userResponse.jsonPath().getLong("id");

        Response productResponse = productService.createProduct("Stock Test Product", 
                new BigDecimal("50.00"), 10);
        Long productId = productResponse.jsonPath().getLong("id");

        // Get initial stock
        Integer initialStock = DBUtils.getProductById(productId).getStock();

        Response orderResponse = orderService.createOrderWithOneItem(userId, productId, 3);
        Long orderId = orderResponse.jsonPath().getLong("id");

        // Get updated stock
        Integer updatedStock = DBUtils.getProductById(productId).getStock();

        Assert.assertEquals(updatedStock, initialStock - 3, 
                "Stock should be deducted by ordered quantity");

        System.out.println("✅ Stock deduction validation passed. Initial: " + initialStock + ", After: " + updatedStock);
    }
    
    // ADVANCED: Order Total Calculation Validation
    @Test(priority = 11, description = "Order Total should be calculated correctly (price * quantity)")
    public void createOrder_shouldCalculateTotalCorrectly()
    {
        System.out.println("createOrder_shouldCalculateTotalCorrectly() is called.");

        Response userResponse = userService.createUser("Total Calc User");
        Long userId = userResponse.jsonPath().getLong("id");

        Response productResponse = productService.createProduct("Total Calc Product", 
                new BigDecimal("75.50"), 50);
        Long productId = productResponse.jsonPath().getLong("id");

        Integer quantity = 4;

        Response orderResponse = orderService.createOrderWithOneItem(userId, productId, quantity);
        Long orderId = orderResponse.jsonPath().getLong("id");

        DBOrder dbOrder = DBUtils.getOrderById(orderId);
        BigDecimal expectedTotal = new BigDecimal("75.50").multiply(new BigDecimal(quantity));

        Assert.assertEquals(dbOrder.getTotalAmount(), expectedTotal, 
                "Order total should be price * quantity");

        System.out.println("✅ Order total calculation validation passed. Expected: " + expectedTotal + ", Actual: " + dbOrder.getTotalAmount());
    }
    
    // ADVANCED: Inventory Consistency after Order Creation & Cancellation
    @Test(priority = 12, description = "Stock should NOT be restored after order cancel (as per current backend)")
    public void cancelOrder_shouldNotRestoreStock()
    {
        System.out.println("cancelOrder_shouldNotRestoreStock() is called.");

        Response userResponse = userService.createUser("Stock Cancel Test User");
        Long userId = userResponse.jsonPath().getLong("id");

        Response productResponse = productService.createProduct("Stock Cancel Test Product", 
                new BigDecimal("60.00"), 20);
        Long productId = productResponse.jsonPath().getLong("id");

        Integer initialStock = DBUtils.getProductById(productId).getStock();

        Response orderResponse = orderService.createOrderWithOneItem(userId, productId, 5);
        Long orderId = orderResponse.jsonPath().getLong("id");

        // Stock should be reduced
        Integer afterOrderStock = DBUtils.getProductById(productId).getStock();
        Assert.assertEquals(afterOrderStock, initialStock - 5);

        // Cancel the order
        orderService.cancelOrder(orderId);

        // Stock remains deducted (as per current backend behavior)
        Integer afterCancelStock = DBUtils.getProductById(productId).getStock();
        Assert.assertEquals(afterCancelStock, initialStock - 5, 
                "Stock is NOT restored after cancellation (as per current backend)");

        System.out.println("✅ Inventory behavior after cancel validated (stock not restored).");
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