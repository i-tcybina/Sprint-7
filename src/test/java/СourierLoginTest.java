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
    @Before
    public void setUp() {
        // повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
        // если в классе будет несколько тестов, указывать её придётся только один раз
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"Test\", \"password\": \"1234\", \"firstName\": \"Test\"}")
                .when()
                .post("/api/v1/courier");
    }

    // создаём метод автотеста
    @Test
    public void IDCourier() {
        Integer Id = given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"Test\", \"password\": \"1234\"}")
                .when()
                .post("/api/v1/courier/login")
                .then().extract().body().path("id");
                Id.equals(notNullValue());
    }
    @Test
    public void CodeCourier() {
            given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"Test\", \"password\": \"1234\"}")
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(200);
            }
    @Test
    public void CourierLoginNotPassword() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body("{\"login\": \"Test\", \"password\": \"\"}")
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);

    }
    @Test
    public void CourierLoginNotLogin() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body("{\"login\": \"\", \"password\": \"1234\"}")
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);

    }
    @Test
    public void CourierLoginNotPasswordAndLogin() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body("{\"login\": \"Test\", \"password\": \"\"}")
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);

    }
    @Test
    public void CourierLoginWrongLogin() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body("{\"login\": \"Test2\", \"password\": \"1234\"}")
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }
    @Test
    public void CourierLoginWrongPassword() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body("{\"login\": \"Test\", \"password\": \"1233\"}")
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }
    @Test
    public void CourierLoginWrongPasswordAndLogin() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body("{\"login\": \"Test2\", \"password\": \"1233\"}")
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }
    @After
    public void delete() {
        Integer Id = given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"Test\", \"password\": \"1234\"}")
                .when()
                .post("/api/v1/courier/login")
                .then().extract().body().path("id");
        given()
                .delete("/api/v1/courier/{Id}", Id)
                .then().assertThat().statusCode(200);
    }

}
