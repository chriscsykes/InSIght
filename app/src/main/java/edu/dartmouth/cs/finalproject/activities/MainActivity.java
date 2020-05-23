package edu.dartmouth.cs.finalproject.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;


import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import edu.dartmouth.cs.finalproject.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ImageCapture imageCapture;
    private Camera mCamera;
    private DrawerLayout drawerLayout;

    private LinearLayout linearLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
        startActivity(intent);

        checkPermissions();
        setUpCamera();
        setUpActionBar();
        drawerLayout = findViewById(R.id.drawer_layout);
        linearLayout = findViewById(R.id.linear_layout);

//        SnapScrollView snapScrollView = new SnapScrollView(MainActivity.this);
//        snapScrollView.setFeatureItems();

        linearLayout = findViewById(R.id.linear_layout);

        // get device screen width
        int width = getScreenWidth(MainActivity.this);
        Log.d(TAG, "screenwidth: " + width);

        // size each button to the width of the screen
        int child_count = linearLayout.getChildCount();
        Log.d(TAG, "Child count: " + child_count);
        for (int i=0; i<child_count; i++) {
            Button button = (Button) linearLayout.getChildAt(i);
            button.setWidth(width);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_items, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_help){
            NavigationView navigationView = findViewById(R.id.navigation);
            drawerLayout.openDrawer(navigationView);
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * helper to set up action bar
     */
    private void setUpActionBar() {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
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





//    public class SnapScrollView extends HorizontalScrollView {
//
//        private static final int SWIPE_MIN_DISTANCE = 5;
//        private static final int SWIPE_THRESHOLD_VELOCITY = 300;
//        private static final String TAG = "SnapScrollView";
//
//        private ArrayList<Button> mItems = null;
//        private GestureDetector mGestureDetector;
//        private int mActiveFeature = 0;
//        LinearLayout linearLayout;
//
//
//        public SnapScrollView(Context context, AttributeSet attrs, int defStyle) {
//            super(context, attrs, defStyle);
//        }
//
//        public SnapScrollView(Context context, AttributeSet attrs) {
//            super(context, attrs);
//        }
//
//        public SnapScrollView(Context context) {
//            super(context);
//        }
//
//        public void setFeatureItems() {
//
//            linearLayout = findViewById(R.id.linear_layout);
//
//            // get device screen width
//            int width = getScreenWidth(MainActivity.this);
//            Log.d(TAG, "screenwidth: " + width);
//
//            // size each button to the width of the screen
//            int child_count = linearLayout.getChildCount();
//            Log.d(TAG, "Child count: " + child_count);
//            for (int i = 0; i < child_count; i++) {
//                Button button = (Button) linearLayout.getChildAt(i);
//                button.setWidth(width);
//            }
//
//            setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    //If the user swipes
//                    if (mGestureDetector.onTouchEvent(event)) {
//                        return true;
//                    } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
//                        int scrollX = getScrollX();
//                        int featureWidth = v.getMeasuredWidth();
//                        mActiveFeature = ((scrollX + (featureWidth / 2)) / featureWidth);
//                        int scrollTo = mActiveFeature * featureWidth;
//                        smoothScrollTo(scrollTo, 0);
//                        return true;
//                    } else {
//                        return false;
//                    }
//                }
//            });
//
//            mGestureDetector = new GestureDetector(new MyGestureDetector());
//
//        }
//
//
//        class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
//            @Override
//            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                try {
//                    //right to left
//                    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                        int featureWidth = getMeasuredWidth();
//                        mActiveFeature = (mActiveFeature < (mItems.size() - 1)) ? mActiveFeature + 1 : mItems.size() - 1;
//                        smoothScrollTo(mActiveFeature * featureWidth, 0);
//                        return true;
//                    }
//                    //left to right
//                    else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                        int featureWidth = getMeasuredWidth();
//                        mActiveFeature = (mActiveFeature > 0) ? mActiveFeature - 1 : 0;
//                        smoothScrollTo(mActiveFeature * featureWidth, 0);
//                        return true;
//                    }
//                } catch (Exception e) {
//                    Log.e("Fling", "There was an error processing the Fling event:" + e.getMessage());
//                }
//                return false;
//            }
//        }
//
//    }

}
