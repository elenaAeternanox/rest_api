package com.github.elenaAeternaNox.rest_api.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.github.elenaAeternaNox.rest_api.test_base.UiTestBase;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.github.elenaAeternaNox.rest_api.filters.CustomLogFilter.customLogFilter;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.is;

public class DemowebshopTest extends UiTestBase {

    private String body;

    private RequestSpecification demoWebShopRequest = with()
            .baseUri("http://demowebshop.tricentis.com")
            .filter(customLogFilter().withCustomTemplates())
            .log().all()
            .contentType("application/x-www-form-urlencoded; charset=UTF-8");

    @Test
    @Tag("API")
    void checkWishListAPI() {
        body = "addtocart_53.EnteredQuantity=1";
        step("Add product to Wishlist", () -> {
            given()
                    .spec(demoWebShopRequest)
                    .body(body)
                    .when()
                    .post("addproducttocart/details/53/2")
                    .then()
                    .log().all()
                    .statusCode(200)
                    .body("updatetopwishlistsectionhtml", is("(1)"))
                    .body("message", is("The product has been added to your <a href=\"/wishlist\">wishlist</a>"));
        });
    }

    @Test
    @Tags({@Tag("API"), @Tag("UI")})
    void checkUsersAddress() {
        Configuration.baseUrl = "http://demowebshop.tricentis.com/";

        String login = "elena@qa.guru";
        String password = "elena@qa.guru";
        SelenideElement address = $(".address-list");

        step("Get cookie and set it to browser by API", () -> {
            String authorizationCookie = given()
                    .spec(demoWebShopRequest)
                    .formParam("Email", login)
                    .formParam("Password", password)
                    .when()
                    .post("login")
                    .then()
                    .log().all()
                    .statusCode(302)
                    .extract()
                    .cookie("NOPCOMMERCE.AUTH");

            step("Open minimal content, because cookie can be set when site is opened", () ->
                    open("Themes/DefaultClean/Content/images/logo.png"));

            step("Set cookie to to browser", () ->
                    getWebDriver().manage().addCookie(
                            new Cookie("NOPCOMMERCE.AUTH", authorizationCookie)));
        });

        step("Open user's address", () ->
                open("customer/addresses"));

        step("Check user's address", () -> {

            step("Check the address's title", () ->
                    address.$(".title").shouldHave(text("qa qa")));

            step("Check the user's name", () ->
                    address.$(".name").shouldHave(text("qa qa")));

            step("Check the email", () ->
                    address.$(".email").shouldHave(text("Email: elena@qu.guru")));

            step("Check the phone number", () ->
                    address.$(".phone").shouldHave(text("Phone number: +1234567")));

            step("Check the address", () ->
                    address.$(".address1").shouldHave(text("Address")));

            step("Check the city, state, zip code", () ->
                    address.$(".city-state-zip").shouldHave(text("City, Alberta ZipCode")));

            step("Check the country", () ->
                    address.$(".country").shouldHave(text("Canada")));
        });
    }
}
