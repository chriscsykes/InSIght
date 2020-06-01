
<<<<<<< Updated upstream
//package edu.dartmouth.cs.finalproject.activities.ui.login;
//
//import android.app.Activity;
//
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProviders;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.annotation.Nullable;
//import androidx.annotation.StringRes;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.speech.RecognizerIntent;
//import android.speech.tts.TextToSpeech;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.inputmethod.EditorInfo;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import org.w3c.dom.Text;
//
//import java.util.List;
//
//import edu.dartmouth.cs.finalproject.R;
//import edu.dartmouth.cs.finalproject.activities.TextToSpeechActivity;
//import edu.dartmouth.cs.finalproject.activities.audio.TextToSpeechEngine;
//import edu.dartmouth.cs.finalproject.activities.ui.login.LoginViewModel;
//import edu.dartmouth.cs.finalproject.activities.ui.login.LoginViewModelFactory;
//
//public class LoginActivity extends AppCompatActivity {
//
//    private LoginViewModel loginViewModel;
//    private TextToSpeechEngine textToSpeechEngine;
//    private static final int SPEECH_REQUEST_CODE = 0;
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
//                .get(LoginViewModel.class);
//        textToSpeechEngine= new TextToSpeechEngine(this);
//        textToSpeechEngine.speakText("Welcome to Insight. Please spell out" +
//                "your first name character by character", "intro");
//
//        displaySpeechRecognizer();
//
//        final TextView usernameEditText = findViewById(R.id.textView2);
//
//        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
//
//        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
//            @Override
//            public void onChanged(@Nullable LoginFormState loginFormState) {
//                if (loginFormState == null) {
//                    return;
//                }
//                if (loginFormState.getUsernameError() != null) {
//                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
//                }
//            }
//        });
//
//        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
//            @Override
//            public void onChanged(@Nullable LoginResult loginResult) {
//                if (loginResult == null) {
//                    return;
//                }
//                loadingProgressBar.setVisibility(View.GONE);
//                if (loginResult.getError() != null) {
//                    showLoginFailed(loginResult.getError());
//                }
//                if (loginResult.getSuccess() != null) {
//                    updateUiWithUser(loginResult.getSuccess());
//                }
//                setResult(Activity.RESULT_OK);
//
//                //Complete and destroy login activity once successful
//                finish();
//            }
//        });
//
////        TextWatcher afterTextChangedListener = new TextWatcher() {
////            @Override
////            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
////                // ignore
////            }
////
////            @Override
////            public void onTextChanged(CharSequence s, int start, int before, int count) {
////                // ignore
////            }
////
////            @Override
////            public void afterTextChanged(Editable s) {
////                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
////                        passwordEditText.getText().toString());
////            }
////        };
////        usernameEditText.addTextChangedListener(afterTextChangedListener);
////        passwordEditText.addTextChangedListener(afterTextChangedListener);
////        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
////
////            @Override
////            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
////                if (actionId == EditorInfo.IME_ACTION_DONE) {
////                    loginViewModel.login(usernameEditText.getText().toString(),
////                            passwordEditText.getText().toString());
////                }
////                return false;
////            }
////        });
//
////        loginButton.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                loadingProgressBar.setVisibility(View.VISIBLE);
////                loginViewModel.login(usernameEditText.getText().toString(),
////                        passwordEditText.getText().toString());
////            }
////        });
//    }
//
//    private void updateUiWithUser(LoggedInUserView model) {
//        String welcome = getString(R.string.welcome) + model.getDisplayName();
//        // TODO : initiate successful logged in experience
//        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
//    }
//
//    private void showLoginFailed(@StringRes Integer errorString) {
//        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
//    }
//
//    // Create an intent that can start the Speech Recognizer activity
//    private void displaySpeechRecognizer() {
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//// Start the activity, the intent will be populated with the speech text
//        startActivityForResult(intent, SPEECH_REQUEST_CODE);
//    }
//
//
//    // This callback is invoked when the Speech Recognizer returns.
//// This is where you process the intent and extract the speech text from the intent.
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode,
//                                    Intent data) {
//        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
//            List<String> results = data.getStringArrayListExtra(
//                    RecognizerIntent.EXTRA_RESULTS);
//            String spokenText = results.get(0);
//            Toast.makeText(this, spokenText, Toast.LENGTH_SHORT).show();
//            // Do something with spokenText
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//}
=======
import android.app.Activity;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.dartmouth.cs.finalproject.R;
import edu.dartmouth.cs.finalproject.activities.IntroActivity;
import edu.dartmouth.cs.finalproject.activities.TextToSpeechActivity;
import edu.dartmouth.cs.finalproject.activities.audio.TextToSpeechEngine;
import edu.dartmouth.cs.finalproject.activities.ui.login.LoginViewModel;
import edu.dartmouth.cs.finalproject.activities.ui.login.LoginViewModelFactory;

import static android.os.SystemClock.sleep;

//used code from smartHerd's SpeechToText Github!
//https://github.com/smartherd/SpeechToText.git

public class LoginActivity extends AppCompatActivity {


    private static final String USERNAME = "username";//key for retrieving username
    private TextToSpeechEngine textToSpeechEngine;
    private static final int SPEECH_REQUEST_CODE = 0;
    private String mName;
    private TextView mMessage;
    private ProgressBar mLoadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textToSpeechEngine= new TextToSpeechEngine(this);
        setContentView(R.layout.activity_login);

        mMessage = (TextView)findViewById(R.id.textView2);

        mLoadingProgressBar = findViewById(R.id.loading);
        mLoadingProgressBar.setVisibility(View.GONE);
        startDialogue();
        //beginSpeechRecognizer();

//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {

//                loginViewModel.login(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString());
//            }
//        });
    }

    private void startDialogue() {
        textToSpeechEngine.speakText("Welcome to Insight. Please tell us" +
                " your first name", "intro");
    }

    private void loginSuccess() {
        textToSpeechEngine.speakText("Welcome "+ mName, "loginSuccess");
        mLoadingProgressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {
             startOnBoarder();
        }, 3000);   //3 seconds


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //beginSpeechRecognizer();
    }

    @Override
    protected void onDestroy() {
        textToSpeechEngine.closeTextToSpeechEngine();
        super.onDestroy();
    }

    private void startOnBoarder() {
        Intent intent = new Intent(this, IntroActivity.class);
        intent.putExtra(USERNAME, mName);
        startActivity(intent);
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
        }
    }


    // This callback is invoked when the Speech Recognizer returns.
// This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mMessage.setText("Welcome " + result.get(0));
                    mName= result.get(0);
                    loginSuccess();
                }
                else{
                    textToSpeechEngine.speakText("I am sorry. I could not quite hear that. Can you repeat your name for me?", "name_second_try");
                    beginSpeechRecognizer();
                }
                break;
        }
    }
}
>>>>>>> Stashed changes
