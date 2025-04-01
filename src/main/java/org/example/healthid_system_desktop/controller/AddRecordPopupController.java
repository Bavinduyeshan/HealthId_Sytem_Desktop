package org.example.healthid_system_desktop.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.healthid_system_desktop.model.Disease;
import org.example.healthid_system_desktop.model.MedicalRecord;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class AddRecordPopupController implements Initializable {

    @FXML private TextField patientIdField;
    @FXML private TextField doctorIdField;
    @FXML private ComboBox<Disease> diseaseComboBox; // Changed to ComboBox
    @FXML private TextArea diagnosticDataArea;
    @FXML private TextArea treatmentsArea;
    @FXML private TextField reportUrlField;

    private patientController parentController;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String DISEASES_URL = "http://localhost:8081/diseases/getAll"; // Adjust to your endpoint

    private static final String ADD_MEDICAL_RECORD_URL = "http://localhost:8081/medical-records/add";
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fetchDiseases();
    }

    public void setPatientId(String patientId) {
        patientIdField.setText(patientId);
    }

    public void setDoctorId(String doctorId) {
        doctorIdField.setText(doctorId);
        doctorIdField.setEditable(false); // Optional: Make it non-editable
    }

    public void setParentController(patientController controller) {
        this.parentController = controller;
    }

    private void fetchDiseases() {
        try {
            ResponseEntity<List<Disease>> response = restTemplate.exchange(
                    DISEASES_URL, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Disease>>() {}
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Disease> diseases = response.getBody();
                diseaseComboBox.setItems(FXCollections.observableArrayList(diseases)); // Populate ComboBox
            } else {
                showAlert("Error", "Failed to fetch diseases: No data received.");
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to fetch diseases: " + e.getMessage());
        }
    }


    @FXML
    private void save() {
        if (diseaseComboBox.getSelectionModel().getSelectedItem() == null) {
            showAlert("Error", "Please select a disease.");
            return;
        }

        MedicalRecord newRecord = new MedicalRecord();
        try {
            newRecord.setPatientID(Integer.parseInt(patientIdField.getText()));
            newRecord.setDoctor_Id(Integer.parseInt(doctorIdField.getText()));
        } catch (NumberFormatException e) {
            showAlert("Error", "Patient ID and Doctor ID must be valid integers.");
            return;
        }
        newRecord.setName(diseaseComboBox.getSelectionModel().getSelectedItem().getName());
        newRecord.setDiagnosticData(diagnosticDataArea.getText());
        newRecord.setTreatments(treatmentsArea.getText());
        newRecord.setReportUrl(reportUrlField.getText());
        newRecord.setCreatedAt(LocalDateTime.now());
        newRecord.setUpdatedAt(LocalDateTime.now());

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<MedicalRecord> request = new HttpEntity<>(newRecord, headers);
            // Change to expect a String response instead of MedicalRecord
            ResponseEntity<String> response = restTemplate.postForEntity(
                    ADD_MEDICAL_RECORD_URL, request, String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                // Since the backend doesn't return a MedicalRecord, add the local newRecord to the list
                parentController.addMedicalRecordToList(newRecord);
                showAlert("Success", "Medical record added successfully!");
                cancel();
            } else {
                showAlert("Error", "Failed to add medical record: HTTP " + response.getStatusCodeValue());
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to add medical record: " + e.getMessage());
        }
    }

    @FXML
    private void cancel() {
        Stage stage = (Stage) patientIdField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}