package ru.praktikum.services.qa.scooter.steps;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import ru.praktikum.services.qa.scooter.model.Order;

import static io.restassured.RestAssured.given;
import static ru.praktikum.services.qa.scooter.constants.Endpoints.*;

public class OrderSteps {
    public static RequestSpecification getBaseReqSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URI)
                .build();
    }

    @Step("Create new order {order}")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .spec(getBaseReqSpec())
                .body(order)
                .when()
                .post(ORDER_POST_CREATE)
                .then();
    }

    @Step("Get list of orders")
    public ValidatableResponse getListOfOrders() {
        return given()
                .spec(getBaseReqSpec())
                .get(ORDER_GET_LIST)
                .then();
    }

    @Step("Cancel order")
    public ValidatableResponse cancelOrder(int track) {
        return given()
                .spec(getBaseReqSpec())
                .queryParam("track", track)
                .put(ORDER_PUT_CANCEL)
                .then();
    }
}
