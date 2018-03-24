package com.abdulwd.lifeline;

public class UserInformation {

    private String userEmail, userNumber;

    public UserInformation() {

    }

    public UserInformation(String email, String number) {
        this.userEmail = email;
        this.userNumber = number;

    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }
}
