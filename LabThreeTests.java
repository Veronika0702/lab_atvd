package org.example;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.apache.hc.core5.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LabThreeTests {

    private static final String MOCK_SERVER = "https://b96c3f7b-c973-4890-aed3-cc4178d6da32.mock.pstmn.io";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = MOCK_SERVER;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build();
        RestAssured.responseSpecification = new ResponseSpecBuilder()
                .build();
    }

    @Test
    public void testGetOwnerNameSuccess() {
        given()
                .get("/ownerName/success")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("name", equalTo("Veronika Hnykina"));
    }

    @Test
    public void testGetOwnerNameUnsuccess() {
       given()
               .get("/ownerName/unsuccess")
                .then()
                .statusCode(403)
                .body("exceprion", equalTo("I won't say my name!"));
    }

    @Test
    public void testPostCreateSomethingWithPermission() {
        given()
                .queryParam("permission", "yes")
                .post("/createSomething")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("result", equalTo("'Nothing' was created"));
    }

    @Test
    public void testPostCreateSomethingWithoutPermission() {
        given()
                .post("/createSomething")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("result", equalTo("You don't have permission to create Something"));
    }

    @Test
    public void testPutUpdateMe() {
        Map<String, String> body = Map.of(
                "name", "",
                "surname", ""
        );

        given()
                .body(body)
                .put("/updateMe")
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testDeleteWorld() {
        given()
                .header("SessionID", "123456789")
                .delete("/deleteWorld")
                .then()
                .statusCode(HttpStatus.SC_GONE)
                .body("world", equalTo("0"));
    }
}
