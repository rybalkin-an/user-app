package com.github.rybalkin_an.app.testdata.random.user;

import com.github.rybalkin_an.app.testdata.TestUser;
import com.github.rybalkin_an.app.user.model.User;
import com.github.rybalkin_an.app.user.model.UserData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

public class TestUserGenerator extends User {

    private UUID id;
    private String firstName;
    private String lastName;
    private String birthdate;
    private UserData userData;
    private String registrationDate;
    private int version;

    private static final String[] FIRST_NAMES = {"John", "Emily", "Tim", "Anna", "Michael", "Sarah"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Armstrong", "Taylor", "Brown", "Anderson"};
    private static final Random random = new Random();

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public TestUserGenerator() {
        UserData userData = new UserData();
        userData.setUserId(random.nextLong());
        userData.setId(random.nextLong());
        userData.setCompleted(random.nextBoolean());
        userData.setTitle("test");

        this.id = UUID.randomUUID();
        this.firstName = getRandomFirstName();
        this.lastName = getRandomLastName();
        this.birthdate = getRandomBirthdate();
        this.registrationDate = getCurrentDate();
        this.version = 1;
        this.userData = userData;
    }

    public static TestUser generateRandomUser() {
        return new TestUser();
    }

    private static String getRandomFirstName() {
        return FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
    }

    private static String getRandomLastName() {
        return LAST_NAMES[random.nextInt(LAST_NAMES.length)];
    }

    private static String getRandomBirthdate() {
        int year = random.nextInt(50) + 1950;
        int month = random.nextInt(12) + 1;
        int day = random.nextInt(28) + 1;
        LocalDate birthdate = LocalDate.of(year, month, day);
        return birthdate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private static String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
