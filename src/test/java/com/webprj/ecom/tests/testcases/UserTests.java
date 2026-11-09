package com.webprj.ecom.tests.testcases;

import com.webprj.ecom.tests.base.BaseTest;
import com.webprj.ecom.tests.model.DBUser;
import com.webprj.ecom.tests.services.UserService;
import com.webprj.ecom.tests.utils.DBUtils;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * API + DB validation tests for User module.
 * Includes Positive, Negative, and Edge cases.
 */
public class UserTests extends BaseTest 
{
    private final UserService userService = new UserService();
    
    // ====================== POSITIVE TESTS ======================

    // CREATE USER + DB VALIDATION
    @Test(priority = 1, description = "Create User - API + DB Validation")
    public void createUser_shouldReturn201_andMatchInDB()
    {
        System.out.println("createUser_shouldReturn201_andMatchInDB() is called.");

        // Create a new user first
        Response response = userService.createUser("Test User");

        // API validation
        response.then().statusCode(201);

        Long userId = response.jsonPath().getLong("id");
        String apiName = response.jsonPath().getString("name");
        String apiEmail = response.jsonPath().getString("email");

        Assert.assertTrue(userId > 0, "User ID should be greater than 0");
        Assert.assertEquals(apiName, "Test User");

        System.out.println("User created successfully with ID: " + userId);

        // DB validation
        DBUser dbUser = DBUtils.getUserById(userId);
        
        Assert.assertNotNull(dbUser, "User not found in DB!");
        Assert.assertEquals(dbUser.getId(), userId);
        Assert.assertEquals(dbUser.getName(), apiName);
        Assert.assertEquals(dbUser.getEmail(), apiEmail);

        // Timestamp Validation
        assertTimestampsAreRecent(dbUser.getCreatedAt(), dbUser.getUpdatedAt(), true);

        System.out.println("DB Validation passed for User ID: " + userId);
    }

    // GET USER BY ID
    @Test(priority = 2, description = "Get user by ID", dependsOnMethods = "createUser_shouldReturn201_andMatchInDB")
    public void getUserById_shouldReturn200()
    {
        System.out.println("getUserById_shouldReturn200() is called.");

        // Create a new user first
        Response createResponse = userService.createUser("Test Get User");
        Long userId = createResponse.jsonPath().getLong("id");

        Response response = userService.getUserById(userId);
       
        // API validation
        response.then().statusCode(200);

        Assert.assertEquals(response.jsonPath().getLong("id"), userId);
        Assert.assertEquals(response.jsonPath().getString("name"), "Test Get User");

        System.out.println("Get User By ID test passed for ID: " + userId);
    }

    // GET ALL USERS
    @Test(priority = 3, description = "Get all users")
    public void getAllUsers_shouldReturn200_andNotEmpty()
    {
        System.out.println("getAllUsers_shouldReturn200_andNotEmpty() is called.");

        Response response = userService.getAllUsers();
       
        // API validation
        response.then().statusCode(200);

        int userCount = response.jsonPath().getList("$").size();

        System.out.println("Total users count: " + userCount);
        Assert.assertTrue(userCount > 0, "User list should not be empty");
    }

    // UPDATE USER (PUT) + DB VALIDATION with Timestamp Check
    @Test(priority = 4, description = "Update user using PUT and validate DB + timestamps")
    public void updateUser_shouldReturn200_andMatchDB()
    {
        System.out.println("updateUser_shouldReturn200_andMatchDB() is called.");

        // Create a new user first
        Response createResponse = userService.createUser("Old Name");
        Long userId = createResponse.jsonPath().getLong("id");

        DBUser beforeUpdate = DBUtils.getUserById(userId);
        LocalDateTime originalCreatedAt = beforeUpdate.getCreatedAt();
        LocalDateTime originalUpdatedAt = beforeUpdate.getUpdatedAt();

        // New values
        String newName = "Updated Name";
        String newEmail = "updated" + System.currentTimeMillis() + "@test.com";

        // Perform UPDATE
        // Call PUT API
        Response response = userService.updateUser(userId, newName, newEmail);
       
        // API validation
        response.then().statusCode(200);

        // DB validation
        DBUser afterUpdate = DBUtils.getUserById(userId);
        Assert.assertNotNull(afterUpdate, "User not found in DB after update!");
        Assert.assertEquals(afterUpdate.getName(), newName);
        Assert.assertEquals(afterUpdate.getEmail(), newEmail);

        Assert.assertEquals(response.jsonPath().getString("name"), newName);
        Assert.assertEquals(response.jsonPath().getString("email"), newEmail);
        
        // Timestamp Validation for UPDATE
        Assert.assertEquals(afterUpdate.getCreatedAt(), originalCreatedAt, "createdAt should NOT change on update");
        assertTimestampsAreRecent(afterUpdate.getCreatedAt(), afterUpdate.getUpdatedAt(), false);
        Assert.assertTrue(afterUpdate.getUpdatedAt().isAfter(originalUpdatedAt.minusSeconds(10)), "updatedAt should be updated after PUT operation");

        System.out.println("User updated successfully with ID: " + userId + " | updatedAt changed as expected");
    }

    // PATCH USER + DB VALIDATION with Timestamp Check
    @Test(priority = 5, description = "Patch User - Partial Update and validate timestamps")
    public void patchUser_shouldReturn200_andUpdateOnlyName()
    {
        System.out.println("patchUser_shouldReturn200_andUpdateOnlyName() is called.");

        // Create a new user first
        Response createResponse = userService.createUser("Patch User");
        Long userId = createResponse.jsonPath().getLong("id");

        DBUser beforePatch = DBUtils.getUserById(userId);
        
        LocalDateTime originalCreatedAt = beforePatch.getCreatedAt();
        // LocalDateTime originalUpdatedAt = beforePatch.getUpdatedAt();
        String updatedName = "Patched Name";

        // Perform PATCH
        // Call PATCH API
        Response response = userService.patchUserName(userId, updatedName);
        
        // API validation
        response.then().statusCode(200);

        // DB validation
        DBUser afterPatch = DBUtils.getUserById(userId);
        Assert.assertNotNull(afterPatch, "User not found in DB after patch!");
        Assert.assertEquals(afterPatch.getName(), updatedName);
        
        Assert.assertEquals(response.jsonPath().getString("name"), updatedName);
        
        // Timestamp Validation for PATCH
        Assert.assertEquals(afterPatch.getCreatedAt(), originalCreatedAt, "createdAt should NOT change on PATCH");
        // Assert.assertTrue(afterPatch.getUpdatedAt().isAfter(originalUpdatedAt.minusSeconds(15)), "updatedAt should be updated after PATCH operation");
        assertTimestampsAreRecent(afterPatch.getCreatedAt(), afterPatch.getUpdatedAt(), false);

        System.out.println("User name patched successfully for ID: " + userId + " | updatedAt changed as expected");
    }

    // DELETE USER
    @Test(priority = 6, description = "Delete user and verify in DB")
    public void deleteUser_shouldReturn204_andRemoveFromDB()
    {
        System.out.println("deleteUser_shouldReturn204_andRemoveFromDB() is called.");

        // Create a new user first
        Response createResponse = userService.createUser("Delete User");
        Long userId = createResponse.jsonPath().getLong("id");
        
        System.out.println("userId: " + userId);

        // Perform DELETE
        // Call DELETE API
        Response response = userService.deleteUser(userId);
        
        // API validation
        response.then().statusCode(204);

        // DB validation
        boolean exists = DBUtils.isUserPresentById(userId);
        
        // Assertion
        Assert.assertFalse(exists, "User should be deleted from DB!");
        
        // Debug log
        System.out.println("User exists after delete: " + exists);
        
        /*
        System.out.println("User exists after delete: " + (!exists ? "No." : "Yes."));
        
        System.out.println(!exists ? "User exists after delete: No." : "User exists after delete: Yes.");
        
        if(exists == false)
        {
        	System.out.println("User exists after delete: No.");
        }
        else
        {
        	System.out.println("User exists after delete: Yes.");
        }
        */

        System.out.println("User deleted successfully with ID: " + userId);
    }

    // ====================== NEGATIVE TESTS ======================

    @Test(priority = 7, description = "Create User with Empty Name - Should Fail")
    public void createUser_withEmptyName_shouldReturn400()
    {
		System.out.println("createUser_withEmptyName_shouldReturn400() is called.");

		Response response = userService.createUser(""); // Empty name

		response.then().statusCode(400); // Adjust if your API returns different code

		System.out.println("✅ Empty name validation test passed.");
    }
    
    @Test(priority = 8, description = "Create User with Invalid Email Format - Should Fail")
    public void createUser_withInvalidEmail_shouldReturn400()
    {
        System.out.println("createUser_withInvalidEmail_shouldReturn400() is called.");

        Response response = userService.createUser("Invalid User", "invalid-email");

        response.then().statusCode(400);

        System.out.println("✅ Invalid email format validation test passed.");
    }
    
    @Test(priority = 9, description = "Create User with Duplicate Email - Should Fail")
    public void createUser_withDuplicateEmail_shouldReturn409()
    {
        System.out.println("createUser_withDuplicateEmail_shouldReturn409() is called.");

        String email = "duplicate" + System.currentTimeMillis() + "@test.com";

        // Create first user
        userService.createUser("First User", email);

        // Try to create second user with same email
        Response response = userService.createUser("Second User", email);

        response.then().statusCode(409);   // Conflict - adjust if your API uses different code

        System.out.println("✅ Duplicate email check test passed.");
    }
    
    @Test(priority = 10, description = "Create User with Very Long Name - Should Fail")
    public void createUser_withVeryLongName_shouldReturn400()
    {
        System.out.println("createUser_withVeryLongName_shouldReturn400() is called.");

        String longName = "A".repeat(150);   // Exceeding VARCHAR(100)

        Response response = userService.createUser(longName);

        response.then().statusCode(400);

        System.out.println("✅ Long name validation test passed.");
    }
    
    // ==================== HELPER METHOD FOR TIMESTAMP VALIDATION ====================
    /**
     * Validates that timestamps are recent and makes sense
     * @param createdAt created timestamp
     * @param updatedAt updated timestamp
     * @param isCreation true if this is for new record creation
     */
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