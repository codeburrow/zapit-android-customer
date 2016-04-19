package com.example.android.fintech_hackathon;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PaymentActivity extends AppCompatActivity {

    // LOG_TAG
    private static final String TAG = "Payment Activity";
    // Test TextView
    private TextView testText;
    // Payment Button
    private Button paymentButton;
    // Scanned IoT - product-slug
    private String scanned_IoT;

    // HashMap to store the data of the IoT
    HashMap<String, String> dataMap = new HashMap<>();
    // Flag to know if the product is available
    private boolean payable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Get the intent and assign it to a local variable
        Intent intent = getIntent();
        // Extract the scanned_IoT delivered from the RuntimeScan Activity
        scanned_IoT = intent.getStringExtra("product-slug");

        // Get the payment Buttom from the xml
        paymentButton = (Button) findViewById(R.id.payment_button);
        // Disable the paymentButton until the checkPayment
        paymentButton.setEnabled(payable);

        testText = (TextView) findViewById(R.id.test_text);

        if (scanned_IoT != null){
            testText.setText(scanned_IoT);

            CheckPaymentAsyncTask checkPayment = new CheckPaymentAsyncTask();
            checkPayment.execute(scanned_IoT);

        }

    }

    public void goBack(View view) {
        this.finish();
    }

    public void pay(View view) {
        MakePaymentAsyncTask makePayment = new MakePaymentAsyncTask();
        makePayment.execute(scanned_IoT);
    }


    /**
     *  Check product's payment_status
     */
    private class CheckPaymentAsyncTask extends AsyncTask<String, Void, Boolean> {

        // LOG_TAG
        private static final String LOG_TAG = "CheckPayment";
        // Error
        String error;

        @Override
        protected Boolean doInBackground(String... args){
<<<<<<< HEAD
            // JSON Parser
            JSONParser jsonParser = new JSONParser();
            // Status_code
            int status_code;

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(Constants.TAG_PRODUCT_SLUG, args[0]));

                // Make Http GET Request
                JSONObject json = jsonParser.makeHttpRequest(
                        Constants.GET_PAYMENT_STATUS_URL, "GET", params);

                /*
                    Try to get the error message .
                 */
                try {
                    // error - try to get the error
                    error = json
                            .getJSONObject(Constants.TAG_ERROR)
                            .getString(Constants.TAG_MESSAGE);

                    Log.e(LOG_TAG, error);
                } catch (JSONException je){
                    Log.e(LOG_TAG, "error catch: " + je.getMessage());

                    // error doesn't exist
                    error = null;
                }


                /*
                    Get the status_code .
                 */
                try {
                    // status_code - success
                    status_code = json.getInt(Constants.TAG_STATUS_CODE);
                } catch (JSONException je){
                    // status_code - exists inside the error tag
                    status_code = json.getJSONObject(Constants.TAG_ERROR)
                            .getInt(Constants.TAG_STATUS_CODE);
                }



                if (error != null){
                    // What will happen when there is an error

                } else {
                    if (status_code == 200){
                        /*
                         *   Fill the dataMap
                         */
                        String[] dataTagsList = {Constants.TAG_SLUG,
                                Constants.TAG_NAME,
                                Constants.TAG_PRICE,
                                Constants.TAG_DESCRIPTION};

                        for (String dataAttribute : dataTagsList) {
                            dataMap.put(dataAttribute, json.getJSONObject(Constants.TAG_DATA)
                                    .getString(dataAttribute));
                        }

                        /*
                         *  Display content of dataMap using Iterator
                         */
                        Set set = dataMap.entrySet();
                        Iterator iterator = set.iterator();
                        while(iterator.hasNext()) {
                            Map.Entry mEntry = (Map.Entry)iterator.next();
                            Log.e("dataMap", mEntry.getKey() + " : " + mEntry.getValue());
                        }

                        // payed
                        return json.getJSONObject(Constants.TAG_DATA)
                                .getBoolean(Constants.TAG_PAYED);
                    }

                }

            } catch (JSONException je) {
                Log.e(LOG_TAG, "catch: " + je.getMessage());
            }

            return true;
        }


        @Override
        protected void onPostExecute(Boolean payedStatus) {
            if (payedStatus){
                Log.e(LOG_TAG, "Payed");

                // Someone has already bought this product
                if (error == null){
                    setInfoText();
                } else {
                    String errorMessage = testText.getText() + "\n" + error;
                    testText.setText(errorMessage);
                }
            } else {
                Log.e(LOG_TAG, "Un-payed");

                // This product can be bought
                payable = true;
                // Enable the paymentButton
                paymentButton.setEnabled(payable);

                setInfoText();
            }
        }

        private void setInfoText(){
            // This is the String to be set in testText
            String info = "";
            // Iterate through dataMap and prepare the info String
            Set set = dataMap.entrySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()) {
                Map.Entry mEntry = (Map.Entry)iterator.next();
                info += mEntry.getKey() + ": " + mEntry.getValue() + "\n";
            }
            // TestText
            testText.setText(info);
        }

    }


    /**
     *  Make a payment
     */
    private class MakePaymentAsyncTask extends AsyncTask<String, Void, Boolean> {

        // LOG_TAG
        private static final String LOG_TAG = "MakePayment";

        @Override
        protected Boolean doInBackground(String... args){
            // Status_code
            int status_code;
            // Error
            String error;
            // JSON Parser
            JSONParser jsonParser = new JSONParser();

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("product-slug", args[0]));

                // Make Http GET Request
                JSONObject json = jsonParser.makeHttpRequest(
                        Constants.MAKE_PAYMENT_URL, "GET", params);


                /*
                    Try to get the error message .
                 */
                try {
                    // error - try to get the error
                    error = json
                            .getJSONObject(Constants.TAG_ERROR)
                            .getString(Constants.TAG_MESSAGE);

                    Log.e(LOG_TAG, error);
                } catch (JSONException je){
                    Log.e(LOG_TAG, "error catch: " + je.getMessage());

                    // error doesn't exist
                    error = null;
                }


                /*
                    Get the status_code .
                 */
                try {
                    // status_code - success
                    status_code = json.getInt(Constants.TAG_STATUS_CODE);
                } catch (JSONException je){
                    // status_code - exists inside the error tag
                    status_code = json.getJSONObject(Constants.TAG_ERROR)
                            .getInt(Constants.TAG_STATUS_CODE);
                }


                if (error != null) {
                    // What will happen when there is an error

                } else {
                    if (status_code == 200){
                        return json.getJSONObject(Constants.TAG_DATA)
                                .getBoolean(Constants.TAG_PAYED);
                    }
                }


            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage());
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean payedStatus) {
            if (payedStatus){
                Log.e(LOG_TAG, "PAYED");

                // This product cannot be bought again
                payable = false;
                // Disable the paymentButton
                paymentButton.setEnabled(payable);
            }

        }

    }



}
