package com.github.elenaAeternaNox.rest_api.tests;

import com.github.elenaAeternaNox.rest_api.annotations.Layer;
import com.github.elenaAeternaNox.rest_api.annotations.Microservice;
import com.github.elenaAeternaNox.rest_api.models.books_shop.GenerateToken;
import com.github.elenaAeternaNox.rest_api.models.books_shop.UserLoginData;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;

import static com.github.elenaAeternaNox.rest_api.filters.CustomLogFilter.customLogFilter;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Layer("rest")
@Owner("ekomarova")
@Story("BookShop")
@Feature("BookShop")
@Tag("API")
public class BooksShopTest {

    private UserLoginData userLoginData = new UserLoginData();

    public UserLoginData setUserLoginData() {
        userLoginData.setUserName("alex");
        userLoginData.setPassword("asdsad#frew_DFS2");
        return userLoginData;
    }

    String expectedStatus = "Success";
    String expectedResult = "User authorized successfully.";

    @BeforeAll
    static void prepare() {
        RestAssured.baseURI = "https://demoqa.com";
    }

    @Microservice("Books")
    @DisplayName("Check API books without logs")
    @Test
    void noLogsTest() {
            given()
                    .get("/BookStore/v1/Books")
                    .then()
                    .body("books", hasSize(greaterThan(0)));
    }

    @Microservice("Books")
    @DisplayName("Check API books with all logs")
    @Test
    void withAllLogsTest() {
            given()
                    .log().all()
                    .get("/BookStore/v1/Books")
                    .then()
                    .log().all()
                    .body("books", hasSize(greaterThan(0)));
    }

    @Microservice("Books")
    @DisplayName("Check API books with some logs")
    @Test
    void withSomeLogsTest() {
            given()
                    .log().uri()
                    .log().body()
                    .get("/BookStore/v1/Books")
                    .then()
                    .log().body()
                    .body("books", hasSize(greaterThan(0)));
    }

    @Microservice("Authorization")
    @DisplayName("Check API user's authorize")
    @Test
    @Disabled
    void authorizeApiTest() {
            GenerateToken generateToken =
                    given()
                            .contentType("application/json")
                            .accept("application/json")
                            .body(setUserLoginData())
                            .when()
                            .log().uri()
                            .log().body()
                            .post("/Account/v1/GenerateToken")
                            .then()
                            .log().body()
                            .extract().as(GenerateToken.class);

            assertEquals(expectedStatus, generateToken.getStatus());
            assertEquals(expectedResult, generateToken.getResult());
    }

    @Microservice("Authorization")
    @DisplayName("Check API user's authorize with Listener")
    @Test
    void authorizeWithListenerTest() {
            GenerateToken generateToken =
                    given()
                            .filter(new AllureRestAssured())
                            .contentType("application/json")
                            .accept("application/json")
                            .body(setUserLoginData())
                            .when()
                            .log().uri()
                            .log().body()
                            .post("/Account/v1/GenerateToken")
                            .then()
                            .log().body()
                            .extract().as(GenerateToken.class);

            assertEquals(expectedStatus, generateToken.getStatus());
            assertEquals(expectedResult, generateToken.getResult());
    }

    @Microservice("Authorization")
    @DisplayName("Check API user's authorize with custom log filter")
    @Test
    void authorizeWithTemplatesTest() {
            GenerateToken generateToken =
                    given()
                            .filter(customLogFilter().withCustomTemplates())
                            .contentType("application/json")
                            .accept("application/json")
                            .body(setUserLoginData())
                            .when()
                            .log().uri()
                            .log().body()
                            .post("/Account/v1/GenerateToken")
                            .then()
                            .log().body()
                            .extract().as(GenerateToken.class);

            assertEquals(expectedStatus, generateToken.getStatus());
            assertEquals(expectedResult, generateToken.getResult());
    }
}
