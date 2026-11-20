package mydrinkshop.tests;

import net.serenitybdd.junit5.SerenityJUnit5Extension;
import mydrinkshop.pages.HomePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SerenityJUnit5Extension.class)
public class OpenOlxHomePageTest {

    HomePage homePage;

    @Test
    void should_open_olx_home_page() {
        homePage.openHomePage();
        assertTrue(homePage.isHomePageLoaded());
    }
}