package com.sazhina.tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;

public class ReqresTests {

    @Test
    void checkSingleUser() {
        get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("data.first_name", is("Janet"))
                .body("data.last_name", is("Weaver"))
                .body("data.email", is("janet.weaver@reqres.in"));
    }

    @Test
    void checkSingleUserNotFound() {
        get("https://reqres.in/api/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    void checkListResource() {
        get("https://reqres.in/api/unknown")
                .then()
                .statusCode(200)
                .body("per_page", is(6))
                .body("total", is(12));

    }

    @Test
    void checkListResourceBadPractice() {
        Response  response =
                get("https://reqres.in/api/unknown")
                        .then()
                        .extract().response();

        assertThat(response.path("data").toString())
                .contains("blue turquoise");

    }

    @Test
    void checkCreateUser() {
        given()
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
    void checkUpdateUser() {
        given()
                .contentType(JSON)
                .body("{ \"newTag\": \"newInfo\" }")
                .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("newTag", is("newInfo"));

    }

    @Test
    void checkDeleteUser() {
        given()
                .delete("https://reqres.in/api/users/2")
                .then()
                .statusCode(204);
    }

    @Test
    void successfulLogin() {
        given()
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
    void negativeLogin() {
        given()
                .contentType(JSON)
                .body("{\"email\": \"eve.holt@reqres.in\"}")
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

}