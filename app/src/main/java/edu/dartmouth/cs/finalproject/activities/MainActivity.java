package edu.dartmouth.cs.finalproject.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

import edu.dartmouth.cs.finalproject.R;
import edu.dartmouth.cs.finalproject.activities.audio.TextToSpeechEngine;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ImageCapture imageCapture;
    private Camera mCamera;
    private DrawerLayout drawerLayout;

    private LinearLayout linearLayout;
    private TextToSpeechEngine mTextToSpeechEngine;
    private NavigationView navigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        launchOnBoarder();
        checkPermissions();
        setUpCamera();
        setUpActionBar();

        drawerLayout = findViewById(R.id.drawer_layout);
        linearLayout = findViewById(R.id.linear_layout);
        navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(item -> handleNavigationItemSelected(item));
        mTextToSpeechEngine = new TextToSpeechEngine(this);


        linearLayout = findViewById(R.id.linear_layout);

        // get device screen width
        int width = getScreenWidth(MainActivity.this);
        Log.d(TAG, "screenwidth: " + width);

        // size each button to the width of the screen
        int child_count = linearLayout.getChildCount();
        Log.d(TAG, "Child count: " + child_count);
        for (int i = 0; i < child_count; i++) {
            Button button = (Button) linearLayout.getChildAt(i);
            button.setWidth(width);
        }
    }

    private boolean handleNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_tutorials:
                mTextToSpeechEngine.speakText("Read Tutorials", Constants.readTutorialsId);
                readTutorials();
                break;
            case R.id.nav_feedback:
                mTextToSpeechEngine.speakText("Provide feedback", Constants.feedBackId);
                provideFeedBack();
                break;
            case R.id.nav_request_call:
                mTextToSpeechEngine.speakText("Request a call", Constants.requestCallId);
                requestCall();
                break;
            case R.id.nav_about_insight:
                mTextToSpeechEngine.speakText("About Insight", Constants.aboutInsightId);
                learnAboutInsight();
                break;
            case R.id.nav_share_with_friends:
                mTextToSpeechEngine.speakText("Share with Friends", Constants.shareWithFriendsId);
                shareWithFriends();
                break;
            default:
                return false;
        }
        return true;

    }

    /*
     * Allows the user to send feedback to developers
     */
    private void provideFeedBack() {
    }

    /*
     * Allows the user to share the app with friends
     */
    private void shareWithFriends() {
    }

    /*
     * Sends user to Insight homePage
     */
    private void learnAboutInsight() {
    }

    /*
     * Allows the user to make a call to request for assistance
     */
    private void requestCall() {
    }

    /*
     * Probably takes User them to website tutorials/ onBoarder Screen
     */
    private void readTutorials() {
    }

    /*
     * Determines if this is the app's first installation before launching onBoarder
     * else we take note of the app's installation using shared Prefs
     */
    private void launchOnBoarder() {
        SharedPreferences prefs = this.getPreferences(MODE_PRIVATE);
        if (!prefs.getBoolean(Constants.firstTime, false)) {
            // we should run our one time code
            Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
            startActivity(intent);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(Constants.firstTime, true);
            editor.apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_items, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_help) {
            NavigationView navigationView = findViewById(R.id.navigation);
            drawerLayout.openDrawer(navigationView);
            mTextToSpeechEngine.speakText("help", Constants.helpId);
        }else{
            // No other action button in action bar
            mTextToSpeechEngine.speakText("Insight App Logo", Constants.helpId);
        }



        Log.d(TAG, "onOptionsItemSelected: " + item.getItemId());


        return super.onOptionsItemSelected(item);
    }

    /*
     * helper to set up action bar
     */
    private void setUpActionBar() {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // Remove default title text
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayShowTitleEnabled(false);

    }


    /*
     * Helper method to setUp CameraX in preview mode and capture mode
     * Preview mode allows the user to view preview of camera
     * Capture mode allows the user to take a picture
     */
    private void setUpCamera() {
        // If Permissions denied simply return
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)) {
            return;

        }
        PreviewView previewView = findViewById(R.id.preview_view);

        ListenableFuture cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                // Camera provider is now guaranteed to be available
                ProcessCameraProvider cameraProvider = (ProcessCameraProvider) cameraProviderFuture.get();

                // Set up the view finder use case to display camera preview
                Preview preview = new Preview.Builder().build();

                // Set up the capture use case to allow users to take photos
                imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build();

                // Choose the camera by requiring a lens facing
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                // Attach use cases to the camera with the same lifecycle owner
                mCamera = cameraProvider.bindToLifecycle(
                        (this),
                        cameraSelector,
                        preview,
                        imageCapture);

                // Connect the preview use case to the previewView
                preview.setSurfaceProvider(
                        previewView.createSurfaceProvider(mCamera.getCameraInfo()));
            } catch (InterruptedException | ExecutionException e) {
                // Currently no exceptions thrown. cameraProviderFuture.get() should
                // not block since the listener is being called, so no need to
                // handle InterruptedException.
            }
        }, ContextCompat.getMainExecutor(this));
    }

    /*
     * Check if the user already granted permissions
     * Proceed if already granted else request for permissions
     * Permissions Requested: CAMERA and EXTERNAL STORAGE
     */
    private void checkPermissions() {
        // if permissions are not granted for camera, and external storage, request for them
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.
                    requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            Constants.REQUEST_CODE_CAMERA_PERMISSION);

        }
    }

    /*
     * Called when the user responds to permission request
     * On first denial, we show rationale dialog, and offer another chance
     * Proceed to request again if rationale is considered, else don't request again
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.REQUEST_CODE_CAMERA_PERMISSION) {
            // if permissions are granted, launch camera
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                // permission was granted, yay!

                setUpCamera();
                // set Boolean permission RESULT = true;
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // permission denied, show rationale
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
                ) {
                    AlertDialog.Builder rationale = new AlertDialog.Builder(this);
                    rationale.setMessage("Without the camera the app can't function. Do you grant permission?")
                            .setTitle("Camera Access");

                    rationale.setPositiveButton("OK", (dialog, which) -> ActivityCompat.
                            requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    Constants.REQUEST_CODE_CAMERA_PERMISSION));

                    rationale.setNegativeButton("NO", (dialog, which) -> {
                        dialog.dismiss();
                        MainActivity.this.finish();
                    });
                    rationale.show();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /*
     * Helper function that gets the screen with in order to properly size each button
     */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /*
     * Driver methods for Text-to-Speech Feature
     */
    public void handleTextRecognition(View view) {
        mTextToSpeechEngine.speakText("Short Text", Constants.shortTextId);
    }

    /*
     * Driver methods for Text-to-Speech Feature
     */
    public void handleBarCodeRecognition(View view) {
        mTextToSpeechEngine.speakText("Bar Code", Constants.barCodeId);
    }

    /*
     * Driver methods for Image Recognition feature Feature
     */
    public void handleImageRecognition(View view) {
        mTextToSpeechEngine.speakText("Image", Constants.imageId);
    }

    /*
     * Driver methods for Currency Recognition Feature
     */
    public void handleCurrencyRecognition(View view) {
        mTextToSpeechEngine.speakText("Currency", Constants.currencyId);
    }

    /*
     * Driver methods for Color recognition Feature
     */
    public void handleColorRecognition(View view) {
        mTextToSpeechEngine.speakText("Color", Constants.colorId);
    }
}


