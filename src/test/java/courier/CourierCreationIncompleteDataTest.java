package courier;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.services.qa.scooter.model.Courier;
import ru.praktikum.services.qa.scooter.steps.CourierSteps;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static org.hamcrest.CoreMatchers.is;

@RunWith(Parameterized.class)
public class CourierCreationIncompleteDataTest {
    private final String login;
    private final String password;
    private final String firstName;
    private Courier courier;
    private CourierSteps courierSteps;

    public CourierCreationIncompleteDataTest(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    @Parameterized.Parameters(name = "Test data: login - {0}, password - {1}")
    public static Object[][] getData() {
        return new Object[][]{
                {"", "Vasilisa123", "Василиса"},
                {"Vasilisa", "", "Василиса"},
                {"", "", "Василиса"}
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
        courierSteps = new CourierSteps();
        courier = new Courier(login, password, firstName);
    }

    @Test
    @DisplayName("Creation of a courier without login or/and password")
    public void createCourierIncompleteDataFail() {
        courierSteps.createCourier(courier)
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }
}
