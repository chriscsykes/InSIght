package edu.dartmouth.cs.finalproject.activities.ui.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Queue;

import edu.dartmouth.cs.finalproject.R;
import edu.dartmouth.cs.finalproject.activities.IntroActivity;
import edu.dartmouth.cs.finalproject.activities.MainActivity;
import edu.dartmouth.cs.finalproject.activities.audio.TextToSpeechEngine;
import edu.dartmouth.cs.finalproject.activities.constants.Constants;
import edu.dartmouth.cs.finalproject.activities.data.model.User;

//used code from smartHerd's SpeechToText Github!
////https://github.com/smartherd/SpeechToText.git

public class LoginActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private static final String TAG = "LoginActivity";
    private TextToSpeechEngine textToSpeechEngine;
    private String mName;
    private TextView mMessage;
    private ProgressBar mLoadingProgressBar;
    private DatabaseReference mDatabase;

    private boolean userExists;

    edu.dartmouth.cs.finalproject.utils.Preference preference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // FIRST check SharedPrefs to see if user has logged out or not so we know whether or not to prompt signup/login...
        preference = new edu.dartmouth.cs.finalproject.utils.Preference(this);

        // if user hasn't logged out, take them to main activity
        if (preference.getLoginStatus() == true) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            return;
        }


        textToSpeechEngine= new TextToSpeechEngine(this,  this);
        setContentView(R.layout.activity_login);

        mMessage = (TextView)findViewById(R.id.textView2);

        mLoadingProgressBar = findViewById(R.id.loading);
        mLoadingProgressBar.setVisibility(View.GONE);


        // initialize database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("user");
        // mDatabase.setValue("Hello, World!");

        // int userID = randomInt();
        // writeNewUser(String.valueOf(userID), "Test User");
    }

    private void startDialogue() {
        textToSpeechEngine.speakText("Welcome to Insight. Please spell out your first name" +
                " character by character", Constants.loginIntroductionId);
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
        finish(); // calls onDestroy which closes textToSpeechEngine
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
//    @SuppressLint("SetTextI18n")
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

                // query database with the name given
                Query query = FirebaseDatabase.getInstance().getReference("user")
                        .orderByChild("username")
                        .equalTo(mName);
                query.addListenerForSingleValueEvent(valueEventListener);

                // if user is in the database, send them to MainActivity
                if (userExists == true) {
                    textToSpeechEngine.speakText("Welcome back "+ mName, "loginSuccess");
                    mLoadingProgressBar.setVisibility(View.VISIBLE);

                    // user is now logged in
                    preference.setLoginStatus(true);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                    finish();
                }
                else {
                    // need to add the new user to the database, and sent them to the onBoarder
//                    int userID = randomInt();
                    // Generate a reference to a new location and add some data using push()
                    DatabaseReference pushedNewUserRef = mDatabase.push();
                    // Get the unique ID generated by a push()
                    String postId = pushedNewUserRef.getKey();
                    writeNewUser(String.valueOf(postId), mName);
                    loginSuccess();
                }

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

    // adds a new user to the database
    private void writeNewUser(String userID, String username) {
        User user = new User(username);
        mDatabase.child(userID)
                .setValue(user);
    }

    // generates a random integer from [1, 100000] to be used as userID
    // Trying to use firebase given key
//    private int randomInt() {
//        int random = (int) (Math.random() * 1000000 + 1);
//        return random;
//    }

    // listens for result from query and determines whether a user with that name exists
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                userExists = true;
            }
            else {
                userExists = false;
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.w(TAG, "query onCancelled", databaseError.toException());
        }
    };

}
