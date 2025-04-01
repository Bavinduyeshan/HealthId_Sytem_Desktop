package org.example.healthid_system_desktop.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MedicalRecord {


    private Long Id;



//    @ManyToOne
//    @JoinColumn(name = "patient_id")
//    private Patient patient;

     private Disease disease; //



    private  Integer patientID;


    private  Integer doctor_Id;

    private String name;


    private String diagnosticData;


    private String treatments;


    private String reportUrl;


    private LocalDateTime createdAt = LocalDateTime.now();


    private LocalDateTime updatedAt = LocalDateTime.now();

    // Default constructor
    public MedicalRecord() {
    }

    // Getters and Setters


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public Integer getPatientID() {
        return patientID;
    }

    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
    }

    public Integer getDoctor_Id() {
        return doctor_Id;
    }

    public void setDoctor_Id(Integer doctor_Id) {
        this.doctor_Id = doctor_Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiagnosticData() {
        return diagnosticData;
    }

    public void setDiagnosticData(String diagnosticData) {
        this.diagnosticData = diagnosticData;
    }

    public String getTreatments() {
        return treatments;
    }

    public void setTreatments(String treatments) {
        this.treatments = treatments;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.format("MedicalRecord [ID=%s, Disease=%s, PatientID=%s, DoctorID=%s, Diagnosis=%s, Treatments=%s, ReportUrl=%s, CreatedAt=%s, UpdatedAt=%s]",
                Id, disease, patientID, doctor_Id, diagnosticData, treatments, reportUrl,
                createdAt != null ? createdAt.format(formatter) : "N/A",
                updatedAt != null ? updatedAt.format(formatter) : "N/A");
    }


}
