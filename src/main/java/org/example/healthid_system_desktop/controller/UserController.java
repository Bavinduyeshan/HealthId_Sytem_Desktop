//package org.example.healthid_system_desktop.controller;
//
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Alert.AlertType;
//import javafx.scene.control.CheckBox;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//import javafx.scene.image.ImageView;
//import javafx.scene.text.Text;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.control.Button;
//import javafx.stage.Stage;
//import org.example.healthid_system_desktop.model.User;
//import org.example.healthid_system_desktop.service.LoginService;
//
//import java.io.IOException;
//
//
//public class UserController {
//
//    @FXML
//    private TextField txtusername;
//
//    @FXML
//    private PasswordField txtpassword;
//
//    @FXML
//    private Button btnSignIn;
//
//    @FXML
//    private CheckBox chkRemember;
//
//    @FXML
//    private ImageView eyeIcon;
//
//    @FXML
//    private Text txtForgotPassword;
//
//    // Reference to the LoginService (Model)
//    private final LoginService loginService = new LoginService();
//
//    // Handle login functionality
//    @FXML
//    public void handleLogin() {
//       /* String username = txtusername.getText().trim();
//        String password = txtpassword.getText().trim();
//
//        if (username.isEmpty() || password.isEmpty()) {
//            showAlert("Error", "Please enter both username and password.");
//            return;
//        }
//
//        // Create a User object (Model)
//        User user = new User(username, password);
//
//        // Authenticate the user via the LoginService (Model)
//        boolean isAuthenticated = loginService.authenticateUser(user);
//
//        if (isAuthenticated) {
//            showAlert("Success", "Login Successful!");
//        } else {
//            showAlert("Error", "Invalid username or password.");
//        }*/
//        String username = txtusername.getText().trim();
//        String password = txtpassword.getText().trim();
//
//        if (username.isEmpty() || password.isEmpty()) {
//            showAlert("Error", "Please enter both username and password.");
//            return;
//        }
//
//        User user = new User(username, password);
//
//        try {
//            String token = loginService.authenticateUser(user);
//            if (token != null) {
//                showAlert("Success", "Login Successful!");
//                loadDashboard(token); // Transition to dashboard
//            } else {
//                showAlert("Error", "Invalid username or password.");
//            }
//        } catch (Exception e) {
//            showAlert("Error", e.getMessage());
//        }
//    }
//
//    private void loadDashboard(String token) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthid_system_desktop/Patients2.fxml")); // Adjust path to your FXML
//            Parent root = loader.load();
//
//            // Get the patientController and pass the token
//            patientController controller = loader.getController();
//            controller.setAuthToken(token);
//
//            Stage stage = (Stage) btnSignIn.getScene().getWindow(); // Get current stage
//            stage.setScene(new Scene(root));
//            stage.setTitle("Patient Dashboard");
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            showAlert("Error", "Failed to load dashboard: " + e.getMessage());
//        }
//    }
//
//    // Show alert for success or error
//    private void showAlert(String title, String message) {
//        Alert alert = new Alert(AlertType.INFORMATION);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    // Optional: Handle the eye icon click for password visibility toggle
//    @FXML
//    public void togglePasswordVisibility(MouseEvent event) {
//        if (txtpassword.isVisible()) {
//            txtpassword.setVisible(false);
//            eyeIcon.setOpacity(1.0);
//        } else {
//            txtpassword.setVisible(true);
//            eyeIcon.setOpacity(0.5);
//        }
//    }
//
//    // Optional: Handle forgot password click
//    @FXML
//    public void handleForgotPassword(MouseEvent event) {
//        // Handle forgot password functionality here
//        showAlert("Forgot Password", "Password recovery functionality coming soon.");
//    }
//}


package org.example.healthid_system_desktop.controller;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.healthid_system_desktop.model.User;
import org.example.healthid_system_desktop.service.LoginService;
import org.example.healthid_system_desktop.service.UserDetails;

import java.io.IOException;

/**
 * Controller for the login screen in the JavaFX application.
 * Manages user authentication and transitions to the dashboard.
 */
public class UserController {

    // FXML-injected UI components
    @FXML private TextField txtusername;         // Username input field
    @FXML private PasswordField txtpassword;     // Password input field
    @FXML private Button btnSignIn;              // Sign-in button
    @FXML private CheckBox chkRemember;          // "Remember Me" checkbox (not implemented)
    @FXML private ImageView eyeIcon;             // Icon to toggle password visibility
    @FXML private Text txtForgotPassword;        // "Forgot Password" text link

    @FXML
    private Button btnRegisterNow;


    private Stage stage;
    private HostServices hostServices;
    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    // Instance of LoginService for authentication and API calls
    private final LoginService loginService = new LoginService();

    /**
     * Handles the login button click event.
     * Validates input, authenticates the user, and loads the dashboard if successful.
     */
    @FXML
    public void handleLogin() {
        String username = txtusername.getText().trim();  // Get and trim username
        String password = txtpassword.getText().trim();  // Get and trim password

        // Validate that fields are not empty
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both username and password.");
            return;
        }

        // Create a User object with the credentials
        User user = new User(username, password);

        try {
            // Authenticate user and get token
            String token = loginService.authenticateUser(user);
            if (token != null) {
                // Fetch user details (role and IDs) using the token
                UserDetails userDetails = loginService.fetchUserDetails(token);
                if (userDetails != null) {
                    showAlert("Success", "Login Successful!");
                    loadDashboard(token, userDetails); // Transition to dashboard
                } else {
                    showAlert("Error", "Could not determine user role or ID.");
                }
            } else {
                showAlert("Error", "Invalid username or password.");
            }
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    /**
     * Loads the dashboard scene (Patients2.fxml) and passes token and user details.
     * @param token JWT token from authentication
     * @param userDetails User details including role and IDs
     */
    private void loadDashboard(String token, UserDetails userDetails) {
        try {
            // Corrected path with leading "/"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthid_system_desktop/Dashboard.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("FXML file not found at /org/example/healthid_system_desktop/Dashboard.fxml");
            }
            Parent root = loader.load();

            // Get the Dashboard controller and pass token/details
            Dashboard controller = loader.getController();
            controller.setAuthToken(token);         // Added method
            controller.setUserDetails(userDetails); // Added method
            // Pass HostServices and other data to DashboardController
            controller.setHostServices(hostServices);

            Stage stage = (Stage) btnSignIn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(userDetails.getRole() + " Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load dashboard: " + e.getMessage());
        }
    }

    /**
     * Displays an alert dialog with a title and message.
     * @param title Alert title
     * @param message Alert message
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Toggles password field visibility when the eye icon is clicked.
     * @param event Mouse event from clicking the icon
     */
    @FXML
    public void togglePasswordVisibility(MouseEvent event) {
        if (txtpassword.isVisible()) {
            txtpassword.setVisible(false);
            eyeIcon.setOpacity(1.0); // Fully visible icon
        } else {
            txtpassword.setVisible(true);
            eyeIcon.setOpacity(0.5); // Dimmed icon
        }
    }

    /**
     * Handles "Forgot Password" click event by opening a popup for email input.
     * @param event Mouse event from clicking the text
     */
    @FXML
    public void handleForgotPassword(MouseEvent event) {
        try {
            // Load the ForgotPasswordPopup.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthid_system_desktop/ForgotPasswordPopUp.fxml"));
            Parent root = loader.load();

            // Create a new Stage for the popup
            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));
            popupStage.setTitle("Forgot Password");

            // Make the popup modal (blocks interaction with the login form until closed)
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(txtusername.getScene().getWindow());

            // Prevent resizing
            popupStage.setResizable(false);

            // Show the popup and wait for it to close
            popupStage.showAndWait();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open forgot password popup: " + e.getMessage());
        }
    }

    @FXML
    private void openRegister(ActionEvent event) {
        try {
            // Load Register.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/healthid_system_desktop/Register.fxml"));
            Scene registerScene = new Scene(loader.load(), 816, 541);

            // Get the current stage and set the new scene
            Stage stage = (Stage) btnRegisterNow.getScene().getWindow();
            stage.setScene(registerScene);
            stage.setTitle("Register");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open registration form: " + e.getMessage());
        }
    }

    // Utility method to show alerts
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Clear login form fields
    private void clearLoginFields() {
        txtusername.clear();
        txtpassword.clear();
    }

    // Clear registration form fields

}