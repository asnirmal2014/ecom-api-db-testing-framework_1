package com.webprj.ecom.tests.services;

import com.webprj.ecom.tests.client.UserClient;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the service layer for User API testing.
 * It prepares request payloads and calls the UserClient methods.
 */
// Controller → Service → Repository → MySQL
public class UserService 
{
    private final UserClient userClient = new UserClient();

    /**
     * CREATE USER with dynamic email (Recommended for most tests)
     */
    public Response createUser(String name) 
    {
        String email = "user" + System.currentTimeMillis() + "@test.com";

        Map<String, Object> payload = new HashMap<>();
        payload.put("name", name);
        payload.put("email", email);

        return userClient.createUser(payload);
    }

    /**
     * CREATE USER with custom name and email
     */
    public Response createUser(String name, String email) 
    {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", name);
        payload.put("email", email);

        return userClient.createUser(payload);
    }

    /**
     * GET USER BY ID
     */
    public Response getUserById(Long userId) 
    {
        return userClient.getUserById(userId);
    }

    /**
     * GET ALL USERS
     */
    public Response getAllUsers() 
    {
        return userClient.getAllUsers();
    }

    /**
     * FULL UPDATE (PUT)
     */
    public Response updateUser(Long userId, String name, String email) 
    {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", name);
        payload.put("email", email);

        return userClient.updateUser(userId, payload);
    }

    /**
     * PARTIAL UPDATE (PATCH) - Only Name
     */
    public Response patchUserName(Long userId, String name) 
    {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", name);

        return userClient.patchUser(userId, payload);
    }

    /**
     * DELETE USER
     */
    public Response deleteUser(Long userId) 
    {
        return userClient.deleteUser(userId);
    }
}