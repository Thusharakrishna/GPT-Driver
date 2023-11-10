package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ApiAutomation {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://reqres.in";
    }

    @Test(priority = 1)
    public void createRequestTest() {
        String requestBody = "{\"name\": \"morpheus\",\"job\": \"leader\"}";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/users");

        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertEquals(response.jsonPath().get("name"), "morpheus");
        Assert.assertEquals(response.jsonPath().get("job"), "leader");

        String id = response.jsonPath().get("id");
        String createdAt = response.jsonPath().get("createdAt");

        // Use id and createdAt for subsequent requests
    }

    @Test(priority = 2)
    public void updateRequestTest() {
        String requestBody = "{\"name\": \"morpheus\",\"job\": \"zion resident\"}";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .put("/api/users/{id}", 236);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().get("name"), "morpheus");
        Assert.assertEquals(response.jsonPath().get("job"), "zion resident");

        // Use response data for validation or subsequent requests
    }

    @Test(priority = 3)
    public void deleteRequestTest() {
        Response response = RestAssured.delete("/api/users/{id}", 236);

        Assert.assertEquals(response.getStatusCode(), 204);

        // Use response data for validation or subsequent requests
    }

    @Test(priority = 4)
    public void fetchUserRequestTest() {
        Response response = RestAssured.get("/api/users/{id}", 236);

        Assert.assertEquals(response.getStatusCode(), 404);

        // Use response data for validation or subsequent requests
    }

    @Test(priority = 5)
    public void createRequestFailure() {
        String requestBody = "{\"name\": \"morpheus\",\"job\": \"leader\"}";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/users");

        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertEquals(response.jsonPath().get("name"), "wrong name return");
        Assert.assertEquals(response.jsonPath().get("job"), "wrong job return");

        String id = response.jsonPath().get("id");
        String createdAt = response.jsonPath().get("createdAt");

        // Use id and createdAt for subsequent requests
    }
}
