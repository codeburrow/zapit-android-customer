package com.example.android.fintech_hackathon;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RuntimeScan extends AppCompatActivity {

    // LOG_TAG
    private static final String TAG = "MainActivity";
    // SurfaceView in order to display the preview frames captured by the camera.
    SurfaceView cameraView;
    // TextView to display the contents of the QR codes the API detects
    TextView barcodeInfo;
    // CameraSource
    CameraSource cameraSource;
    // Request code for runtime permissions
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_scan);


        // Get SurfaceView from the xml.
        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        // Get TextView from the xml.
        barcodeInfo = (TextView) findViewById(R.id.code_info);

        /*
            the CameraSource needs a BarcodeDetector,
            create one using the BarcodeDetector.Builder class.
         */
        BarcodeDetector barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();

        /*
            To fetch a stream of images from the device’s camera
            and display them in the SurfaceView,
            create a new instance of the CameraSource class using CameraSource.Builder.
        */
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build();

        /*
            Add a callback to the SurfaceHolder of the SurfaceView
            so that you know when you can start drawing the preview frames.
         */
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // Call the start method of the CameraSource to start drawing the preview frames.
                try {
                    if (Build.VERSION.SDK_INT >= 23) {
                        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.CAMERA);
                        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[] {Manifest.permission.CAMERA},
                                    REQUEST_CODE_ASK_PERMISSIONS);
                            return;
                        }
                    }
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // Call the stop method of the CameraSource to stop drawing the preview frames.
                cameraSource.stop();
            }
        });


        /**
         * Tell the BarcodeDetector what it should do when it detects a QR code.
         * Create an instance of a class that implements the Detector.Processor interface
         * and pass it to the setProcessor method of the BarcodeDetector.
         * Android Studio will automatically generate stubs for the methods of the interface.
         */
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                // Get the SparseArray of Barcode objects by calling the getDetectedItems() method
                // of the Detector.Detections class.
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    // Use the post method of the TextView
                    barcodeInfo.post(new Runnable() {
                        public void run() {
                            CheckPaymentAsyncTask checkPayment = new CheckPaymentAsyncTask();
                            checkPayment.execute(barcodes.valueAt(0).rawValue);
                        }
                    });
                }

            }
        });

    }



    private class CheckPaymentAsyncTask extends AsyncTask<String, Void, String> {

        // LOG_TAG
        private static final String LOG_TAG = "CheckPayment";
        // Response tags
        private static final String GET_PAYMENT_STATUS_URL = "https://zapit-web.herokuapp.com/api/v1/products/payment/status";
        private static final String TAG_STATUS_CODE = "status_code";
        private static final String TAG_ERROR = "error_code";
        private static final String TAG_DATA = "data";
        private static final String TAG_PAYED = "payed";
        private static final String TAG_MESSAGE = "message";

        @Override
        protected String doInBackground(String... args){
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
                        GET_PAYMENT_STATUS_URL, "GET", params);

                // json status_code success element
                status_code = json.getInt(TAG_STATUS_CODE);

                if (status_code == 200){
                    return json.getJSONObject(TAG_DATA).getString(TAG_PAYED);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String payedStatus) {
            if (payedStatus != null){
                Log.e(LOG_TAG, payedStatus);

                if (payedStatus.equals("0")){
                    // Update the TextView
                    barcodeInfo.setText("Unpayed");
                }
            }
        }


    }



}
