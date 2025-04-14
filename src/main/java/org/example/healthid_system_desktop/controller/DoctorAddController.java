package org.example.healthid_system_desktop.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DoctorAddController {


    @FXML
    private TextField txtFirstname;
    @FXML
    private TextField txtLastname;
    @FXML
    private TextField txtPhonenumber;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtSpecialization;
    @FXML
    private TextField txtExperience;
    @FXML
    private TextField txtEducation;
    @FXML
    private Button btnSubmitDoctor;
    @FXML
    private Button btnBackToLogin;

    private Integer userId; // To store the user ID passed from registration

    // Method to initialize the controller with user ID
    public void initializeWithUserId(Integer userId) {
        this.userId=userId;

    }

    @FXML
    private void handleSubmitDoctor(ActionEvent event) {
        // Validate input fields
        if (validateInputs()) {
            try {
                // Collect doctor details
                String firstName = txtFirstname.getText();
                String lastName = txtLastname.getText();
                String phoneNumber = txtPhonenumber.getText();
                String email = txtEmail.getText();
                String specialization = txtSpecialization.getText();
                String experience = txtExperience.getText();
                String education = txtEducation.getText();

                // Create JSON payload
                String jsonPayload = String.format(
                        "{\"userId\":\"%s\",\"firstname\":\"%s\",\"lastname\":\"%s\",\"phonenumber\":\"%s\",\"email\":\"%s\",\"specilization\":\"%s\",\"experience\":%s,\"education\":\"%s\"}",
                        userId, firstName, lastName, phoneNumber, email, specialization, experience, education
                );

                // Make HTTP POST request to the Spring Boot API
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8082/doctors/add"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                // Check response status
                if (response.statusCode() == 200 || response.statusCode() == 201) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Doctor profile added successfully!");
                    // Optionally, redirect to another screen (e.g., dashboard)
                    // openDashboard();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to add doctor profile: " + response.body());
                }

            } catch (IOException | InterruptedException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to connect to the server: " + e.getMessage());
            }
        }
    }

    @FXML
    private void openLogin(ActionEvent event) {
        try {
            // Load the login FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthid_system_desktop/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnBackToLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login - HealthID System");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load login screen: " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        if (txtFirstname.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "First Name is required.");
            return false;
        }
        if (txtLastname.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Last Name is required.");
            return false;
        }
        if (txtPhonenumber.getText().isEmpty() || !txtPhonenumber.getText().matches("\\d{10}")) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Valid Phone Number is required (10 digits).");
            return false;
        }
        if (txtEmail.getText().isEmpty() || !txtEmail.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Valid Email is required.");
            return false;
        }
        if (txtSpecialization.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Specialization is required.");
            return false;
        }
        if (txtExperience.getText().isEmpty() || !txtExperience.getText().matches("\\d+")) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Valid Years of Experience is required (numeric).");
            return false;
        }
        if (txtEducation.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Education is required.");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}