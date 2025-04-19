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



//dashbaord full
//can we integrate nice visually apealing attractive charts to dashboard to increse attraction<?xml version="1.0" encoding="UTF-8"?>
//
//        <?import javafx.scene.control.Button?>
//        <?import javafx.scene.control.Label?>
//        <?import javafx.scene.image.Image?>
//        <?import javafx.scene.image.ImageView?>
//        <?import javafx.scene.layout.AnchorPane?>
//        <?import javafx.scene.layout.HBox?>
//        <?import javafx.scene.layout.VBox?>
//        <?import javafx.scene.shape.Rectangle?>
//        <?import javafx.scene.text.Font?>
//
//        <AnchorPane prefHeight="900.0" prefWidth="1525.0" stylesheets="@dashboard.css" xmlns="http://javafx.com/javafx/23.0.1" xmlns:fx="http://javafx.com/fxml/1" fx:controller="org.example.healthid_system_desktop.controller.Dashboard">
//        <children>
//        <!-- Sidebar -->
//        <VBox layoutX="14.0" layoutY="174.0" prefHeight="711.0" prefWidth="238.0" spacing="15" styleClass="sidebar">
//        <children>
//        <Button fx:id="patientScanButton" onAction="#openPatients" styleClass="sidebar-button" text="Patient Scan" />
//        <Button fx:id="doctorDataButton" onAction="#openDoctorView" styleClass="sidebar-button" text="Doctor Data" />
//        <Button styleClass="sidebar-button" text="Reports" />
//        <Button styleClass="sidebar-button" text="Settings" />
//        </children>
//        </VBox>
//
//        <!-- Main Content Area -->
//        <VBox layoutX="292.0" layoutY="22.0" prefHeight="823.0" prefWidth="1224.0" spacing="30">
//        <children>
//        <!-- Header -->
//        <HBox prefHeight="80.0" prefWidth="1200.0" styleClass="header-card">
//        <children>
//        <Label fx:id="dashboardLabel" styleClass="header-title" text="Welcome to Your Dashboard" />
//        </children>
//        </HBox>
//
//        <!-- Cards -->
//        <HBox alignment="CENTER" spacing="30">
//        <!-- Patient Count Card -->
//        <VBox prefHeight="180.0" prefWidth="360.0" spacing="15" styleClass="dashboard-card">
//        <children>
//        <Label styleClass="card-title" text="Total Patients" />
//        <Label fx:id="patientCountLabel" styleClass="card-value-patient" text="0" />
//        <Rectangle fx:id="patientProgress" arcHeight="10" arcWidth="10" fill="#10B981" height="10.0" styleClass="progress-bar" width="10.0" />
//        </children>
//        </VBox>
//
//        <!-- Doctor Count Card -->
//        <VBox prefHeight="180.0" prefWidth="360.0" spacing="15" styleClass="dashboard-card">
//        <children>
//        <Label styleClass="card-title" text="Total Doctors" />
//        <Label fx:id="doctorCountLabel" styleClass="card-value-doctor" text="0" />
//        <Rectangle fx:id="doctorProgress" arcHeight="10" arcWidth="10" fill="#3B82F6" height="10.0" styleClass="progress-bar" width="0.0" />
//        </children>
//        </VBox>
//
//        <!-- Medical Records Count Card -->
//        <VBox prefHeight="180.0" prefWidth="360.0" spacing="15" styleClass="dashboard-card">
//        <children>
//        <Label styleClass="card-title" text="Medical Records" />
//        <Label fx:id="recordsCountLabel" styleClass="card-value-records" text="0" />
//        <Rectangle fx:id="recordsProgress" arcHeight="10" arcWidth="10" fill="#8B5CF6" height="10.0" styleClass="progress-bar" width="0.0" />
//        </children>
//        </VBox>
//        </HBox>
//        </children>
//        </VBox>
//
//        <!-- Logo -->
//        <ImageView fitHeight="105.0" fitWidth="113.0" layoutX="70.0" layoutY="14.0" pickOnBounds="true" preserveRatio="true">
//        <image>
//        <Image url="@../../../images/8752202.png" />
//        </image>
//        </ImageView>
//        <Label fx:id="usernameLabel" layoutX="19.0" layoutY="136.0" prefHeight="27.0" prefWidth="251.0" styleClass="sidebar-title" text="Welcome, User!">
//        <font>
//        <Font size="18.0" />
//        </font>
//        </Label>
//        </children>
//        </AnchorPane>
//        /* styles.css */
//
//        /* Background for the entire AnchorPane */
//        .root {
//        -fx-background-color: linear-gradient(to bottom right, #e0e7ff, #f0f5ff);
//        }
//
//        /* Sidebar */
//        .sidebar {
//        -fx-background-color: linear-gradient(to bottom, #1e3a8a, #1e6bd6);
//        -fx-background-radius: 20px;
//        -fx-padding: 25px;
//        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 12, 0, 0, 3);
//        }
//
//        /* Sidebar Buttons */
//        .sidebar-button {
//        -fx-background-color: #388af5;
//        -fx-text-fill: #ffffff;
//        -fx-font-size: 16px;
//        -fx-font-family: 'Arial';
//        -fx-pref-width: 190px;
//        -fx-pref-height: 50px;
//        -fx-background-radius: 12px;
//        -fx-alignment: CENTER_LEFT;
//        -fx-padding: 0 20 0 20;
//        -fx-cursor: hand;
//        }
//
//        .sidebar-button:hover {
//        -fx-background-color: #1e6bd6;
//        }
//
//        /* Header Card */
//        .header-card {
//        -fx-background-color: #ffffff;
//        -fx-background-radius: 15px;
//        -fx-padding: 20px;
//        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);
//        }
//
//        /* Header Title */
//        .header-title {
//        -fx-font-size: 28px;
//        -fx-font-weight: bold;
//        -fx-text-fill: #1e3a8a;
//        -fx-font-family: 'Arial';
//        }
//
//        /* Dashboard Card */
//        .dashboard-card {
//        -fx-background-color: #ffffff;
//        -fx-background-radius: 20px;
//        -fx-padding: 20px;
//        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 4);
//        -fx-cursor: hand;
//        }
//
//        .dashboard-card:hover {
//        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 6);
//        -fx-translate-y: -5px;
//        }
//
//        /* Card Title */
//        .card-title {
//        -fx-font-size: 18px;
//        -fx-font-weight: bold;
//        -fx-text-fill: #1e3a8a;
//        -fx-font-family: 'Arial';
//        }
//
//        /* Card Values */
//        .card-value-patient {
//        -fx-font-size: 32px;
//        -fx-text-fill: #10B981; /* Green for patients */
//        -fx-font-family: 'Arial';
//        }
//
//        .card-value-doctor {
//        -fx-font-size: 32px;
//        -fx-text-fill: #388af5; /* Blue for doctors */
//        -fx-font-family: 'Arial';
//        }
//
//        .card-value-records {
//        -fx-font-size: 32px;
//        -fx-text-fill: #8B5CF6; /* Purple for records */
//        -fx-font-family: 'Arial';
//        }
//
//        /* Progress Bar */
//        .progress-bar {
//        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);
//        }
//
//        /* Existing styles (kept for consistency) */
//
//        /* Left Pane with Image */
//        .left-pane {
//        -fx-background-color: linear-gradient(to bottom right, #388af5, #1e6bd6);
//        -fx-background-radius: 20px;
//        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.5, 2, 2);
//        }
//
//        /* Login Card (VBox) */
//        .login-card {
//        -fx-background-color: white;
//        -fx-background-radius: 20px;
//        -fx-padding: 40px;
//        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0.3, 0, 5);
//        }
//
//        /* Form Label Styling */
//        .form-label {
//        -fx-font-size: 12px;
//        -fx-text-fill: #6b7280;
//        -fx-font-family: 'Arial';
//        }
//
//        /* TextField Styling */
//        .text-field {
//        -fx-background-radius: 10px;
//        -fx-padding: 12px;
//        -fx-border-color: #d1d5db;
//        -fx-border-radius: 10px;
//        -fx-background-color: #f9fafb;
//        -fx-font-family: 'Arial';
//        -fx-font-size: 14px;
//        }
//
//        .text-field:hover {
//        -fx-background-color: #f1f5f9;
//        -fx-border-color: #388af5;
//        }
//
//        /* ScrollPane Styling */
//        .scroll-pane {
//        -fx-background: transparent;
//        -fx-background-color: transparent;
//        -fx-border-color: transparent;
//        }
//
//        .scroll-pane .viewport {
//        -fx-background-color: transparent;
//        }
//
//        /* Style the ScrollBar */
//        .scroll-pane .scroll-bar:vertical {
//        -fx-background-color: transparent;
//        -fx-pref-width: 8px;
//        -fx-padding: 0 5px 0 0;
//        }
//
//        .scroll-pane .scroll-bar:vertical .thumb {
//        -fx-background-color: #6b7280;
//        -fx-background-radius: 5px;
//        }
//
//        .scroll-pane .scroll-bar:vertical .thumb:hover {
//        -fx-background-color: #388af5;
//        }
//
//        /* ComboBox Styling */
//        .combo-box {
//        -fx-background-radius: 10px;
//        -fx-padding: 12px;
//        -fx-border-color: #d1d5db;
//        -fx-border-radius: 10px;
//        -fx-background-color: #f9fafb;
//        -fx-font-family: 'Arial';
//        -fx-font-size: 14px;
//        }
//
//        .combo-box:hover {
//        -fx-background-color: #f1f5f9;
//        -fx-border-color: #388af5;
//        }
//
//        .combo-box .list-cell {
//        -fx-background-color: #f9fafb;
//        -fx-text-fill: #1e3a8a;
//        -fx-font-family: 'Arial';
//        -fx-font-size: 14px;
//        }
//
//        .combo-box .list-cell:hover {
//        -fx-background-color: #e0f2fe;
//        -fx-text-fill: #1e6bd6;
//        }
//
//        /* Forgot Password Text */
//        .forgot-password {
//        -fx-fill: #388af5;
//        -fx-underline: true;
//        -fx-cursor: hand;
//        -fx-font-family: 'Arial';
//        -fx-font-size: 13px;
//        }
//
//        .forgot-password:hover {
//        -fx-fill: #1e6bd6;
//        }
//
//        /* Sign In Button */
//        .sign-in-button {
//        -fx-background-color: #388af5;
//        -fx-text-fill: white;
//        -fx-background-radius: 10px;
//        -fx-font-size: 16px;
//        -fx-font-family: 'Arial';
//        -fx-font-weight: bold;
//        -fx-padding: 12px;
//        -fx-cursor: hand;
//        }
//
//        .sign-in-button:hover {
//        -fx-background-color: #1e6bd6;
//        }
//
//        /* Register Now Button */
//        .register-button {
//        -fx-background-color: transparent;
//        -fx-text-fill: #388af5;
//        -fx-font-size: 14px;
//        -fx-font-family: 'Arial';
//        -fx-padding: 10px;
//        -fx-border-color: #388af5;
//        -fx-border-radius: 10px;
//        -fx-border-width: 2px;
//        -fx-cursor: hand;
//        }
//
//        .register-button:hover {
//        -fx-background-color: #e0f2fe;
//        -fx-text-fill: #1e6bd6;
//        }
//
//        /* Popup Card */
//        .popup-card {
//        -fx-background-color: white;
//        -fx-background-radius: 15px;
//        -fx-padding: 20px;
//        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0.3, 0, 5);
//        }package org.example.healthid_system_desktop.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import javafx.application.HostServices;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.shape.Rectangle;
//import javafx.stage.Stage;
//import org.example.healthid_system_desktop.service.UserDetails;
//
//import java.io.IOException;
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//
//public class Dashboard {
//
//    @FXML private Label dashboardLabel;
//    @FXML private Button patientScanButton; // Matches fx:id in FXML
//    @FXML private Button doctorDataButton;
//
//    // New FXML elements for cards
//    @FXML private Label patientCountLabel;
//    @FXML private Label doctorCountLabel;
//    @FXML private Label recordsCountLabel;
//    @FXML private Rectangle patientProgress;
//    @FXML private Rectangle doctorProgress;
//    @FXML private Rectangle recordsProgress;
//    @FXML private Label usernameLabel;
//
//    private HostServices hostServices;
//    private String authToken;
//    private UserDetails userDetails;
//
//    private final HttpClient httpClient = HttpClient.newHttpClient();
//    private static final double MAX_PROGRESS_WIDTH = 320.0; // Max width of the progress bar
//    private static final int MAX_COUNT = 1000; // Arbitrary max count for scaling (adjust as needed)
//
//    public void setHostServices(HostServices hostServices) {
//        this.hostServices = hostServices;
//    }
//
//    public void setAuthToken(String token) {
//        this.authToken = token;
//        System.out.println("setAuthToken called with token: " + (token != null ? "present" : "null"));
//    }
//
//    public void setUserDetails(UserDetails userDetails) {
//        this.userDetails = userDetails;
//        System.out.println("setUserDetails called. userDetails: " + (userDetails != null ? userDetails.getUsername() : "null"));
//        updateWelcomeMessage();
//    }
//
//    private void updateWelcomeMessage() {
//        if (userDetails != null) {
//            usernameLabel.setText("Welcome, " + userDetails.getRole() + " " + userDetails.getUsername() + "!");
//            System.out.println("Welcome message updated: Welcome, " + userDetails.getRole() + " " + userDetails.getUsername() + "!");
//        } else {
//            dashboardLabel.setText("Welcome to Your Dashboard!");
//            System.out.println("Welcome message set to fallback: Welcome to Your Dashboard!");
//        }
//    }
//
//    @FXML
//    public void initialize() {
//        System.out.println("Dashboard initialize called. authToken: " + (authToken != null ? "present" : "null") + ", userDetails: " + (userDetails != null ? userDetails.getUsername() : "null"));
//        updateWelcomeMessage(); // Set initial message (likely fallback)
//        fetchCounts(); // Proceed with counts
//    }
//
//    private void fetchUserDetails() {
//        try {
//            String userUrl = "http://localhost:8080/users/api/user/me";
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(new URI(userUrl))
//                    .header("Authorization", "Bearer " + authToken)
//                    .header("Content-Type", "application/json")
//                    .GET()
//                    .build();
//            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//
//            if (response.statusCode() == 200) {
//                ObjectMapper mapper = new ObjectMapper();
//                UserDetails fetchedDetails = mapper.readValue(response.body(), UserDetails.class);
//                this.userDetails = fetchedDetails;
//                updateWelcomeMessage();
//                System.out.println("Fetched user details: " + fetchedDetails.getUsername() + ", role: " + fetchedDetails.getRole());
//            } else {
//                System.err.println("Failed to fetch user details. Status: " + response.statusCode() + ", Body: " + response.body());
//                updateWelcomeMessage();
//            }
//        } catch (Exception e) {
//            System.err.println("Error fetching user details: " + e.getMessage());
//            updateWelcomeMessage();
//        }
//    }
//
//    private void fetchCounts() {
//        try {
//            // Replace with your actual backend API endpoints
//            String patientUrl = "http://localhost:8083/pateints"; // Update this
//            String docurl="http://localhost:8082/doctors";
//            int patientCount = fetchData(patientUrl + "/count");
//            int doctorCount = fetchData(docurl + "/doccount");
//            //int recordsCount = fetchData(baseUrl + "/records/count");
//
//
//            // Update labels
//            patientCountLabel.setText(String.valueOf(patientCount));
//            doctorCountLabel.setText(String.valueOf(doctorCount));
//            // recordsCountLabel.setText(String.valueOf(recordsCount));
//
//            // Update progress bars (simulate filling line)
//            patientProgress.setWidth(Math.min((patientCount / (double) MAX_COUNT) * MAX_PROGRESS_WIDTH, MAX_PROGRESS_WIDTH));
//            doctorProgress.setWidth(Math.min((doctorCount / (double) MAX_COUNT) * MAX_PROGRESS_WIDTH, MAX_PROGRESS_WIDTH));
//            // recordsProgress.setWidth(Math.min((recordsCount / (double) MAX_COUNT) * MAX_PROGRESS_WIDTH, MAX_PROGRESS_WIDTH));
//        } catch (Exception e) {
//            e.printStackTrace();
//            patientCountLabel.setText("N/A");
//            doctorCountLabel.setText("N/A");
//            recordsCountLabel.setText("N/A");
//            showErrorAlert("Failed to fetch counts: " + e.getMessage());
//        }
//    }
//
//    private int fetchData(String url) throws Exception {
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(new URI(url))
//                .header("Authorization", "Bearer " + authToken) // Add auth token if required
//                .GET()
//                .build();
//        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//        return Integer.parseInt(response.body().trim()); // Assumes response is a plain number
//    }
//
//    @FXML
//    public void openPatients(javafx.event.ActionEvent event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthid_system_desktop/Patients2.fxml"));
//            if (loader.getLocation() == null) {
//                throw new IOException("Patients2.fxml not found at /org/example/healthid_system_desktop/Patients2.fxml");
//            }
//            Parent root = loader.load();
//
//            patientController controller = loader.getController();
//            controller.setAuthToken(authToken);
//            if (userDetails != null) {
//                controller.setUserDetails(userDetails);
//            }
//            controller.setHostServices(hostServices);
//
//            Stage dashboardStage = (Stage) patientScanButton.getScene().getWindow();
//            Stage patientStage = new Stage();
//            patientStage.setTitle("Patient Data");
//            patientStage.setScene(new Scene(root, 1280, 720));
//            patientStage.setResizable(true);
//            patientStage.show();
//            dashboardStage.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            showErrorAlert("Failed to load Patients: " + e.getMessage());
//        }
//    }
//
//    @FXML
//    public void openDoctorView(javafx.event.ActionEvent event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthid_system_desktop/DoctorView.fxml"));
//            if (loader.getLocation() == null) {
//                throw new IOException("DoctorView.fxml not found at /org/example/healthid_system_desktop/DoctorView.fxml");
//            }
//            Parent root = loader.load();
//
//            DoctorController controller = loader.getController();
//            controller.setStage(new Stage());
//            if (userDetails != null && userDetails.getDoctorId() != null) {
//                controller.setLoggedInDoctorId(userDetails.getDoctorId());
//            }
//            controller.setAuthToken(authToken);
//
//            Stage dashboardStage = (Stage) doctorDataButton.getScene().getWindow();
//            Stage doctorStage = new Stage();
//            doctorStage.setTitle("Doctor Profile");
//            doctorStage.setScene(new Scene(root, 980, 701));
//            doctorStage.setResizable(true);
//            doctorStage.show();
//            dashboardStage.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            showErrorAlert("Failed to load Doctor View: " + e.getMessage());
//        }
//    }
//
//    private void showErrorAlert(String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Error");
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//}