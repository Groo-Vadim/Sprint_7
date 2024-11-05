package couriers;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class CourierSetup {
    // Базовый URL
    private final String BASE_URL = "http://qa-scooter.praktikum-services.ru";
    @Step("Метод для создания тела запроса для создания курьера")
    // Метод для создания тела запроса для создания курьера
    public String createRequestBody(String login, String password, String firstName) {
        return "{\"login\":\"" + login + "\",\"password\":\"" + password + "\",\"firstName\":\"" + firstName + "\"}";
    }
    @Step("Метод для создания курьера")
    // Метод для создания курьера
    public Response createCourier(String jsonBody) {
        return given()
                .header("Content-Type", "application/json")
                .body(jsonBody)
                .when()
                .post(BASE_URL + "/api/v1/courier");
    }
    @Step("Метод для логина курьера")
    // Метод для логина курьера
    public Response loginCourier(String login, String password) {
        String loginJsonBody = "{\"login\":\"" + login + "\",\"password\":\"" + password + "\"}";
        return given()
                .header("Content-Type", "application/json")
                .body(loginJsonBody)
                .when()
                .post(BASE_URL + "/api/v1/courier/login");
    }
    @Step("Метод для удаления курьера")
    // Метод для удаления курьера
    public Response deleteCourier(int courierId) {
        return given()
                .when()
                .delete(BASE_URL + "/api/v1/courier/" + courierId);
    }
}