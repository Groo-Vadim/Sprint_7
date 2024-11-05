import orders.OrderSetup;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Test;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;


public class GetListOrdersTest{

    @Test
    @Step("Тест: проверка, что в тело ответа возвращается список заказов")
    public void getOrders() {
        Response response = OrderSetup.getOrders();
        OrderSetup.checkResponseBody(response);
        response.then().assertThat().body("orders", hasSize(greaterThan(0))).and().statusCode(SC_OK);
    }
}