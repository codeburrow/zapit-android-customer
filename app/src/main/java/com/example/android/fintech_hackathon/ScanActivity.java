package com.example.android.fintech_hackathon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;

public class ScanActivity extends AppCompatActivity implements View.OnClickListener {

    // LOG_TAG
    private static final String TAG = "ScanActivity";
    // Scan Button
    private Button scanBtn;
    // Text Views to display the format and the content of the barcode
    private TextView formatTxt, contentTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        /*
            Get button and textViews from the xml
         */
        scanBtn = (Button)findViewById(R.id.scan_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);

        scanBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        // Check whether the scanning button has been pressed.
        if( v.getId() == R.id.scan_button ){
            Log.e(TAG , "Scan button is pressed");

            // Scan
            // Create an instance of the Intent Integrator class we imported.
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);

            // Now we can call on the Intent Integrator method to start scanning.
            scanIntegrator.initiateScan();

        }

    }
}
