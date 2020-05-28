package edu.dartmouth.cs.finalproject.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;

import java.util.Locale;

import edu.dartmouth.cs.finalproject.R;
import edu.dartmouth.cs.finalproject.activities.audio.TextToSpeechEngine;

public class IntroActivity extends AppIntro implements TextToSpeech.OnInitListener {
    private static final String TAG = IntroActivity.class.getName();
    private TextToSpeechEngine mTextToSpeechEngine;
    private int currentPage  = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialise text-to-speech engine with custom onInitListener
        mTextToSpeechEngine = new TextToSpeechEngine(IntroActivity.this, this);


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
                R.drawable.facial_recognition_logo_2,
                ContextCompat.getColor(getApplicationContext(), R.color.slide),
                Color.BLACK,
                Color.BLACK));
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            mTextToSpeechEngine.setLanguage(Locale.UK);
            handlePageSelected(currentPage);
            Log.d(TAG, "onInit: okay");
        }
    }

    @Override
    protected void onPageSelected(int position) {
        handlePageSelected(position);
        super.onPageSelected(position);
    }

    private void handlePageSelected(int position) {
        switch (position) {
            case 0:
                mTextToSpeechEngine.speakText(getString(R.string.welcome_slide) + ". " +
                        getString(R.string.welcome_string), "DEFAULT");
                currentPage = 0;
                break;
            case 1:
                mTextToSpeechEngine.speakText(getString(R.string.text_to_speech_slide) + ". " +
                        getString(R.string.text_to_speech_string), "DEFAULT");
                currentPage = 1;
                break;
            case 2:
                mTextToSpeechEngine.speakText(getString(R.string.barcode_scanner_slide) + ". " +
                        getString(R.string.barcode_scanner_string), "DEFAULT");
                currentPage = 2;
                break;
            case 3:
                mTextToSpeechEngine.speakText(getString(R.string.image_recognition_slide) + ". " +
                        getString(R.string.image_recognition_string), "DEFAULT");
                currentPage = 3;
                break;
        }
        Log.d(TAG, "handlePageSelected: " + position);
    }

    @Override
    protected void onSkipPressed(Fragment currentFragment) {
        mTextToSpeechEngine.speakText(getString(R.string.skip_slide), "DEFAULT");
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    protected void onDonePressed(Fragment currentFragment) {
        mTextToSpeechEngine.speakText(getString(R.string.done), "DEFAULT");
        super.onDonePressed(currentFragment);
        finish();
    }

    @Override
    protected void onNextPressed(Fragment currentFragment) {
        mTextToSpeechEngine.speakText(getString(R.string.next_slide), "DEFAULT");
        super.onNextPressed(currentFragment);
    }

    @Override
    protected void onDestroy() {
        mTextToSpeechEngine.closeTextToSpeechEngine();
        super.onDestroy();
    }


}
