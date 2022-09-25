import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private String[] color;
    public CreateOrderTest(String[] color) {
        this.color = color;
            }
    @Parameterized.Parameters // добавили аннотацию
    public static Object[][] getColor() {
        return new Object[][] {
                { new String[] {"BLACK", "GREY"}},
                { new String[] {"GREY"}},
                { new String[] {"BLACK"}},
                { new String[] {}},
        };
    }

    @Before
    public void setUp() {
        // повторяющуюся для разных ручек часть URL лучше записать в переменную в методе Before
        // если в классе будет несколько тестов, указывать её придётся только один раз
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

    }
    //String[] color = new String[] {"BLACK"};
    Orders orders = new Orders("Naruto","Uchiha","Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", color);
    @Test
    public void CourierLoginWrongLogin() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(orders)
                        .when()
                        .post("/api/v1/orders");
                response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(201);
    }
}
