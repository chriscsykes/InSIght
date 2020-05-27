package edu.dartmouth.cs.finalproject.activities.data.model;

public class User {

    private String username;
    private int phoneNumber;
    private FeatureHistory featureHistory;


    public User(String username, int phoneNumber, FeatureHistory featureHistory) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.featureHistory = featureHistory;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String username) {
        this.phoneNumber = phoneNumber;
    }

    public FeatureHistory getFeatureHistory() {
        return featureHistory;
    }

    public void setFeatureHistory(FeatureHistory featureHistory) {
        this.featureHistory = featureHistory;
    }
}
