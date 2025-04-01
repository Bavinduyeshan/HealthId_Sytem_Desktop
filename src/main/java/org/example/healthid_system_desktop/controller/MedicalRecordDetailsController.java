package org.example.healthid_system_desktop.controller;

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
    @FXML private Button editButton;
    @FXML private Button saveButton;
    @FXML private Button deleteButton;
    @FXML private Button fancyButton;
    @FXML private Label usernameLabel;

    private Stage stage;
    private MedicalRecord medicalRecord;
    private Integer loggedInDoctorId; // To store the logged-in doctor's ID
    private Runnable onDeleteCallback; // Callback to notify parent controller of deletion
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String UPDATE_MEDICAL_RECORD_URL = "http://localhost:8081/medical-records/update/";
    private static final String DELETE_MEDICAL_RECORD_URL = "http://localhost:8081/medical-records/delete/";

    public void setMedicalRecord(MedicalRecord record) {
        this.medicalRecord = record;
        patientIdLabel.setText(String.valueOf(record.getPatientID()));
        doctorIdLabel.setText(String.valueOf(record.getDoctor_Id()));
        diseaseLabel.setText(record.getName() != null ? record.getName() : "N/A"); // Adjusted for String (since you changed to name in previous steps)
        diagnosticDataTextArea.setText(record.getDiagnosticData());
        treatmentsTextArea.setText(record.getTreatments());
        createdAtLabel.setText(record.getCreatedAt() != null ? record.getCreatedAt().toString() : "N/A");

        // Enable Edit/Delete buttons only if the logged-in doctor matches the record's doctor
//        if (loggedInDoctorId == null || !loggedInDoctorId.equals(record.getDoctor_Id())) {
//            editButton.setDisable(true);
//            deleteButton.setDisable(true);
//        }
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
    private void toggleEdit() {
        boolean isEditable = !diagnosticDataTextArea.isEditable();
        diagnosticDataTextArea.setEditable(isEditable);
        treatmentsTextArea.setEditable(isEditable);
        editButton.setVisible(!isEditable);
        saveButton.setVisible(isEditable);
    }

    @FXML
    private void saveChanges() {
        if (loggedInDoctorId == null || !loggedInDoctorId.equals(medicalRecord.getDoctor_Id())) {
            showAlert("Error", "You are not authorized to update this record.");
            return;
        }

        // Update the record with new values
        medicalRecord.setDiagnosticData(diagnosticDataTextArea.getText());
        medicalRecord.setTreatments(treatmentsTextArea.getText());
        medicalRecord.setUpdatedAt(LocalDateTime.now());

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<MedicalRecord> request = new HttpEntity<>(medicalRecord, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    UPDATE_MEDICAL_RECORD_URL + medicalRecord.getId(), // Assuming MedicalRecord has an ID field
                    HttpMethod.PUT,
                    request,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                showAlert("Success", "Medical record updated successfully!");
                toggleEdit(); // Switch back to view mode
            } else {
                showAlert("Error", "Failed to update medical record: HTTP " + response.getStatusCodeValue());
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to update medical record: " + e.getMessage());
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
                            DELETE_MEDICAL_RECORD_URL + medicalRecord.getId(), // Assuming MedicalRecord has an ID field
                            HttpMethod.DELETE,
                            null,
                            String.class
                    );

                    if (responseEntity.getStatusCode().is2xxSuccessful()) {
                        showAlert("Success", "Medical record deleted successfully!");
                        if (onDeleteCallback != null) {
                            onDeleteCallback.run(); // Notify parent to refresh
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

    @FXML
    private void closeWindow() {
        stage.close();
    }

    @FXML
    public void handelHover(javafx.scene.input.MouseEvent mouseEvent) {
        fancyButton.setStyle("-fx-background-color: #1976D2; -fx-translate-x: 5px; -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
    }

    @FXML
    public void handleHoverExit(javafx.scene.input.MouseEvent mouseEvent) {
        fancyButton.setStyle("-fx-background-color: #2196F3; -fx-translate-x: 0; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleEditHover(MouseEvent mouseEvent) {
    }

    public void handleSaveHover(MouseEvent mouseEvent) {
    }

    public void handleDeleteHover(MouseEvent mouseEvent) {
    }

    public void handleEditHoverExit(MouseEvent mouseEvent) {
    }

    public void handleSaveHoverExit(MouseEvent mouseEvent) {
    }

    public void handleDeleteHoverExit(MouseEvent mouseEvent) {
    }
}