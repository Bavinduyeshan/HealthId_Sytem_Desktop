package org.example.healthid_system_desktop.controller;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.healthid_system_desktop.model.MedicalRecord;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

public class MedicalRecordDetailsController {

    @FXML private Label patientIdLabel;
    @FXML private Label diseaseLabel;
    @FXML private Label doctorIdLabel;
    @FXML private TextArea diagnosticDataTextArea;
    @FXML private TextArea treatmentsTextArea;
    @FXML private Label createdAtLabel;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button closeButton;
    @FXML private Label usernameLabel;

    private HostServices hostServices;

    @FXML private Label reportUrlLabel; // Added to display report URL
    @FXML private Button viewReportButton; // Added to view report

    private Stage stage;
    private MedicalRecord medicalRecord;
    private Integer loggedInDoctorId;
    private Runnable onDeleteCallback;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String UPDATE_MEDICAL_RECORD_URL = "http://localhost:8081/medical-records/update/";
    private static final String DELETE_MEDICAL_RECORD_URL = "http://localhost:8081/medical-records/";


    // Setter for HostServices (injected by parent controller)
    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public void setMedicalRecord(MedicalRecord record) {
        this.medicalRecord = record;
        patientIdLabel.setText(String.valueOf(record.getPatientID()));
        doctorIdLabel.setText(String.valueOf(record.getDoctor_Id()));
        diseaseLabel.setText(record.getDisease().getName() != null ? record.getDisease().getName() : "N/A");
        diagnosticDataTextArea.setText(record.getDiagnosticData());
        treatmentsTextArea.setText(record.getTreatments());
        createdAtLabel.setText(record.getCreatedAt() != null ? record.getCreatedAt().toString() : "N/A");
        reportUrlLabel.setText(record.getReportUrl() != null ? record.getReportUrl() : "No report available");
        viewReportButton.setDisable(record.getReportUrl() == null); // Disable button if no report
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setLoggedInDoctorId(Integer doctorId) {
        this.loggedInDoctorId = doctorId;
    }

    public void setOnDeleteCallback(Runnable callback) {
        this.onDeleteCallback = callback;
    }

    public void setUsername(String username) {
        usernameLabel.setText(username != null && !username.trim().isEmpty() ? "Welcome, " + username + "!" : "Welcome, User!");
    }

    @FXML
    private void updateRecord() {
        if (loggedInDoctorId == null || !loggedInDoctorId.equals(medicalRecord.getDoctor_Id())) {
            showAlert("Error", "You are not authorized to update this record.");
            return;
        }

        boolean isEditable = !diagnosticDataTextArea.isEditable();
        diagnosticDataTextArea.setEditable(isEditable);
        treatmentsTextArea.setEditable(isEditable);

        if (!isEditable) {
            // Save changes when toggling back from edit mode
            medicalRecord.setDiagnosticData(diagnosticDataTextArea.getText());
            medicalRecord.setTreatments(treatmentsTextArea.getText());
            medicalRecord.setUpdatedAt(LocalDateTime.now());

            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<MedicalRecord> request = new HttpEntity<>(medicalRecord, headers);

                ResponseEntity<String> response = restTemplate.exchange(
                        UPDATE_MEDICAL_RECORD_URL + medicalRecord.getId(),
                        HttpMethod.PUT,
                        request,
                        String.class
                );

                if (response.getStatusCode().is2xxSuccessful()) {
                    showAlert("Success", "Medical record updated successfully!");
                } else {
                    showAlert("Error", "Failed to update medical record: HTTP " + response.getStatusCodeValue());
                }
            } catch (Exception e) {
                showAlert("Error", "Failed to update medical record: " + e.getMessage());
            }
        }
    }

    @FXML
    private void deleteRecord() {
        if (loggedInDoctorId == null || !loggedInDoctorId.equals(medicalRecord.getDoctor_Id())) {
            showAlert("Error", "You are not authorized to delete this record.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this record?");
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    ResponseEntity<String> responseEntity = restTemplate.exchange(
                            DELETE_MEDICAL_RECORD_URL + medicalRecord.getId(),
                            HttpMethod.DELETE,
                            null,
                            String.class
                    );

                    if (responseEntity.getStatusCode().is2xxSuccessful()) {
                        showAlert("Success", "Medical record deleted successfully!");
                        if (onDeleteCallback != null) {
                            onDeleteCallback.run();
                        }
                        stage.close();
                    } else {
                        showAlert("Error", "Failed to delete medical record: HTTP " + responseEntity.getStatusCodeValue());
                    }
                } catch (Exception e) {
                    showAlert("Error", "Failed to delete medical record: " + e.getMessage());
                }
            }
        });
    }


//    @FXML
//    private void viewReport() {
//        System.out.println("HostServices: " + hostServices);
//        System.out.println("Report URL: " + medicalRecord.getReportUrl());
//        if (medicalRecord.getReportUrl() != null && hostServices != null) {
//            hostServices.showDocument(medicalRecord.getReportUrl());
//        } else {
//            showAlert("Error", "No report available or unable to open it.");
//        }
//    }
@FXML
private void viewReport() {
    System.out.println("HostServices: " + hostServices);
    System.out.println("Report URL: " + medicalRecord.getReportUrl());
    if (medicalRecord != null && medicalRecord.getReportUrl() != null && hostServices != null) {
        String fullUrl = "http://localhost:8081" + medicalRecord.getReportUrl(); // Adjust base URL as needed
        System.out.println("Opening full URL: " + fullUrl);
        hostServices.showDocument(fullUrl);
    } else {
        showAlert("Error", "No report available or unable to open it.");
    }
}

    @FXML
    private void closeWindow() {
        stage.close();
    }

    @FXML
    public void handleHover(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #16A085; -fx-text-fill: #FFFFFF; -fx-font-size: 14; -fx-background-radius: 8; -fx-padding: 10; -fx-translate-x: 5;");
    }

    @FXML
    public void handleHoverExit(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #1ABC9C; -fx-text-fill: #FFFFFF; -fx-font-size: 14; -fx-background-radius: 8; -fx-padding: 10; -fx-translate-x: 0;");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}