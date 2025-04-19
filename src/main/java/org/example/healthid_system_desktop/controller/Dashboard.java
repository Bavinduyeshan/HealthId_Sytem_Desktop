package org.example.healthid_system_desktop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
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
    @FXML private Button patientScanButton;
    @FXML private Button doctorDataButton;
    @FXML private Label patientCountLabel;
    @FXML private Label doctorCountLabel;
    @FXML private Label recordsCountLabel;
    @FXML private Rectangle patientProgress;
    @FXML private Rectangle doctorProgress;
    @FXML private Rectangle recordsProgress;
    @FXML private Label usernameLabel;
    @FXML private PieChart dataDistributionChart;
    @FXML private BarChart<String, Number> departmentChart;
    @FXML private LineChart<String, Number> recordsTrendChart;

    private HostServices hostServices;
    private String authToken;
    private UserDetails userDetails;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private static final double MAX_PROGRESS_WIDTH = 320.0;
    private static final int MAX_COUNT = 1000;

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public void setAuthToken(String token) {
        this.authToken = token;
        System.out.println("setAuthToken called with token: " + (token != null ? "present" : "null"));
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
        System.out.println("setUserDetails called. userDetails: " + (userDetails != null ? userDetails.getUsername() : "null"));
        updateWelcomeMessage();
    }

    private void updateWelcomeMessage() {
        if (userDetails != null) {
            usernameLabel.setText("Welcome, " + userDetails.getRole() + " " + userDetails.getUsername() + "!");
            System.out.println("Welcome message updated: Welcome, " + userDetails.getRole() + " " + userDetails.getUsername() + "!");
        } else {
            dashboardLabel.setText("Welcome to Your Dashboard!");
            System.out.println("Welcome message set to fallback: Welcome to Your Dashboard!");
        }
    }

    @FXML
    public void initialize() {
        System.out.println("Dashboard initialize called. authToken: " + (authToken != null ? "present" : "null") + ", userDetails: " + (userDetails != null ? userDetails.getUsername() : "null"));
        updateWelcomeMessage();
        fetchCounts();
        initializeCharts();
    }

    private void fetchCounts() {
        try {
            String patientUrl = "http://localhost:8083/pateints";
            String docUrl = "http://localhost:8082/doctors";
            String recordurl="http://localhost:8081/medical-records";
            int patientCount = fetchData(patientUrl + "/count");
            int doctorCount = fetchData(docUrl + "/doccount");
            int recordsCount = fetchData(recordurl + "/count"); // Add endpoint for records

            // Update labels
            patientCountLabel.setText(String.valueOf(patientCount));
            doctorCountLabel.setText(String.valueOf(doctorCount));
            recordsCountLabel.setText(String.valueOf(recordsCount));

            // Update progress bars
            patientProgress.setWidth(Math.min((patientCount / (double) MAX_COUNT) * MAX_PROGRESS_WIDTH, MAX_PROGRESS_WIDTH));
            doctorProgress.setWidth(Math.min((doctorCount / (double) MAX_COUNT) * MAX_PROGRESS_WIDTH, MAX_PROGRESS_WIDTH));
            recordsProgress.setWidth(Math.min((recordsCount / (double) MAX_COUNT) * MAX_PROGRESS_WIDTH, MAX_PROGRESS_WIDTH));

            // Update Pie Chart
            dataDistributionChart.getData().clear();
            dataDistributionChart.getData().addAll(
                    new PieChart.Data("Patients", patientCount),
                    new PieChart.Data("Doctors", doctorCount)
//                    new PieChart.Data("Records", recordsCount)
            );
        } catch (Exception e) {
            e.printStackTrace();
            patientCountLabel.setText("N/A");
            doctorCountLabel.setText("N/A");
            recordsCountLabel.setText("N/A");
            showErrorAlert("Failed to fetch counts: " + e.getMessage());
        }
    }

    private void initializeCharts() {
        // Bar Chart: Department Statistics (Mock Data)
        XYChart.Series<String, Number> patientSeries = new XYChart.Series<>();
        patientSeries.setName("Patients");
        patientSeries.getData().addAll(
                new XYChart.Data<>("Cardiology", 50),
                new XYChart.Data<>("Neurology", 30),
                new XYChart.Data<>("Pediatrics", 20)
        );

        XYChart.Series<String, Number> doctorSeries = new XYChart.Series<>();
        doctorSeries.setName("Doctors");
        doctorSeries.getData().addAll(
                new XYChart.Data<>("Cardiology", 10),
                new XYChart.Data<>("Neurology", 15),
                new XYChart.Data<>("Pediatrics", 5)
        );

        departmentChart.getData().addAll(patientSeries, doctorSeries);

        // Line Chart: Records Trend (Mock Data)
        XYChart.Series<String, Number> recordsSeries = new XYChart.Series<>();
        recordsSeries.setName("Records");
        recordsSeries.getData().addAll(
                new XYChart.Data<>("Jan", 100),
                new XYChart.Data<>("Feb", 150),
                new XYChart.Data<>("Mar", 200),
                new XYChart.Data<>("Apr", 180),
                new XYChart.Data<>("May", 250)
        );

        recordsTrendChart.getData().add(recordsSeries);
    }

    private int fetchData(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Authorization", "Bearer " + authToken)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return Integer.parseInt(response.body().trim());
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
            controller.setAuthToken(authToken);
            if (userDetails != null) {
                controller.setUserDetails(userDetails);
            }
            controller.setHostServices(hostServices);

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