package edu.dartmouth.cs.finalproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import edu.dartmouth.cs.finalproject.R;
import edu.dartmouth.cs.finalproject.activities.audio.TextToSpeechEngine;

public class RequestCallActivity extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;
    private EditText mEditTextNum;

    private TextToSpeechEngine mTextToSpeechEngine;
    private TextToSpeechDriver mTextToSpeechDriver;

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

        mTextToSpeechDriver = new TextToSpeechDriver(this);
        mTextToSpeechEngine = new TextToSpeechEngine(this);

        // get references to EditText and ImageView
        mEditTextNum = findViewById(R.id.edit_text_number);
        ImageView imageCall = findViewById(R.id.image_call);

        // when the phone image is pressed
        imageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
    }

    private void makePhoneCall() {
        String numToCall = mEditTextNum.getText().toString();

        // make sure a number was entered
        if (numToCall.trim().length() > 0) {
            // check for permission
            if (ContextCompat.checkSelfPermission(RequestCallActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(RequestCallActivity.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            }
            else {
                // make the actual phone call
                String dial = "tel:" + numToCall;
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(dial));
                startActivity(intent);
            }
        }
        else {
            Toast.makeText(RequestCallActivity.this, R.string.enter_phone_number, Toast.LENGTH_SHORT).show();
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
//            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
