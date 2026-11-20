package mydrinkshop.pages;

import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;

public class AccountPage extends PageObject {

    private final By accountIndicator = By.xpath("//*[contains(text(),'Contul tău') or contains(text(),'Profil') or contains(text(),'Account') or contains(@href,'account')]");
    private final By addListingButton = By.xpath("//a[contains(@href,'adding') or contains(.,'Adaugă anunț') or contains(.,'Adauga anunt') or contains(.,'Adaugă anunț nou') or contains(.,'Add listing')]");
    private final By logoutButton = By.xpath("//a[contains(@href,'logout') or contains(.,'Ieși') or contains(.,'Iesi') or contains(.,'Logout') or contains(.,'Deconectare')]");

    public boolean isUserLoggedIn() {
        return $(accountIndicator).isPresent() || $(addListingButton).isPresent();
    }

    public void goToAddListing() {
        $(addListingButton).waitUntilClickable().click();
    }

    public boolean isAddListingButtonVisible() {
        return $(addListingButton).isPresent() && $(addListingButton).isVisible();
    }

    public void logout() {
        if ($(logoutButton).isPresent()) {
            $(logoutButton).waitUntilClickable().click();
        }
    }
}
