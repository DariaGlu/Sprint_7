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

import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierLoginTest {
    private static final String NONE_PASSWORD = "";
    private static final String NONE_LOGIN = "";
    private int courierId;
    private CourierCreds courierCreds;
    private Courier courier;
    private CourierSteps courierSteps;
    private String password;
    private String login;
    private final String fakeLogin = RandomStringUtils.randomAlphanumeric(3, 10);
    private final String fakePassword = RandomStringUtils.randomAlphanumeric(6, 12);


    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter()
        );
    }

    @Before
    public void setUp() {
        courierSteps = new CourierSteps();
        login = RandomStringUtils.randomAlphanumeric(3, 10);
        password = RandomStringUtils.randomAlphanumeric(6, 12);
        String firstName = RandomStringUtils.randomAlphanumeric(3, 10);
        courier = new Courier(login, password, firstName);
        courierSteps.createCourier(courier);
    }

    @After
    public void clearData() {
        courierId = courierSteps.loginCourier(courierCreds.from(courier))
                .extract().path("id");
        courierSteps.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Courier logs in using created data")
    public void loginCourierSucceed() {
        courierSteps.loginCourier(courierCreds.from(courier))
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Courier logs in without login")
    public void courierLogsInWithoutLoginFail() {
        courierCreds = new CourierCreds(NONE_LOGIN, password);
        courierSteps.loginCourier(courierCreds)
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Courier logs in without password")
    public void courierLogsInWithoutPasswordFail() {
        courierCreds = new CourierCreds(login, NONE_PASSWORD);
        courierSteps.loginCourier(courierCreds)
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Courier logs in with wrong login")
    public void courierLogsInWithWrongLoginFail() {
        courierCreds = new CourierCreds(fakeLogin, password);
        courierSteps.loginCourier(courierCreds)
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .body("message", is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Courier logs in with wrong password")
    public void courierLogsInWithWrongPasswordFail() {
        courierCreds = new CourierCreds(login, fakePassword);
        courierSteps.loginCourier(courierCreds)
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .body("message", is("Учетная запись не найдена"));
    }
}

