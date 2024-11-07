package couriers;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

// Класс для работы с API курьеров
public class CourierSetup {
    // Базовый URL
    private final String BASE_URL = "http://qa-scooter.praktikum-services.ru";
    private static final String CREATE_COURIER_ENDPOINT = "/api/v1/courier";
    private static final String LOGIN_COURIER_ENDPOINT = "/api/v1/courier/login";
    private static final String DELETE_COURIER_ENDPOINT = "/api/v1/courier/";

    private static final Gson gson = new Gson(); // Инициализация Gson

    @Step("Метод для создания тела запроса для создания курьера")
    public String createRequestBody(String login, String password, String firstName) {
        Courier courier = new Courier(login, password, firstName);
        return gson.toJson(courier);
    }

    @Step("Метод для создания курьера")
    public Response createCourier(String jsonBody) {
        return given()
                .header("Content-Type", "application/json")
                .body(jsonBody)
                .when()
                .post(BASE_URL + CREATE_COURIER_ENDPOINT);
    }

    @Step("Метод для логина курьера")
    public Response loginCourier(String login, String password) {
        Courier loginRequest = new Courier(login, password);
        String loginJsonBody = gson.toJson(loginRequest);
        return given()
                .header("Content-Type", "application/json")
                .body(loginJsonBody)
                .when()
                .post(BASE_URL + LOGIN_COURIER_ENDPOINT);
    }

    @Step("Метод для удаления курьера")
    public Response deleteCourier(int courierId) {
        return given()
                .when()
                .delete(BASE_URL + DELETE_COURIER_ENDPOINT + courierId);
    }
}