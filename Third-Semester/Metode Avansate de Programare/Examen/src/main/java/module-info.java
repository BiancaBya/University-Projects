module com.example.examen {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.examen to javafx.fxml, javafx.base;

    exports Domain;
    exports Service;
    exports Repository;
    exports com.example.examen;
}
