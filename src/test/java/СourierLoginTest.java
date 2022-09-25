import com.sun.xml.bind.v2.model.core.ID;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class СourierLoginTest {
    CourierLogin сourierLogin = new CourierLogin("Test", "1234","Test");


    @Before
    public void setUp() {
        // повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
        // если в классе будет несколько тестов, указывать её придётся только один раз
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        given()
                .header("Content-type", "application/json")
                .body(сourierLogin)
                .when()
                .post("/api/v1/courier");
    }

    // создаём метод автотеста
    @Test
    public void IDCourier() {
        LoginPassword loginPassword = new LoginPassword("Test" , "1234");
        Integer Id = given()
                .header("Content-type", "application/json")
                .body(loginPassword)
                .when()
                .post("/api/v1/courier/login")
                .then().extract().body().path("id");
                Id.equals(notNullValue());
    }
    @Test
    public void CodeCourier() {
        LoginPassword loginPassword = new LoginPassword("Test" , "1234");
            given()
                .header("Content-type", "application/json")
                .body(loginPassword)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(200);
            }
    @Test
    public void CourierLoginNotPassword() {
        LoginPassword loginPassword = new LoginPassword("Test" , "");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginPassword)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);

    }
    @Test
    public void CourierLoginNotLogin() {
        LoginPassword loginPassword = new LoginPassword("" , "1234");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginPassword)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);

    }
    @Test
    public void CourierLoginNotPasswordAndLogin() {
        LoginPassword loginPassword = new LoginPassword("Test" , "");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginPassword)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);

    }
    @Test
    public void CourierLoginWrongLogin() {
        LoginPassword loginPassword = new LoginPassword("Test2" , "1234");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginPassword)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }
    @Test
    public void CourierLoginWrongPassword() {
        LoginPassword loginPassword = new LoginPassword("Test" , "1233");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginPassword)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }
    @Test
    public void CourierLoginWrongPasswordAndLogin() {
        LoginPassword loginPassword = new LoginPassword("Test2" , "1233");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(loginPassword)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }
    @After
    public void delete() {
        LoginPassword loginPassword = new LoginPassword("Test" , "1234");
        Integer Id = given()
                .header("Content-type", "application/json")
                .body(loginPassword)
                .when()
                .post("/api/v1/courier/login")
                .then().extract().body().path("id");
        given()
                .delete("/api/v1/courier/{Id}", Id)
                .then().assertThat().statusCode(200);
    }

}
