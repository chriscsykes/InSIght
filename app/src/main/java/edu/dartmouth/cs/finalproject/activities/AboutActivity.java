package edu.dartmouth.cs.finalproject.activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;

import edu.dartmouth.cs.finalproject.R;
import edu.dartmouth.cs.finalproject.activities.audio.TextToSpeechEngine;

public class AboutActivity extends AppCompatActivity {

    private TextToSpeechEngine mTextToSpeechEngine;
    private TextToSpeechDriver mTextToSpeechDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.about_insight_title);


        mTextToSpeechDriver = new TextToSpeechDriver(this);
        mTextToSpeechEngine = new TextToSpeechEngine(this);

        // mTextToSpeechEngine.speakText("Touch screen to learn more about insight", Constants.aboutInsightId);

    }

    // allows user to press back arrow to go back to MainActivity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
//            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
