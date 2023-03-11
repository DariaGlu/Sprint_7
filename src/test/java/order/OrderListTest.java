package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.praktikum.services.qa.scooter.steps.OrderSteps;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderListTest {
    private OrderSteps orderSteps;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter()
        );
    }

    @Before
    public void setUp() {
        orderSteps = new OrderSteps();
    }

    @Test
    @DisplayName("Get list of orders")
    public void getListOfOrders() {
        orderSteps.getListOfOrders()
                .statusCode(HTTP_OK)
                .and()
                .body("orders", notNullValue());
    }
}
