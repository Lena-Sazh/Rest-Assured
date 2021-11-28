package com.sazhina.specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;

import static com.sazhina.filters.CustomLogFilter.customLogFilter;
import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

public class Spec {
    public static RequestSpecification baseRequest = with()
            .baseUri("https://reqres.in")
            .basePath("/api")
            .filter(customLogFilter().withCustomTemplates())
            .log().all()
            .contentType(ContentType.JSON);

    public static ResponseSpecification goodResponseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectBody("id", Matchers.is(notNullValue()))
            .build();

    public static ResponseSpecification badResponseSpec = new ResponseSpecBuilder()
            .expectStatusCode(400)
            .expectBody("error", is("Missing password"))
            .build();
}