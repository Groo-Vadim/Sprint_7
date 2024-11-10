package couriers;


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

    @Step("Метод для создания курьера")
    public Response createCourier(Courier courier) {
        return given()
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post(BASE_URL + CREATE_COURIER_ENDPOINT);
    }

    @Step("Метод для логина курьера")
    public Response loginCourier(Courier courier) {
        return given()
                .header("Content-Type", "application/json")
                .body(courier)
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