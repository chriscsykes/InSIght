package edu.dartmouth.cs.finalproject.activities;

import android.content.Intent;
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
import edu.dartmouth.cs.finalproject.activities.constants.Constants;

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

        // slide 3: image recognition
        addSlide(AppIntroFragment.newInstance(
                getString(R.string.image_recognition_slide),
                getString(R.string.image_recognition_string),
                R.drawable.image_recognition_logo,
                ContextCompat.getColor(getApplicationContext(), R.color.slide),
                Color.BLACK,
                Color.BLACK));

//        // slide 4: image recognition
//        addSlide(AppIntroFragment.newInstance(
//                getString(R.string.image_recognition_slide),
//                getString(R.string.image_recognition_string),
//                R.drawable.facial_recognition_logo_2,
//                ContextCompat.getColor(getApplicationContext(), R.color.slide),
//                Color.BLACK,
//                Color.BLACK));

        // Slide with basic instructions on how to use the app
        addSlide(AppIntroFragment.newInstance(getString(R.string.instructions_slide),
                getString(R.string.instructions_string),
                R.drawable.instructions_logo,
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
                        getString(R.string.welcome_string) + getString(R.string.swipe_left_instruction), "DEFAULT");
                currentPage = 0;
                break;
            case 1:
                mTextToSpeechEngine.speakText(getString(R.string.text_to_speech_slide) + ". " +
                        getString(R.string.text_to_speech_string) + getString(R.string.swipe_left_instruction), "DEFAULT");
                currentPage = 1;
                break;
            case 2:
                mTextToSpeechEngine.speakText(getString(R.string.image_recognition_slide) + ". " +
                        getString(R.string.image_recognition_string) + getString(R.string.swipe_left_instruction), "DEFAULT");
                currentPage = 2;
                break;
            case 3:
                mTextToSpeechEngine.speakText(getString(R.string.instructions_slide) + ". " +
                        getString(R.string.instructions_string) + getString(R.string.press_done_instruction), "DEFAULT");
                currentPage = 3;
                break;
        }
        Log.d(TAG, "handlePageSelected: " + position);
    }

    @Override
    protected void onSkipPressed(Fragment currentFragment) {
        mTextToSpeechEngine.speakText(getString(R.string.skip_slide), "DEFAULT");
        Intent intent = new Intent(this, MainActivity.class);

        String userName = getIntent().getStringExtra(Constants.USERNAME);
        intent.putExtra(Constants.USERNAME, userName);
        startActivity(intent);
        finish();
        super.onSkipPressed(currentFragment);
    }

    @Override
    protected void onDonePressed(Fragment currentFragment) {
        mTextToSpeechEngine.speakText(getString(R.string.done), "DEFAULT");
        Intent intent = new Intent(this, MainActivity.class);

        String userName = getIntent().getStringExtra(Constants.USERNAME);
        intent.putExtra(Constants.USERNAME, userName);
        startActivity(intent);
        finish();
        super.onDonePressed(currentFragment);
    }

    @Override
    protected void onNextPressed(Fragment currentFragment) {
        mTextToSpeechEngine.speakText(getString(R.string.next_slide), "DEFAULT");
        super.onNextPressed(currentFragment);
    }

    @Override
    protected void onDestroy() {
        if (mTextToSpeechEngine != null){
            mTextToSpeechEngine.closeTextToSpeechEngine();
            Log.d(TAG, "onPause");
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (mTextToSpeechEngine != null){
            mTextToSpeechEngine.closeTextToSpeechEngine();
            mTextToSpeechEngine = null;
            Log.d(TAG, "onPause");
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mTextToSpeechEngine == null){
            mTextToSpeechEngine = new TextToSpeechEngine(this, this);
            mTextToSpeechEngine.setLanguage(Locale.UK);
        }
        Log.d(TAG, "onResume");
        super.onResume();
    }

}
