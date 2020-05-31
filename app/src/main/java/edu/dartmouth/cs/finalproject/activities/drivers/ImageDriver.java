package edu.dartmouth.cs.finalproject.activities.drivers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceImageLabelerOptions;

import java.util.List;

import edu.dartmouth.cs.finalproject.activities.Constants;
import edu.dartmouth.cs.finalproject.activities.audio.TextToSpeechEngine;

public class ImageDriver {
    private static final String TAG = TextToSpeechDriver.class.getName();
    private TextToSpeechEngine mTextToSpeechEngine;

    public ImageDriver(Context context) {
        mTextToSpeechEngine = new TextToSpeechEngine(context);
    }

    /*
     * Provides labels for objects in an image using OnDeviceImageLabeler
     * If Recognition is successful, we provide the recognized as audio
     * Else, we log the error for now
     * imageProxy.getImage() is marked as experimental hence the annotation usage below
     */
    @androidx.camera.core.ExperimentalGetImage
    @SuppressLint("UnsafeExperimentalUsageError")
    public void labelImages(ImageProxy imageProxy, int degrees) {
        if (imageProxy == null) {
            Log.d(TAG, "imageProxy: null reference");
            return;
        }

        Image mediaImage = imageProxy.getImage();

        if (mediaImage == null) {
            Log.d(TAG, "imageProxy.getImage(): null reference returned");
            return;
        }
        // Grab image rotation
        int rotation = degreesToFirebaseRotation(degrees);

        // set label detector options
        FirebaseVisionOnDeviceImageLabelerOptions options =
                new FirebaseVisionOnDeviceImageLabelerOptions.Builder()
                        .setConfidenceThreshold(0.8f)
                        .build();

        // Create a FirebaseVisionImage object from image
        FirebaseVisionImage image = FirebaseVisionImage.fromMediaImage(mediaImage, rotation);

        // Obtain an instance of FirebaseVisionImageLabeler
        FirebaseVisionImageLabeler detector = FirebaseVision.getInstance()
                .getOnDeviceImageLabeler(options);


        // start detector, passing the image to the processImage method
        Task<List<FirebaseVisionImageLabel>> result =
                detector.processImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                                        // Task completed successfully
                                        if (labels.isEmpty()) {
                                            // Very rare but if image is without labels, prompt the user
                                            mTextToSpeechEngine.speakText("No labels could be detected. " +
                                                    "Try retaking the image", Constants.detectedLabelId);
                                        } else {
                                            // get each label
                                            String text = "";
                                            for (FirebaseVisionImageLabel label : labels) {
                                                text += label.getText() + " ";
                                            }
                                            // we can edit this
                                            String audio = "This image contains the following items. " + text;
                                            Log.d(TAG, "onSuccess: " + audio);
                                            mTextToSpeechEngine.speakText(audio, Constants.detectedLabelId);
                                        }
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        Log.d(TAG, "onFailure: Image processing failed " + e.getMessage());
                                        mTextToSpeechEngine.closeTextToSpeechEngine();
                                    }
                                });
    }

    /*
     * Provides labels for objects in an image using OnCloudImageLabeler
     * If Recognition is successful, we provide the recognized as audio
     * Else, we log the error for now
     * imageProxy.getImage() is marked as experimental hence the annotation usage below
     */
    @androidx.camera.core.ExperimentalGetImage
    @SuppressLint("UnsafeExperimentalUsageError")
    private void labelImagesCloud(ImageProxy imageProxy, int degrees) {
        if (imageProxy == null) {
            Log.d(TAG, "imageProxy: null reference");
            return;
        }

        Image mediaImage = imageProxy.getImage();

        if (mediaImage == null) {
            Log.d(TAG, "imageProxy.getImage(): null reference returned");
            return;
        }
        // Grab image rotation
        int rotation = degreesToFirebaseRotation(degrees);

        // required: without this line you cant use <image> from <imageProxy>
        /**
         * issue with cameraX
         * {@link 'https://issuetracker.google.com/issues/153249512'
         */
        mediaImage.getPlanes()[0].getBuffer().rewind();

        // set label detector options
        FirebaseVisionCloudImageLabelerOptions options = new FirebaseVisionCloudImageLabelerOptions.Builder()
                .setConfidenceThreshold(0.7f) // set the minimum confidence required:
                .build();

        // Create a FirebaseVisionImage object from image
        FirebaseVisionImage image = FirebaseVisionImage.fromMediaImage(mediaImage, rotation);

        // Obtain an instance of FirebaseVisionImageLabeler
        FirebaseVisionImageLabeler detector = FirebaseVision.getInstance()
                .getCloudImageLabeler(options);


        // start detector, passing the image to the processImage method
        Task<List<FirebaseVisionImageLabel>> result =
                detector.processImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                                        // Task completed successfully
                                        if (labels.isEmpty()) {
                                            // Very rare but if image is without labels, prompt the user
                                            mTextToSpeechEngine.speakText("No labels could be detected. " +
                                                    "Try retaking the image", Constants.detectedLabelId);
                                        } else {
                                            // get each label
                                            StringBuilder text = new StringBuilder();
                                            for (FirebaseVisionImageLabel label : labels) {
                                                text.append(label.getText()).append(" ");
                                            }
                                            String audio = "This image contains the following items. " + text;
                                            mTextToSpeechEngine.speakText(audio, Constants.detectedLabelId);
                                        }
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        Log.d(TAG, "onFailure: Image processing failed " + e.getMessage());
                                        mTextToSpeechEngine.closeTextToSpeechEngine();
                                    }
                                });
    }

    /*
     * helper method to convert camera rotation to Firebase Rotation
     */
    private int degreesToFirebaseRotation(int degrees) {
        switch (degrees) {
            case 0:
                return FirebaseVisionImageMetadata.ROTATION_0;
            case 90:
                return FirebaseVisionImageMetadata.ROTATION_90;
            case 180:
                return FirebaseVisionImageMetadata.ROTATION_180;
            case 270:
                return FirebaseVisionImageMetadata.ROTATION_270;
            default:
                throw new IllegalArgumentException(
                        "Rotation must be 0, 90, 180, or 270.");
        }
    }

    public TextToSpeechEngine getTextToSpeechEngine() {
        return mTextToSpeechEngine;
    }
}
