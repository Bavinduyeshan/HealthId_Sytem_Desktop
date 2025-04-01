package org.example.healthid_system_desktop.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.healthid_system_desktop.service.UserDetails;

import java.io.IOException;

public class Dashboard {

    @FXML private Label dashboardLabel;
    @FXML private Button patientScanButton; // Matches fx:id in FXML

    private String authToken;
    private UserDetails userDetails;

    @FXML
    public void initialize() {
        if (userDetails != null) {
            dashboardLabel.setText("Welcome, " + userDetails.getRole() + " " + userDetails.getUsername() + "!");
        } else {
            dashboardLabel.setText("Welcome to Your Dashboard!");
        }
    }

    public void setAuthToken(String token) {
        this.authToken = token;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    @FXML
    public void openPatients(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthid_system_desktop/Patients2.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("Patients2.fxml not found at /org/example/healthid_system_desktop/Patients2.fxml");
            }
            Parent root = loader.load();

            patientController controller = loader.getController();
            controller.setAuthToken(authToken);
            if (userDetails != null) {
                controller.setUserDetails(userDetails);
            }

            // Get the current dashboard stage and close it
            Stage dashboardStage = (Stage) patientScanButton.getScene().getWindow();

            // Create a new stage for the patient window
            Stage patientStage = new Stage();
            patientStage.setTitle("Patient Data");
            patientStage.setScene(new Scene(root, 1280, 720)); // Set size to match layout
            patientStage.setResizable(true);
            patientStage.show();

            // Close the dashboard stage after opening the patient window
            dashboardStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Failed to load Patients: " + e.getMessage());
            alert.showAndWait();
        }
    }
}

