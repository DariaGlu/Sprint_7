package courier;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.praktikum.services.qa.scooter.model.Courier;
import ru.praktikum.services.qa.scooter.model.CourierCreds;
import ru.praktikum.services.qa.scooter.steps.CourierSteps;

import static java.net.HttpURLConnection.HTTP_CONFLICT;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static org.hamcrest.CoreMatchers.is;

public class CourierCreationCompleteDataTest {
    private int courierId;
    private CourierCreds courierCreds;
    private Courier courier;
    private CourierSteps courierSteps;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter()
        );
    }

    @Before
    public void setUp() {
        courierSteps = new CourierSteps();
        String login = RandomStringUtils.randomAlphanumeric(3, 10);
        String password = RandomStringUtils.randomAlphanumeric(6, 12);
        String firstName = RandomStringUtils.randomAlphanumeric(3, 10);
        courier = new Courier(login, password, firstName);
    }

    @After
    public void clearData() {
        courierId = courierSteps.loginCourier(courierCreds.from(courier))
                .extract().path("id");
        courierSteps.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Creation of a courier with unique data")
    public void createCourierUniqueLoginSucceed() {
        courierSteps.createCourier(courier)
                .assertThat()
                .statusCode(HTTP_CREATED)
                .and()
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Creation of a courier using the same login")
    public void createCourierNotUniqueLoginFail() {
        courierSteps.createCourier(courier);
        courierSteps.createCourier(courier)
                .assertThat()
                .statusCode(HTTP_CONFLICT)
                .and()
                .body("message", is("Этот логин уже используется"));
    }
}
