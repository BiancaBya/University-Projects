package mydrinkshop.pages;

import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;

public class HomePage extends PageObject {

    private final By acceptCookiesButton = By.xpath("//button[contains(.,'Accept') or contains(.,'Accept all') or contains(.,'Acceptă') or contains(.,'Accept toate')]");
    private final By myAccountButton = By.xpath("//a[contains(@href,'account') or contains(.,'Cont') or contains(.,'Account') or contains(.,'Autentificare') or contains(.,'Conectare')]");
    private final By body = By.tagName("body");

    public void openHomePage() {
        openUrl("https://www.olx.ro/");
    }

    public boolean isHomePageLoaded() {
        return $(body).isVisible();
    }

    public void acceptCookiesIfPresent() {
        if ($(acceptCookiesButton).isPresent()) {
            try {
                $(acceptCookiesButton).waitUntilClickable().click();
            } catch (Exception ignored) {
            }
        }
    }

    public void clickMyAccount() {
        $(myAccountButton).waitUntilClickable().click();
    }

    public boolean isMyAccountButtonVisible() {
        return $(myAccountButton).isVisible();
    }
}

