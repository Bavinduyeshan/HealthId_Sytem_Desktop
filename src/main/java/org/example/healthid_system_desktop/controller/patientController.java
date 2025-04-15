////package org.example.healthid_system_desktop.controller;
////
////import javafx.animation.TranslateTransition;
////import javafx.application.Platform;
////import javafx.fxml.FXML;
////import javafx.fxml.FXMLLoader;
////import javafx.scene.Parent;
////import javafx.scene.Scene;
////import javafx.scene.control.*;
////import javafx.scene.input.MouseEvent;
////import javafx.scene.layout.AnchorPane;
////import javafx.scene.layout.VBox;
////import javafx.stage.Modality;
////import javafx.stage.Stage;
////import javafx.util.Duration;
////import org.example.healthid_system_desktop.model.MedicalRecord;
////import org.example.healthid_system_desktop.model.Patient;
////import org.example.healthid_system_desktop.service.UserDetails;
////import org.springframework.core.ParameterizedTypeReference;
////import org.springframework.http.HttpEntity;
////import org.springframework.http.HttpHeaders;
////import org.springframework.http.HttpMethod;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.client.RestTemplate;
////
////import java.io.BufferedReader;
////import java.io.IOException;
////import java.io.InputStreamReader;
////import java.time.LocalDateTime;
////import java.time.format.DateTimeFormatter;
////import java.util.List;
////
////public class patientController {
////
////    @FXML public Button addRecordButton; // Now properly annotated with @FXML
////    @FXML private Button scanButton;
////    @FXML private TextField patientIdField;
////    @FXML private VBox menuBar;
////    @FXML private Button toggleMenuButton;
////    @FXML private AnchorPane centerPane;
////    @FXML private Label usernameLabel;
////    @FXML private Label userIdLabel;
////    @FXML private Label firstnameLabel;
////    @FXML private Label lastnameLabel;
////    @FXML private Label dobLabel;
////    @FXML private Label genderLabel;
////    @FXML private Label emailLabel;
////    @FXML private Label phoneLabel;
////    @FXML private Label addressLabel;
////    @FXML private Label createdDateLabel;
////    @FXML private Label lastModifiedDateLabel;
////    @FXML private Label lastModifiedByLabel;
////    @FXML private ListView<MedicalRecord> recordsListView;
////    @FXML private VBox recordFormContainer;
////    @FXML private DatePicker recordDate;
////    @FXML private TextField diagnosisField;
////    @FXML private TextArea treatmentArea;
////    @FXML private TextArea notesArea;
////
////    private static final double MENU_WIDTH = 189.0;
////    private static final double FULL_WIDTH = 1280.0;
////    private static final double CENTER_WIDTH = FULL_WIDTH - MENU_WIDTH;
////
////    private String authToken;
////    private UserDetails userDetails;
////    private final RestTemplate restTemplate = new RestTemplate();
////    private static final String USER_INFO_URL = "http://localhost:8080/users/api/user/me";
////    private static final String BACKEND_URL = "http://localhost:8083/pateints/get/";
////    private static final String MEDICAL_RECORDS_URL = "http://localhost:8081/medical-records/medical-records/";
////    private static final String PYTHON_SCRIPT_PATH = "src/qr_scanner.py";
////
////    @FXML
////    public void initialize() {
////        scanButton.setOnAction(event -> scanQrCode());
////        recordsListView.setOnMouseClicked(this::handleRecordClick);
////        recordFormContainer.setVisible(false); // Ensure inline form is hidden initially
////
////        // Set action for addRecordButton to open the popup
////        addRecordButton.setOnAction(event -> addRecord());
////
////        // Add hover effects to sidebar buttons
////        menuBar.getChildren().forEach(node -> {
////            if (node instanceof Button) {
////                Button btn = (Button) node;
////                btn.setOnMouseEntered(this::handleMouseEntered);
////                btn.setOnMouseExited(this::handleMouseExited);
////                if ("Dashboard".equals(btn.getText())) {
////                    btn.setOnAction(event -> openDashboard());
////                }
////            }
////        });
////        scanButton.setOnMouseEntered(this::handleScanMouseEntered);
////        scanButton.setOnMouseExited(this::handleScanMouseExited);
////    }
////
////    public void setAuthToken(String token) {
////        this.authToken = token;
////        displayUsername();
////    }
////
////    public void setUserDetails(UserDetails details) {
////        this.userDetails = details;
////    }
////
////    private void displayUsername() {
////        try {
////            HttpHeaders headers = new HttpHeaders();
////            headers.set("Authorization", "Bearer " + authToken);
////            HttpEntity<String> entity = new HttpEntity<>(headers);
////
////            ResponseEntity<String> response = restTemplate.exchange(
////                    USER_INFO_URL, HttpMethod.GET, entity, String.class
////            );
////
////            if (response.getStatusCode().is2xxSuccessful()) {
////                String username = response.getBody();
////                usernameLabel.setText(username != null && !username.trim().isEmpty() ? "Welcome, " + username + "!" : "Welcome, User!");
////            } else {
////                showAlert("Error", "Failed to fetch username: HTTP " + response.getStatusCodeValue());
////                usernameLabel.setText("Welcome, User!");
////            }
////        } catch (Exception e) {
////            showAlert("Error", "Failed to fetch username: " + e.getMessage());
////            usernameLabel.setText("Welcome, User!");
////        }
////    }
////
////    @FXML
////    private void toggleMenu() {
////        boolean isVisible = menuBar.isVisible();
////        TranslateTransition transition = new TranslateTransition(Duration.millis(300), centerPane);
////        transition.setOnFinished(event -> {
////            menuBar.setVisible(!isVisible);
////            menuBar.setManaged(!isVisible);
////        });
////
////        if (isVisible) {
////            transition.setToX(-MENU_WIDTH);
////            centerPane.setPrefWidth(FULL_WIDTH);
////        } else {
////            transition.setToX(0);
////            centerPane.setPrefWidth(CENTER_WIDTH);
////        }
////
////        transition.play();
////        toggleMenuButton.setText(isVisible ? "☰" : "✕");
////    }
////
////    @FXML
////    private void openDashboard() {
////        try {
////            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthid_system_desktop/Dashboard.fxml"));
////            if (loader.getLocation() == null) {
////                throw new IOException("Dashboard.fxml not found at /org/example/healthid_system_desktop/Dashboard.fxml");
////            }
////            Parent root = loader.load();
////
////            Dashboard controller = loader.getController();
////            controller.setAuthToken(authToken);
////            if (userDetails != null) {
////                controller.setUserDetails(userDetails);
////            }
////
////            Stage patientStage = (Stage) toggleMenuButton.getScene().getWindow();
////            Stage dashboardStage = new Stage();
////            dashboardStage.setTitle(userDetails != null ? userDetails.getRole() + " Dashboard" : "Dashboard");
////            dashboardStage.setScene(new Scene(root, 1400, 900));
////            dashboardStage.setResizable(true);
////            dashboardStage.show();
////
////            patientStage.close();
////        } catch (IOException e) {
////            e.printStackTrace();
////            showAlert("Error", "Failed to load Dashboard: " + e.getMessage());
////        }
////    }
////
////    @FXML
////    private void scanQrCode() {
////        new Thread(() -> {
////            try {
////                ProcessBuilder pb = new ProcessBuilder("python", PYTHON_SCRIPT_PATH);
////                Process process = pb.start();
////
////                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
////                String line = reader.readLine();
////
////                process.waitFor();
////
////                if (line != null && !line.startsWith("ERROR:")) {
////                    Integer patientId = Integer.valueOf(line.trim());
////                    Platform.runLater(() -> {
////                        patientIdField.setText(String.valueOf(patientId));
////                        fetchPatientData(patientId);
////                    });
////                } else {
////                    Platform.runLater(() -> showAlert("Error", line != null ? line : "No QR code detected."));
////                }
////            } catch (Exception e) {
////                Platform.runLater(() -> showAlert("Error", "Failed to scan QR code: " + e.getMessage()));
////            }
////        }).start();
////    }
////
////    private void fetchPatientData(Integer patientId) {
////        try {
////            Patient patient = restTemplate.getForObject(BACKEND_URL + patientId, Patient.class);
////            ResponseEntity<List<MedicalRecord>> response = restTemplate.exchange(
////                    MEDICAL_RECORDS_URL + patientId, HttpMethod.GET, null,
////                    new ParameterizedTypeReference<List<MedicalRecord>>() {}
////            );
////            List<MedicalRecord> medicalRecords = response.getBody();
////
////            if (patient != null) {
////                patient.setMedicalRecords(medicalRecords);
////                updatePatientUI(patient);
////            } else {
////                showAlert("Error", "Patient not found.");
////            }
////        } catch (Exception e) {
////            showAlert("Error", "Failed to fetch patient data: " + e.getMessage());
////        }
////    }
////
////    private void updatePatientUI(Patient patient) {
////        userIdLabel.setText(patient.getUserId() != null ? patient.getUserId().toString() : "N/A");
////        firstnameLabel.setText(patient.getFirstname() != null ? patient.getFirstname() : "N/A");
////        lastnameLabel.setText(patient.getLastname() != null ? patient.getLastname() : "N/A");
////        dobLabel.setText(patient.getDateOfBirth() != null ? patient.getDateOfBirth().format(DateTimeFormatter.ISO_DATE) : "N/A");
////        genderLabel.setText(patient.getGender() != null ? patient.getGender() : "N/A");
////        emailLabel.setText(patient.getEmail() != null ? patient.getEmail() : "N/A");
////        phoneLabel.setText(patient.getPhone() != null ? patient.getPhone() : "N/A");
////        addressLabel.setText(patient.getAddress() != null ? patient.getAddress() : "N/A");
////
////        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
////        createdDateLabel.setText(patient.getCreatedDate() != null ? patient.getCreatedDate().format(displayFormatter) : "N/A");
////        lastModifiedDateLabel.setText(patient.getLastModifiedDate() != null ? patient.getLastModifiedDate().format(displayFormatter) : "N/A");
////        safeSetText(lastModifiedByLabel, patient.getLastModifiedBy(), "Not modified");
////
////        recordsListView.getItems().clear();
////        if (patient.getMedicalRecords() != null && !patient.getMedicalRecords().isEmpty()) {
////            recordsListView.getItems().addAll(patient.getMedicalRecords());
////        } else {
////            recordsListView.getItems().add(new MedicalRecord());
////        }
////
////        recordsListView.setCellFactory(param -> new ListCell<MedicalRecord>() {
////            @Override
////            protected void updateItem(MedicalRecord record, boolean empty) {
////                super.updateItem(record, empty);
////                setText(empty || record == null ? null : formatMedicalRecord(record));
////            }
////        });
////    }
////
////    @FXML
////    private void addRecord() {
////        try {
////            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthid_system_desktop/AddRecordPopup.fxml"));
////            if (loader.getLocation() == null) {
////                throw new IOException("AddRecordPopup.fxml not found");
////            }
////            Parent root = loader.load();
////
////            AddRecordPopupController controller = loader.getController();
////            String patientId = patientIdField.getText();
////            if (patientId == null || patientId.trim().isEmpty()) {
////                showAlert("Error", "Patient ID is not set. Please scan a QR code first.");
////                return;
////            }
////            controller.setPatientId(patientId);
////            controller.setParentController(this);
////            if (userDetails != null && userDetails.getDoctorId() != null) {
////                controller.setDoctorId(userDetails.getDoctorId().toString());
////            } else {
////                showAlert("Error", "Doctor ID not available.");
////                return;
////            }
////
////            Stage popupStage = new Stage();
////            popupStage.initModality(Modality.APPLICATION_MODAL);
////            popupStage.setTitle("Add New Medical Record");
////            popupStage.setScene(new Scene(root, 600, 400));
////            popupStage.showAndWait();
////        } catch (IOException e) {
////            e.printStackTrace();
////            showAlert("Error", "Failed to load Add Record popup: " + e.getMessage());
////        }
////    }
////
////    @FXML
////    private void saveRecord() {
////        // This method is no longer used since popup handles saving
////        recordFormContainer.setVisible(false);
////        clearRecordForm();
////    }
////
////    @FXML
////    private void cancelRecord() {
////        recordFormContainer.setVisible(false);
////        clearRecordForm();
////    }
////
////    private void clearRecordForm() {
////        recordDate.setValue(null);
////        diagnosisField.clear();
////        treatmentArea.clear();
////        notesArea.clear();
////    }
////
////    private void handleRecordClick(MouseEvent mouseEvent) {
////        if (mouseEvent.getClickCount() == 2) {
////            MedicalRecord selectedRecord = recordsListView.getSelectionModel().getSelectedItem();
////            if (selectedRecord != null) {
////                openRecordDetails(selectedRecord);
////            } else {
////                showError("No record selected!");
////            }
////        }
////    }
////
////    private void openRecordDetails(MedicalRecord record) {
////        try {
////            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthid_system_desktop/recordDetails.fxml"));
////            Parent root = loader.load();
////            MedicalRecordDetailsController controller = loader.getController();
////            controller.setMedicalRecord(record);
////
////            Stage stage = new Stage();
////            stage.initModality(Modality.APPLICATION_MODAL);
////            stage.setTitle("Record Details");
////            stage.setScene(new Scene(root));
////            stage.show();
////        } catch (IOException e) {
////            e.printStackTrace();
////            showError("Error loading record details.");
////        }
////    }
////
////    private void showError(String message) {
////        Alert alert = new Alert(Alert.AlertType.ERROR);
////        alert.setContentText(message);
////        alert.show();
////    }
////
////    private void showAlert(String title, String message) {
////        Alert alert = new Alert(Alert.AlertType.ERROR);
////        alert.setTitle(title);
////        alert.setHeaderText(null);
////        alert.setContentText(message);
////        alert.getDialogPane().setPrefSize(400, 200);
////        alert.showAndWait();
////    }
////
////    private void safeSetText(Label label, Object value, String defaultValue) {
////        label.setText(value != null ? value.toString() : defaultValue);
////    }
////
////    private String formatMedicalRecord(MedicalRecord record) {
////        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
////        String createdAt = record.getCreatedAt() != null ? record.getCreatedAt().format(formatter) : "N/A";
////        String updatedAt = record.getUpdatedAt() != null ? record.getUpdatedAt().format(formatter) : "N/A";
////        String patientId = record.getPatientID() != null ? record.getPatientID().toString() : "N/A";
////        String disease = record.getDisease() != null ? record.getDisease().getName() : "N/A"; // Adjusted for String
////        String diagnosticData = record.getDiagnosticData() != null ? record.getDiagnosticData() : "N/A";
////        String treatments = record.getTreatments() != null ? record.getTreatments() : "N/A";
////        String reportUrl = record.getReportUrl() != null ? record.getReportUrl() : "N/A";
////        return String.format("Created: %s | Updated: %s | Patient ID: %s | Disease: %s | Diagnosis: %s | Treatments: %s | Report: %s",
////                createdAt, updatedAt, patientId, disease, diagnosticData, treatments, reportUrl);
////    }
////
////    public void handleMouseEntered(MouseEvent event) {
////        Button button = (Button) event.getSource();
////        button.setStyle("-fx-background-color: #546E7A; -fx-text-fill: #FFFFFF; -fx-font-size: 14; -fx-pref-width: 210; -fx-background-radius: 8; -fx-padding: 10;");
////    }
////
////    public void handleMouseExited(MouseEvent event) {
////        Button button = (Button) event.getSource();
////        button.setStyle("-fx-background-color: #455A64; -fx-text-fill: #FFFFFF; -fx-font-size: 14; -fx-pref-width: 210; -fx-background-radius: 8; -fx-padding: 10;");
////    }
////
////    public void handleScanMouseEntered(MouseEvent event) {
////        Button button = (Button) event.getSource();
////        button.setStyle("-fx-font-size: 14; -fx-background-color: #FFB300; -fx-text-fill: #FFFFFF; -fx-padding: 10 20; -fx-background-radius: 6; -fx-font-weight: bold;");
////    }
////
////    public void handleScanMouseExited(MouseEvent event) {
////        Button button = (Button) event.getSource();
////        button.setStyle("-fx-font-size: 14; -fx-background-color: #FFA726; -fx-text-fill: #FFFFFF; -fx-padding: 10 20; -fx-background-radius: 6; -fx-font-weight: bold;");
////    }
////
////    public void addMedicalRecordToList(MedicalRecord newRecord) {
////        recordsListView.getItems().add(newRecord);
////    }
////}
//
//
//package org.example.healthid_system_desktop.controller;
//
//import javafx.animation.TranslateTransition;
//import javafx.application.HostServices;
//import javafx.application.Platform;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.VBox;
//import javafx.stage.Modality;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//import org.example.healthid_system_desktop.model.MedicalRecord;
//import org.example.healthid_system_desktop.model.Patient;
//import org.example.healthid_system_desktop.service.UserDetails;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//public class patientController {
//
//    @FXML public Button addRecordButton;
//    @FXML private Button scanButton;
//    @FXML private TextField patientIdField;
//    @FXML private VBox menuBar;
//    @FXML private Button toggleMenuButton;
//    @FXML private AnchorPane centerPane;
//    @FXML private Label usernameLabel;
//    @FXML private Label userIdLabel;
//    @FXML private Label firstnameLabel;
//    @FXML private Label lastnameLabel;
//    @FXML private Label dobLabel;
//    @FXML private Label genderLabel;
//    @FXML private Label emailLabel;
//    @FXML private Label phoneLabel;
//    @FXML private Label addressLabel;
//    @FXML private Label createdDateLabel;
//    @FXML private Label lastModifiedDateLabel;
//    @FXML private Label lastModifiedByLabel;
//    @FXML private ListView<MedicalRecord> recordsListView;
//    @FXML private VBox recordFormContainer;
//    @FXML private DatePicker recordDate;
//    @FXML private TextField diagnosisField;
//    @FXML private TextArea treatmentArea;
//    @FXML private TextArea notesArea;
//
//    private HostServices hostServices;
//    private static final double MENU_WIDTH = 189.0;
//    private static final double FULL_WIDTH = 1280.0;
//    private static final double CENTER_WIDTH = FULL_WIDTH - MENU_WIDTH;
//
//    private String authToken;
//    private UserDetails userDetails;
//    private final RestTemplate restTemplate = new RestTemplate();
//    private static final String USER_INFO_URL = "http://localhost:8080/users/api/user/me";
//    private static final String BACKEND_URL = "http://localhost:8083/pateints/get/";
//    private static final String MEDICAL_RECORDS_URL = "http://localhost:8081/medical-records/medical-records/";
//    private static final String PYTHON_SCRIPT_PATH = "src/qr_scanner.py";
//
//    @FXML
//    public void initialize() {
//        scanButton.setOnAction(event -> scanQrCode());
//        recordsListView.setOnMouseClicked(this::handleRecordClick);
//        recordFormContainer.setVisible(false);
//
//        addRecordButton.setOnAction(event -> addRecord());
//
//        menuBar.getChildren().forEach(node -> {
//            if (node instanceof Button) {
//                Button btn = (Button) node;
//                btn.setOnMouseEntered(this::handleMouseEntered);
//                btn.setOnMouseExited(this::handleMouseExited);
//                if ("Dashboard".equals(btn.getText())) {
//                    btn.setOnAction(event -> openDashboard());
//                }
//            }
//        });
//        scanButton.setOnMouseEntered(this::handleScanMouseEntered);
//        scanButton.setOnMouseExited(this::handleScanMouseExited);
//    }
//
//    public void setAuthToken(String token) {
//        this.authToken = token;
//        displayUsername();
//    }
//
//    public void setUserDetails(UserDetails details) {
//        this.userDetails = details;
//    }
//
//    private void displayUsername() {
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("Authorization", "Bearer " + authToken);
//            HttpEntity<String> entity = new HttpEntity<>(headers);
//
//            ResponseEntity<String> response = restTemplate.exchange(
//                    USER_INFO_URL, HttpMethod.GET, entity, String.class
//            );
//
//            if (response.getStatusCode().is2xxSuccessful()) {
//                String username = response.getBody();
//                usernameLabel.setText(username != null && !username.trim().isEmpty() ? "Welcome,DOCTOR " + username + "!" : "Welcome, User!");
//            } else {
//                showAlert("Error", "Failed to fetch username: HTTP " + response.getStatusCodeValue());
//                usernameLabel.setText("Welcome, User!");
//            }
//        } catch (Exception e) {
//            showAlert("Error", "Failed to fetch username: " + e.getMessage());
//            usernameLabel.setText("Welcome, User!");
//        }
//    }
//
//    @FXML
//    private void toggleMenu() {
//        boolean isVisible = menuBar.isVisible();
//        TranslateTransition transition = new TranslateTransition(Duration.millis(300), centerPane);
//        transition.setOnFinished(event -> {
//            menuBar.setVisible(!isVisible);
//            menuBar.setManaged(!isVisible);
//        });
//
//        if (isVisible) {
//            transition.setToX(-MENU_WIDTH);
//            centerPane.setPrefWidth(FULL_WIDTH);
//        } else {
//            transition.setToX(0);
//            centerPane.setPrefWidth(CENTER_WIDTH);
//        }
//
//        transition.play();
//        toggleMenuButton.setText(isVisible ? "☰" : "✕");
//    }
//
//    @FXML
//    private void openDashboard() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthid_system_desktop/Dashboard.fxml"));
//            if (loader.getLocation() == null) {
//                throw new IOException("Dashboard.fxml not found at /org/example/healthid_system_desktop/Dashboard.fxml");
//            }
//            Parent root = loader.load();
//
//            Dashboard controller = loader.getController();
//            controller.setAuthToken(authToken);
//            if (userDetails != null) {
//                controller.setUserDetails(userDetails);
//            }
//
//            Stage patientStage = (Stage) toggleMenuButton.getScene().getWindow();
//            Stage dashboardStage = new Stage();
//            dashboardStage.setTitle(userDetails != null ? userDetails.getRole() + " Dashboard" : "Dashboard");
//            dashboardStage.setScene(new Scene(root, 1400, 900));
//            dashboardStage.setResizable(true);
//            dashboardStage.show();
//
//            patientStage.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            showAlert("Error", "Failed to load Dashboard: " + e.getMessage());
//        }
//    }
//
//    @FXML
//    private void scanQrCode() {
//        new Thread(() -> {
//            try {
//                ProcessBuilder pb = new ProcessBuilder("python", PYTHON_SCRIPT_PATH);
//                Process process = pb.start();
//
//                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//                String line = reader.readLine();
//
//                process.waitFor();
//
//                if (line != null && !line.startsWith("ERROR:")) {
//                    Integer patientId = Integer.valueOf(line.trim());
//                    Platform.runLater(() -> {
//                        patientIdField.setText(String.valueOf(patientId));
//                        fetchPatientData(patientId);
//                    });
//                } else {
//                    Platform.runLater(() -> showAlert("Error", line != null ? line : "No QR code detected."));
//                }
//            } catch (Exception e) {
//                Platform.runLater(() -> showAlert("Error", "Failed to scan QR code: " + e.getMessage()));
//            }
//        }).start();
//    }
//
//    private void fetchPatientData(Integer patientId) {
//        try {
//            Patient patient = restTemplate.getForObject(BACKEND_URL + patientId, Patient.class);
//            ResponseEntity<List<MedicalRecord>> response = restTemplate.exchange(
//                    MEDICAL_RECORDS_URL + patientId, HttpMethod.GET, null,
//                    new ParameterizedTypeReference<List<MedicalRecord>>() {}
//            );
//            List<MedicalRecord> medicalRecords = response.getBody();
//
//            if (patient != null) {
//                patient.setMedicalRecords(medicalRecords);
//                updatePatientUI(patient);
//            } else {
//                showAlert("Error", "Patient not found.");
//            }
//        } catch (Exception e) {
//            showAlert("Error", "Failed to fetch patient data: " + e.getMessage());
//        }
//    }
//
//    private void updatePatientUI(Patient patient) {
//        userIdLabel.setText(patient.getUserId() != null ? patient.getUserId().toString() : "N/A");
//        firstnameLabel.setText(patient.getFirstname() != null ? patient.getFirstname() : "N/A");
//        lastnameLabel.setText(patient.getLastname() != null ? patient.getLastname() : "N/A");
//        dobLabel.setText(patient.getDateOfBirth() != null ? patient.getDateOfBirth().format(DateTimeFormatter.ISO_DATE) : "N/A");
//        genderLabel.setText(patient.getGender() != null ? patient.getGender() : "N/A");
//        emailLabel.setText(patient.getEmail() != null ? patient.getEmail() : "N/A");
//        phoneLabel.setText(patient.getPhone() != null ? patient.getPhone() : "N/A");
//        addressLabel.setText(patient.getAddress() != null ? patient.getAddress() : "N/A");
//
//        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//        createdDateLabel.setText(patient.getCreatedDate() != null ? patient.getCreatedDate().format(displayFormatter) : "N/A");
//        lastModifiedDateLabel.setText(patient.getLastModifiedDate() != null ? patient.getLastModifiedDate().format(displayFormatter) : "N/A");
//        safeSetText(lastModifiedByLabel, patient.getLastModifiedBy(), "Not modified");
//
//        recordsListView.getItems().clear();
////        if (patient.getMedicalRecords() != null && !patient.getMedicalRecords().isEmpty()) {
////            recordsListView.getItems().addAll(patient.getMedicalRecords());
////        } else {
////            recordsListView.getItems().add(new MedicalRecord());
////        }
////
////        recordsListView.setCellFactory(param -> new ListCell<MedicalRecord>() {
////            @Override
////            protected void updateItem(MedicalRecord record, boolean empty) {
////                super.updateItem(record, empty);
////                setText(empty || record == null ? null : formatMedicalRecord(record));
////            }
////        });
//        List<MedicalRecord> medicalRecords = patient.getMedicalRecords();
//        if (medicalRecords != null && !medicalRecords.isEmpty()) {
//            // If there are medical records, display them
//            recordsListView.getItems().addAll(medicalRecords);
//        } else {
//            // If no medical records, add a placeholder item to show the message
//            recordsListView.getItems().add(null); // Use null to indicate no records
//        }
//
//        // Update the cell factory to handle the "No medical records found" message
//        recordsListView.setCellFactory(param -> new ListCell<MedicalRecord>() {
//            @Override
//            protected void updateItem(MedicalRecord record, boolean empty) {
//                super.updateItem(record, empty);
//                if (empty || record == null) {
//                    // Display "No medical records found" if the item is null or empty
//                    setText("No medical records found");
//                    setStyle("-fx-text-fill: #666666; -fx-font-style: italic;"); // Gray and italic for the message
//                } else {
//                    // Display the medical record if it exists
//                    setText(formatMedicalRecord(record));
//                    setStyle(""); // Reset style for normal records
//                }
//            }
//        });
//    }
//
//    @FXML
//    private void addRecord() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthid_system_desktop/AddRecordPopup.fxml"));
//            if (loader.getLocation() == null) {
//                throw new IOException("AddRecordPopup.fxml not found");
//            }
//            Parent root = loader.load();
//
//            AddRecordPopupController controller = loader.getController();
//            String patientId = patientIdField.getText();
//            if (patientId == null || patientId.trim().isEmpty()) {
//                showAlert("Error", "Patient ID is not set. Please scan a QR code first.");
//                return;
//            }
//            controller.setPatientId(patientId);
//            controller.setParentController(this);
//            if (userDetails != null && userDetails.getDoctorId() != null) {
//                controller.setDoctorId(userDetails.getDoctorId().toString());
//            } else {
//                showAlert("Error", "Doctor ID not available.");
//                return;
//            }
//
//            Stage popupStage = new Stage();
//            popupStage.initModality(Modality.APPLICATION_MODAL);
//            popupStage.setTitle("Add New Medical Record");
//            popupStage.setScene(new Scene(root, 600, 400));
//            popupStage.showAndWait();
//        } catch (IOException e) {
//            e.printStackTrace();
//            showAlert("Error", "Failed to load Add Record popup: " + e.getMessage());
//        }
//    }
//
//    @FXML
//    private void saveRecord() {
//        recordFormContainer.setVisible(false);
//        clearRecordForm();
//    }
//
//    @FXML
//    private void cancelRecord() {
//        recordFormContainer.setVisible(false);
//        clearRecordForm();
//    }
//
//    private void clearRecordForm() {
//        recordDate.setValue(null);
//        diagnosisField.clear();
//        treatmentArea.clear();
//        notesArea.clear();
//    }
//
//    private void handleRecordClick(MouseEvent mouseEvent) {
//        if (mouseEvent.getClickCount() == 2) {
//            MedicalRecord selectedRecord = recordsListView.getSelectionModel().getSelectedItem();
//            if (selectedRecord != null) {
//                openRecordDetails(selectedRecord);
//            } else {
//                showError("No record selected!");
//            }
//        }
//    }
//
////    private void openRecordDetails(MedicalRecord record) {
////        try {
////            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthid_system_desktop/recordDetails.fxml"));
////            Parent root = loader.load();
////            MedicalRecordDetailsController controller = loader.getController();
////            controller.setMedicalRecord(record);
////            controller.setLoggedInDoctorId(userDetails != null ? userDetails.getDoctorId() : null);
////            controller.setOnDeleteCallback(this::refreshMedicalRecords); // Set callback for refresh
////
////            Stage stage = new Stage();
////            stage.initModality(Modality.APPLICATION_MODAL);
////            stage.setTitle("Record Details");
////            stage.setScene(new Scene(root));
////            controller.setStage(stage);
////            stage.show();
////        } catch (IOException e) {
////            e.printStackTrace();
////            showError("Error loading record details.");
////        }
////    }
//private void openRecordDetails(MedicalRecord record) {
//    try {
//        // Check if the reportUrl is a file path and open it with HostServices
////        if (record.getReportUrl() != null && record.getReportUrl().startsWith("/uploads/")) {
////            String fullUrl = "http://localhost:8081" + record.getReportUrl(); // Adjust base URL as needed
////            System.out.println("Opening report URL: " + fullUrl);
////            if (getHostServices() != null) {
////                getHostServices().showDocument(fullUrl); // Open the PDF in the default browser
////            } else {
////                showError("HostServices is not available.");
////            }
////            return; // Exit after opening the file
////        }
//
//
//
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthid_system_desktop/recordDetails.fxml"));
//        Parent root = loader.load();
//        MedicalRecordDetailsController controller = loader.getController();
//        controller.setMedicalRecord(record);
//        Integer doctorId = userDetails != null ? userDetails.getDoctorId() : null;
//        System.out.println("Logged-in Doctor ID: " + doctorId); // Debug statement
//        System.out.println("Record Doctor ID: " + record.getDoctor_Id()); // Debug statement
//        controller.setLoggedInDoctorId(doctorId);
//        controller.setOnDeleteCallback(this::refreshMedicalRecords);
//        controller.setHostServices(getHostServices()); // Inject HostServices
//
//        Stage stage = new Stage();
//        stage.initModality(Modality.APPLICATION_MODAL);
//        stage.setTitle("Record Details");
//        stage.setScene(new Scene(root));
//        controller.setStage(stage);
//        stage.show();
//    } catch (IOException e) {
//        e.printStackTrace();
//        showError("Error loading record details.");
//    }
//}
//
//
//
//    private void refreshMedicalRecords() {
//        String patientId = patientIdField.getText();
//        if (patientId != null && !patientId.trim().isEmpty()) {
//            fetchPatientData(Integer.parseInt(patientId));
//        }
//    }
//
//    private void showError(String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setContentText(message);
//        alert.show();
//    }
//
//    private void showAlert(String title, String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.getDialogPane().setPrefSize(400, 200);
//        alert.showAndWait();
//    }
//
//    private void safeSetText(Label label, Object value, String defaultValue) {
//        label.setText(value != null ? value.toString() : defaultValue);
//    }
//
//    private String formatMedicalRecord(MedicalRecord record) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String createdAt = record.getCreatedAt() != null ? record.getCreatedAt().format(formatter) : "N/A";
//        String updatedAt = record.getUpdatedAt() != null ? record.getUpdatedAt().format(formatter) : "N/A";
//        String patientId = record.getPatientID() != null ? record.getPatientID().toString() : "N/A";
//        String disease = record.getDisease().getName() != null ? record.getName() : "N/A"; // Adjusted for String
//        String diagnosticData = record.getDiagnosticData() != null ? record.getDiagnosticData() : "N/A";
//        String treatments = record.getTreatments() != null ? record.getTreatments() : "N/A";
//        String reportUrl = record.getReportUrl() != null ? record.getReportUrl() : "N/A";
//        return String.format("Created: %s | Updated: %s | Patient ID: %s | Disease: %s | Diagnosis: %s | Treatments: %s | Report: %s",
//                createdAt, updatedAt, patientId, disease, diagnosticData, treatments, reportUrl);
//    }
//
//    public void handleMouseEntered(MouseEvent event) {
//        Button button = (Button) event.getSource();
//        button.setStyle("-fx-background-color: #546E7A; -fx-text-fill: #FFFFFF; -fx-font-size: 14; -fx-pref-width: 210; -fx-background-radius: 8; -fx-padding: 10;");
//    }
//
//    public void handleMouseExited(MouseEvent event) {
//        Button button = (Button) event.getSource();
//        button.setStyle("-fx-background-color: #455A64; -fx-text-fill: #FFFFFF; -fx-font-size: 14; -fx-pref-width: 210; -fx-background-radius: 8; -fx-padding: 10;");
//    }
//
//    public void handleScanMouseEntered(MouseEvent event) {
//        Button button = (Button) event.getSource();
//        button.setStyle("-fx-font-size: 14; -fx-background-color: #FFB300; -fx-text-fill: #FFFFFF; -fx-padding: 10 20; -fx-background-radius: 6; -fx-font-weight: bold;");
//    }
//
//    public void handleScanMouseExited(MouseEvent event) {
//        Button button = (Button) event.getSource();
//        button.setStyle("-fx-font-size: 14; -fx-background-color: #FFA726; -fx-text-fill: #FFFFFF; -fx-padding: 10 20; -fx-background-radius: 6; -fx-font-weight: bold;");
//    }
//
//    public void addMedicalRecordToList(MedicalRecord newRecord) {
//        recordsListView.getItems().add(newRecord);
//    }
//
//    public HostServices getHostServices() {
//        return hostServices;
//    }
//
//    public void setHostServices(HostServices hostServices) {
//        this.hostServices = hostServices;
//    }
//}


package org.example.healthid_system_desktop.controller;

import javafx.animation.TranslateTransition;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.healthid_system_desktop.model.MedicalRecord;
import org.example.healthid_system_desktop.model.Patient;
import org.example.healthid_system_desktop.service.UserDetails;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class patientController {

    @FXML public Button addRecordButton;
    @FXML private Button scanButton;
    @FXML private TextField patientIdField;
    @FXML private VBox menuBar;
    @FXML private Button toggleMenuButton;
    @FXML private AnchorPane centerPane;
    @FXML private Label usernameLabel;
    @FXML private Label userIdLabel;
    @FXML private Label firstnameLabel;
    @FXML private Label lastnameLabel;
    @FXML private Label dobLabel;
    @FXML private Label genderLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label addressLabel;
    @FXML private Label createdDateLabel;
    @FXML private Label lastModifiedDateLabel;
    @FXML private Label lastModifiedByLabel;
    @FXML private ListView<MedicalRecord> recordsListView;
    @FXML private VBox recordFormContainer;
    @FXML private DatePicker recordDate;
    @FXML private TextField diagnosisField;
    @FXML private TextArea treatmentArea;
    @FXML private TextArea notesArea;

    private HostServices hostServices;
    private static final double MENU_WIDTH = 189.0;
    private static final double FULL_WIDTH = 1280.0;
    private static final double CENTER_WIDTH = FULL_WIDTH - MENU_WIDTH;

    private String authToken;
    private UserDetails userDetails;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String USER_INFO_URL = "http://localhost:8080/users/api/user/me";
    private static final String BACKEND_URL = "http://localhost:8083/pateints/get/";
    private static final String MEDICAL_RECORDS_URL = "http://localhost:8081/medical-records/medical-records/";
    private static final String PYTHON_SCRIPT_PATH = "src/qr_scanner.py";

    @FXML
    public void initialize() {
        scanButton.setOnAction(event -> scanQrCode());
        recordsListView.setOnMouseClicked(this::handleRecordClick);
        recordFormContainer.setVisible(false);

        addRecordButton.setOnAction(event -> addRecord());

        menuBar.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button btn = (Button) node;
                btn.setOnMouseEntered(this::handleMouseEntered);
                btn.setOnMouseExited(this::handleMouseExited);
                if ("Dashboard".equals(btn.getText())) {
                    btn.setOnAction(event -> openDashboard());
                }
            }
        });
        scanButton.setOnMouseEntered(this::handleScanMouseEntered);
        scanButton.setOnMouseExited(this::handleScanMouseExited);
    }

    public void setAuthToken(String token) {
        this.authToken = token;
        displayUsername();
    }

    public void setUserDetails(UserDetails details) {
        this.userDetails = details;
    }

    private void displayUsername() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + authToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    USER_INFO_URL, HttpMethod.GET, entity, String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                String username = response.getBody();
                usernameLabel.setText(username != null && !username.trim().isEmpty() ? "Welcome, DOCTOR " + username + "!" : "Welcome, User!");
            } else {
                showAlert("Error", "Failed to fetch username: HTTP " + response.getStatusCodeValue());
                usernameLabel.setText("Welcome, User!");
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to fetch username: " + e.getMessage());
            usernameLabel.setText("Welcome, User!");
        }
    }

    @FXML
    private void toggleMenu() {
        boolean isVisible = menuBar.isVisible();
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), centerPane);
        transition.setOnFinished(event -> {
            menuBar.setVisible(!isVisible);
            menuBar.setManaged(!isVisible);
        });

        if (isVisible) {
            transition.setToX(-MENU_WIDTH);
            centerPane.setPrefWidth(FULL_WIDTH);
        } else {
            transition.setToX(0);
            centerPane.setPrefWidth(CENTER_WIDTH);
        }

        transition.play();
        toggleMenuButton.setText(isVisible ? "☰" : "✕");
    }

    @FXML
    private void openDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthid_system_desktop/Dashboard.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("Dashboard.fxml not found at /org/example/healthid_system_desktop/Dashboard.fxml");
            }
            Parent root = loader.load();

            Dashboard controller = loader.getController();
            controller.setAuthToken(authToken);
            if (userDetails != null) {
                controller.setUserDetails(userDetails);
            }

            Stage patientStage = (Stage) toggleMenuButton.getScene().getWindow();
            Stage dashboardStage = new Stage();
            dashboardStage.setTitle(userDetails != null ? userDetails.getRole() + " Dashboard" : "Dashboard");
            dashboardStage.setScene(new Scene(root, 1400, 900));
            dashboardStage.setResizable(true);
            dashboardStage.show();

            patientStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load Dashboard: " + e.getMessage());
        }
    }

    @FXML
    private void scanQrCode() {
        new Thread(() -> {
            try {
                ProcessBuilder pb = new ProcessBuilder("python", PYTHON_SCRIPT_PATH);
                Process process = pb.start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = reader.readLine();

                process.waitFor();

                if (line != null && !line.startsWith("ERROR:")) {
                    Integer patientId = Integer.valueOf(line.trim());
                    Platform.runLater(() -> {
                        patientIdField.setText(String.valueOf(patientId));
                        fetchPatientData(patientId);
                    });
                } else {
                    Platform.runLater(() -> showAlert("Error", line != null ? line : "No QR code detected."));
                }
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Error", "Failed to scan QR code: " + e.getMessage()));
            }
        }).start();
    }

    private void fetchPatientData(Integer patientId) {
        try {
            // Fetch patient data
            Patient patient = restTemplate.getForObject(BACKEND_URL + patientId, Patient.class);
            if (patient == null) {
                showAlert("Error", "Patient not found.");
                return;
            }

            // Fetch medical records separately in a separate try-catch to avoid affecting patient data display
            List<MedicalRecord> medicalRecords = null;
            try {
                ResponseEntity<List<MedicalRecord>> response = restTemplate.exchange(
                        MEDICAL_RECORDS_URL + patientId, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<MedicalRecord>>() {}
                );
                medicalRecords = response.getBody();
            } catch (RestClientException e) {
                // Log the error and treat it as if there are no medical records
                System.err.println("Failed to fetch medical records for patient " + patientId + ": " + e.getMessage());
                medicalRecords = null; // Ensure medicalRecords is null to indicate failure
            }

            // Update UI with patient data and medical records (or lack thereof)
            patient.setMedicalRecords(medicalRecords);
            updatePatientUI(patient);

        } catch (Exception e) {
            showAlert("Error", "Failed to fetch patient data: " + e.getMessage());
        }
    }

    private void updatePatientUI(Patient patient) {
        // Always display patient data
        userIdLabel.setText(patient.getUserId() != null ? patient.getUserId().toString() : "N/A");
        firstnameLabel.setText(patient.getFirstname() != null ? patient.getFirstname() : "N/A");
        lastnameLabel.setText(patient.getLastname() != null ? patient.getLastname() : "N/A");
        dobLabel.setText(patient.getDateOfBirth() != null ? patient.getDateOfBirth().format(DateTimeFormatter.ISO_DATE) : "N/A");
        genderLabel.setText(patient.getGender() != null ? patient.getGender() : "N/A");
        emailLabel.setText(patient.getEmail() != null ? patient.getEmail() : "N/A");
        phoneLabel.setText(patient.getPhone() != null ? patient.getPhone() : "N/A");
        addressLabel.setText(patient.getAddress() != null ? patient.getAddress() : "N/A");

        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        createdDateLabel.setText(patient.getCreatedDate() != null ? patient.getCreatedDate().format(displayFormatter) : "N/A");
        lastModifiedDateLabel.setText(patient.getLastModifiedDate() != null ? patient.getLastModifiedDate().format(displayFormatter) : "N/A");
        safeSetText(lastModifiedByLabel, patient.getLastModifiedBy(), "Not modified");

        // Handle medical records display
        recordsListView.getItems().clear();
        List<MedicalRecord> medicalRecords = patient.getMedicalRecords();

        if (medicalRecords != null && !medicalRecords.isEmpty()) {
            // If there are medical records, display them
            recordsListView.getItems().addAll(medicalRecords);
        }

        // Update the cell factory to handle the "No medical records found" message
        recordsListView.setCellFactory(param -> new ListCell<MedicalRecord>() {
            @Override
            protected void updateItem(MedicalRecord record, boolean empty) {
                super.updateItem(record, empty);
                if (empty || record == null) {
                    // Display "No medical records found" if the item is null or empty
                    // When the ListView is empty or the item is null, show nothing
                    setText(null);
                    setStyle("");
                } else {
                    // Display the medical record if it exists
                    setText(formatMedicalRecord(record));
                    setStyle(""); // Reset style for normal records
                }
            }
        });
    }

    @FXML
    private void addRecord() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthid_system_desktop/AddRecordPopup.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("AddRecordPopup.fxml not found");
            }
            Parent root = loader.load();

            AddRecordPopupController controller = loader.getController();
            String patientId = patientIdField.getText();
            if (patientId == null || patientId.trim().isEmpty()) {
                showAlert("Error", "Patient ID is not set. Please scan a QR code first.");
                return;
            }
            controller.setPatientId(patientId);
            controller.setParentController(this);
            if (userDetails != null && userDetails.getDoctorId() != null) {
                controller.setDoctorId(userDetails.getDoctorId().toString());
            } else {
                showAlert("Error", "Doctor ID not available.");
                return;
            }

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Add New Medical Record");
            popupStage.setScene(new Scene(root, 600, 400));
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load Add Record popup: " + e.getMessage());
        }
    }

    @FXML
    private void saveRecord() {
        recordFormContainer.setVisible(false);
        clearRecordForm();
    }

    @FXML
    private void cancelRecord() {
        recordFormContainer.setVisible(false);
        clearRecordForm();
    }

    private void clearRecordForm() {
        recordDate.setValue(null);
        diagnosisField.clear();
        treatmentArea.clear();
        notesArea.clear();
    }

    private void handleRecordClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            MedicalRecord selectedRecord = recordsListView.getSelectionModel().getSelectedItem();
            // Only open record details if a real record is selected (not the placeholder)
            if (selectedRecord != null && selectedRecord.getPatientID() != null) {
                openRecordDetails(selectedRecord);
            } else {
                showError("No valid record selected!");
            }
        }
    }

    private void openRecordDetails(MedicalRecord record) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthid_system_desktop/recordDetails.fxml"));
            Parent root = loader.load();
            MedicalRecordDetailsController controller = loader.getController();
            controller.setMedicalRecord(record);
            Integer doctorId = userDetails != null ? userDetails.getDoctorId() : null;
            System.out.println("Logged-in Doctor ID: " + doctorId);
            System.out.println("Record Doctor ID: " + record.getDoctor_Id());
            controller.setLoggedInDoctorId(doctorId);
            controller.setOnDeleteCallback(this::refreshMedicalRecords);
            controller.setHostServices(getHostServices());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Record Details");
            stage.setScene(new Scene(root));
            controller.setStage(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading record details.");
        }
    }

    private void refreshMedicalRecords() {
        String patientId = patientIdField.getText();
        if (patientId != null && !patientId.trim().isEmpty()) {
            fetchPatientData(Integer.parseInt(patientId));
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setPrefSize(400, 200);
        alert.showAndWait();
    }

    private void safeSetText(Label label, Object value, String defaultValue) {
        label.setText(value != null ? value.toString() : defaultValue);
    }

    private String formatMedicalRecord(MedicalRecord record) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String createdAt = record.getCreatedAt() != null ? record.getCreatedAt().format(formatter) : "N/A";
        String updatedAt = record.getUpdatedAt() != null ? record.getUpdatedAt().format(formatter) : "N/A";
        String patientId = record.getPatientID() != null ? record.getPatientID().toString() : "N/A";
        String disease = record.getDisease() != null ? record.getDisease().getName() : "N/A"; // Fixed: Use getter for disease name
        String diagnosticData = record.getDiagnosticData() != null ? record.getDiagnosticData() : "N/A";
        String treatments = record.getTreatments() != null ? record.getTreatments() : "N/A";
        String reportUrl = record.getReportUrl() != null ? record.getReportUrl() : "N/A";
        return String.format("Created: %s | Updated: %s | Patient ID: %s | Disease: %s | Diagnosis: %s | Treatments: %s | Report: %s",
                createdAt, updatedAt, patientId, disease, diagnosticData, treatments, reportUrl);
    }

    public void handleMouseEntered(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #546E7A; -fx-text-fill: #FFFFFF; -fx-font-size: 14; -fx-pref-width: 210; -fx-background-radius: 8; -fx-padding: 10;");
    }

    public void handleMouseExited(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #455A64; -fx-text-fill: #FFFFFF; -fx-font-size: 14; -fx-pref-width: 210; -fx-background-radius: 8; -fx-padding: 10;");
    }

    public void handleScanMouseEntered(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-font-size: 14; -fx-background-color: #FFB300; -fx-text-fill: #FFFFFF; -fx-padding: 10 20; -fx-background-radius: 6; -fx-font-weight: bold;");
    }

    public void handleScanMouseExited(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-font-size: 14; -fx-background-color: #FFA726; -fx-text-fill: #FFFFFF; -fx-padding: 10 20; -fx-background-radius: 6; -fx-font-weight: bold;");
    }

    public void addMedicalRecordToList(MedicalRecord newRecord) {
        recordsListView.getItems().add(newRecord);
    }

    public HostServices getHostServices() {
        return hostServices;
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }
}