import couriers.CourierSetup;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.After;
import static org.hamcrest.Matchers.equalTo;

public class CourierLoginTest {
    // Создаем экземпляр CourierSetup
    private final CourierSetup courierSetup = new CourierSetup();
    // Инициализируем courierId значением -1
    private int courierId = -1;

    @After
    public void deleteCourier() {
        if (courierId != -1) {
            Response deleteResponse = courierSetup.deleteCourier(courierId);
           /* // Проверяем статус-код удаления
            deleteResponse.then().assertThat().statusCode(200);
            deleteResponse.then().assertThat().body("ok", equalTo(true));*/
        }
    }

    @Test
    @DisplayName("Test: courier can login")
    public void courierCanLogin() {

        // Создаем тело запроса для создания курьера
        String jsonBody = courierSetup.createRequestBody("pusher1", "1234", "saske");

        // Отправка POST-запроса для создания курьера
        Response createResponse = courierSetup.createCourier(jsonBody);

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(201);
        createResponse.then().assertThat().body("ok", equalTo(true));

        // Логин курьера
        Response loginResponse = courierSetup.loginCourier("pusher1", "1234");

        // Проверяем статус-код и id курьера
        loginResponse.then().assertThat().statusCode(200);
        String idString = loginResponse.jsonPath().getString("id");
        courierId = Integer.parseInt(idString);
        System.out.println("ID курьера после логина: " + courierId);

        /* Удаление курьера
        Response deleteResponse = courierSetup.deleteCourier(courierId);

        // Проверяем статус-код удаления
        deleteResponse.then().assertThat().statusCode(200);
        deleteResponse.then().assertThat().body("ok", equalTo(true));*/
    }

    @Test
    @DisplayName("Test: login without Login")
    public void loginWithoutLogin() {

        // Создаем тело запроса для создания курьера
        String jsonBody = courierSetup.createRequestBody("pusher1", "1234", "saske");

        // Отправка POST-запроса для создания курьера
        Response createResponse = courierSetup.createCourier(jsonBody);

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(201);
        createResponse.then().assertThat().body("ok", equalTo(true));

        // логин без Логина (передаем пустую строку вместо логина)
        Response loginResponse = courierSetup.loginCourier("", "1234");

        // Проверяем статус-код и содержимое ответа
        loginResponse.then().assertThat().statusCode(400);
        loginResponse.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));

        // Получаем ID курьера для удаления
        Response successfulLoginResponse = courierSetup.loginCourier("pusher1", "1234");
        String idString = successfulLoginResponse.jsonPath().getString("id");
        courierId = Integer.parseInt(idString);

    }

    @Test
    @DisplayName("Test: login without Password")
    public void loginWithoutPassword() {
        // Создаем тело запроса для создания курьера
        String jsonBody = courierSetup.createRequestBody("pusher1", "1234", "saske");

        // Отправка POST-запроса для создания курьера
        Response createResponse = courierSetup.createCourier(jsonBody);

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(201);
        createResponse.then().assertThat().body("ok", equalTo(true));

        // Пытаемся выполнить логин с логином, но без пароля
        Response loginResponse = courierSetup.loginCourier("pusher1", "");

        // Проверяем статус-код и содержимое ответа
        loginResponse.then().assertThat().statusCode(400);
        loginResponse.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));

        // Получаем ID курьера для удаления
        Response successfulLoginResponse = courierSetup.loginCourier("pusher1", "1234");
        String idString = successfulLoginResponse.jsonPath().getString("id");
        courierId = Integer.parseInt(idString);
    }

    @Test
    @DisplayName("Test: login without Login and Password")
    public void loginWithoutLoginAndPassword() {
        // Создаем тело запроса для создания курьера
        String jsonBody = courierSetup.createRequestBody("pusher1", "1234", "saske");

        // Отправка POST-запроса для создания курьера
        Response createResponse = courierSetup.createCourier(jsonBody);

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(201);
        createResponse.then().assertThat().body("ok", equalTo(true));

        // Пытаемся выполнить логин с логином, но без пароля
        Response loginResponse = courierSetup.loginCourier("", "");

        // Проверяем статус-код и содержимое ответа
        loginResponse.then().assertThat().statusCode(400);
        loginResponse.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));

        // Получаем ID курьера для удаления
        Response successfulLoginResponse = courierSetup.loginCourier("pusher1", "1234");
        String idString = successfulLoginResponse.jsonPath().getString("id");
        courierId = Integer.parseInt(idString);
    }

    @Test
    @DisplayName("Test: login with invalid Login")
    public void loginInvalidLogin() {
        // Создаем тело запроса для создания курьера
        String jsonBody = courierSetup.createRequestBody("pusher1", "1234", "saske");

        // Отправка POST-запроса для создания курьера
        Response createResponse = courierSetup.createCourier(jsonBody);

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(201);
        createResponse.then().assertThat().body("ok", equalTo(true));

        // Пытаемся выполнить логин с неверным логином, и с верным паролем
        Response loginResponse = courierSetup.loginCourier("pusher12", "1234");

        // Проверяем статус-код и содержимое ответа
        loginResponse.then().assertThat().statusCode(404);
        loginResponse.then().assertThat().body("message", equalTo("Учетная запись не найдена"));

        // Получаем ID курьера для удаления
        Response successfulLoginResponse = courierSetup.loginCourier("pusher1", "1234");
        String idString = successfulLoginResponse.jsonPath().getString("id");
        courierId = Integer.parseInt(idString);
    }

    @Test
    @DisplayName("Test: login with invalid Password")
    public void loginInvalidPassword() {
        // Создаем тело запроса для создания курьера
        String jsonBody = courierSetup.createRequestBody("pusher1", "1234", "saske");

        // Отправка POST-запроса для создания курьера
        Response createResponse = courierSetup.createCourier(jsonBody);

        // Проверяем статус-код и содержимое ответа
        createResponse.then().assertThat().statusCode(201);
        createResponse.then().assertThat().body("ok", equalTo(true));

        // Пытаемся выполнить логин с верным логином, и неверным паролем
        Response loginResponse = courierSetup.loginCourier("pusher1", "12343");

        // Проверяем статус-код и содержимое ответа
        loginResponse.then().assertThat().statusCode(404);
        loginResponse.then().assertThat().body("message", equalTo("Учетная запись не найдена"));

        // Получаем ID курьера для удаления
        Response successfulLoginResponse = courierSetup.loginCourier("pusher1", "1234");
        String idString = successfulLoginResponse.jsonPath().getString("id");
        courierId = Integer.parseInt(idString);
    }

    @Test
    @DisplayName("Test: login as a non-existent courier")
    public void loginNullLogin() {

        // Пытаемся выполнить логин с несуществующим логином
        Response loginResponse = courierSetup.loginCourier("Jffddeerrd", "1234");

        // Проверяем статус-код и содержимое ответа
        loginResponse.then().assertThat().statusCode(404);
        loginResponse.then().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }
}