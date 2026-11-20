package mydrinkshop.pages;

import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;

public class LoginPage extends PageObject {

    private final By emailField = By.xpath("//input[@type='email' or @name='email' or contains(@placeholder,'mail') or contains(@placeholder,'email')]");
    private final By passwordField = By.xpath("//input[@type='password']");
    private final By loginButton = By.xpath("//button[@type='submit' or contains(.,'Conectează-te') or contains(.,'Autentificare') or contains(.,'Login') or contains(.,'Sign in')]");
    private final By errorMessage = By.xpath("//*[contains(text(),'incorect') or contains(text(),'incorrect') or contains(text(),'invalid') or contains(text(),'greșit') or contains(text(),'gresit') or contains(text(),'Nu am putut') or contains(text(),'parol') or contains(text(),'email')]");
    private final By profileIndicator = By.xpath("//*[contains(text(),'Contul tău') or contains(text(),'Profil') or contains(text(),'Account') or contains(@href,'account')]");

    public boolean isLoginPageLoaded() {
        return $(emailField).isVisible();
    }

    public void enterEmail(String email) {
        $(emailField).waitUntilVisible().clear();
        $(emailField).type(email);
    }

    public void enterPassword(String password) {
        $(passwordField).waitUntilVisible().clear();
        $(passwordField).type(password);
    }

    public boolean isLoginButtonEnabled() {
        return $(loginButton).isEnabled();
    }

    public void clickLogin() {
        $(loginButton).waitUntilVisible().click();
    }

    public void loginWith(String email, String password) {
        enterEmail(email);
        enterPassword(password);

        if (isLoginButtonEnabled()) {
            clickLogin();
        }
    }

    public boolean isErrorMessageVisible() {
        return $(errorMessage).isPresent() && $(errorMessage).isVisible();
    }

    public boolean isStillOnLoginPage() {
        return $(emailField).isPresent() && $(passwordField).isPresent();
    }

    public boolean isProfileIndicatorVisible() {
        return $(profileIndicator).isPresent() && $(profileIndicator).isVisible();
    }
}

