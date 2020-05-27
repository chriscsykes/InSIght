package edu.dartmouth.cs.finalproject.activities.data.model;

public class User {

    private String userID;
    private String username;
    private int phoneNumber;
    private int textToSpeechCount;
    private int imageRecognitionCount;

    public User() {

    }

    public User(String userID, String username, int phoneNumber, int textToSpeechCount, int imageRecognitionCount) {
        this.userID = userID;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.imageRecognitionCount = imageRecognitionCount;
        this.textToSpeechCount = textToSpeechCount;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public int getImageRecognitionCount() {
        return imageRecognitionCount;
    }

    public void setImageRecognitionCount(int imageRecognitionCount) {
        this.imageRecognitionCount = imageRecognitionCount;
    }

    public int getTextToSpeechCount() {
        return textToSpeechCount;
    }

    public void setTextToSpeechCount(int textToSpeechCount) {
        this.textToSpeechCount = textToSpeechCount;
    }
}
