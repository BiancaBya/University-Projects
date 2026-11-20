package mydrinkshop.tests;

public class ListingData {
    private final String title;
    private final String description;
    private final String price;

    public ListingData(String title, String description, String price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }
}
