package com.example.android.fintech_hackathon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.widget.TextView;

public class RuntimeScan extends AppCompatActivity {

    // LOG_TAG
    private static final String TAG = "MainActivity";
    // SurfaceView in order to display the preview frames captured by the camera.
    SurfaceView cameraView;
    // TextView to display the contents of the QR codes the API detects
    TextView barcodeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_scan);

        // Get SurfaceView from the xml.
        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        // Get TextView from the xml.
        barcodeInfo = (TextView) findViewById(R.id.code_info);

        
    }
}
