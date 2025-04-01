//package org.example.healthid_system_desktop;
//
//import org.example.healthid_system_desktop.model.User;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//
//public class dumy {
//}
//package org.example.healthid_system_desktop.service;
//
//
//        import org.example.healthid_system_desktop.model.User;
//        import org.springframework.http.HttpEntity;
//        import org.springframework.http.HttpHeaders;
//        import org.springframework.http.MediaType;
//        import org.springframework.http.ResponseEntity;
//        import org.springframework.web.client.RestTemplate;
//
//public class UserService {
//    private final RestTemplate restTemplate = new RestTemplate();
//    private static final String BASE_URL = "http://localhost:8080/users";
//
//    private static String authToken; // Store token globally
//
//    public boolean login(String username, String password) {
//        User loginRequest = new User(username, password);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<User> request = new HttpEntity<>(loginRequest, headers);
//
//        try {
//            ResponseEntity<org.example.healthid_system_desktop.service.UserService.AuthResponse> response = restTemplate.postForEntity(BASE_URL + "/login", request, org.example.healthid_system_desktop.service.UserService.AuthResponse.class);
//            if (response.getStatusCode().is2xxSuccessful()) {
//                authToken = response.getBody().getToken(); // Store JWT token
//                return true;
//            } else {
//                System.out.println("Login failed: " + response.getStatusCode() + " : " + response.getBody());
//            }
//        } catch (Exception e) {
//            System.out.println("Login failed: " + e.getMessage());
//        }
//        return false;
//    }
//
//
//    public String getAuthToken() {
//        return authToken;
//    }
//
//    public static class AuthResponse {
//        private String token;
//        public String getToken() { return token; }
//        public void setToken(String token) { this.token = token; }
//    }
//}


//
//
//
//package org.example.healthid_system_desktop.controller;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Alert.AlertType;
//import javafx.scene.control.CheckBox;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//import javafx.scene.image.ImageView;
//import javafx.scene.text.Text;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.control.Button;
//
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//
//import org.json.JSONObject;
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
//    // Handle login functionality
//    @FXML
//    public void handleLogin() {
//        String username = txtusername.getText().trim();
//        String password = txtpassword.getText().trim();
//
//        if (username.isEmpty() || password.isEmpty()) {
//            showAlert("Error", "Please enter both username and password.");
//            return;
//        }
//
//        try {
//            // Prepare the API call to Spring Boot backend
//            String apiUrl = "http://localhost:8080/users/login"; // Modify with actual API URL
//            URL url = new URL(apiUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setDoOutput(true);
//
//            // Create a JSON object with the username and password
//            JSONObject json = new JSONObject();
//            json.put("username", username);
//            json.put("password", password);
//
//            // Send the JSON object as the request body
//            try (OutputStream os = connection.getOutputStream()) {
//                byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
//                os.write(input, 0, input.length);
//            }
//
//            // Get the response code from the server
//            int responseCode = connection.getResponseCode();
//
//            // Handle the server's response
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                // Read the response (could be a success message, token, etc.)
//                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String inputLine;
//                StringBuilder response = new StringBuilder();
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                in.close();
//
//                // Assuming the backend returns a JSON response with a success message or token
//                showAlert("Success", "Login Successful!");
//            } else {
//                showAlert("Error", "Invalid username or password.");
//            }
//
//            connection.disconnect();
//        } catch (Exception e) {
//            e.printStackTrace();
//            showAlert("Error", "An error occurred during login. Please try again.");
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
