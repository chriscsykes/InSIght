package edu.dartmouth.cs.finalproject.activities.ui.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

import edu.dartmouth.cs.finalproject.R;
import edu.dartmouth.cs.finalproject.activities.IntroActivity;
import edu.dartmouth.cs.finalproject.activities.audio.TextToSpeechEngine;
import edu.dartmouth.cs.finalproject.activities.constants.Constants;

//used code from smartHerd's SpeechToText Github!
////https://github.com/smartherd/SpeechToText.git

public class LoginActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private static final String TAG = LoginActivity.class.getName();
    private TextToSpeechEngine textToSpeechEngine;
    private String mName;
    private TextView mMessage;
    private ProgressBar mLoadingProgressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textToSpeechEngine= new TextToSpeechEngine(this,  this);
        setContentView(R.layout.activity_login);

        mMessage = (TextView)findViewById(R.id.textView2);

        mLoadingProgressBar = findViewById(R.id.loading);
        mLoadingProgressBar.setVisibility(View.GONE);
    }


    private void startDialogue() {
        textToSpeechEngine.speakText("Welcome to Insight. Please tell us" +
                " your first name", Constants.loginIntroductionId);
    }

    private void loginSuccess() {
        textToSpeechEngine.speakText("Welcome "+ mName, "loginSuccess");
        mLoadingProgressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(this::startOnBoarder, 3000);   //3 seconds


    }

    @Override
    protected void onStart() {
        super.onStart();
    }



    @Override
    protected void onDestroy() {
        textToSpeechEngine.closeTextToSpeechEngine();
        super.onDestroy();
    }

    private void startOnBoarder() {
        Intent intent = new Intent(this, IntroActivity.class);
        intent.putExtra(Constants.USERNAME, mName);
        startActivity(intent);
        finish();
        //Can we also close the textToSpeech engine at this point?
        //textToSpeechEngine.closeTextToSpeechEngine();
    }

    // Create an intent that can start the Speech Recognizer activity
    public void beginSpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
            textToSpeechEngine.speakText("Your Device Don't Support Speech Input", "speech_not_supported");
        }
    }


    // This callback is invoked when the Speech Recognizer returns.
    // This is where you process the intent and extract the speech text from the intent.
    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                assert result != null;
                mMessage.setText("Welcome " + result.get(0));
                mName = result.get(0);
                loginSuccess();
            } else {
                textToSpeechEngine.speakText("I am sorry. I could not quite hear that. Can you repeat your name for me?", "name_second_try");
                beginSpeechRecognizer();
            }
        }
    }

    @Override
    public void onInit(int status) {
        if (status != TextToSpeech.ERROR){
            startDialogue();
            textToSpeechEngine.getTextToSpeech().setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                }

                @Override
                public void onDone(String utteranceId) {
                   if (utteranceId.equals(Constants.loginIntroductionId)){
                       beginSpeechRecognizer();
                   }
                }

                @Override
                public void onError(String utteranceId) {

                }
            });
        }
    }
}
