package com.example.moneyflow;

import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


public class MainActivity extends AppCompatActivity {
    private KeyguardManager keyguardManager;
    FingerprintHandler fingerprintHandler;
    private FingerprintManager.CryptoObject cryptoObject;
    private FingerprintManager fingerprintManager;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        Log.d("one", "kkkkkkkkkkkkone");

        keyguardManager =
                (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        fingerprintManager =
                (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.USE_FINGERPRINT}, 1);
        if (!fingerprintManager.isHardwareDetected()) {
            Toast.makeText(this, "Fingerprint sensor not detected", Toast.LENGTH_SHORT).show();
        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Sensor permissions are required", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.USE_FINGERPRINT}, 1);
        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
            Toast.makeText(this, "Enroll fingerprints to unlock using just your finger", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS));
        } else if(!keyguardManager.isKeyguardSecure()){
            Toast.makeText(this, "Add lock to your in settings", Toast.LENGTH_SHORT).show();
        } else {
            fingerprintHandler = new FingerprintHandler(this);
            fingerprintHandler.start_auth(fingerprintManager, null);
        }
        AdView mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId("ca-app-pub-7537420617397049/4895539543ca-app-pub-3940256099942544/6300978111");//ca-app-pub-7537420617397049/4895539543
        MobileAds.initialize(this, "ca-app-pub-7537420617397049~8732209751");//"ca-app-pub-7537420617397049~8732209751"
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void login(View view) {
        EditText t_password = findViewById(R.id.t_password);
        if ("1234".equals(t_password.getText().toString())) {
            Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(this, mainMenu.class));
            startActivity(new Intent(this, FirebaseActivity.class));
        } else {
            Toast.makeText(this, "Unauthorized", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(0);
        super.onStop();
    }
}

