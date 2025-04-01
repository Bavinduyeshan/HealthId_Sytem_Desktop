package org.example.healthid_system_desktop;



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login Page");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
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