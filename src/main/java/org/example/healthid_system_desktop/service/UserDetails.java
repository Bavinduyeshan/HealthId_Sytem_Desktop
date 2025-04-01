package org.example.healthid_system_desktop.service;

public class UserDetails {

    private String username;   // Username from User Service
    private String role;       // "PATIENT" or "DOCTOR"
    private Integer patientId; // Patient ID if user is a patient
    private Integer doctor_Id;   // Doctor ID if user is a doctor

    public UserDetails(String username, String role, Integer patientId, Integer doctorId) {
        this.username = username;
        this.role = role;
        this.patientId = patientId;
        this.doctor_Id = doctorId;
    }

    public String getUsername() { return username; }
    public String getRole() { return role; }
    public Integer getPatientId() { return patientId; }
    public Integer getDoctorId() { return doctor_Id; }
}
