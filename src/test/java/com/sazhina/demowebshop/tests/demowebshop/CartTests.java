package com.sazhina.demowebshop.tests.demowebshop;

import com.codeborne.selenide.Selenide;
import com.sazhina.demowebshop.config.webshop.App;
import com.sazhina.demowebshop.helpers.AllureRestAssuredFilter;
import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class CartTests extends TestBase {

    @BeforeAll
    static void configureBaseUrl() {
        RestAssured.baseURI = App.config.apiUrl();
        Configuration.baseUrl = App.config.webUrl();
    }

    @Test
    @DisplayName("Add item to Cart (API + UI)")
    void addItemToCartWithCookieTest() {
        step("Get cookie by API and pass it to browser", () -> {
            String authorizationCookie =
                    given()
                            .filter(AllureRestAssuredFilter.withCustomTemplates())
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .formParam("Email", App.config.userLogin())
                            .formParam("Password", App.config.userPassword())
                            .when()
                            .post("/login")
                            .then()
                            .statusCode(302)
                            .extract()
                            .cookie("NOPCOMMERCE.AUTH");

            step("Open some content to set cookie", () ->
                    open("/Themes/DefaultClean/Content/images/logo.png"));

            step("Pass cookie to browser", () ->
                    getWebDriver().manage().addCookie(
                            new Cookie("NOPCOMMERCE.AUTH", authorizationCookie)));
        });

        step("Add item to Cart", () -> {
            Response response =
                    given()
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .body("product_attribute_16_5_4=14&product_attribute_16_6_5=15&" +
                                    "product_attribute_16_3_6=19&product_attribute_16_4_7=44&" +
                                    "product_attribute_16_8_8=22&addtocart_16.EnteredQuantity=1")
                            .cookie("Nop.customer=876119dc-dd7f-4867-8bd1-a2502d97224c;")
                            .when()
                            .post("http://demowebshop.tricentis.com/addproducttocart/details/16/1")
                            .then()
                            .statusCode(200)
                            .body("success", is(true))
                            .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                            .extract().response();

            System.out.println("Response: " + response.path("updatetopcartsectionhtml"));
        });

        step("Open Cart page and check item quantity", () ->{
            given()
                    .get("/cart")
                    .then()
                    .statusCode(200);
        });

        open("/cart");
        Selenide.$x("//span[@class='cart-qty']").shouldHave(text("(1)"));
    }
}
