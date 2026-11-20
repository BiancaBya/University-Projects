module drinkshop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    requires org.controlsfx.controls;

    opens drinkshop.ui to javafx.fxml;
    exports drinkshop.ui;

    opens drinkshop.domain to  javafx.base;
    exports drinkshop.domain;


    opens drinkshop.repository;
    opens drinkshop.validator;
    opens drinkshop.service;

    exports drinkshop.repository;
    exports drinkshop.validator;
    exports drinkshop.service;
}