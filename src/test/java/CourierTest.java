import couriers.Courier;
import couriers.CourierSetup;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CourierTest {
    // Инициализируем courierId значением -1
    private int courierId = -1;
    // Создаем экземпляр CourierSetup
    private final CourierSetup courierSetup = new CourierSetup();

    @Test
    @DisplayName("Test: courier can be created")
    public void createLoginAndDeleteCourier() {
        // Создаем тело запроса для создания курьера
        Response createResponse = courierSetup.createCourier(new Courier("expediter1", "1234", "saske"));

        // Проверяем статус-код и выводим сообщение об ошибке, если он не 201
        createResponse.then().assertThat().statusCode(SC_CREATED);
        createResponse.then().assertThat().body("ok", equalTo(true));

        // Логин курьера и получение ID
        Response loginResponse = courierSetup.loginCourier(new Courier("expediter1", "1234"));

        // Проверяем статус-код логина
        loginResponse.then().assertThat().statusCode(SC_OK);

        // Сохраняем ID курьера для последующего использования после успешного логина
        String idString = loginResponse.jsonPath().getString("id");
        courierId = Integer.parseInt(idString); // Преобразуем строку в int
        System.out.println("Полученный ID курьера: " + courierId); // Логирование

        // Удаление курьера
        Response deleteResponse = courierSetup.deleteCourier(courierId);

        // Проверяем статус-код удаления
        deleteResponse.then().assertThat().statusCode(SC_OK);
        // Проверяем, что ответ содержит поле ok: true
        deleteResponse.then().assertThat().body("ok", equalTo(true));
    }
    @Test
    @DisplayName("Test: create two identical couriers")
    public void createDuplicateCourier() {
        // Данные для курьера
        Response createResponse1 = courierSetup.createCourier(new Courier("expediter1", "1234", "saske"));

        // Проверяем статус-код и содержимое ответа первого курьера
        createResponse1.then().assertThat().statusCode(SC_CREATED);
        createResponse1.then().assertThat().body("ok", equalTo(true));

        // Логин под данными первого курьера, чтобы убедиться, что он создан
        Response loginResponse1 = courierSetup.loginCourier(new Courier("expediter1", "1234"));

        // Проверяем статус-код логина и id курьера
        loginResponse1.then().assertThat().statusCode(SC_OK);
        String idString = loginResponse1.jsonPath().getString("id");
        courierId = Integer.parseInt(idString);
        System.out.println("ID первого курьера: " + courierId);

        // Пытаемся создать второго курьера с теми же данными
        Response createResponse2 = courierSetup.createCourier(new Courier("expediter1", "1234", "saske"));
        // Проверяем статус-код и ожидание ошибки
        createResponse2.then().assertThat().statusCode(SC_CONFLICT);
        createResponse2.then().assertThat().body("message", equalTo("Этот логин уже используется"));

        // Удаление курьера
        Response deleteResponse = courierSetup.deleteCourier(courierId);
        deleteResponse.then().assertThat().statusCode(SC_OK);
        deleteResponse.then().assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Test: create courier without Login")
    public void createCourierWithoutLogin() {
        // Создаем тело запроса без логина
        Response createResponse = courierSetup.createCourier(new Courier("", "1234", "saske"));

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(SC_BAD_REQUEST);
        createResponse.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Test: create courier without Password")
    public void createCourierWithoutPassword() {
        // Создаем тело запроса без пароля
        Response createResponse = courierSetup.createCourier(new Courier("expediter1", "", "saske"));

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(SC_BAD_REQUEST);
        createResponse.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Test: create courier without Login and Password")
    public void createCourierWithoutLoginAndPassword() {
        // Создаем тело запроса без логина и пароля
        Response createResponse = courierSetup.createCourier(new Courier("", "", "saske"));

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(SC_BAD_REQUEST);
        createResponse.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

}