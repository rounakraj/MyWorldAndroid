package com.app.shopchatmyworldra;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.app.shopchatmyworldra.constant.MyApplication;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

public class CustomFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = CustomFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        String Message = "";
        String Type = "";

        if (remoteMessage == null)

            return;

        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
        }
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject object = new JSONObject(remoteMessage.getData().get("alldata"));
                Message = object.getString("message");
                sendNotification(Message);
                MyApplication.Title = object.getString("title");
                if (object.getString("title").equals("user")) {
                  /* int counter = 0;
                    if (MyPreferences.getActiveInstance(CustomFirebaseMessagingService.this).getbudge().equals("")) {
                        counter=counter+1;
                        MyPreferences.getActiveInstance(CustomFirebaseMessagingService.this).setbudge(String.valueOf(counter));
                    } else {
                        counter = Integer.parseInt(MyPreferences.getActiveInstance(CustomFirebaseMessagingService.this).getbudge());
                        counter=counter+1;
                        MyPreferences.getActiveInstance(CustomFirebaseMessagingService.this).setbudge(String.valueOf(counter));

                    }*/

                    MyApplication.userId2 = object.getString("userId2");
                    MyApplication.userNamechat = object.getString("userName");
                    MyApplication.emilId = object.getString("emailId");
                }
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("notification");
                sendBroadcast(broadcastIntent);


            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }


    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        try {


            if (intent != null) {
                Bundle bundle = intent.getExtras();
                JSONObject object = new JSONObject(bundle.get("alldata").toString());
                MyApplication.Title = object.getString("title");
                if (object.getString("title").equals("user")) {
                    int counter = 0;
                    if (!MyPreferences.getActiveInstance(CustomFirebaseMessagingService.this).getbudge().equals("")) {
                        counter = Integer.parseInt(MyPreferences.getActiveInstance(CustomFirebaseMessagingService.this).getbudge());
                        counter++;
                        MyPreferences.getActiveInstance(CustomFirebaseMessagingService.this).setbudge(String.valueOf(counter));
                    } else {
                        counter++;
                        MyPreferences.getActiveInstance(CustomFirebaseMessagingService.this).setbudge(String.valueOf(counter));
                    }

                    MyApplication.userId2 = object.getString("userId2");
                    MyApplication.userNamechat = object.getString("userName");
                    MyApplication.emilId = object.getString("emailId");
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sendNotification(String message) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("My World")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
