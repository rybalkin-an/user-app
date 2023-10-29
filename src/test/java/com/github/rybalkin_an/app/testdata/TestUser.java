package com.github.rybalkin_an.app.testdata;

import com.github.rybalkin_an.app.user.model.User;

import java.util.UUID;

import static com.github.rybalkin_an.app.utils.StringHelper.getCurrentDate;

public class TestUser extends User {

    private UUID id;
    private String firstName;
    private String lastName;
    private String birthdate;
    private String registrationDate;
    private int version;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public TestUser() {
        this.id =  UUID.randomUUID();
        this.firstName = "Tim";
        this.lastName = "Armstrong";
        this.birthdate = "1965-11-25";
        this.registrationDate = getCurrentDate();
        this.version = 1;
    }
}
