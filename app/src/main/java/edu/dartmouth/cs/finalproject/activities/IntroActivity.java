package edu.dartmouth.cs.finalproject.activities;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;

import edu.dartmouth.cs.finalproject.R;

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // slide 1: welcome
        addSlide(AppIntroFragment.newInstance(
                getString(R.string.welcome_slide),
                getString(R.string.welcome_string),
                R.drawable.app_logo_2,
                ContextCompat.getColor(getApplicationContext(), R.color.slide),
                Color.BLACK,
                Color.BLACK));

        // slide 2: text-to-speech
        addSlide(AppIntroFragment.newInstance(
                getString(R.string.text_to_speech_slide),
                getString(R.string.text_to_speech_string),
                R.drawable.text_to_speech,
                ContextCompat.getColor(getApplicationContext(), R.color.slide),
                Color.BLACK,
                Color.BLACK));

        // slide 3: barcode scanner
        addSlide(AppIntroFragment.newInstance(
                getString(R.string.barcode_scanner_slide),
                getString(R.string.barcode_scanner_string),
                R.drawable.barcode_scanner_logo,
                ContextCompat.getColor(getApplicationContext(), R.color.slide),
                Color.BLACK,
                Color.BLACK));

        // slide 4: image recognition
        addSlide(AppIntroFragment.newInstance(
                getString(R.string.image_recognition_slide),
                getString(R.string.image_recognition_string),
                R.drawable.image_recognition_logo,
                ContextCompat.getColor(getApplicationContext(), R.color.slide),
                Color.BLACK,
                Color.BLACK));
    }

    @Override
    protected void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    protected void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }
}
