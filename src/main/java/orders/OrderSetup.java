package orders;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;

public class OrderSetup {
    private static final String BASE_URL = "http://qa-scooter.praktikum-services.ru";

    private static final String CREATE_ORDER_ENDPOINT = "/api/v1/orders";
    private static final String CANCEL_ORDER_ENDPOINT = "/api/v1/orders/cancel";
    private static final String GET_ORDER_ENDPOINT = "/api/v1/orders?limit=10&page=0&nearestStation=[\"110\"]";

    //Создание заказа
    public static Response createOrder(Order order) {
        return given()
                .header("Content-Type", "application/json")
                .body(order)
                .post(BASE_URL + CREATE_ORDER_ENDPOINT);
    }

    public static int checkTrackingNumber(Response response, int responseCode) {
        response.then().assertThat().body("track", not(0)).and().statusCode(responseCode);
        int trackNumber = response.jsonPath().getInt("track");
        System.out.println("Track number: " + trackNumber);
        return trackNumber;
    }

    //Отмена заказа
    public static Response cancelOrder(int trackNumber) {
        String requestBody = "{ \"track\": " + trackNumber + " }";

        return given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .put(BASE_URL + CANCEL_ORDER_ENDPOINT);
    }

    //Получение списка заказов
    public static Response getOrders() {
        return given()
                .header("Content-Type", "application/json")
                .get(BASE_URL + GET_ORDER_ENDPOINT);
    }

    //Проверка списка заказов в теле ответа
    public static void checkResponseBody(Response response) {
        JsonPath jsonPath = new JsonPath(response.asString());
        List<Map<String, Object>> orders = jsonPath.getList("orders");
        //Создание экземпляра gson с форматированием
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        // Преобразование списка заказов в JSON
        String ordersJson = gson.toJson(orders);
        // Вывод код ответа
        System.out.println("Код ответа: " + response.getStatusCode() + ", Тело ответа: " + response.asString());
        // Вывод JSON списка заказов
        System.out.println(ordersJson);
    }

}