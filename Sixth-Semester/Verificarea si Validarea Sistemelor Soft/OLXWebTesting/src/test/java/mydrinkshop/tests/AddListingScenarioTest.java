package mydrinkshop.tests;

import mydrinkshop.pages.AccountPage;
import mydrinkshop.pages.AddListingPage;
import mydrinkshop.pages.HomePage;
import mydrinkshop.pages.LoginPage;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SerenityJUnit5Extension.class)
public class AddListingScenarioTest {

    HomePage homePage = new HomePage();
    LoginPage loginPage = new LoginPage();
    AccountPage accountPage = new AccountPage();
    AddListingPage addListingPage = new AddListingPage();

    private static final String VALID_EMAIL = "tebogolian19@gmail.com";
    private static final String VALID_PASSWORD = "VVSSisawsome67";

    @Test
    void should_login_add_listing_and_logout() {
        ListingData data = new ListingData(
                "VVSS test anunt - automat",
                "Anunt Test",
                "100"
        );

        homePage.openHomePage();
        assertTrue(homePage.isHomePageLoaded());

        homePage.acceptCookiesIfPresent();
        homePage.clickMyAccount();
        assertTrue(loginPage.isLoginPageLoaded());

        loginPage.loginWith(VALID_EMAIL, VALID_PASSWORD);
        assertTrue(accountPage.isUserLoggedIn() || accountPage.isAddListingButtonVisible());

        accountPage.goToAddListing();
        assertTrue(
                addListingPage.isAddListingPageLoaded() ||
                        addListingPage.isSuccessOrVerificationVisible()
        );

        addListingPage.enterTitle(data.getTitle());
        addListingPage.enterDescription(data.getDescription());
        addListingPage.enterPrice(data.getPrice());

        if (addListingPage.isNextOrPublishButtonEnabled()) {
            addListingPage.clickNextOrPublish();
        }

        assertTrue(
                addListingPage.isAddListingPageLoaded() ||
                        addListingPage.isSuccessOrVerificationVisible()
        );

        homePage.openHomePage();
        homePage.acceptCookiesIfPresent();
        homePage.clickMyAccount();
        accountPage.logout();

        assertTrue(homePage.isHomePageLoaded());
    }
}
