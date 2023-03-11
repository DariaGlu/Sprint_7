package ru.praktikum.services.qa.scooter.steps;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import ru.praktikum.services.qa.scooter.model.Courier;
import ru.praktikum.services.qa.scooter.model.CourierCreds;

import static io.restassured.RestAssured.given;
import static ru.praktikum.services.qa.scooter.constants.Endpoints.*;

public class CourierSteps {
    public static RequestSpecification getBaseReqSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URI)
                .build();
    }

    @Step("Create new courier {courier}")
    public ValidatableResponse createCourier(Courier courier) {
        return given()
                .spec(getBaseReqSpec())
                .body(courier)
                .when()
                .post(COURIER_POST_CREATE)
                .then();
    }

    @Step("Login as {courierCreds}")
    public ValidatableResponse loginCourier(CourierCreds courierCreds) {
        return given()
                .spec(getBaseReqSpec())
                .body(courierCreds)
                .when()
                .post(COURIER_POST_LOGIN)
                .then();
    }

    @Step("Delete courier {id}")
    public ValidatableResponse deleteCourier(int id) {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .delete(COURIER_DELETE + id)
                .then();
    }
}
