package com.sazhina.tests;

import io.restassured.response.Response;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sazhina.filters.CustomLogFilter.customLogFilter;
import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

public class ReqresTests {

    @Test
    @DisplayName("GET - Single user")
    void checkSingleUser() {
        given()
                .filter(customLogFilter().withCustomTemplates())
                .get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("data.first_name", is("Janet"))
                .body("data.last_name", is("Weaver"))
                .body("data.email", is("janet.weaver@reqres.in"));
    }

    @Test
    @DisplayName("GET - Single user not found")
    void checkSingleUserNotFound() {
        given()
                .filter(customLogFilter().withCustomTemplates())
                .get("https://reqres.in/api/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("GET - List resource")
    void checkListResource() {
        given()
                .filter(customLogFilter().withCustomTemplates())
                .get("https://reqres.in/api/unknown")
                .then()
                .statusCode(200)
                .body("per_page", is(6))
                .body("total", is(12));
    }

    @Test
    @DisplayName("GET - List resource with bad practice example")
    void checkListResourceBadPractice() {
        Response  response =
                given()
                        .filter(customLogFilter().withCustomTemplates())
                        .get("https://reqres.in/api/unknown")
                        .then()
                        .extract().response();

        assertThat(response.path("data").toString())
                .contains("blue turquoise");

    }

    @Test
    @DisplayName("POST - Create user")
    void checkCreateUser() {
        given()
                .filter(customLogFilter().withCustomTemplates())
                .contentType(JSON)
                .body("{ \"name\": \"Lena\", \"job\": \"QA\" }")
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .body("name", is("Lena"))
                .body("job", is("QA"));
    }

    @Test
    @DisplayName("PUT - Update user")
    void checkUpdateUserWithPut() {
        given()
                .filter(customLogFilter().withCustomTemplates())
                .contentType(JSON)
                .body("{ \"newTag\": \"newInfo\" }")
                .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("newTag", is("newInfo"));

    }

    @Test
    @DisplayName("PATCH - Update user")
    void checkUpdateUserWithPatch() {
        given()
                .filter(customLogFilter().withCustomTemplates())
                .contentType(JSON)
                .body("{ \"newTag2\": \"newInfo2\" }")
                .when()
                .patch("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("newTag2", is("newInfo2"))
                .body("updatedAt", Matchers.is(notNullValue()));

    }

    @Test
    @DisplayName("DELETE - Delete user")
    void checkDeleteUser() {
        given()
                .filter(customLogFilter().withCustomTemplates())
                .delete("https://reqres.in/api/users/2")
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("POST - Successful login")
    void successfulLogin() {
        given()
                .filter(customLogFilter().withCustomTemplates())
                .contentType(JSON)
                .body("{\"email\": \"eve.holt@reqres.in\"," +
                        "\"password\": \"cityslicka\"}")
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    @DisplayName("POST - Unsuccessful login")
    void negativeLogin() {
        given()
                .filter(customLogFilter().withCustomTemplates())
                .contentType(JSON)
                .body("{\"email\": \"eve.holt@reqres.in\"}")
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    @DisplayName("POST - Successful registration")
    void successfulRegister() {
        given()
                .filter(customLogFilter().withCustomTemplates())
                .contentType(JSON)
                .body("{\"email\": \"eve.holt@reqres.in\"," + "\"password\": \"pistol\"}")
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(200)
                .body("id", Matchers.is(notNullValue()))
                .body("token", Matchers.is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    @DisplayName("POST - Unsuccessful registration")
    void unsuccessfulRegister() {
        given()
                .filter(customLogFilter().withCustomTemplates())
                .contentType(JSON)
                .body("{\"email\": \"sydney@fife\"}")
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(400)
                .body("error", Matchers.is("Missing password"));
    }

}