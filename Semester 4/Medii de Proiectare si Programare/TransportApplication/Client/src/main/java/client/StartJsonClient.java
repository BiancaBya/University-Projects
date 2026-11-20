package client;

import client.gui.LoginController;
import client.gui.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import network.jsonprotocol.ServicesJsonProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.IServices;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class StartJsonClient extends Application {

    private Stage primaryStage;

    private static int defaultPort = 55555;
    private static String defaultServer = "localhost";

    private static Logger logger = LogManager.getLogger(StartJsonClient.class);

    public void start(Stage primaryStage) throws Exception {
        logger.debug("In start");

        Properties clientProps = new Properties();
        try{
            clientProps.load(StartJsonClient.class.getResourceAsStream("/client.properties"));
            logger.info("Client properties set {} ",clientProps);
            clientProps.list(System.out);
        }catch (IOException e) {
            logger.error("Cannot find chatclient.properties {}", String.valueOf(e));
            logger.debug("Looking for chatclient.properties in folder {}",(new File(".")).getAbsolutePath());
            return;
        }

        String serverIP = clientProps.getProperty("server.host", defaultServer);
        int serverPort = defaultPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            logger.error("Wrong port number {}", ex.getMessage());
            logger.debug("Using default port: {}", defaultPort);
        }
        logger.info("Using server IP {}", serverIP);
        logger.info("Using server port {}", serverPort);

        IServices server = new ServicesJsonProxy(serverIP, serverPort);



        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("Views/login.fxml")
        );

        Parent root=loader.load();

        LoginController loginCtrl =
                loader.<LoginController>getController();
        loginCtrl.setServer(server);



        FXMLLoader mainLoader = new FXMLLoader(
                getClass().getClassLoader().getResource("Views/main.fxml")
        );

        Parent mainRoot=mainLoader.load();

        MainController mainCtrl =
                mainLoader.<MainController>getController();
        mainCtrl.setServer(server);



        loginCtrl.setMainController(mainCtrl);
        loginCtrl.setParent(mainRoot);

        primaryStage.setTitle("Transport");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
}



