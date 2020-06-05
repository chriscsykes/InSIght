package edu.dartmouth.cs.finalproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

import edu.dartmouth.cs.finalproject.R;
import edu.dartmouth.cs.finalproject.activities.audio.TextToSpeechEngine;
import edu.dartmouth.cs.finalproject.activities.constants.Constants;

public class AboutActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final String TAG = "AboutActivity";
    private TextToSpeechEngine mTextToSpeechEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.about_insight_title);

        Log.d(TAG, "onCreate()");
    }

    // allows user to press back arrow to go back to MainActivity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            mTextToSpeechEngine.speakText("Back", "DEFAULT");
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onInit(int status) {
        mTextToSpeechEngine.setLanguage(Locale.UK);
        readAboutInsight();
        Log.d(TAG, "onInit: okay");
    }

    /*
     * Provides audio feedback to the user about Insight App
     */
    private void readAboutInsight() {
        String aboutMessage = getDescriptions();
        mTextToSpeechEngine.speakText(aboutMessage, Constants.aboutInsightId);
    }

    /*
     * grabs references for the different sections of the description
     */
    private String getDescriptions() {
        return getString(R.string.about_insight) + " \n "
                + getString(R.string.creators_paragraph) + " \n "
                + getString(R.string.about_paragraph) + " \n "
                + getString(R.string.features_paragraph) + " \n"
                + getString(R.string.contact_info_paragraph) + "\n "
                + getString(R.string.credits_paragraph);
    }

    @Override
    protected void onPause() {
        if (mTextToSpeechEngine != null) {
            mTextToSpeechEngine.closeTextToSpeechEngine();
            Log.d(TAG, "onPause");
        }
        super.onPause();
    }


    @Override
    protected void onResume() {
        mTextToSpeechEngine = new TextToSpeechEngine(this, this);
        mTextToSpeechEngine.setLanguage(Locale.UK);
        readAboutInsight();
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mTextToSpeechEngine != null) {
            mTextToSpeechEngine.closeTextToSpeechEngine();
            Log.d(TAG, "onDestroy");
        }
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        if (intent != null) {
            String source = intent.getStringExtra(Constants.SOURCE);
            if (source != null && source.equals(Constants.MAIN_ACTIVITY)) {
                startActivity(new Intent(AboutActivity.this, MainActivity.class));
            }
        }
        super.onBackPressed();
    }
}
