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

public class ReadTutorialsActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final String TAG = "ReadTutorialsActivity";
    private TextToSpeechEngine mTextToSpeechEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_tutorials);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.read_tutorials_title);
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

    /*
     * begins audio feedback once onCreate is done and TTS engine is initialised
     */
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            mTextToSpeechEngine.setLanguage(Locale.UK);
            readTutorials();
            Log.d(TAG, "onInit: okay");
        }
    }

    /*
     * Provides audio feedback to the user about app tutorials
     */
    private void readTutorials() {
        String tutorialText = getTutorialDescriptions();
        mTextToSpeechEngine.speakText(tutorialText, Constants.readTutorialsId);
    }

    /*
     * grabs references for the different sections of the description
     */
    private String getTutorialDescriptions() {
        String fullDescription = getString(R.string.tutorial_title) + "\n "
                + getString(R.string.description1) + "\n "
                + getString(R.string.description2) + "\n "
                + getString(R.string.description3) + "\n "
                + getString(R.string.description4);

        return fullDescription;
    }

    @Override
    protected void onPause() {
        if (mTextToSpeechEngine != null){
            mTextToSpeechEngine.closeTextToSpeechEngine();
            Log.d(TAG, "onPause");
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        mTextToSpeechEngine = new TextToSpeechEngine(this, this);
        mTextToSpeechEngine.setLanguage(Locale.UK);
        readTutorials();
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mTextToSpeechEngine != null){
            mTextToSpeechEngine.closeTextToSpeechEngine();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        if (intent != null){
            String source = intent.getStringExtra(Constants.SOURCE);
            if (source != null && source.equals(Constants.MAIN_ACTIVITY)){
                startActivity(new Intent(ReadTutorialsActivity.this, MainActivity.class));
            }
        }
        super.onBackPressed();
    }
}
