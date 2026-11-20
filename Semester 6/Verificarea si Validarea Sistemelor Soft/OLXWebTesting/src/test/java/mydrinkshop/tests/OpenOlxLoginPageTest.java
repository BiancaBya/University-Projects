package mydrinkshop.tests;

import net.serenitybdd.junit5.SerenityJUnit5Extension;
import mydrinkshop.pages.HomePage;
import mydrinkshop.pages.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SerenityJUnit5Extension.class)
public class OpenOlxLoginPageTest {

    HomePage homePage = new HomePage();
    LoginPage loginPage = new LoginPage();

    @Test
    void should_open_login_page() {
        homePage.openHomePage();
        homePage.acceptCookiesIfPresent();
        homePage.clickMyAccount();

        assertTrue(loginPage.isLoginPageLoaded());
    }
}


