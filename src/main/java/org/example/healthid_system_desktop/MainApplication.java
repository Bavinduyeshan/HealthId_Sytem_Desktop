package org.example.healthid_system_desktop;



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.healthid_system_desktop.controller.UserController;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthid_system_desktop/Login.fxml"));
        Scene scene = new Scene(loader.load());
        UserController loginController = loader.getController();

        // Inject HostServices into LoginController
        loginController.setHostServices(getHostServices());

        primaryStage.setScene(scene);
        primaryStage.setTitle("HealthID System - Login");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
//package org.example.healthid_system_desktop;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//public class MainApplication extends Application {
//    @Override
//    public void start(Stage stage) throws Exception {
//        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Patients2.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        stage.setTitle("Login Page");
//        stage.setScene(scene);
//
//        // Center the window on the screen
//        stage.centerOnScreen();
//
//        // Set full screen mode
//        stage.setFullScreen(true);
//
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }
//}