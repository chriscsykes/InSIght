package edu.dartmouth.cs.finalproject.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Locale;

import edu.dartmouth.cs.finalproject.R;
import edu.dartmouth.cs.finalproject.activities.audio.TextToSpeechEngine;
import edu.dartmouth.cs.finalproject.activities.constants.Constants;

public class RequestCallActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final int REQUEST_CALL = 1;
    private static final String TAG = ReadTutorialsActivity.class.getName();
    private EditText mEditTextNum;
    private static final String NUM_TO_CALL = "8089839872";

    private TextToSpeechEngine mTextToSpeechEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_call);

        // set up action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Request a Call");

        mTextToSpeechEngine = new TextToSpeechEngine(this, this);

        // get references to EditText and ImageView
        mEditTextNum = findViewById(R.id.edit_text_number);
        ImageView imageCall = findViewById(R.id.image_call);

        // set the predetermined phone number to call for help
        mEditTextNum.setText(NUM_TO_CALL);

        // when the phone image is pressed
        imageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
    }

    // checks for permission and sets up call
    private void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(RequestCallActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RequestCallActivity.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }
        else {
            // make the actual phone call
            String dial = "tel:" + NUM_TO_CALL;
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(dial));
            startActivity(intent);
        }
    }

    // will make the phone call if permission is granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            }
            else {
                Toast.makeText(this, R.string.phone_call_permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // allows user to press back arrow to go back to MainActivity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            mTextToSpeechEngine.setLanguage(Locale.UK);
            readCallGuide();
            Log.d(TAG, "onInit: okay");
        }
    }

    /*
     * informs the user of the call button in the middle of the screen
     */
    private void readCallGuide() {
       if ( mTextToSpeechEngine.getTextToSpeech().isSpeaking()){
           Log.d(TAG, "readCallGuide: is speaking");
           mTextToSpeechEngine.speakText(getString(R.string.call_guide), "DEFAULT", TextToSpeech.QUEUE_ADD);
        }
       else{
           mTextToSpeechEngine.speakText(getString(R.string.call_guide), "DEFAULT");
       }
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        if (intent != null){
            String source = intent.getStringExtra(Constants.SOURCE);
            if (source != null && source.equals(Constants.MAIN_ACTIVITY)){
                startActivity(new Intent(RequestCallActivity.this, MainActivity.class));
            }
        }
        super.onBackPressed();
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
        readCallGuide();
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
}

