package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.services.qa.scooter.model.Order;
import ru.praktikum.services.qa.scooter.steps.OrderSteps;

import java.util.List;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreationTest {
    List<String> color;
    private Order order;
    private OrderSteps orderSteps;
    private int track;

    public OrderCreationTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Test data: picked color is {0}")
    public static Object[][] getData() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("GREY", "BLACK")},
                {List.of()},
        };
    }

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter()
        );
    }

    @Before
    public void setUp() {
        orderSteps = new OrderSteps();
        String firstName = "Василий";
        String lastName = "Пупкин";
        String address = "пр. Вернандского, д.8";
        String metroStation = "Проспект Вернадского";
        String phone = "+79218526414";
        int rentTime = 5;
        String deliveryDate = "2022-04-20";
        String comment = "Очень жду";
        order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
    }

    @After
    public void clearData() {
        orderSteps.cancelOrder(track);
    }

    @Test
    @DisplayName("Create an order with picked scooter color")
    public void createOrderWithPikedColorSucceed() {
        ValidatableResponse response = orderSteps.createOrder(order)
                .assertThat()
                .statusCode(HTTP_CREATED)
                .and()
                .body("track", notNullValue());
        track = response.extract().path("track");
    }
}
