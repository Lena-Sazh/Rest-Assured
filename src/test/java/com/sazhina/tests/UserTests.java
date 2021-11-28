package com.sazhina.tests;

import com.sazhina.lombok.LombokUserData;
import com.sazhina.models.UserData;
import org.junit.jupiter.api.Test;

import static com.sazhina.specs.Spec.baseRequest;
import static com.sazhina.specs.Spec.goodResponseSpec;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTests {
    @Test
    void singleUser() {
        // @formatter:off
        given()
                .spec(baseRequest)
                .when()
                .get("/users/2")
                .then()
                .spec(goodResponseSpec)
                .log().body();
        // @formatter:on
    }

    @Test
    void listOfUsers() {
        // @formatter:off
        given()
                .spec(baseRequest)
                .when()
                .get("/users?page=2")
                .then()
                .log().body();
        // @formatter:on
    }

    @Test
    void singleUserWithModel() {
        // @formatter:off
        UserData data = given()
                .spec(baseRequest)
                .when()
                .get("/users/2")
                .then()
                .spec(goodResponseSpec)
                .log().body()
                .extract().as(UserData.class);
        // @formatter:on
        assertEquals(2, data.getData().getId());
    }

    @Test
    void singleUserWithLombokModel() {
        // @formatter:off
        LombokUserData data = given()
                .spec(baseRequest)
                .when()
                .get("/users/2")
                .then()
                .spec(goodResponseSpec)
                .log().body()
                .extract().as(LombokUserData.class);
        // @formatter:on
        assertEquals(2, data.getUser().getId());
    }

    @Test
    public void checkEmailUsingGroovy() {
        // @formatter:off
        given()
                .spec(baseRequest)
                .when()
                .get("/users")
                .then()
                .log().body()
                .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                        hasItem("eve.holt@reqres.in"));
        // @formatter:on
    }

}
