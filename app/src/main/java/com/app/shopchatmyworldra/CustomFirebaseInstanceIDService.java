package com.app.shopchatmyworldra;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class CustomFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = CustomFirebaseInstanceIDService.class.getSimpleName();
    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        System.out.println("getTOKEN"+"FROMSERVER>>>>>>>>"+refreshedToken);
        Log.d(TAG, "Token Value: " + refreshedToken);




    }
}
