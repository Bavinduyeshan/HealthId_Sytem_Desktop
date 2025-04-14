package org.example.healthid_system_desktop.controller;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Pattern;
import org.json.JSONObject;

public class RegisterController {

    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtConfirmPassword;
    @FXML
    private Button btnSignUp;

    @FXML
    private ComboBox<String> roleComboBox;

    private static final String BASE_URL = "http://localhost:8080/users"; // Update with your backend URL
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String role = roleComboBox.getSelectionModel().getSelectedItem();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        // Validation
        if (username.isEmpty() || email.isEmpty() || role == null || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
            return;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid email address.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Passwords do not match.");
            return;
        }

        if (password.length() < 6) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Password must be at least 6 characters long.");
            return;
        }

        // Prepare JSON payload
        String jsonPayload = String.format(
                "{\"username\":\"%s\",\"email\":\"%s\",\"role\":\"%s\",\"password\":\"%s\"}",
                username, email, role, password
        );

        // Send data to backend
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/add"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                // Log the registration response
                System.out.println("Registration API Response: " + response.body());

                // Fetch userId using the username
                String id = fetchUserIdByUsername(username);
                if (id == null) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve user ID for username: " + username);
                    return;
                }

                showAlert(Alert.AlertType.INFORMATION, "Success", "Registration successful!");
                clearFields();

                redirectToDoctorAddForm(Integer.valueOf(id));
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Registration failed: " + response.body());
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to connect to the server: " + e.getMessage());
        }
    }


    // Method to fetch userId by username
    private String fetchUserIdByUsername(String username) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/byUsername/" + username))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Log the response for debugging
                System.out.println("Fetch User API Response: " + response.body());

                // Parse the response to extract userId
                JSONObject jsonResponse = new JSONObject(response.body());
                if (!jsonResponse.has("id")) {
                    System.out.println("Error: userId not found in response.");
                    return null;
                }
                return String.valueOf(jsonResponse.getInt("id"));
            } else {
                System.out.println("Error fetching user data: " + response.body());
                return null;
            }
        } catch (Exception e) {
            System.out.println("Exception while fetching userId: " + e.getMessage());
            return null;
        }
    }

    // Method to redirect to DoctorAddForm
    private void redirectToDoctorAddForm(Integer userId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthid_system_desktop/DoctorsAddForm.fxml"));
            Parent root = loader.load();

            // Get the DoctorController and pass the userId
            DoctorAddController doctorController = loader.getController();
            doctorController.initializeWithUserId(Integer.valueOf(String.valueOf(userId)));

            // Set up the new scene
            Stage stage = (Stage) btnSignUp.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Doctor Profile - HealthID System");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load doctor add form: " + e.getMessage());
        }
    }

    // Utility method to show alerts
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Clear form fields after successful registration
    private void clearFields() {
        txtUsername.clear();
        txtEmail.clear();
        txtPassword.clear();
        txtConfirmPassword.clear();
    }

    // Existing login handler (from your Login.fxml, for completeness)
    @FXML
    private void handleLogin(ActionEvent event) {
        // Implement login logic here if needed
        showAlert(Alert.AlertType.INFORMATION, "Login", "Login functionality not implemented yet.");
    }
}