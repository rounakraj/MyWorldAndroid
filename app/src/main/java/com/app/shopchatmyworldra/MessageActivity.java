package com.app.shopchatmyworldra;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.shopchatmyworldra.adapter.ChatAdapter;

import com.app.shopchatmyworldra.constant.HttpClient;
import com.app.shopchatmyworldra.constant.MyApplication;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.ChatMessage;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import cz.msebera.android.httpclient.Header;

/**
 * Created by bali on 29/2/16.
 */
public class MessageActivity extends AppCompatActivity {

    private ListView messagesContainer;
    private EditText chatText;
    private TextView toolbartitle;
    private ImageView buttonSend;
    private FrameLayout farmelayout;
    private String username;
    private Runnable runnable2;
    private ChatAdapter adapter;
    private String message;
    private boolean keepupdating = true;
    private ImageView imVCature_pic;
    private Handler handler2 = new Handler();
    private ArrayList<ChatMessage> chatHistory = new ArrayList<ChatMessage>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbartitle = (TextView) findViewById(R.id.toolbar_title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        imVCature_pic = (ImageView) findViewById(R.id.imVCature_pic);
        username = MyPreferences.getActiveInstance(MessageActivity.this).getFirstName();
        //toolbartitle.setText(username);
        chatText = (EditText) findViewById(R.id.msg);
        username = getIntent().getStringExtra("userName");
        buttonSend = (ImageView) findViewById(R.id.send);
        farmelayout = (FrameLayout) findViewById(R.id.farmelayout);
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        farmelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = chatText.getEditableText().toString().trim();
                if (!message.equals("")) {
                    new ServiceAsnc().execute();
                    chatText.setText("");
                    imVCature_pic.setImageBitmap(null);
                } else {
                    Toast.makeText(MessageActivity.this, "Enter your message.", Toast.LENGTH_LONG).show();
                }

            }
        });

        runnable2 = new Runnable() {
            public void run() {
                validateGetMessage();
                if (keepupdating)
                    handler2.postDelayed(runnable2, 10000);
            }
        };

        MyApplication.Title = "";

        ImageView ivBackArrow = (ImageView) findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        ImageView ivHome=(ImageView)findViewById(R.id.ivHome);
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MessageActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });


    }



    @Override
    public void onResume() {
        super.onResume();
        keepupdating = true;
        handler2.postDelayed(runnable2, 100);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }


    protected void validateGetMessage() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(MessageActivity.this).getUserId());
        client.post(WebServices.GetMESSAGE_URL, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        Log.e("", "Hash -> " + responseCode.toString());
                        Log.e("", "On Success of NoTs");
                        Log.e("GetMessage", "" + responseCode.toString());
                        parseResultgetMessage(responseCode.toString());
                        if (chatHistory != null) {
                            Collections.reverse(chatHistory);
                            adapter = new ChatAdapter(MessageActivity.this, chatHistory);

                            messagesContainer.setAdapter(adapter);
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();

                    }

                });

    }


    private void parseResultgetMessage(String result) {

        try {
            JSONObject response = new JSONObject(result);
            JSONArray data = response.getJSONArray("chatList");
            chatHistory.clear();
            for (int j = 0; j < data.length(); j++) {
                JSONObject messagedata = data.getJSONObject(j);
                ChatMessage chatMessage = new ChatMessage();


                if (messagedata.getString("chatFlag").equals("1")) {

                    chatMessage.setMessage(messagedata.getString("chatMsg"));
                    chatMessage.setDate(messagedata.getString("chatTime"));
                    chatMessage.setMe(false);
                } else {

                    chatMessage.setMessage(messagedata.getString("chatMsg"));
                    chatMessage.setDate(messagedata.getString("chatTime"));
                    chatMessage.setMe(true);
                }
                chatHistory.add(chatMessage);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public class ServiceAsnc extends AsyncTask<String, Void, String> {

        private String response;
        ProgressDialog mDialog;

        public ServiceAsnc() {

        }

        @Override

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

          /*  mDialog = ProgressDialog.show(MessageActivity.this, "", "Please wait", true);
            mDialog.setCancelable(false);*/
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            response = callService();


            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            // TODO Auto-generated method stub
            super.onPostExecute(response);
            if (mDialog != null) {

                mDialog.dismiss();
            }

            if (response == null) {
                Toast.makeText(MessageActivity.this, "There is some problem with the server. Please try again after sometime.",
                        Toast.LENGTH_LONG).show();
            } else {

                try {
                    JSONObject result = new JSONObject(response);
                    chatHistory.clear();

                    JSONArray data = result.getJSONArray("chatList");
                    for (int j = 0; j < data.length(); j++) {
                        JSONObject messagedata = data.getJSONObject(j);
                        ChatMessage chatMessage = new ChatMessage();


                        if (messagedata.getString("chatFlag").equals("1")) {

                            chatMessage.setMessage(messagedata.getString("chatMsg"));
                            chatMessage.setDate(messagedata.getString("chatTime"));
                            chatMessage.setMe(false);
                        } else {

                            chatMessage.setMessage(messagedata.getString("chatMsg"));
                            chatMessage.setDate(messagedata.getString("chatTime"));
                            chatMessage.setMe(true);
                        }
                        chatHistory.add(chatMessage);
                    }
                    if (chatHistory != null) {
                        Collections.reverse(chatHistory);
                        adapter = new ChatAdapter(MessageActivity.this, chatHistory);
                        messagesContainer.setAdapter(adapter);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Log.d("##########Response", "" + response);

            try {
                String success, msg, error_msg;
                if ((response != null) && !response.equals("")) {
                    Log.e("*******************", response);
                    JSONObject object = new JSONObject(response);
                    success = object.getString("responseCode");

                    if (!success.equals("200")) {
                        error_msg = object.getString("error_msg");
                        Toast.makeText(MessageActivity.this, error_msg,
                                Toast.LENGTH_SHORT).show();

                    }
                    {
                       /* Intent intent = new Intent(MessageActivity.this, HomeActivity.class);
                        startActivity(intent);*/
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        private String callService() {
            // TODO Auto-generated method stub
            String url = WebServices.MESSAGE_URL;
            HttpClient client = new HttpClient(url);
            try {
                client.connectForMultipart();
                client.addFormPart("userType", "user");
                client.addFormPart("userId", MyPreferences.getActiveInstance(MessageActivity.this).getUserId());
                client.addFormPart("chatMsg", message);
              /*  if(message!=null)
                {
                    client.addFormPart(AppConstants.MESSAGE, message);
                }*/
               /* if (imageArray != null) {
                    client.addFilePart("image", Frow + ".png", imageArray);
                }*/
                client.finishMultipart();
                response = client.getResponse().toString();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return response;
        }

    }

}








