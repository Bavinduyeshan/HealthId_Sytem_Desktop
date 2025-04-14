package org.example.healthid_system_desktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.healthid_system_desktop.model.Doctor;
import org.example.healthid_system_desktop.service.UserDetails;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;



import com.fasterxml.jackson.databind.ObjectMapper;

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





    @FXML private TextField firstnameField;
    @FXML private TextField lastnameField;
    @FXML private TextField specializationField;
    @FXML private TextField phoneNumberField;
    @FXML private TextField emailField;
    @FXML private TextField experienceField;
    @FXML private TextField educationField;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String DOCTOR_API_URL = "http://localhost:8082/doctors/ByDoc/";
    private static final String UPDATE_DOCTOR_API_URL = "http://localhost:8082/doctors/update/"; // Adjust if different
    private Integer loggedInDoctorId; // To store the logged-in doctor's ID
    private Stage stage;


    private String authToken;
    private UserDetails userDetails;

    private boolean isEditMode = false;

    private  Doctor currentDoctor;





    // Initialize method called after FXML is loaded
    @FXML
    public void initialize() {
        if (loggedInDoctorId != null) {
            fetchDoctorData();
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
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
        if (userDetails != null && userDetails.getDoctorId() != null) {
            setLoggedInDoctorId(userDetails.getDoctorId());
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
    private void openPatients() {
        // Implement patient scan functionality here
        System.out.println("Opening patients view...");
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
