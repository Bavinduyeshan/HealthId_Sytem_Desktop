//package org.example.healthid_system_desktop.service;
//
//import org.example.healthid_system_desktop.model.User;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.nio.charset.StandardCharsets;
//
//public class LoginService {
//
//    private static final String API_URL = "http://localhost:8080/users/login"; // Modify with actual API URL
//
//    // Authenticate user by sending request to backend
//    public boolean authenticateUser(User user) {
//        try {
//            JSONObject json = createLoginJson(user.getUsername(), user.getPassword());
//
//            HttpClient client = HttpClient.newHttpClient();
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(API_URL))
//                    .header("Content-Type", "application/json")
//                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
//                    .build();
//
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            return response.statusCode() == HttpURLConnection.HTTP_OK;
//
//        } catch (java.net.ConnectException e) {
//            // Handle network errors like no connection to server
//            e.printStackTrace();
//            return false;
//        } catch (IOException e) {
//            // Handle I/O issues during the request
//            e.printStackTrace();
//            return false;
//        } catch (Exception e) {
//            // Catch any other exceptions
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    // Utility method to create the JSON body for the login request
//    private JSONObject createLoginJson(String username, String password) {
//        JSONObject json = new JSONObject();
//        json.put("username", username);
//        json.put("password", password);
//        return json;
//    }
//
//    // Optional: Asynchronous version of authenticateUser (non-blocking)
//    public void authenticateUserAsync(User user) {
//        try {
//            JSONObject json = createLoginJson(user.getUsername(), user.getPassword());
//
//            HttpClient client = HttpClient.newHttpClient();
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(API_URL))
//                    .header("Content-Type", "application/json")
//                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
//                    .build();
//
//            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
//                    .thenApply(response -> {
//                        if (response.statusCode() == HttpURLConnection.HTTP_OK) {
//                            // Handle success
//                            System.out.println("Login successful!");
//                        } else {
//                            // Handle failure
//                            System.out.println("Invalid username or password.");
//                        }
//                        return null;
//                    })
//                    .exceptionally(e -> {
//                        // Handle exceptions asynchronously
//                        e.printStackTrace();
//                        return null;
//                    });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}



//
//package org.example.healthid_system_desktop.service;
//
//import org.example.healthid_system_desktop.model.User;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//
//public class LoginService {
//
//    private static final String API_URL = "http://localhost:8080/users/login"; // Modify with actual API URL
//
//    // Authenticate user and return the token if successful
//    public String authenticateUser(User user) throws Exception {
//        try {
//            JSONObject json = createLoginJson(user.getUsername(), user.getPassword());
//
//            HttpClient client = HttpClient.newHttpClient();
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(API_URL))
//                    .header("Content-Type", "application/json")
//                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
//                    .build();
//
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            if (response.statusCode() == HttpURLConnection.HTTP_OK) {
//                // Parse the token from the response body
//                JSONObject responseJson = new JSONObject(response.body());
//                return responseJson.getString("token"); // Adjust key based on your backend response
//            } else {
//                return null; // Authentication failed
//            }
//        } catch (java.net.ConnectException e) {
//            throw new Exception("Cannot connect to server: " + e.getMessage());
//        } catch (IOException e) {
//            throw new Exception("I/O error during login: " + e.getMessage());
//        } catch (Exception e) {
//            throw new Exception("Login failed: " + e.getMessage());
//        }
//    }
//
//    // Utility method to create the JSON body for the login request
//    private JSONObject createLoginJson(String username, String password) {
//        JSONObject json = new JSONObject();
//        json.put("username", username);
//        json.put("password", password);
//        return json;
//    }
//}



package org.example.healthid_system_desktop.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.example.healthid_system_desktop.model.User;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Service class to handle user authentication and fetch user details from backend services.
 */
public class LoginService {

    // API endpoints for authentication and user details
    private static final String LOGIN_URL = "http://localhost:8080/users/login";          // Login endpoint
    private static final String USER_API_URL = "http://localhost:8080/users/byUsername/"; // Endpoint to get userId by username
    private static final String PATIENT_API_URL = "http://localhost:8083/patients/byUserId/"; // Endpoint to get patient details
    private static final String DOCTOR_API_URL = "http://localhost:8082/doctors/ByUserId/";   // Endpoint to get doctor details
    private static final String JWT_SECRET = "lxK8roAuO9eYbuzJdVLMSJWnQWbnstt4+0htpxie8KE7gmMTdpNGpNkzikfiEZderpl6ikl0P9SHPIqaPjvo1A==";              // JWT secret key (replace with actual key)

    private static final String FORGOT_PASSWORD_URL = "http://localhost:8080/users/forgot-password";

    /**
     * Authenticates a user by sending credentials to the User Service and returns a JWT token.
     *
     * @param user User object containing username and password
     * @return JWT token if successful, null if authentication fails
     * @throws Exception if an error occurs during the request
     */
    public String authenticateUser(User user) throws Exception {
        try {
            // Create JSON payload with username and password
            JSONObject json = createLoginJson(user.getUsername(), user.getPassword());

            // Build and send HTTP POST request to login endpoint
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(LOGIN_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if response is successful (HTTP 200)
            if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                JSONObject responseJson = new JSONObject(response.body());
                return responseJson.getString("token"); // Extract and return the token
            } else {
                return null; // Authentication failed
            }
        } catch (java.net.ConnectException e) {
            throw new Exception("Cannot connect to server: " + e.getMessage());
        } catch (IOException e) {
            throw new Exception("I/O error during login: " + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Login failed: " + e.getMessage());
        }
    }

    /**
     * Extracts the username from a JWT token.
     *
     * @param token JWT token received from authentication
     * @return Username stored in the token's subject claim
     */
    public String extractUsernameFromToken(String token) {
        try {
            // Parse the JWT token using the secret key
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(JWT_SECRET)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject(); // Return the username
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse token: " + e.getMessage());
        }
    }

    /**
     * Fetches user details (role and IDs) by querying User, Patient, and Doctor Services.
     * @param token JWT token from authentication
     * @return UserDetails object with username, role, and patientId/doctorId, or null if not found
     * @throws Exception if an error occurs during API calls
     */
//    public UserDetails fetchUserDetails(String token) throws Exception {
//        // Extract username from the token
//        String username = extractUsernameFromToken(token);
//        HttpClient client = HttpClient.newHttpClient();
//
//        // Step 1: Get numeric userId from User Service using username
//        HttpRequest userRequest = HttpRequest.newBuilder()
//                .uri(URI.create(USER_API_URL + username))
//                .header("Authorization", "Bearer " + token)
//                .GET()
//                .build();
//        HttpResponse<String> userResponse = client.send(userRequest, HttpResponse.BodyHandlers.ofString());
//        if (userResponse.statusCode() != 200) {
//            System.out.println("Failed to fetch userId: " + userResponse.statusCode());
//            return null;
//        }
//        JSONObject userJson = new JSONObject(userResponse.body());
//        Integer userId = userJson.getInt("id"); // Extract numeric userId
//
//        // Step 2: Try Patient Service with userId
////        HttpRequest patientRequest = HttpRequest.newBuilder()
////                .uri(URI.create(PATIENT_API_URL + userId))
////                .header("Authorization", "Bearer " + token)
////                .GET()
////                .build();
////        HttpResponse<String> patientResponse = client.send(patientRequest, HttpResponse.BodyHandlers.ofString());
////        if (patientResponse.statusCode() == 200) {
////            JSONObject patientJson = new JSONObject(patientResponse.body());
////            Integer patientId = patientJson.getInt("patientId"); // Extract patientId
////            return new UserDetails(username, "PATIENT", patientId, null);
////        }
//
//        // Step 3: Try Doctor Service with userId
//        HttpRequest doctorRequest = HttpRequest.newBuilder()
//                .uri(URI.create(DOCTOR_API_URL + userId))
//                .header("Authorization", "Bearer " + token)
//                .GET()
//                .build();
//        HttpResponse<String> doctorResponse = client.send(doctorRequest, HttpResponse.BodyHandlers.ofString());
//        if (doctorResponse.statusCode() == 200) {
//            JSONObject doctorJson = new JSONObject(doctorResponse.body());
//            String doctorId = doctorJson.getString("doctorId"); // Extract doctorId
//            return new UserDetails(username, "DOCTOR", null, doctorId);
//        }
//
//        // Return null if user is neither a patient nor a doctor
//        System.out.println("No role found for userId: " + userId);
//        return null;
//    }

    /**
     * Extracts the username from a JWT token.
     *
     * @param token JWT token received from authentication
     * @return Username stored in the token's subject claim
     */
    public UserDetails fetchUserDetails(String token) throws Exception {
        // Extract username from the token
        String username = extractUsernameFromToken(token);
        System.out.println("Extracted username from token: " + username); // Debug log
        HttpClient client = HttpClient.newHttpClient();

        // Step 1: Get numeric userId from User Service using username
        HttpRequest userRequest = HttpRequest.newBuilder()
                .uri(URI.create(USER_API_URL + username))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();
        HttpResponse<String> userResponse = client.send(userRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("User Service response status: " + userResponse.statusCode()); // Debug log
        System.out.println("User Service response body: " + userResponse.body()); // Debug log
        if (userResponse.statusCode() != 200) {
            System.out.println("Failed to fetch userId for username: " + username);
            return null;
        }
        JSONObject userJson = new JSONObject(userResponse.body());
        Integer userId;
        try {
            userId = userJson.getInt("id"); // Expecting {"id": <userId>, "username": <username>}
        } catch (Exception e) {
            System.out.println("Failed to parse userId from response: " + e.getMessage());
            return null;
        }
        System.out.println("Fetched userId: " + userId); // Debug log

        // Step 2: Try Doctor Service with userId
        HttpRequest doctorRequest = HttpRequest.newBuilder()
                .uri(URI.create(DOCTOR_API_URL + userId))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();
        HttpResponse<String> doctorResponse = client.send(doctorRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Doctor Service response status: " + doctorResponse.statusCode()); // Debug log
        System.out.println("Doctor Service response body: " + doctorResponse.body()); // Debug log
        if (doctorResponse.statusCode() == 200) {
            try {
                JSONObject doctorJson = new JSONObject(doctorResponse.body());
                // Adjust to match the actual field name "doctor_Id" from the response
                Integer doctor_Id = doctorJson.getInt("doctor_Id"); // Changed from "doctorId" to "doctor_Id"
                System.out.println("Fetched doctorId: " + doctor_Id); // Debug log
                return new UserDetails(username, "DOCTOR", null, doctor_Id);
            } catch (Exception e) {
                System.out.println("Failed to parse doctor response: " + e.getMessage());
                return null;
            }
        } else {
            System.out.println("Doctor Service failed with status: " + doctorResponse.statusCode());
            System.out.println("No role found for userId: " + userId);
            return null;
        }
    }

    /**
     * Creates a JSON object for the login request body.
     *
     * @param username User's username
     * @param password User's password
     * @return JSONObject with username and password
     */
    private JSONObject createLoginJson(String username, String password) {
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("password", password);
        return json;
    }


    /**
     * Sends a password reset request to the backend for the given email.
     *
     * @param email User's email address
     * @return true if the request was successful, false otherwise
     * @throws Exception if an error occurs during the request
     */
    public boolean requestPasswordReset(String email) throws Exception {
        try {
            // Create JSON payload with email
            JSONObject json = new JSONObject();
            json.put("email", email);
            System.out.println("Request payload: " + json.toString()); // Log the payload

            // Build and send HTTP POST request to forgot password endpoint
            HttpClient client = HttpClient.newHttpClient();
            System.out.println("Sending request to: " + FORGOT_PASSWORD_URL); // Log the URL
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(FORGOT_PASSWORD_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Log response details
            System.out.println("Response status code: " + response.statusCode());
            System.out.println("Response body: " + response.body());

            // Check if response is successful (HTTP 200)
            if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Password reset request successful.");
                return true;
            } else {
                System.out.println("Password reset request failed with status: " + response.statusCode());
                return false;
            }
        } catch (java.net.ConnectException e) {
            System.out.println("Connection error: " + e.getMessage());
            throw new Exception("Cannot connect to server: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
            throw new Exception("I/O error during password reset request: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            throw new Exception("Password reset request failed: " + e.getMessage());
        }
    }
}

/**
 * Data class to hold user details fetched from services.
 */
