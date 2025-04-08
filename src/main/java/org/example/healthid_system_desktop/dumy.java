//package org.example.healthid_system_desktop;
//
//import org.example.healthid_system_desktop.model.User;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//
//public class dumy {
//}
//package org.example.healthid_system_desktop.service;
//
//
//        import org.example.healthid_system_desktop.model.User;
//        import org.springframework.http.HttpEntity;
//        import org.springframework.http.HttpHeaders;
//        import org.springframework.http.MediaType;
//        import org.springframework.http.ResponseEntity;
//        import org.springframework.web.client.RestTemplate;
//
//public class UserService {
//    private final RestTemplate restTemplate = new RestTemplate();
//    private static final String BASE_URL = "http://localhost:8080/users";
//
//    private static String authToken; // Store token globally
//
//    public boolean login(String username, String password) {
//        User loginRequest = new User(username, password);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<User> request = new HttpEntity<>(loginRequest, headers);
//
//        try {
//            ResponseEntity<org.example.healthid_system_desktop.service.UserService.AuthResponse> response = restTemplate.postForEntity(BASE_URL + "/login", request, org.example.healthid_system_desktop.service.UserService.AuthResponse.class);
//            if (response.getStatusCode().is2xxSuccessful()) {
//                authToken = response.getBody().getToken(); // Store JWT token
//                return true;
//            } else {
//                System.out.println("Login failed: " + response.getStatusCode() + " : " + response.getBody());
//            }
//        } catch (Exception e) {
//            System.out.println("Login failed: " + e.getMessage());
//        }
//        return false;
//    }
//
//
//    public String getAuthToken() {
//        return authToken;
//    }
//
//    public static class AuthResponse {
//        private String token;
//        public String getToken() { return token; }
//        public void setToken(String token) { this.token = token; }
//    }
//}


//
//
//
//package org.example.healthid_system_desktop.controller;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Alert.AlertType;
//import javafx.scene.control.CheckBox;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//import javafx.scene.image.ImageView;
//import javafx.scene.text.Text;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.control.Button;
//
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//
//import org.json.JSONObject;
//
//public class UserController {
//
//    @FXML
//    private TextField txtusername;
//
//    @FXML
//    private PasswordField txtpassword;
//
//    @FXML
//    private Button btnSignIn;
//
//    @FXML
//    private CheckBox chkRemember;
//
//    @FXML
//    private ImageView eyeIcon;
//
//    @FXML
//    private Text txtForgotPassword;
//
//    // Handle login functionality
//    @FXML
//    public void handleLogin() {
//        String username = txtusername.getText().trim();
//        String password = txtpassword.getText().trim();
//
//        if (username.isEmpty() || password.isEmpty()) {
//            showAlert("Error", "Please enter both username and password.");
//            return;
//        }
//
//        try {
//            // Prepare the API call to Spring Boot backend
//            String apiUrl = "http://localhost:8080/users/login"; // Modify with actual API URL
//            URL url = new URL(apiUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setDoOutput(true);
//
//            // Create a JSON object with the username and password
//            JSONObject json = new JSONObject();
//            json.put("username", username);
//            json.put("password", password);
//
//            // Send the JSON object as the request body
//            try (OutputStream os = connection.getOutputStream()) {
//                byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
//                os.write(input, 0, input.length);
//            }
//
//            // Get the response code from the server
//            int responseCode = connection.getResponseCode();
//
//            // Handle the server's response
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                // Read the response (could be a success message, token, etc.)
//                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String inputLine;
//                StringBuilder response = new StringBuilder();
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                in.close();
//
//                // Assuming the backend returns a JSON response with a success message or token
//                showAlert("Success", "Login Successful!");
//            } else {
//                showAlert("Error", "Invalid username or password.");
//            }
//
//            connection.disconnect();
//        } catch (Exception e) {
//            e.printStackTrace();
//            showAlert("Error", "An error occurred during login. Please try again.");
//        }
//    }
//
//    // Show alert for success or error
//    private void showAlert(String title, String message) {
//        Alert alert = new Alert(AlertType.INFORMATION);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    // Optional: Handle the eye icon click for password visibility toggle
//    @FXML
//    public void togglePasswordVisibility(MouseEvent event) {
//        if (txtpassword.isVisible()) {
//            txtpassword.setVisible(false);
//            eyeIcon.setOpacity(1.0);
//        } else {
//            txtpassword.setVisible(true);
//            eyeIcon.setOpacity(0.5);
//        }
//    }
//
//    // Optional: Handle forgot password click
//    @FXML
//    public void handleForgotPassword(MouseEvent event) {
//        // Handle forgot password functionality here
//        showAlert("Forgot Password", "Password recovery functionality coming soon.");
//    }
//}



//
//<?xml version="1.0" encoding="UTF-8"?>
//
//        <?import javafx.scene.control.Button?>
//        <?import javafx.scene.control.Label?>
//        <?import javafx.scene.image.Image?>
//        <?import javafx.scene.image.ImageView?>
//        <?import javafx.scene.layout.AnchorPane?>
//        <?import javafx.scene.layout.HBox?>
//        <?import javafx.scene.layout.VBox?>
//
//
//<AnchorPane prefHeight="701.0" prefWidth="980.0" style="-fx-background-color: white;" xmlns="http://javafx.com/javafx/23.0.1" xmlns:fx="http://javafx.com/fxml/1" fx:controller="org.example.healthid_system_desktop.DoctorView">
//<children>
//<VBox layoutX="14.0" layoutY="154.0" prefHeight="711.0" prefWidth="246.0" spacing="15" style="-fx-background-color: #2C3E50; -fx-background-radius: 20; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);">
//<children>
//<AnchorPane prefHeight="248.0" prefWidth="210.0">
//<children>
//<AnchorPane layoutX="-10.0" layoutY="-25.0" prefHeight="626.0" prefWidth="238.0">
//<children>
//<Button layoutX="14.0" layoutY="253.0" style="-fx-background-color: #1ABC9C; -fx-text-fill: #ECF0F1; -fx-font-size: 16; -fx-pref-width: 210; -fx-pref-height: 50; -fx-background-radius: 10; -fx-alignment: CENTER_LEFT; -fx-padding: 0 15 0 15;" styleClass="sidebar-button" text="Settings" />
//<Button fx:id="patientScanButton" layoutX="14.0" layoutY="104.0" onAction="#openPatients" style="-fx-background-color: #1ABC9C; -fx-text-fill: #ECF0F1; -fx-font-size: 16; -fx-pref-width: 210; -fx-pref-height: 50; -fx-background-radius: 10; -fx-alignment: CENTER_LEFT; -fx-padding: 0 15 0 15;" styleClass="sidebar-button" text="Patient Scan" textAlignment="JUSTIFY" />
//<Button layoutX="14.0" layoutY="174.0" style="-fx-background-color: #1ABC9C; -fx-text-fill: #ECF0F1; -fx-font-size: 16; -fx-pref-width: 210; -fx-pref-height: 50; -fx-background-radius: 10; -fx-alignment: CENTER_LEFT; -fx-padding: 0 15 0 15;" styleClass="sidebar-button" text="Doctor Data" />
//<Button layoutX="14.0" layoutY="328.0" style="-fx-background-color: #1ABC9C; -fx-text-fill: #ECF0F1; -fx-font-size: 16; -fx-pref-width: 210; -fx-pref-height: 50; -fx-background-radius: 10; -fx-alignment: CENTER_LEFT; -fx-padding: 0 15 0 15;" styleClass="sidebar-button" text="Reports" />
//</children>
//</AnchorPane>
//</children>
//</AnchorPane>
//</children>
//</VBox>
//<HBox alignment="CENTER_LEFT" layoutX="289.0" layoutY="34.0" prefHeight="75.0" prefWidth="906.0" spacing="20" style="-fx-background-color: #2196F3; -fx-padding: 20; -fx-background-radius: 8;">
//<children>
//<Label style="-fx-text-fill: white; -fx-font-size: 24; -fx-font-weight: bold;" text="Doctor Records" />
//</children>
//</HBox>
//<ImageView fitHeight="105.0" fitWidth="145.0" layoutX="85.0" layoutY="34.0" pickOnBounds="true" preserveRatio="true">
//<image>
//<Image url="@../../../../../../../HealthId%20system%20Frontend/my-project/src/assets/lgo.jpg" />
//</image>
//</ImageView>
//</children>
//</AnchorPane>



//dashborad202084
//<?xml version="1.0" encoding="UTF-8"?>
//        <?import javafx.scene.control.*?>
//        <?import javafx.scene.image.Image?>
//        <?import javafx.scene.image.ImageView?>
//        <?import javafx.scene.layout.*?>
//        <?import javafx.scene.shape.Rectangle?>
//        <?import javafx.scene.text.Font?>
//
//<AnchorPane prefHeight="900.0" prefWidth="1525.0" style="-fx-background-color: #F8FAFC;" stylesheets="@styles.css" xmlns="http://javafx.com/javafx/23.0.1" xmlns:fx="http://javafx.com/fxml/1" fx:controller="org.example.healthid_system_desktop.controller.Dashboard">
//<children>
//<!-- Sidebar -->
//<VBox layoutX="14.0" layoutY="174.0" prefHeight="711.0" prefWidth="238.0" spacing="20" style="-fx-background-color: #2C3E50; -fx-background-radius: 20; -fx-padding: 25; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 12, 0, 0, 3);">
//<children>
//<Button fx:id="patientScanButton" onAction="#openPatients" text="Patient Scan" style="-fx-background-color: #1ABC9C; -fx-text-fill: #ECF0F1; -fx-font-size: 16; -fx-pref-width: 190; -fx-pref-height: 50; -fx-background-radius: 12; -fx-alignment: CENTER_LEFT; -fx-padding: 0 20 0 20;" styleClass="sidebar-button" />
//<Button fx:id="doctorDataButton" onAction="#openDoctorView" text="Doctor Data" style="-fx-background-color: #1ABC9C; -fx-text-fill: #ECF0F1; -fx-font-size: 16; -fx-pref-width: 190; -fx-pref-height: 50; -fx-background-radius: 12; -fx-alignment: CENTER_LEFT; -fx-padding: 0 20 0 20;" styleClass="sidebar-button" />
//<Button text="Reports" style="-fx-background-color: #1ABC9C; -fx-text-fill: #ECF0F1; -fx-font-size: 16; -fx-pref-width: 190; -fx-pref-height: 50; -fx-background-radius: 12; -fx-alignment: CENTER_LEFT; -fx-padding: 0 20 0 20;" styleClass="sidebar-button" />
//<Button text="Settings" style="-fx-background-color: #1ABC9C; -fx-text-fill: #ECF0F1; -fx-font-size: 16; -fx-pref-width: 190; -fx-pref-height: 50; -fx-background-radius: 12; -fx-alignment: CENTER_LEFT; -fx-padding: 0 20 0 20;" styleClass="sidebar-button" />
//</children>
//</VBox>
//
//<!-- Main Content Area -->
//<VBox layoutX="292.0" layoutY="22.0" prefHeight="823.0" prefWidth="1224.0" spacing="30">
//<children>
//<!-- Header -->
//<HBox prefHeight="80.0" prefWidth="1200.0" style="-fx-background-color: #FFFFFF; -fx-background-radius: 15; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);">
//<children>
//<Label fx:id="dashboardLabel" text="Welcome to Your Dashboard" style="-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #1E3A8A;" />
//</children>
//</HBox>
//
//<!-- Cards -->
//<HBox spacing="30" alignment="CENTER">
//<!-- Patient Count Card -->
//<VBox prefHeight="180.0" prefWidth="360.0" spacing="15" style="-fx-background-color: #FFFFFF; -fx-background-radius: 20; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 4);">
//<children>
//<Label text="Total Patients" style="-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #1F2937;" />
//<Label fx:id="patientCountLabel" text="0" style="-fx-font-size: 32; -fx-text-fill: #10B981;" />
//<Rectangle fx:id="patientProgress" height="10.0" width="10.0" fill="#10B981" arcHeight="10" arcWidth="10" style="-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);" />
//</children>
//</VBox>
//
//<!-- Doctor Count Card -->
//<VBox prefHeight="180.0" prefWidth="360.0" spacing="15" style="-fx-background-color: #FFFFFF; -fx-background-radius: 20; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 4);">
//<children>
//<Label text="Total Doctors" style="-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #1F2937;" />
//<Label fx:id="doctorCountLabel" text="0" style="-fx-font-size: 32; -fx-text-fill: #3B82F6;" />
//<Rectangle fx:id="doctorProgress" height="10.0" width="0.0" fill="#3B82F6" arcHeight="10" arcWidth="10" style="-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);" />
//</children>
//</VBox>
//
//<!-- Medical Records Count Card -->
//<VBox prefHeight="180.0" prefWidth="360.0" spacing="15" style="-fx-background-color: #FFFFFF; -fx-background-radius: 20; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 4);">
//<children>
//<Label text="Medical Records" style="-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #1F2937;" />
//<Label fx:id="recordsCountLabel" text="0" style="-fx-font-size: 32; -fx-text-fill: #8B5CF6;" />
//<Rectangle fx:id="recordsProgress" height="10.0" width="0.0" fill="#8B5CF6" arcHeight="10" arcWidth="10" style="-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);" />
//</children>
//</VBox>
//</HBox>
//</children>
//</VBox>
//
//<!-- Logo -->
//<ImageView fitHeight="122.0" fitWidth="163.0" layoutX="70.0" layoutY="14.0" pickOnBounds="true" preserveRatio="true">
//<image>
//<Image url="@../../../images/lgo.jpg" />
//</image>
//</ImageView>
//</children>
//</AnchorPane>

//with recent
//<?xml version="1.0" encoding="UTF-8"?>
//        <?import javafx.scene.chart.LineChart?>
//        <?import javafx.scene.chart.NumberAxis?>
//        <?import javafx.scene.control.*?>
//        <?import javafx.scene.image.Image?>
//        <?import javafx.scene.image.ImageView?>
//        <?import javafx.scene.layout.*?>
//        <?import javafx.scene.shape.Rectangle?>
//        <?import javafx.scene.text.Font?>
//
//<AnchorPane prefHeight="900.0" prefWidth="1525.0" style="-fx-background-color: #F8FAFC;" stylesheets="@styles.css" xmlns="http://javafx.com/javafx/23.0.1" xmlns:fx="http://javafx.com/fxml/1" fx:controller="org.example.healthid_system_desktop.controller.Dashboard">
//<children>
//<!-- Sidebar -->
//<VBox layoutX="14.0" layoutY="174.0" prefHeight="711.0" prefWidth="238.0" spacing="20" style="-fx-background-color: #2C3E50; -fx-background-radius: 20; -fx-padding: 25; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 12, 0, 0, 3);">
//<children>
//<Button fx:id="patientScanButton" onAction="#openPatients" text="Patient Scan" style="-fx-background-color: #1ABC9C; -fx-text-fill: #ECF0F1; -fx-font-size: 16; -fx-pref-width: 190; -fx-pref-height: 50; -fx-background-radius: 12; -fx-alignment: CENTER_LEFT; -fx-padding: 0 20 0 20;" styleClass="sidebar-button" />
//<Button fx:id="doctorDataButton" onAction="#openDoctorView" text="Doctor Data" style="-fx-background-color: #1ABC9C; -fx-text-fill: #ECF0F1; -fx-font-size: 16; -fx-pref-width: 190; -fx-pref-height: 50; -fx-background-radius: 12; -fx-alignment: CENTER_LEFT; -fx-padding: 0 20 0 20;" styleClass="sidebar-button" />
//<Button text="Reports" style="-fx-background-color: #1ABC9C; -fx-text-fill: #ECF0F1; -fx-font-size: 16; -fx-pref-width: 190; -fx-pref-height: 50; -fx-background-radius: 12; -fx-alignment: CENTER_LEFT; -fx-padding: 0 20 0 20;" styleClass="sidebar-button" />
//<Button text="Settings" style="-fx-background-color: #1ABC9C; -fx-text-fill: #ECF0F1; -fx-font-size: 16; -fx-pref-width: 190; -fx-pref-height: 50; -fx-background-radius: 12; -fx-alignment: CENTER_LEFT; -fx-padding: 0 20 0 20;" styleClass="sidebar-button" />
//</children>
//</VBox>
//
//<!-- Main Content Area -->
//<VBox layoutX="292.0" layoutY="22.0" prefHeight="823.0" prefWidth="1224.0" spacing="30">
//<children>
//<!-- Header with Search Bar -->
//<HBox prefHeight="80.0" prefWidth="1200.0" spacing="20" style="-fx-background-color: #FFFFFF; -fx-background-radius: 15; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);">
//<children>
//<Label fx:id="dashboardLabel" text="Welcome to Your Dashboard" style="-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #1E3A8A;" />
//<TextField fx:id="searchField" promptText="Search Patients..." prefWidth="300.0" style="-fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: #CBD5E1; -fx-padding: 8;" onAction="#searchPatients" />
//</children>
//</HBox>
//
//<!-- Cards -->
//<HBox spacing="30" alignment="CENTER">
//<!-- Patient Count Card -->
//<VBox prefHeight="180.0" prefWidth="360.0" spacing="15" style="-fx-background-color: #FFFFFF; -fx-background-radius: 20; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 4);" styleClass="card">
//<children>
//<Label text="Total Patients" style="-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #1F2937;" />
//<Label fx:id="patientCountLabel" text="0" style="-fx-font-size: 32; -fx-text-fill: #10B981;" />
//<Rectangle fx:id="patientProgress" height="10.0" width="0.0" fill="#10B981" arcHeight="10" arcWidth="10" style="-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);" />
//</children>
//</VBox>
//
//<!-- Doctor Count Card -->
//<VBox prefHeight="180.0" prefWidth="360.0" spacing="15" style="-fx-background-color: #FFFFFF; -fx-background-radius: 20; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 4);" styleClass="card">
//<children>
//<Label text="Total Doctors" style="-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #1F2937;" />
//<Label fx:id="doctorCountLabel" text="0" style="-fx-font-size: 32; -fx-text-fill: #3B82F6;" />
//<Rectangle fx:id="doctorProgress" height="10.0" width="0.0" fill="#3B82F6" arcHeight="10" arcWidth="10" style="-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);" />
//</children>
//</VBox>
//
//<!-- Medical Records Count Card -->
//<VBox prefHeight="180.0" prefWidth="360.0" spacing="15" style="-fx-background-color: #FFFFFF; -fx-background-radius: 20; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 4);" styleClass="card">
//<children>
//<Label text="Medical Records" style="-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #1F2937;" />
//<Label fx:id="recordsCountLabel" text="0" style="-fx-font-size: 32; -fx-text-fill: #8B5CF6;" />
//<Rectangle fx:id="recordsProgress" height="10.0" width="0.0" fill="#8B5CF6" arcHeight="10" arcWidth="10" style="-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);" />
//</children>
//</VBox>
//</HBox>
//
//<!-- Recent Activity Log -->
//<VBox spacing="15" prefHeight="300.0" prefWidth="1180.0" style="-fx-background-color: #FFFFFF; -fx-background-radius: 20; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 4);">
//<children>
//<Label text="Recent Activity" style="-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #1F2937;" />
//<ListView fx:id="activityLog" prefHeight="250.0" prefWidth="1140.0" style="-fx-background-color: #F9FAFB; -fx-border-color: #CBD5E1; -fx-border-radius: 10; -fx-background-radius: 10;" />
//</children>
//</VBox>
//
//<!-- Mini Trend Chart -->
//<VBox spacing="15" prefHeight="200.0" prefWidth="1180.0" style="-fx-background-color: #FFFFFF; -fx-background-radius: 20; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 4);">
//<children>
//<Label text="Patient Trends (Last 30 Days)" style="-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #1F2937;" />
//<LineChart fx:id="patientTrendChart" prefHeight="150.0" prefWidth="1140.0" legendVisible="false" animated="true">
//<xAxis><NumberAxis label="Days" /></xAxis>
//<yAxis><NumberAxis label="Patients" /></yAxis>
//</LineChart>
//</children>
//</VBox>
//</children>
//</VBox>
//
//<!-- Logo -->
//<ImageView fitHeight="122.0" fitWidth="163.0" layoutX="70.0" layoutY="14.0" pickOnBounds="true" preserveRatio="true">
//<image>
//<Image url="@../../../images/lgo.jpg" />
//</image>
//</ImageView>
//</children>
//</AnchorPane>
