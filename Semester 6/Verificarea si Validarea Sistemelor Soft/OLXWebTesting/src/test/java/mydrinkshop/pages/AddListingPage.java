package mydrinkshop.pages;

import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;

public class AddListingPage extends PageObject {

    private final By pageTitle = By.xpath("//*[contains(text(),'Adaugă anunț') or contains(text(),'Adauga anunt') or contains(text(),'Publică anunț') or contains(text(),'Publica anunt') or contains(text(),'Add listing')]");
    private final By categorySection = By.xpath("//*[contains(text(),'Categorie') or contains(text(),'Category')]");
    private final By titleField = By.xpath("//input[contains(@name,'title') or contains(@placeholder,'Titlu') or contains(@placeholder,'title')]");
    private final By descriptionField = By.xpath("//textarea[contains(@name,'description') or contains(@placeholder,'Descriere') or contains(@placeholder,'description')]");
    private final By priceField = By.xpath("//input[contains(@name,'price') or contains(@placeholder,'Preț') or contains(@placeholder,'Pret') or contains(@placeholder,'price')]");
    private final By nextOrPublishButton = By.xpath("//button[contains(.,'Continuă') or contains(.,'Continua') or contains(.,'Mai departe') or contains(.,'Publică') or contains(.,'Publica') or contains(.,'Next') or contains(.,'Publish')]");
    private final By successOrVerification = By.xpath("//*[contains(text(),'Anunțul tău') or contains(text(),'Anuntul tau') or contains(text(),'Verificare') or contains(text(),'telefon') or contains(text(),'SMS') or contains(text(),'Publicat') or contains(text(),'Felicitări') or contains(text(),'Felicitari')]");

    public boolean isAddListingPageLoaded() {
        return $(pageTitle).isPresent() || $(categorySection).isPresent() || $(titleField).isPresent();
    }

    public void enterTitle(String title) {
        if ($(titleField).isPresent()) {
            $(titleField).waitUntilVisible().clear();
            $(titleField).type(title);
        }
    }

    public void enterDescription(String description) {
        if ($(descriptionField).isPresent()) {
            $(descriptionField).waitUntilVisible().clear();
            $(descriptionField).type(description);
        }
    }

    public void enterPrice(String price) {
        if ($(priceField).isPresent()) {
            $(priceField).waitUntilVisible().clear();
            $(priceField).type(price);
        }
    }

    public boolean isNextOrPublishButtonEnabled() {
        return $(nextOrPublishButton).isPresent() && $(nextOrPublishButton).isEnabled();
    }

    public void clickNextOrPublish() {
        $(nextOrPublishButton).waitUntilVisible().click();
    }

    public boolean isSuccessOrVerificationVisible() {
        return $(successOrVerification).isPresent() && $(successOrVerification).isVisible();
    }
}
