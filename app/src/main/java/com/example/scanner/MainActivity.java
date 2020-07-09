package com.example.scanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    BarcodeDetector detector;
    CameraSource cameraSource;
    TextView result;
    TextView temp;
    final int RequestCameraPermissionID = 1001;
    HashMap<String, Integer> map;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(surfaceView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView = findViewById(R.id.cameraPreview);
        result = findViewById(R.id.textResult);
        temp = findViewById(R.id.temp);
        map = new HashMap<>();


        detector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        cameraSource = new CameraSource.Builder(this, detector)
                .setRequestedPreviewSize(1920, 1080).setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, RequestCameraPermissionID);
                    return;
                }
                try {
                    cameraSource.start(surfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }
            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> barcodes = detections.getDetectedItems();
                //Log.d("TESTSSSSS", barcodes.valueAt(0).displayValue);
                if (barcodes.size() > 0) {
                    result.setText(barcodes.valueAt(0).displayValue);
                    Log.i("PRODUCT NAME", lookupBarcode(barcodes.valueAt(0).displayValue));
                    temp.setText(lookupBarcode(barcodes.valueAt(0).displayValue));
                    map.put(barcodes.valueAt(0).displayValue, 1);
                    Log.d("TESTSSSSS", barcodes.valueAt(0).displayValue);
                    //send product info name to textview
                    //String barcode = barcodes.valueAt(0).displayValue;
                    //Log.i("PRODUCT NAME", lookupBarcode(barcode));
                }
                Log.i("INFO ON ALL BARCODES", map.toString());
            }

        });


        }

    /**
     * lookups barcode info via web scraping
     * @param barcodeNum the UPC or barcode number found from the vision API
     * @return the product name
     */
    public String lookupBarcode(String barcodeNum) {
            final String url = "https://www.barcodelookup.com/" + barcodeNum;
            String prodName = null;
            try {
                final Document doc = Jsoup.connect(url).get();
                Elements prodInfo = doc.select("div.col-md-6.product-details h4");
                prodName = prodInfo.text();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (prodName != null) {
                return prodName;
            }
            return "Could not find barcode";
    }


}
