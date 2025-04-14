package org.example.healthid_system_desktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.healthid_system_desktop.service.LoginService;

public class ForgotPasswordController {

    @FXML
    private TextField txtEmail;

    private final LoginService loginService = new LoginService();

    @FXML
    public void handleSubmit() {
        String email = txtEmail.getText().trim();

        if (email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter your email.");
            return;
        }

        try {
            boolean success = loginService.requestPasswordReset(email);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Password reset link sent to your email.");
                closePopup();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to send password reset link. Please try again.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to send password reset link: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closePopup() {
        Stage stage = (Stage) txtEmail.getScene().getWindow();
        stage.close();
    }
}