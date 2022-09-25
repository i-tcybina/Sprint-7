// импортируем RestAssured
import io.restassured.RestAssured;
// импортируем Before
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
// импортируем Test
import org.junit.Test;
// дополнительный статический импорт нужен, чтобы использовать given(), get() и then()
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class AddCourierTest {

    // аннотация Before показывает, что метод будет выполняться перед каждым тестовым методом
    @Before
    public void setUp() {
        // повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
        // если в классе будет несколько тестов, указывать её придётся только один раз
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    // создаём метод автотеста
    @Test
    public void AddCourier() {
        // метод given() помогает сформировать запрос
        Response response =given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"Test\", \"password\": \"1234\", \"firstName\": \"Test\"}")
                .when()
                .post("/api/v1/courier");
                response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
        }

    @Test
    public void AddCourierNotPassword() {
        Response response =
        given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"Test\", \"password\": \"\", \"firstName\": \"Test\"}")
                .when()
                .post("/api/v1/courier");
                 response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
        given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"Test\", \"password\": \"1234\"}")
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(404);

    }
    @Test
    public void AddCourierNotLogin() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body("{\"login\": \"\", \"password\": \"1234\", \"firstName\": \"Test\"}")
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
        given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"Test\", \"password\": \"1234\"}")
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(404);
    }
    @Test
    public void AddCourierNotFirstName() {
        Response response =given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"Test\", \"password\": \"1234\", \"firstName\": \"\"}")
                .when()
                .post("/api/v1/courier");
        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
      }
    @Test
    public void AddTwoIdenticalCourier() {
                given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"Test\", \"password\": \"1234\", \"firstName\": \"Test\"}")
                .when()
                .post("/api/v1/courier")
                .then().statusCode(201);
        Response response =given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"Test\", \"password\": \"1234\", \"firstName\": \"Test\"}")
                .when()
                .post("/api/v1/courier");
        response.then().assertThat().body("message", equalTo("Этот логин уже используется"))
                .and()
                .statusCode(409);
    }
    @After
    public void delete() {
        Integer Id = given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"Test\", \"password\": \"1234\"}")
                .when()
                .post("/api/v1/courier/login")
                .then().extract().body().path("id");
        if (Id != null)
        {
            given()
                    .delete("/api/v1/courier/{Id}", Id)
                    .then().assertThat().statusCode(200);
        }

    }
}

