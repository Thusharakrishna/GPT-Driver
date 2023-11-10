package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Sample {

    private static String baseUrl = "https://reqres.in/api/users";
    private static int id;

    @Test(priority = 1)
    public void createRequestTest() {
        String requestBody = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(baseUrl);

        response.then().statusCode(201);

        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("\"name\":\"morpheus\""));
        Assert.assertTrue(responseBody.contains("\"job\":\"leader\""));
        Assert.assertTrue(responseBody.contains("\"id\":"));
        Assert.assertTrue(responseBody.contains("\"createdAt\":"));

        id = response.jsonPath().getInt("id");
    }

    @Test(priority = 2)
    public void updateRequestTest() {
        String requestBody = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .put(baseUrl + "/" + id);

        response.then().statusCode(200);

        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("\"name\":\"morpheus\""));
        Assert.assertTrue(responseBody.contains("\"job\":\"zion resident\""));
    }

    @Test(priority = 3)
    public void deleteRequestTest() {
        Response response = RestAssured
                .given()
                .delete(baseUrl + "/" + id);

        response.then().statusCode(204);
    }

    @Test(priority = 4)
    public void fetchUserRequestTest() {
        Response response = RestAssured
                .given()
                .get(baseUrl + "/" + id);

        response.then().statusCode(404);
    }

    @Test(priority = 5)
    public void createRequestTestWithWrongData() {
        String requestBody = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(baseUrl);

        response.then().statusCode(201);

        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("\"name\":\"wrong name return\""));
        Assert.assertTrue(responseBody.contains("\"job\":\"wrong job return\""));
        Assert.assertTrue(responseBody.contains("\"id\":"));
        Assert.assertTrue(responseBody.contains("\"createdAt\":"));
    }
}
