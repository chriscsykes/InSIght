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
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.Arrays;

import edu.dartmouth.cs.finalproject.activities.constants.Constants;
import edu.dartmouth.cs.finalproject.activities.audio.TextToSpeechEngine;

public class TextToSpeechDriver {
    private static final String TAG = TextToSpeechDriver.class.getName();
    private TextToSpeechEngine mTextToSpeechEngine;

    public TextToSpeechDriver(Context context) {
        mTextToSpeechEngine = new TextToSpeechEngine(context);
    }

    /*
     * Recognizes text using OnDeviceTextRecognizer
     * If Recognition is successful, we provide the recognized as audio
     * Else, we log the error for now
     * imageProxy.getImage() is marked as experimental hence the annotation usage below
     */
    @androidx.camera.core.ExperimentalGetImage
    @SuppressLint("UnsafeExperimentalUsageError")
    public void recognizeText(ImageProxy imageProxy, int degrees) {
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

        // Create a FirebaseVisionImage object from image
        FirebaseVisionImage image = FirebaseVisionImage.fromMediaImage(mediaImage, rotation);

        // Obtain an instance of FirebaseVisionTextRecognizer.
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();

        // Finally, pass the image to the processImage method:
        Task<FirebaseVisionText> result =
                detector.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                // Task completed successfully
                                /* receives full chunk of text */
                                 String detectedText = firebaseVisionText.getText();
                                 if (detectedText.isEmpty()){
                                     // image without text, prompt the user
                                     mTextToSpeechEngine.speakText("No text detected", Constants.detectedTextId);
                                 }else{
                                     mTextToSpeechEngine.speakText(detectedText, Constants.detectedTextId);
                                 }


                                /* provides audio feedback line by line: we might tweak this part a bit */
//                                for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
//                                    for (FirebaseVisionText.Line line : block.getLines()) {
//                                        String detectedText = line.getText();
//                                        mTextToSpeechEngine.speakText(detectedText, Constants.detectedTextId);
//                                    }
//                                }
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        Log.d(TAG, "onFailure: TextToSpeech processing failed " + e.getMessage());
                                        mTextToSpeechEngine.closeTextToSpeechEngine();
                                    }
                                });
    }


    /*
     * Recognizes text using OnCloudTextRecognizer
     * If Recognition is successful, we provide the recognized as audio
     * Else, we log the error for now
     * imageProxy.getImage() is marked as experimental hence the annotation usage below
     */
    @androidx.camera.core.ExperimentalGetImage
    @SuppressLint("UnsafeExperimentalUsageError")
    public void recognizeTextCloud(ImageProxy imageProxy, int degrees) {
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


        // provides language hints to assist with language detection
        FirebaseVisionCloudTextRecognizerOptions options = new FirebaseVisionCloudTextRecognizerOptions.Builder()
                .setLanguageHints(Arrays.asList("en", "hi"))
                .build();

        // Create a FirebaseVisionImage object from image
        FirebaseVisionImage image = FirebaseVisionImage.fromMediaImage(mediaImage, rotation);

        // Obtain an instance of FirebaseVisionTextRecognizer.
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                .getCloudTextRecognizer(options);

        // Finally, pass the image to the processImage method:
        Task<FirebaseVisionText> result =
                detector.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                // Task completed successfully
                                /* receives full chunk of text */
                                String detectedText = firebaseVisionText.getText();
                                if (detectedText.isEmpty()){
                                    // image without text, prompt the user
                                    mTextToSpeechEngine.speakText("No text detected", Constants.detectedTextId);
                                }else{
                                    mTextToSpeechEngine.speakText(detectedText, Constants.detectedTextId);
                                }
                                /* provides audio feedback line by line: we might tweak this part a bit */
//                                for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
//                                    for (FirebaseVisionText.Line line : block.getLines()) {
//                                        String detectedText = line.getText();
//                                        mTextToSpeechEngine.speakText(detectedText, Constants.detectedTextId);
//                                    }
//                                }
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        Log.d(TAG, "onFailure: TextToSpeech processing failed " + e.getMessage());
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
