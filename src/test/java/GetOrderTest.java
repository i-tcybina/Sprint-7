import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
    @Test
    public void AddCourier() {
        Response response =given()
                .header("Content-type", "application/json")
                .get("/api/v1/orders");
        response.then().assertThat().body("orders.id[0]", notNullValue())
                .and()
                .statusCode(200);
    }
}
