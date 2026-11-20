package mydrinkshop.tests;

import net.serenitybdd.junit5.SerenityJUnit5Extension;
import mydrinkshop.pages.AccountPage;
import mydrinkshop.pages.AddListingPage;
import mydrinkshop.pages.HomePage;
import mydrinkshop.pages.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SerenityJUnit5Extension.class)
public class LoginLogoutScenarioTest {

    HomePage homePage = new HomePage();
    LoginPage loginPage = new LoginPage();
    AccountPage accountPage = new AccountPage();
    AddListingPage addListingPage = new AddListingPage();

    private static final String VALID_EMAIL = "bogdan.rebeles@stud.ubbcluj.ro";
    private static final String VALID_PASSWORD = "testingisVERYc00l";

    @Test
    void should_login_open_add_listing_and_logout() {
        homePage.openHomePage();
        assertTrue(homePage.isHomePageLoaded());

        homePage.acceptCookiesIfPresent();
        homePage.clickMyAccount();
        assertTrue(loginPage.isLoginPageLoaded());

        loginPage.loginWith(VALID_EMAIL, VALID_PASSWORD);
        assertTrue(accountPage.isUserLoggedIn() || accountPage.isAddListingButtonVisible());

        accountPage.goToAddListing();
        assertTrue(addListingPage.isAddListingPageLoaded());

        homePage.openHomePage();
        homePage.acceptCookiesIfPresent();
        homePage.clickMyAccount();
        accountPage.logout();

        assertTrue(homePage.isHomePageLoaded());
    }
}
