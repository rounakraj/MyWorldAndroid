package com.app.shopchatmyworldra;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.shopchatmyworldra.adapter.FriendRequestAdapter;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.MyApplication;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.FriendRequestResources;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by MMAD on 05-09-2017.
 */

public class FriendRequest extends AppCompatActivity implements FriendRequestAdapter.FriendRequestCallback{
    FriendRequestAdapter adapter;
    ArrayList<FriendRequestResources> followerArrayList;
    private RecyclerView mRecyclerView;
    String UserID;
    TextView toolbartitle,txtnofollower;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        MyApplication.Title="";
        UserID = getIntent().getStringExtra("userId");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        toolbartitle=(TextView)findViewById(R.id.toolbar_title);
        toolbartitle.setText("Friend Request");
        txtnofollower=(TextView)findViewById(R.id.txtnofollower);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(FriendRequest.this));
        MyApplication.Title="";
        validateFriendList(UserID);
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
                Intent intent=new Intent(FriendRequest.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });


    }


    protected void validateFriendList(String userID) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.add("userId",userID);
        client.post(WebServices.showfriendrequest, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(FriendRequest.this,"");

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        Log.e("", "Hash -> " + responseCode.toString());
                        Log.e("", "On Success of NoTs");



                        parseResult(responseCode.toString());
                        if (followerArrayList != null) {
                            adapter = new FriendRequestAdapter(FriendRequest.this,followerArrayList, UserID,FriendRequest.this);
                            mRecyclerView.setAdapter(adapter);
                        }else {

                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), FriendRequest.this);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();
                    }

                });

    }

    private void parseResult(String result) {

        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.getJSONArray("friendRequest");
            followerArrayList = new ArrayList<FriendRequestResources>();
            Log.e("Response", "--->>" + response.toString(2));
            if (posts != null) {
                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.getJSONObject(i);
                    FriendRequestResources follower = new FriendRequestResources();
                    follower.setUserId(post.getString("userId"));
                    follower.setUserName(post.getString("firstName"));
                    follower.setLastName(post.getString("lastName"));
                    follower.setProfileImage(post.getString("profileImage"));
                    //follower.setEmail(post.getString("emailId"));
                    followerArrayList.add(follower);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void acceptUserId(String userId1) {
        validateAcceptFriend(userId1);
    }

    private void validateAcceptFriend(String userId1) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId2",MyPreferences.getActiveInstance(FriendRequest.this).getUserId());
        params.add("userId1",userId1);

        client.post(WebServices.AcceptFriend, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(FriendRequest.this,"");

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        try {
                            JSONObject object = new JSONObject(responseCode.toString());
                            Log.e("Response", "--->>" + object.toString(2));
                            String message=object.getString("responseMessage");
                            if(object.getString("responseCode").equals("200"))
                            {

                                FriendRequest.this.finish();

                            }else {
                                CommanMethod.showAlert(message, FriendRequest.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();
                    }

                });

    }

    @Override
    public void regectUserId(String userId1) {
        validateRejectFriend(userId1);
    }


    private void validateRejectFriend(String userId1) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId2",MyPreferences.getActiveInstance(FriendRequest.this).getUserId());
        params.add("userId1",userId1);

        client.post(WebServices.RejectFrien, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(FriendRequest.this,"");

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        try {
                            JSONObject object = new JSONObject(responseCode.toString());
                            Log.e("Response", "--->>" + object.toString(2));
                            String message=object.getString("responseMessage");
                            if(object.getString("responseCode").equals("200"))
                            {

                               FriendRequest.this.finish();

                            }else {
                                CommanMethod.showAlert(message,FriendRequest.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();
                    }

                });

    }
}