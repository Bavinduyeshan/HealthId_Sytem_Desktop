//package org.example.healthid_system_desktop.controller;
//
//import javafx.collections.FXCollections;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Alert;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.TextArea;
//import javafx.scene.control.TextField;
//import javafx.stage.Stage;
//import org.example.healthid_system_desktop.model.Disease;
//import org.example.healthid_system_desktop.model.MedicalRecord;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.*;
//import org.springframework.web.client.RestTemplate;
//
//import java.net.URL;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.ResourceBundle;
//
//public class AddRecordPopupController implements Initializable {
//
//    @FXML private TextField patientIdField;
//    @FXML private TextField doctorIdField;
//    @FXML private ComboBox<Disease> diseaseComboBox; // Changed to ComboBox
//    @FXML private TextArea diagnosticDataArea;
//    @FXML private TextArea treatmentsArea;
//    @FXML private TextField reportUrlField;
//
//    private patientController parentController;
//    private final RestTemplate restTemplate = new RestTemplate();
//    private static final String DISEASES_URL = "http://localhost:8081/diseases/getAll"; // Adjust to your endpoint
//
//    private static final String ADD_MEDICAL_RECORD_URL = "http://localhost:8081/medical-records/add";
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        fetchDiseases();
//    }
//
//    public void setPatientId(String patientId) {
//        patientIdField.setText(patientId);
//    }
//
//    public void setDoctorId(String doctorId) {
//        doctorIdField.setText(doctorId);
//        doctorIdField.setEditable(false); // Optional: Make it non-editable
//    }
//
//    public void setParentController(patientController controller) {
//        this.parentController = controller;
//    }
//
//    private void fetchDiseases() {
//        try {
//            ResponseEntity<List<Disease>> response = restTemplate.exchange(
//                    DISEASES_URL, HttpMethod.GET, null,
//                    new ParameterizedTypeReference<List<Disease>>() {}
//            );
//
//            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
//                List<Disease> diseases = response.getBody();
//                diseaseComboBox.setItems(FXCollections.observableArrayList(diseases)); // Populate ComboBox
//            } else {
//                showAlert("Error", "Failed to fetch diseases: No data received.");
//            }
//        } catch (Exception e) {
//            showAlert("Error", "Failed to fetch diseases: " + e.getMessage());
//        }
//    }
//
//
//    @FXML
//    private void save() {
//        if (diseaseComboBox.getSelectionModel().getSelectedItem() == null) {
//            showAlert("Error", "Please select a disease.");
//            return;
//        }
//
//        MedicalRecord newRecord = new MedicalRecord();
//        try {
//            newRecord.setPatientID(Integer.parseInt(patientIdField.getText()));
//            newRecord.setDoctor_Id(Integer.parseInt(doctorIdField.getText()));
//        } catch (NumberFormatException e) {
//            showAlert("Error", "Patient ID and Doctor ID must be valid integers.");
//            return;
//        }
//        newRecord.setName(diseaseComboBox.getSelectionModel().getSelectedItem().getName());
//        newRecord.setDiagnosticData(diagnosticDataArea.getText());
//        newRecord.setTreatments(treatmentsArea.getText());
//        newRecord.setReportUrl(reportUrlField.getText());
//        newRecord.setCreatedAt(LocalDateTime.now());
//        newRecord.setUpdatedAt(LocalDateTime.now());
//
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
//            HttpEntity<MedicalRecord> request = new HttpEntity<>(newRecord, headers);
//            // Change to expect a String response instead of MedicalRecord
//            ResponseEntity<String> response = restTemplate.postForEntity(
//                    ADD_MEDICAL_RECORD_URL, request, String.class
//            );
//
//            if (response.getStatusCode().is2xxSuccessful()) {
//                // Since the backend doesn't return a MedicalRecord, add the local newRecord to the list
//                parentController.addMedicalRecordToList(newRecord);
//                showAlert("Success", "Medical record added successfully!");
//                cancel();
//            } else {
//                showAlert("Error", "Failed to add medical record: HTTP " + response.getStatusCodeValue());
//            }
//        } catch (Exception e) {
//            showAlert("Error", "Failed to add medical record: " + e.getMessage());
//        }
//    }
//
//    @FXML
//    private void cancel() {
//        Stage stage = (Stage) patientIdField.getScene().getWindow();
//        stage.close();
//    }
//
//    private void showAlert(String title, String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//}



 package org.example.healthid_system_desktop.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.healthid_system_desktop.model.Disease;
import org.example.healthid_system_desktop.model.MedicalRecord;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class AddRecordPopupController implements Initializable {

    @FXML private TextField patientIdField;
    @FXML private TextField doctorIdField;
    @FXML private ComboBox<Disease> diseaseComboBox;
    @FXML private TextArea diagnosticDataArea;
    @FXML private TextArea treatmentsArea;
    @FXML private Button uploadReportButton; // Replace TextField with Button
    @FXML private Label selectedFileLabel;   // To show the selected file name

    private patientController parentController;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String DISEASES_URL = "http://localhost:8081/diseases/getAll";
    private static final String ADD_MEDICAL_RECORD_URL = "http://localhost:8081/medical-records/add";
    private String authToken;
    private File selectedReportFile; // Store the selected file

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fetchDiseases();
        selectedFileLabel.setText("No file selected");
    }

    public void setPatientId(String patientId) {
        patientIdField.setText(patientId);
        patientIdField.setEditable(false);
    }

    public void setDoctorId(String doctorId) {
        doctorIdField.setText(doctorId);
        doctorIdField.setEditable(false);
    }

    public void setParentController(patientController controller) {
        this.parentController = controller;
    }

    public void setAuthToken(String token) {
        this.authToken = token;
    }

    private void fetchDiseases() {
        try {
            HttpHeaders headers = new HttpHeaders();
            if (authToken != null) {
                headers.set("Authorization", "Bearer " + authToken);
            }
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<List<Disease>> response = restTemplate.exchange(
                    DISEASES_URL,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Disease>>() {}
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Disease> diseases = response.getBody();
                diseaseComboBox.setItems(FXCollections.observableArrayList(diseases));
                diseaseComboBox.setCellFactory(param -> new ListCell<Disease>() {
                    @Override
                    protected void updateItem(Disease item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty || item == null ? null : item.getName());
                    }
                });
                diseaseComboBox.setButtonCell(diseaseComboBox.getCellFactory().call(null));
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to fetch diseases: HTTP " + response.getStatusCodeValue());
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to fetch diseases: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void uploadReport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Report File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        Stage stage = (Stage) uploadReportButton.getScene().getWindow();
        selectedReportFile = fileChooser.showOpenDialog(stage);

        if (selectedReportFile != null) {
            selectedFileLabel.setText(selectedReportFile.getName());
        } else {
            selectedFileLabel.setText("No file selected");
        }
    }

//    @FXML
//    private void save() {
//        if (diseaseComboBox.getSelectionModel().getSelectedItem() == null) {
//            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a disease.");
//            return;
//        }
//        if (diagnosticDataArea.getText().isEmpty()) {
//            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please provide diagnostic data.");
//            return;
//        }
//        if (treatmentsArea.getText().isEmpty()) {
//            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please provide treatment details.");
//            return;
//        }
//
//        MedicalRecord newRecord = new MedicalRecord(); // Use Medical_Records to match backend
//        try {
//            newRecord.setPatientID(Integer.parseInt(patientIdField.getText()));
//            newRecord.setDoctor_Id(Integer.parseInt(doctorIdField.getText()));
//        } catch (NumberFormatException e) {
//            showAlert(Alert.AlertType.ERROR, "Validation Error", "Patient ID and Doctor ID must be valid numbers.");
//            return;
//        }
//
//        Disease selectedDisease = diseaseComboBox.getSelectionModel().getSelectedItem();
//        newRecord.setName(selectedDisease.getName());
//        newRecord.setDisease(selectedDisease);
//        newRecord.setDiagnosticData(diagnosticDataArea.getText());
//        newRecord.setTreatments(treatmentsArea.getText());
//        newRecord.setCreatedAt(LocalDateTime.parse(LocalDateTime.now().toString()));
//        newRecord.setUpdatedAt(LocalDateTime.parse(LocalDateTime.now().toString()));
//
//        HttpEntity<org.springframework.util.MultiValueMap<String, Object>> request = null;
//
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//            if (authToken != null) {
//                headers.set("Authorization", "Bearer " + authToken);
//            }
//
//            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//            body.add("medicalRecord", newRecord);
//            if (selectedReportFile != null) {
//                System.out.println("Selected file: " + selectedReportFile.getAbsolutePath() + ", Size: " + selectedReportFile.length() + " bytes");
//                FileSystemResource fileResource = new FileSystemResource(selectedReportFile);
//                body.add("report", fileResource); // Change "reportFile" to "report"
//                System.out.println("Sending file: " + fileResource.getPath() + ", Size: " + fileResource.contentLength() + " bytes");
//            } else {
//                System.out.println("No file selected for upload");
//            }
//
//            request = new HttpEntity<>(body, headers);
//
//            System.out.println("Sending request to: " + ADD_MEDICAL_RECORD_URL);
//            ResponseEntity<MedicalRecord> response = restTemplate.exchange(
//                    ADD_MEDICAL_RECORD_URL,
//                    HttpMethod.POST,
//                    request,
//                    MedicalRecord.class
//            );
//
//            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
//                MedicalRecord savedRecord = response.getBody();
//                parentController.addMedicalRecordToList(savedRecord);
//                showAlert(Alert.AlertType.INFORMATION, "Success", "Medical record and report added successfully!");
//                cancel();
//            } else {
//                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add medical record: HTTP " + response.getStatusCodeValue());
//            }
//        } catch (Exception e) {
//            showAlert(Alert.AlertType.ERROR, "Exception", "Failed to add medical record: " + e.getMessage());
//            e.printStackTrace();
//            if (request != null) {
//                try {
//                    ResponseEntity<String> debugResponse = restTemplate.exchange(
//                            ADD_MEDICAL_RECORD_URL,
//                            HttpMethod.POST,
//                            request,
//                            String.class
//                    );
//                    System.out.println("Raw server response: " + debugResponse.getBody());
//                } catch (Exception debugEx) {
//                    System.err.println("Debug fetch failed: " + debugEx.getMessage());
//                    debugEx.printStackTrace();
//                }
//            } else {
//                System.err.println("Debug skipped: Request object is null");
//            }
//        }
//    }

    @FXML
    private void save() {
        if (diseaseComboBox.getSelectionModel().getSelectedItem() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a disease.");
            return;
        }
        if (diagnosticDataArea.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please provide diagnostic data.");
            return;
        }
        if (treatmentsArea.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please provide treatment details.");
            return;
        }

        MedicalRecord newRecord = new MedicalRecord();
        try {
            newRecord.setPatientID(Integer.parseInt(patientIdField.getText()));
            newRecord.setDoctor_Id(Integer.parseInt(doctorIdField.getText()));
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Patient ID and Doctor ID must be valid numbers.");
            return;
        }

        Disease selectedDisease = diseaseComboBox.getSelectionModel().getSelectedItem();
        newRecord.setName(selectedDisease.getName());
        newRecord.setDisease(selectedDisease);
        newRecord.setDiagnosticData(diagnosticDataArea.getText());
        newRecord.setTreatments(treatmentsArea.getText());
        newRecord.setCreatedAt(LocalDateTime.now()); // Simplified parsing
        newRecord.setUpdatedAt(LocalDateTime.now());

        HttpEntity<MultiValueMap<String, HttpEntity<?>>> request = null;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            if (authToken != null) {
                headers.set("Authorization", "Bearer " + authToken);
            }

            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            // Use "record" to match backend @RequestPart("record")
            builder.part("record", newRecord, MediaType.APPLICATION_JSON);
            if (selectedReportFile != null) {
                // Use "report" to match backend @RequestPart("report")
                builder.part("report", new FileSystemResource(selectedReportFile))
                        .header("Content-Disposition", "form-data; name=report; filename=" + selectedReportFile.getName());
            }

            request = new HttpEntity<>(builder.build(), headers);

            System.out.println("Sending request to: " + ADD_MEDICAL_RECORD_URL);
            ResponseEntity<MedicalRecord> response = restTemplate.exchange(
                    ADD_MEDICAL_RECORD_URL,
                    HttpMethod.POST,
                    request,
                    MedicalRecord.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                MedicalRecord savedRecord = response.getBody();
                parentController.addMedicalRecordToList(savedRecord);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Medical record and report added successfully!");
                cancel();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add medical record: HTTP " + response.getStatusCodeValue());
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Exception", "Failed to add medical record: " + e.getMessage());
            e.printStackTrace();
            if (request != null) {
                try {
                    ResponseEntity<String> debugResponse = restTemplate.exchange(
                            ADD_MEDICAL_RECORD_URL,
                            HttpMethod.POST,
                            request,
                            String.class
                    );
                    System.out.println("Raw server response: " + debugResponse.getBody());
                } catch (Exception debugEx) {
                    System.err.println("Debug fetch failed: " + debugEx.getMessage());
                    debugEx.printStackTrace();
                }
            }
        }
    }

    private HttpHeaders getJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
    @FXML
    private void cancel() {
        Stage stage = (Stage) patientIdField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}