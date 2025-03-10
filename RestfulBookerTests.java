package org.example;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RestfulBookerTests {
    private final int GROUP_NUMBER = 122;
    private final int STUDENT_NUMBER = 20;
    private final String STUDENT_NAME = "Hnykina_V.O.";
    private int bookingId;
    private String authToken;

    @BeforeAll
    public void setup() {
        RestAssured.baseURI = "http://restful-booker.herokuapp.com";
    }

    @Test
    @Order(1)
    @DisplayName("GET /ping - перевірка доступності API")
    public void testPing() {
        given()
                .when().get("/ping")
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    @Order(2)
    @DisplayName("POST /auth - отримання токена авторизації")
    public void testCreateAuthToken() {
        String authBody = "{\"username\": \"admin\", \"password\": \"password123\"}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(authBody)
                .when().post("/auth");

        response.then().statusCode(200);
        authToken = response.jsonPath().getString("token");
    }

    @Test
    @Order(3)
    @DisplayName("POST /booking - створення нового бронювання")
    public void testCreateBooking() {
        String requestBody = "{\"firstname\": \"" + STUDENT_NAME + "\", " +
                "\"lastname\": \"Group" + GROUP_NUMBER + "-Student" + STUDENT_NUMBER + "\", " +
                "\"totalprice\": " + (100 + GROUP_NUMBER) + ", " +
                "\"depositpaid\": true, " +
                "\"bookingdates\": {\"checkin\": \"2025-03-10\", \"checkout\": \"2025-03-15\"}, " +
                "\"additionalneeds\": \"Breakfast\"}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/booking");

        response.then().statusCode(200);
        bookingId = response.jsonPath().getInt("bookingid");
    }

    @Test
    @Order(4)
    @DisplayName("GET /booking/:id - перевірка створеного бронювання")
    public void testGetBooking() {
        given()
                .when().get("/booking/" + bookingId)
                .then()
                .statusCode(200)
                .body("firstname", equalTo(STUDENT_NAME));
    }

    @Test
    @Order(5)
    @DisplayName("PUT /booking/:id - оновлення бронювання")
    public void testUpdateBooking() {
        String requestBody = "{\"firstname\": \"" + STUDENT_NAME + "\", " +
                "\"lastname\": \"Group" + GROUP_NUMBER + "-Student" + STUDENT_NUMBER + "\", " +
                "\"totalprice\": " + (150 + GROUP_NUMBER) + ", " +
                "\"depositpaid\": false, " +
                "\"bookingdates\": {\"checkin\": \"2025-04-01\", \"checkout\": \"2025-04-07\"}, " +
                "\"additionalneeds\": \"Lunch\"}";

        given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + authToken)
                .body(requestBody)
                .when().put("/booking/" + bookingId)
                .then().statusCode(200)
                .body("totalprice", equalTo(150 + GROUP_NUMBER));
    }
}
