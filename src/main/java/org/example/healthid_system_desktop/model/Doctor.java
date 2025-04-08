package org.example.healthid_system_desktop.model;

public class Doctor {



    private Integer doctor_Id;
    private Integer userId;

    // Add other fields if they might be included
    private String firstname;
    private String lastname;
    private String phonenumber;
    private String email;
    private String specilization; // Note: typo in your JSON, should be "specialization"
    private String education;
    private String experience;

    // Default constructor for deserialization
    public Doctor() {}

    // Getters
    public Integer getDoctor_Id() { return doctor_Id; }
    public Integer getUserId() { return userId; }
    public String getFirstname() { return firstname; }
    public String getLastname() { return lastname; }
    public String getPhonenumber() { return phonenumber; }
    public String getEmail() { return email; }
    public String getSpecilization() { return specilization; }
    public String getEducation() { return education; }
    public String getExperience() { return experience; }


    public void setDoctor_Id(Integer doctor_Id) {
        this.doctor_Id = doctor_Id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSpecilization(String specilization) {
        this.specilization = specilization;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}
