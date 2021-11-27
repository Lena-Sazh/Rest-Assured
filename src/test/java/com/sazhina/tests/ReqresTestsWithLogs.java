package com.sazhina.tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;

public class ReqresTestsWithLogs {

    @Test
    void checkSingleUserAllLogs() {
        get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .log().all()
                .body("data.first_name", is("Janet"))
                .body("data.last_name", is("Weaver"))
                .body("data.email", is("janet.weaver@reqres.in"));
    }

    @Test
    void checkSingleUserNotFoundNoLogs() {
        get("https://reqres.in/api/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    void checkListResourceNoLogs() {
        get("https://reqres.in/api/unknown")
                .then()
                .statusCode(200)
                .body("per_page", is(6))
                .body("total", is(12));

    }

    @Test
    void checkCreateUserWithSomeLogs() {
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
    void checkUpdateUserWithSomeLogs() {
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
    void checkDeleteUserWithAllLogs() {
        given()
                .delete("https://reqres.in/api/users/2")
                .then()
                .statusCode(204);
    }

    @Test
    void successfulLoginWithListener() {
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
    void successfulLoginWithTemplates() {
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
    void successfulLoginWithScheme() {
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
    void negativeLoginWithListener() {
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