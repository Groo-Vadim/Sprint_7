import io.qameta.allure.Step;
import orders.OrderSetup;
import orders.Order;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.apache.http.HttpStatus.*;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;
    private int trackNumber;

    public OrderCreateTest(String firstName, String lastName, String address, String metroStation,
                           String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getOrderData() {
        return new Object[][] {
                {"Ivani", "Petrov", "Stroiteley 22", "1", "+7 123 456 78 90", 3, "08.11.2024", "need it fast", new String[]{"BLACK"}},
                {"Vano2", "Petrov", "Stroiteley 22", "1", "+7 123 456 78 90", 3, "08.11.2024", "need it fast", new String[]{"GRAY"}},
                {"Vane3", "Petrov", "Stroiteley 22", "1", "+7 123 456 78 90", 3, "08.11.2024", "need it fast", new String[]{"BLACK", "GRAY"}},
                {"Vani4", "Petrov", "Stroiteley 22", "1", "+7 123 456 78 90", 3, "08.11.2024", "need it fast", new String[]{}},
        };
    }

    @Test
    @Step("Тест: создание заказа")
    public void createOrderTest() {
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        Response response = OrderSetup.createOrder(order);
        // Сохраняем трек-номер
        trackNumber = OrderSetup.checkTrackingNumber(response, SC_CREATED);
    }

    @After
    public void cancelOrderTest() {
        // Отмена заказа только если трек-номер не равен 0
        if (trackNumber != 0) {
            OrderSetup.cancelOrder(trackNumber);
            //System.out.println("Order with track " + trackNumber + " has been canceled.");
        }
    }
}
