package com.sazhina.tests;

import com.sazhina.models.UserData;
import com.sazhina.lombok.LombokUserData;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sazhina.specs.Spec.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresTestsSpec {

    @Test
    @DisplayName("SPEC+Lombok - Single user")
    void checkSingleUser() {
        LombokUserData data = given()
                .spec(baseRequest)
                .get("/users/5")
                .then()
                .spec(goodResponseSpec)
                .log().body()
                .extract().as(LombokUserData.class);
        assertEquals(5, data.getUser().getId());
        assertEquals("charles.morris@reqres.in", data.getUser().getEmail());
        assertEquals("Charles", data.getUser().getFirstName());
        assertEquals("Morris", data.getUser().getLastName());
    }

    @Test
    @DisplayName("SPEC+Groovy - Users list")
    void checkUsersList() {
        given()
                .spec(baseRequest)
                .when()
                .get("/users?page=2")
                .then()
                .spec(goodResponseSpec)
                .log().body()
                .body("data.findAll{it.id}.id.flatten()", hasItem(12))
                .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()", hasItem("lindsay.ferguson@reqres.in"))
                .body("data.findAll{it.first_name}.first_name.flatten()", hasItem("George"))
                .body("data.findAll{it.last_name}.last_name.flatten()", hasItem("Fields"));
    }

    @Test
    @DisplayName("SPEC - Single user not found")
    void checkSingleUserNotFound() {
        given()
                .spec(baseRequest)
                .get("/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("SPEC+Model - Create user") // не работает
    void checkCreateUser() {
        UserData data = given()
                .spec(baseRequest)
                .body("{ \"first_name\": \"Lena\", \"job\": \"QA\" }")
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .extract().as(UserData.class);
        assertEquals("Lena", data.getData().getFirstName());
        assertEquals("QA", data.getData().getJob());
    }

    @Test
    @DisplayName("SPEC - Delete user")
    void checkDeleteUser() {
        given()
                .spec(baseRequest)
                .delete("/users/2")
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("SPEC - Successful login")
    void successfulLogin() {
        given()
                .spec(baseRequest)
                .body("{\"email\": \"eve.holt@reqres.in\"," +
                        "\"password\": \"cityslicka\"}")
                .when()
                .post("/login")
                .then()
                .spec(goodResponseSpec);
    }

    @Test
    @DisplayName("SPEC - Unsuccessful login")
    void negativeLogin() {
        given()
                .spec(baseRequest)
                .body("{\"email\": \"eve.holt@reqres.in\"}")
                .when()
                .post("/login")
                .then()
                .spec(badResponseSpec);
    }

    @Test
    @DisplayName("SPEC - Successful registration")
    void successfulRegister() {
        given()
                .spec(baseRequest)
                .body("{\"email\": \"eve.holt@reqres.in\"," + "\"password\": \"pistol\"}")
                .when()
                .post("/register")
                .then()
                .spec(goodResponseSpec);
    }

    @Test
    @DisplayName("SPEC - Unsuccessful registration")
    void unsuccessfulRegister() {
        given()
                .spec(baseRequest)
                .body("{\"email\": \"sydney@fife\"}")
                .when()
                .post("/register")
                .then()
                .spec(badResponseSpec);
    }

}
