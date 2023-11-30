package com.example.kidcare.Model;

public class ModelChildInfo {
    String firstName, lastName, birthDate, gender,status,daycareID;

    public String getDaycareID() {
        return daycareID;
    }

    public String getStatus() {
        return status;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

    public ModelChildInfo(String firstName, String lastName, String birthDate, String gender,String status,String daycareID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.status = status;
        this.daycareID = daycareID;
    }
}
