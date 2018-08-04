package com.app.shopchatmyworldra;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.Toast;

import com.app.shopchatmyworldra.constant.MyPreferences;

import com.app.shopchatmyworldra.videochat.BaseActivity;
import com.app.shopchatmyworldra.videochat.SinchService;
import com.crashlytics.android.Crashlytics;
import com.sinch.android.rtc.SinchError;

import io.fabric.sdk.android.Fabric;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by legacy on 16-Aug-17.
 */

public class SplashActivity extends BaseActivity implements SinchService.StartFailedListener {
    private static int SPLASH_TIME_OUT = 5000;
    Animation fadeout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);

        printKeyHash();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (MyPreferences.getActiveInstance(SplashActivity.this).getIsLoggedIn()) {

                    if (!MyPreferences.getActiveInstance(SplashActivity.this).getEmailId().equals("")) {
                        loginClicked(MyPreferences.getActiveInstance(SplashActivity.this).getEmailId());
                    }
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    finish();


                } else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);


    }


    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("jk", "Exception(NameNotFoundException) : " + e);
        } catch (NoSuchAlgorithmException e) {
            Log.e("mkm", "Exception(NoSuchAlgorithmException) : " + e);
        }
    }


    //this method is invoked when the connection is established with the SinchService
    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
    }


    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
    }

    //Invoked when just after the service is connected with Sinch
    @Override
    public void onStarted() {

    }

    //Login is Clicked to manually to connect to the Sinch Service
    private void loginClicked(String userName) {

        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
            return;
        }

        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userName);
        } else {
        }
    }
}