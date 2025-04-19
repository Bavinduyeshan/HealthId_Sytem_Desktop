package org.example.healthid_system_desktop.controller;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.healthid_system_desktop.model.Doctor;
import org.example.healthid_system_desktop.service.UserDetails;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;



import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class DoctorController {


    @FXML private Label doctorNameLabel;
    @FXML private Label specializationLabel;
    @FXML private Label doctorIdLabel;
    @FXML private Label phoneNumberLabel;
    @FXML
    private Label emailLabel;
    @FXML private Label experienceLabel;
    @FXML private Label educationLabel;
    @FXML private Button patientScanButton;
    @FXML private Button updateButton;

    @FXML private Button DashboardButton;





    @FXML private TextField firstnameField;
    @FXML private TextField lastnameField;
    @FXML private TextField specializationField;
    @FXML private TextField phoneNumberField;
    @FXML private TextField emailField;
    @FXML private TextField experienceField;
    @FXML private TextField educationField;

    @FXML private Label usernameLabel;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String DOCTOR_API_URL = "http://localhost:8082/doctors/ByDoc/";
    private static final String UPDATE_DOCTOR_API_URL = "http://localhost:8082/doctors/update/"; // Adjust if different
    private Integer loggedInDoctorId; // To store the logged-in doctor's ID
    private Stage stage;

    private HostServices hostServices;


    private String authToken;
    private UserDetails userDetails;

    private static final String USER_INFO_URL = "http://localhost:8080/users/api/user/me";


    private boolean isEditMode = false;

    private  Doctor currentDoctor;


    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }




    // Initialize method called after FXML is loaded
    @FXML
    public void initialize() {
        if (loggedInDoctorId != null) {
            fetchDoctorData();
        }
        // Set action and hover effects for dashboardButton
        if (DashboardButton != null) {
            DashboardButton.setOnAction(event -> openDashboard());
            DashboardButton.setOnMouseEntered(this::handleMouseEntered);
            DashboardButton.setOnMouseExited(this::handleMouseExited);
        }
    }

    // Set the logged-in doctor's ID
    public void setLoggedInDoctorId(Integer doctorId) {
        this.loggedInDoctorId = doctorId;
        fetchDoctorData(); // Fetch data when doctor ID is set
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }



    public void setAuthToken(String token) {
        this.authToken = token;
        displayUsername();
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
        if (userDetails != null && userDetails.getDoctorId() != null) {
            setLoggedInDoctorId(userDetails.getDoctorId());
        }
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
                showError("error loading message"+response.getStatusCodeValue());

                usernameLabel.setText("Welcome, User!");
            }
        } catch (Exception e) {
            showError("error loading message"+ e.getMessage());
            usernameLabel.setText("Welcome, User!");
        }
    }
    // Fetch doctor data from backend
    private void fetchDoctorData() {
        try {
            HttpHeaders headers = new HttpHeaders();
            if (authToken != null) {
                headers.set("Authorization", "Bearer " + authToken);
            }
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Doctor> response = restTemplate.exchange(
                    DOCTOR_API_URL + loggedInDoctorId,
                    HttpMethod.GET,
                    entity,
                    Doctor.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                currentDoctor = response.getBody();
                updateUI(currentDoctor);
            } else {
                showError("Failed to fetch doctor data: HTTP " + response.getStatusCodeValue());
            }
        } catch (Exception e) {
            showError("Error fetching doctor data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Update UI with doctor data
    private void updateUI(Doctor doctor) {
        doctorIdLabel.setText(doctor.getDoctor_Id()!= null ? doctor.getDoctor_Id().toString() : "N/A");
        doctorNameLabel.setText(String.format("Dr. %s %s",
                doctor.getFirstname() != null ? doctor.getFirstname() : "",
                doctor.getLastname() != null ? doctor.getLastname() : ""));
        specializationLabel.setText(doctor.getSpecilization() != null ? doctor.getSpecilization(): "Not specified");
        phoneNumberLabel.setText(doctor.getPhonenumber() != null ? doctor.getPhonenumber() : "Not provided");
        emailLabel.setText(doctor.getEmail() != null ? doctor.getEmail() : "Not provided");
        educationLabel.setText(doctor.getEducation() != null ? doctor.getEducation() : "Not provided");
        experienceLabel.setText(doctor.getExperience() != null ? doctor.getExperience() : "Not provided");



        // Populate text fields for editing
        firstnameField.setText(doctor.getFirstname() != null ? doctor.getFirstname() : "");
        lastnameField.setText(doctor.getLastname() != null ? doctor.getLastname() : "");
        specializationField.setText(doctor.getSpecilization() != null ? doctor.getSpecilization() : "");
        phoneNumberField.setText(doctor.getPhonenumber() != null ? doctor.getPhonenumber() : "");
        emailField.setText(doctor.getEmail() != null ? doctor.getEmail() : "");
        educationField.setText(doctor.getEducation() != null ? doctor.getEducation() : "");
        experienceField.setText(doctor.getExperience() != null ? doctor.getExperience() : "");
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

            Stage doctorstage= (Stage) patientScanButton.getScene().getWindow();
            Stage patientStage = new Stage();
            patientStage.setTitle("Patient Data");
            patientStage.setScene(new Scene(root, 1280, 720));
            patientStage.setResizable(true);
            patientStage.show();
            doctorstage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load Patients: " + e.getMessage());
        }
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
            controller.setHostServices(hostServices);

            // Use patientScanButton as fallback if dashboardButton is null
            Stage doctorStage = (Stage) (DashboardButton != null ? DashboardButton .getScene().getWindow() : patientScanButton.getScene().getWindow());
            Stage dashboardStage = new Stage();
            dashboardStage.setTitle(userDetails != null ? userDetails.getRole() + " Dashboard" : "Dashboard");
            dashboardStage.setScene(new Scene(root, 1525, 900));
            dashboardStage.setResizable(true);
            dashboardStage.show();

            doctorStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Dashboard: " + e.getMessage());
        }
    }


    @FXML
    private void updateDoctorData() {
        if (!isEditMode) {
            if (currentDoctor == null) {
                showError("Cannot enter edit mode: No doctor data available.");
                return;
            }
            toggleEditMode(true);
            updateButton.setText("Save");
            isEditMode = true;
            System.out.println("Entering edit mode for Doctor ID: " + currentDoctor.getDoctor_Id());
        } else {
            saveDoctorData();
            toggleEditMode(false);
            updateButton.setText("Update Profile");
            isEditMode = false;
            System.out.println("Exiting edit mode and saving data.");
        }
    }

    private void toggleEditMode(boolean editMode) {
        doctorNameLabel.setVisible(!editMode);
        specializationLabel.setVisible(!editMode);
        phoneNumberLabel.setVisible(!editMode);
        emailLabel.setVisible(!editMode);
        educationLabel.setVisible(!editMode);
        experienceLabel.setVisible(!editMode);

        firstnameField.setVisible(editMode);
        lastnameField.setVisible(editMode);
        specializationField.setVisible(editMode);
        phoneNumberField.setVisible(editMode);
        emailField.setVisible(editMode);
        educationField.setVisible(editMode);
        experienceField.setVisible(editMode);
    }

    private void saveDoctorData() {
        if (currentDoctor == null) {
            showError("No doctor data to update. Please ensure data is fetched first.");
            return;
        }

        System.out.println("Saving doctor data for ID: " + currentDoctor.getDoctor_Id());
        currentDoctor.setFirstname(firstnameField.getText().isEmpty() ? null : firstnameField.getText());
        currentDoctor.setLastname(lastnameField.getText().isEmpty() ? null : lastnameField.getText());
        currentDoctor.setSpecilization(specializationField.getText().isEmpty() ? null : specializationField.getText());
        currentDoctor.setPhonenumber(phoneNumberField.getText().isEmpty() ? null : phoneNumberField.getText());
        currentDoctor.setEmail(emailField.getText().isEmpty() ? null : emailField.getText());
        currentDoctor.setEducation(educationField.getText().isEmpty() ? null : educationField.getText());
        currentDoctor.setExperience(experienceField.getText().isEmpty() ? null : experienceField.getText());

        try {
            // Prepare headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (authToken != null) {
                headers.set("Authorization", "Bearer " + authToken);
            }

            // Create request entity
            HttpEntity<Doctor> request = new HttpEntity<>(currentDoctor, headers);

            // Debug: Print the request body
            ObjectMapper mapper = new ObjectMapper();
            System.out.println("Request body: " + mapper.writeValueAsString(currentDoctor));

            // Send the update request, expecting a String response
            System.out.println("Sending update request to: " + UPDATE_DOCTOR_API_URL + loggedInDoctorId);
            ResponseEntity<String> response = restTemplate.exchange(
                    UPDATE_DOCTOR_API_URL + loggedInDoctorId,
                    HttpMethod.PUT,
                    request,
                    String.class
            );

            System.out.println("Response status: " + response.getStatusCode());
            System.out.println("Raw server response: " + response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                // Check if the response indicates success
                String responseBody = response.getBody();
                if (responseBody != null && responseBody.contains("Succesfully")) { // Case-insensitive check might be better
                    updateUI(currentDoctor); // Update UI with local data since server doesn't return Doctor object
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Doctor data updated successfully!");
                } else {
                    showError("Update failed: Unexpected response - " + responseBody);
                }
            } else {
                showError("Failed to update doctor data: HTTP " + response.getStatusCodeValue());
            }
        } catch (Exception e) {
            showError("Error updating doctor data: " + e.getMessage());
            e.printStackTrace();

            // Debug: Fetch raw response as String
            try {
                HttpHeaders debugHeaders = new HttpHeaders();
                debugHeaders.setContentType(MediaType.APPLICATION_JSON);
                if (authToken != null) {
                    debugHeaders.set("Authorization", "Bearer " + authToken);
                }
                HttpEntity<Doctor> debugRequest = new HttpEntity<>(currentDoctor, debugHeaders);

                ResponseEntity<String> debugResponse = restTemplate.exchange(
                        UPDATE_DOCTOR_API_URL + loggedInDoctorId,
                        HttpMethod.PUT,
                        debugRequest,
                        String.class
                );
                System.out.println("Debug raw server response: " + debugResponse.getBody());
            } catch (Exception debugEx) {
                System.err.println("Debug fetch failed: " + debugEx.getMessage());
                debugEx.printStackTrace();
            }
        }
    }

    public void handleMouseEntered(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #546E7A; -fx-text-fill: #FFFFFF; -fx-font-size: 14;  -fx-background-radius: 8; -fx-padding: 10;");
    }

    public void handleMouseExited(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #455A64; -fx-text-fill: #FFFFFF; -fx-font-size: 14;  -fx-background-radius: 8; -fx-padding: 10;");
    }
    private void showError(String message) {
        doctorNameLabel.setText("Error");
        specializationLabel.setText("Could not load data");
        doctorIdLabel.setText("N/A");
        phoneNumberLabel.setText("N/A");
        emailLabel.setText("N/A");
        educationLabel.setText("N/A");
        experienceLabel.setText("N/A");
        System.err.println(message);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
