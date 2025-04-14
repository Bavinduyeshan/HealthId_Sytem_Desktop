package org.example.healthid_system_desktop.controller;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.example.healthid_system_desktop.service.UserDetails;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Dashboard {

    @FXML private Label dashboardLabel;
    @FXML private Button patientScanButton; // Matches fx:id in FXML
    @FXML private Button doctorDataButton;

    // New FXML elements for cards
    @FXML private Label patientCountLabel;
    @FXML private Label doctorCountLabel;
    @FXML private Label recordsCountLabel;
    @FXML private Rectangle patientProgress;
    @FXML private Rectangle doctorProgress;
    @FXML private Rectangle recordsProgress;

    private HostServices hostServices;
    private String authToken;
    private UserDetails userDetails;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private static final double MAX_PROGRESS_WIDTH = 320.0; // Max width of the progress bar
    private static final int MAX_COUNT = 1000; // Arbitrary max count for scaling (adjust as needed)

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public void setAuthToken(String token) {
        this.authToken = token;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    @FXML
    public void initialize() {
        // Set welcome message
        if (userDetails != null) {
            dashboardLabel.setText("Welcome, " + userDetails.getRole() + " " + userDetails.getUsername() + "!");
        } else {
            dashboardLabel.setText("Welcome to Your Dashboard!");
        }

        // Fetch and display counts
        fetchCounts();
    }

    private void fetchCounts() {
        try {
            // Replace with your actual backend API endpoints
            String patientUrl = "http://localhost:8083/pateints"; // Update this
            String docurl="http://localhost:8082/doctors";
            int patientCount = fetchData(patientUrl + "/count");
            int doctorCount = fetchData(docurl + "/doccount");
            //int recordsCount = fetchData(baseUrl + "/records/count");


            // Update labels
            patientCountLabel.setText(String.valueOf(patientCount));
            doctorCountLabel.setText(String.valueOf(doctorCount));
           // recordsCountLabel.setText(String.valueOf(recordsCount));

            // Update progress bars (simulate filling line)
            patientProgress.setWidth(Math.min((patientCount / (double) MAX_COUNT) * MAX_PROGRESS_WIDTH, MAX_PROGRESS_WIDTH));
            doctorProgress.setWidth(Math.min((doctorCount / (double) MAX_COUNT) * MAX_PROGRESS_WIDTH, MAX_PROGRESS_WIDTH));
           // recordsProgress.setWidth(Math.min((recordsCount / (double) MAX_COUNT) * MAX_PROGRESS_WIDTH, MAX_PROGRESS_WIDTH));
        } catch (Exception e) {
            e.printStackTrace();
            patientCountLabel.setText("N/A");
            doctorCountLabel.setText("N/A");
            recordsCountLabel.setText("N/A");
            showErrorAlert("Failed to fetch counts: " + e.getMessage());
        }
    }

    private int fetchData(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Authorization", "Bearer " + authToken) // Add auth token if required
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return Integer.parseInt(response.body().trim()); // Assumes response is a plain number
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
            controller.setHostServices(hostServices);

            Stage dashboardStage = (Stage) patientScanButton.getScene().getWindow();
            Stage patientStage = new Stage();
            patientStage.setTitle("Patient Data");
            patientStage.setScene(new Scene(root, 1280, 720));
            patientStage.setResizable(true);
            patientStage.show();
            dashboardStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Failed to load Patients: " + e.getMessage());
        }
    }

    @FXML
    public void openDoctorView(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthid_system_desktop/DoctorView.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("DoctorView.fxml not found at /org/example/healthid_system_desktop/DoctorView.fxml");
            }
            Parent root = loader.load();

            DoctorController controller = loader.getController();
            controller.setStage(new Stage());
            if (userDetails != null && userDetails.getDoctorId() != null) {
                controller.setLoggedInDoctorId(userDetails.getDoctorId());
            }
            controller.setAuthToken(authToken);

            Stage dashboardStage = (Stage) doctorDataButton.getScene().getWindow();
            Stage doctorStage = new Stage();
            doctorStage.setTitle("Doctor Profile");
            doctorStage.setScene(new Scene(root, 980, 701));
            doctorStage.setResizable(true);
            doctorStage.show();
            dashboardStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Failed to load Doctor View: " + e.getMessage());
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}