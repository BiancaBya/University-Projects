package org.example.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.Domain.Admin;
import org.example.Service.Service;

import java.io.IOException;
import java.util.Optional;

public class LoginController {

    private Service service;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label loginMessage;


    public void setService(Service service) {
        this.service = service;
    }


    public void onLoginClicked() throws IOException {

        String email = emailField.getText();
        String password = passwordField.getText();

        Optional<Admin> foundAdmin = service.loginAdmin(email, password);

        if(foundAdmin.isPresent()){
            openMainScene(foundAdmin.get());
        }
        else{
            loginMessage.setText("Invalid email or password");
        }
    }

    private void openMainScene(Admin admin) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/main.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) loginMessage.getScene().getWindow();
        stage.setScene(scene);

        MainController controller = loader.getController();
        controller.setService(service);

        stage.show();

    }

}



