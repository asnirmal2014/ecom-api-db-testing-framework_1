package com.webprj.ecom.tests.client;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * UserClient.java
 * This class contains all HTTP methods to interact with User endpoints.
 * It uses RestAssured to make API calls.
 */
public class UserClient 
{
    private static final String BASE_PATH = "/api/users";
    
    /*
    Here's the rewritten code with the return statement at the last:

    // CREATE USER
    public Response createUser(Object userPayload) 
    {
    	System.out.println("✅ userPayload: " + userPayload + " - createUser()");
    
    	Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(userPayload)
            .when()
            .post(BASE_PATH);
    
    	return response;
    }
	
	The key changes:
	1. Stored the RestAssured response in a local variable `response`
	2. Placed the `return response;` statement as the last line of the method
    */

    /**
     * CREATE USER - POST /api/users
     */
    public Response createUser(Object userPayload) 
    {
        System.out.println("✅ userPayload: " + userPayload + " - createUser()");
        
        /*
    	Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(userPayload)
            .when()
            .post(BASE_PATH);
    
    	return response; 
    	*/
        
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(userPayload)
                .when()
                .post(BASE_PATH);
    }

    /**
     * GET USER BY ID - GET /api/users/{id}
     */
    public Response getUserById(Long userId) 
    {
        return RestAssured
                .given()
                .when()
                .get(BASE_PATH + "/" + userId);
    }

    /**
     * GET ALL USERS - GET /api/users
     */
    public Response getAllUsers() 
    {
        return RestAssured
                .given()
                .when()
                .get(BASE_PATH);
    }

    /**
     * FULL UPDATE (PUT) - PUT /api/users/{id}
     */
    public Response updateUser(Long userId, Object payload) 
    {
        System.out.println("✅ Payload: " + payload + " - updateUser()");
        
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .put(BASE_PATH + "/" + userId);
    }

    /**
     * PARTIAL UPDATE (PATCH) - PATCH /api/users/{id}
     */
    public Response patchUser(Long userId, Object payload) 
    {
        System.out.println("✅ Payload: " + payload + " - patchUser()");
        
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .patch(BASE_PATH + "/" + userId);
    }

    /**
     * DELETE USER - DELETE /api/users/{id}
     */
    public Response deleteUser(Long userId) 
    {
        return RestAssured
                .given()
                .when()
                .delete(BASE_PATH + "/" + userId);
    }
}