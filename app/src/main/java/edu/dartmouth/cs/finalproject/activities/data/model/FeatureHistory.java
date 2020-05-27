package edu.dartmouth.cs.finalproject.activities.data.model;

public class FeatureHistory {
    private int textToSpeechCount;
    private int imageRecognitionCount;

    public FeatureHistory(int textToSpeechCount, int imageRecognitionCount) {
        this.imageRecognitionCount = imageRecognitionCount;
        this.textToSpeechCount = textToSpeechCount;
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
