package com.streamliners.intentsplayground;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.streamliners.intentsplayground.databinding.ActivityIntentsPlaygroundBinding;

public class IntentsPlaygroundActivity extends AppCompatActivity {

    private static final int REQUEST_COUNT = 0;
    ActivityIntentsPlaygroundBinding b;
    private int receivedCount = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupLayout();

        setupHideErrorForEditText();

        //receiving count from saved instance
        if(savedInstanceState!=null){
            if(receivedCount!=101){
                b.result.setText(""+ receivedCount);
                b.result.setVisibility(View.VISIBLE);
            }
        }
    }

    // Saving count by saved instance
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.getInt(Constants.COUNT_VALUE, receivedCount);
    }

    /**
     * Initial setup methods
     */
    private void setupLayout() {
        b = ActivityIntentsPlaygroundBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        setTitle("Intents Playground");
    }

    /**
     * text watcher to hide error for edit text
     */
    private void setupHideErrorForEditText() {
        TextWatcher myTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hideError();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        b.data.getEditText().addTextChangedListener(myTextWatcher);
        b.initialCounterEditText.getEditText().addTextChangedListener(myTextWatcher);
    }


    //Event Handlers

    /**
     * intent to open new activity
     * @param view
     */
    public void openMainActivity(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    /**
     * send implicit intent
     * @param view
     */
    public void sendImplicitIntent(View view) {
        // Validate data input
        String input = b.data.getEditText().getText().toString().trim();
        if(input.isEmpty()){
            b.data.setError("Please Enter something!");
                    return;
        }

        //validate intent Type
        int type = b.intentTypeRGrp.getCheckedRadioButtonId();

        //Handle implicit intent cases
        if(type==R.id.openWebpageRBtn)
            openWebPage(input);
        else if(type==R.id.dialNumberRBtn)
            dialNumber(input);
        else if(type==R.id.shareTextRBtn)
            shareText(input);
        else
            Toast.makeText(this, "Please select an intent type!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Sends initial count
     * @param view
     */
    public void sendData(View view) {
        // Validate data input
        String input = b.initialCounterEditText.getEditText().getText().toString().trim();
        if (input.isEmpty()) {
            b.initialCounterEditText.setError("Please Enter something!");
            return;
        }
        //Get count
        int initialCount = Integer.parseInt(input);

        //Create intent
        Intent intent = new Intent(this,MainActivity.class);

        //Create bundle to pass
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.INITIAL_COUNT_KEY,initialCount);
        bundle.putInt(Constants.MIN_VALUE,-100);
        bundle.putInt(Constants.MAX_VALUE,100);


        //Pass bundle
        intent.putExtras(bundle);

        startActivityForResult(intent,REQUEST_COUNT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);                   // (RESULT_COUNT,RESULT_OK,intent)

        if(requestCode==REQUEST_COUNT && resultCode==RESULT_OK){
            //Get data
            receivedCount = data.getIntExtra(Constants.FINAL_COUNT, Integer.MIN_VALUE);

            //Show data
            b.result.setText("Final Count Received : " + receivedCount);
            b.result.setVisibility(View.VISIBLE);
        }
    }

    // Implicit Intent Sender

    /**
     * send intent to open webPage
     * @param url
     */
        private void openWebPage (String url){
            //Check if input is url
            if (!url.matches("^(http://|https://)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$")) {
                b.data.setError("Invalid URL!");
                return;
            }
            //Good to go , sent intent
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            hideError();
        }

    /**
     * send intent to open  dial number
     * @param number
     */
    private void dialNumber (String number){
            //Check if input is phone number
            if (!number.matches("^\\d{10}$")) {
                b.data.setError("Invalid Mobile Number!");
                return;
            }
            //Good to go , sent intent
            Uri uri = Uri.parse("tel:" + number);
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            startActivity(intent);
            hideError();
        }

    /**
     * send intent to share data
     * @param text
     */
    private void shareText (String text){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(intent, "Share  text via"));

        }


        //utility function
        private void hideError () {
            b.data.setError(null);
        }

}