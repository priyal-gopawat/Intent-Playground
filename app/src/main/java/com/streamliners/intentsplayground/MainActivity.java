package com.streamliners.intentsplayground;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Toast;

import com.streamliners.intentsplayground.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private int qty = 0;
    private ActivityMainBinding b;
    private int minVal,maxVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        getInitialCount();

        if(savedInstanceState!=null){
            qty = savedInstanceState.getInt(Constants.COUNTER,0);
            b.qty.setText(""+qty);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.COUNTER,qty);
    }

    /**
     * Get initial count from intent playground activity
     */
    private void getInitialCount() {
        //Get data from intent
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            qty = bundle.getInt(Constants.INITIAL_COUNT_KEY,0);
            minVal = bundle.getInt(Constants.MIN_VALUE, Integer.MIN_VALUE);
            maxVal = bundle.getInt(Constants.MAX_VALUE, Integer.MAX_VALUE);
        }

        b.qty.setText(String.valueOf(qty));

        if(qty!=0){
            b.sendBackBtn.setVisibility(View.VISIBLE);
        }

    }

    /**
     * Decrements  counter by 1
     * @param view
     */
    public void decQty(View view) {

        b.qty.setText(""+ --qty);
    }

    /**
     * Increments counter by 1
     * @param view
     */
    public void incQty(View view) {
        b.qty.setText(""+ ++qty);
    }

    /**
     * send back final count to intent playground activity
     * @param view
     */
    public void sendBack(View view) {
        //Validate count
        if(qty >= minVal && qty <= maxVal){

            //Send the data
            Intent intent = new Intent();
            intent.putExtra(Constants.FINAL_COUNT,qty);
            setResult(RESULT_OK,intent);

            //Close the activity
            finish();
        }
        //Not in range
        else{
            Toast.makeText(this, "Not In Range !", Toast.LENGTH_SHORT).show();
        }
    }
}