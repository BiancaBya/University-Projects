package client.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Domain.Admin;
import services.IServices;
import services.ServiceException;


import java.io.IOException;
import java.util.Optional;

public class LoginController {

    private IServices server;
    private MainController mainController;
    private Admin crtAdmin;

    private static Logger logger = LogManager.getLogger(LoginController.class);

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label loginMessage;

    Parent mainParent;

    public void setServer(IServices s){
        server=s;
    }

    public void setParent(Parent p){
        mainParent=p;
    }

    public void setMainController(MainController m){this.mainController=m;}


    public void onLoginClicked(ActionEvent actionEvent) throws IOException {

        String email = emailField.getText();
        String password = passwordField.getText();
        crtAdmin = new Admin(email,password);

        try{
            server.loginAdmin(crtAdmin, mainController);

            Stage stage=new Stage();
            stage.setTitle("Window for " + crtAdmin.getEmail());
            stage.setScene(new Scene(mainParent));

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    mainController.logout();
                    logger.debug("Closing application");
                    System.exit(0);
                }
            });

            mainController.initModel();
            stage.show();

            mainController.setAdmin(crtAdmin);
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        } catch (ServiceException e) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Transport");
            alert.setHeaderText("Authentication failure");
            alert.setContentText("Wrong username or password");
            alert.showAndWait();

        }
    }
}



