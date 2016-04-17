package com.example.android.fintech_hackathon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     *
     * Retrieve scanning result
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult =
                IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        /*
            As with any data being retrieved from another app,
            it's vital to check for null values.
            Only proceed if we have a valid result.
         */
        if (scanningResult != null) {
            /*
                We have a result:
                The IntentResult object provides methods to retrieve:
                - the content of the scan
                - the format of the data returned from it
            */
            String scanContent = scanningResult.getContents();  //the content
            String scanFormat = scanningResult.getFormatName();  //the format

            // Write the values to the TextViews in our layout.
            formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("CONTENT: " + scanContent);

        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }


}
