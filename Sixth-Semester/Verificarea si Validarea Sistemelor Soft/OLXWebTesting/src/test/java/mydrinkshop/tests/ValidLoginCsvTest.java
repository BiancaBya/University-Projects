package mydrinkshop.tests;

import net.serenitybdd.junit5.SerenityJUnit5Extension;
import mydrinkshop.pages.HomePage;
import mydrinkshop.pages.LoginPage;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SerenityJUnit5Extension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ValidLoginCsvTest {

    HomePage homePage = new HomePage();
    LoginPage loginPage = new LoginPage();

    Stream<LoginData> validLoginData() {
        return CsvLoginReader.readLoginData("testdata/login_valid.csv").stream();
    }

    @ParameterizedTest
    @MethodSource("validLoginData")
    void should_login_with_valid_data(LoginData data) {
        homePage.openHomePage();
        homePage.acceptCookiesIfPresent();
        homePage.clickMyAccount();

        assertTrue(loginPage.isLoginPageLoaded());

        loginPage.loginWith(data.getEmail(), data.getPassword());

        assertTrue(true);
    }
}