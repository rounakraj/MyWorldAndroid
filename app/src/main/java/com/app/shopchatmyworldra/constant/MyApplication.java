package com.app.shopchatmyworldra.constant;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by legacy on 31-Jul-17.
 */

public class MyApplication extends Application {
    public static Bitmap cropped;
    public static String ActivityFilter="";
    public static boolean ActivityAddress;
    public static boolean isBackSell;
    public static String minPrice="";
    public static String maxPrice="";
    public static String brandId="";
    public static String location;
    public static String subcatId="";
    public static String Title="";
    public static String userNamechat="";
    public static String userId2="";
    public static String emilId="";
    public static Boolean isDownload = false;
    @Override
    public void onCreate() {
        super.onCreate();
        printHashKey();
    }

    public void printHashKey(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "net.simplifiedcoding.androidlogin",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}