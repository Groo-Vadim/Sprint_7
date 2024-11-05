import couriers.CourierSetup;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CourierTest {

    private int courierId = -1; // Инициализируем courierId значением -1
    private final CourierSetup courierSetup = new CourierSetup(); // Создаем экземпляр CourierSetup

    @Test
    @DisplayName("Test: courier can be created")
    public void createLoginAndDeleteCourier() {
        // Создаем тело запроса для создания курьера
        String jsonBody = courierSetup.createRequestBody("expediter1", "1234", "saske");

        // Отправка POST-запроса для создания курьера
        Response createResponse = courierSetup.createCourier(jsonBody);

        // Проверяем статус-код и выводим сообщение об ошибке, если он не 201
        createResponse.then().assertThat().statusCode(201);
        createResponse.then().assertThat().body("ok", equalTo(true));

        // Логин курьера и получение ID
        Response loginResponse = courierSetup.loginCourier("expediter1", "1234");

        // Проверяем статус-код логина
        loginResponse.then().assertThat().statusCode(200);

        // Сохраняем ID курьера для последующего использования после успешного логина
        String idString = loginResponse.jsonPath().getString("id");
        courierId = Integer.parseInt(idString); // Преобразуем строку в int
        System.out.println("Полученный ID курьера: " + courierId); // Логирование

        // Удаление курьера
        Response deleteResponse = courierSetup.deleteCourier(courierId);

        // Проверяем статус-код удаления
        deleteResponse.then().assertThat().statusCode(200);
        // Проверяем, что ответ содержит поле ok: true
        deleteResponse.then().assertThat().body("ok", equalTo(true));
    }
    @Test
    @DisplayName("Test: create two identical couriers")
    public void createDuplicateCourier() {
        // Данные для курьера
        String jsonBody = courierSetup.createRequestBody("expediter1", "1234", "saske");

        // Отправка POST-запроса для создания первого курьера
        Response createResponse1 = courierSetup.createCourier(jsonBody);

        // Проверяем статус-код и содержимое ответа первого курьера
        createResponse1.then().assertThat().statusCode(201);
        createResponse1.then().assertThat().body("ok", equalTo(true));

        // Логин под данными первого курьера, чтобы убедиться, что он создан
        Response loginResponse1 = courierSetup.loginCourier("expediter1", "1234");

        // Проверяем статус-код логина и id курьера
        loginResponse1.then().assertThat().statusCode(200);
        String idString = loginResponse1.jsonPath().getString("id");
        courierId = Integer.parseInt(idString);
        System.out.println("ID первого курьера: " + courierId);

        // Пытаемся создать второго курьера с теми же данными
        Response createResponse2 = courierSetup.createCourier(jsonBody);

        // Проверяем статус-код и ожидание ошибки
        createResponse2.then().assertThat().statusCode(409);
        createResponse2.then().assertThat().body("message", equalTo("Этот логин уже используется"));

        // Удаление курьера
        Response deleteResponse = courierSetup.deleteCourier(courierId);
        deleteResponse.then().assertThat().statusCode(200);
        deleteResponse.then().assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Test: create courier without Login")
    public void createCourierWithoutLogin() {
        // Создаем тело запроса без логина
        String jsonBody = courierSetup.createRequestBody("", "1234", "saske");

        // Отправка POST-запроса для создания курьера
        Response createResponse = courierSetup.createCourier(jsonBody);

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(400);
        createResponse.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Test: create courier without Password")
    public void createCourierWithoutPassword() {
        // Создаем тело запроса без пароля
        String jsonBody = courierSetup.createRequestBody("expediter1", "", "saske");

        // Отправка POST-запроса для создания курьера
        Response createResponse = courierSetup.createCourier(jsonBody);

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(400);
        createResponse.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Test: create courier without Login and Password")
    public void createCourierWithoutLoginAndPassword() {
        // Создаем тело запроса без логина и пароля
        String jsonBody = courierSetup.createRequestBody("", "", "saske");

        // Отправка POST-запроса для создания курьера
        Response createResponse = courierSetup.createCourier(jsonBody);

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(400);
        createResponse.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

}