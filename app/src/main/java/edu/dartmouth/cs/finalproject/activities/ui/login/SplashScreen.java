package edu.dartmouth.cs.finalproject.activities.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import edu.dartmouth.cs.finalproject.R;
import edu.dartmouth.cs.finalproject.activities.MainActivity;
import edu.dartmouth.cs.finalproject.utils.Preference;

public class SplashScreen extends AppCompatActivity {
    private static final int TIME_ELAPSED = 3000;

    // Animation variables
    Animation topAnim, bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // FIRST check SharedPrefs to see if user has logged out or not so we know whether or not to prompt signup/login...
        Preference preference = new Preference(this);

        // if user hasn't logged out, take them to main activity
        if (preference.getLoginStatus()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_splash_screen);
        createFullScreen();
        startAnimation();

        startLoginActivity();
    }

    /*
     * After a couple of seconds launches the login Activity
     */
    private void startLoginActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent  = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, TIME_ELAPSED);
    }

    /* for animation */
    private void startAnimation() {
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        TextView mAppName = findViewById(R.id.app_name);
        ImageView mLoginLogo = findViewById(R.id.login_image);
        TextView mSlogan = findViewById(R.id.slogan);

        mLoginLogo.setAnimation(topAnim);
        mAppName.setAnimation(bottomAnim);
        mSlogan.setAnimation(bottomAnim);
    }

    /*
     * Creates a full screen, hiding app
     */
    private void createFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}