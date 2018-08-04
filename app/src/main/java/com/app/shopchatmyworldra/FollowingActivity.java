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

import com.app.shopchatmyworldra.adapter.FollowingAdapter;

import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.Follower;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


/**
 * Created by bali on 2/1/16.
 */
public class FollowingActivity extends AppCompatActivity implements FollowingAdapter.FollwerCallback{
    FollowingAdapter adapter;
    ArrayList<Follower> followerArrayList;
    private RecyclerView mRecyclerView;
    String userID;
    TextView toolbartitle,txtnofollower;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        toolbartitle=(TextView)findViewById(R.id.toolbar_title);
        toolbartitle.setText("Following");
        txtnofollower=(TextView)findViewById(R.id.txtnofollower);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(FollowingActivity.this));
        userID = getIntent().getStringExtra("userId");
        validateFollwing(userID);

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
                Intent intent=new Intent(FollowingActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });



    }


    protected void validateFollwing(String userId) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.add("userId", userId);
        client.post(WebServices.FOLLOWING_URL, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(FollowingActivity.this,"");

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        Log.e("", "Hash -> " + responseCode.toString());
                        Log.e("", "On Success of NoTs");
                        parseResult(responseCode.toString());
                        if (followerArrayList != null) {
                            adapter = new FollowingAdapter(FollowingActivity.this, followerArrayList,FollowingActivity.this);
                            mRecyclerView.setAdapter(adapter);
                        }else {
                            txtnofollower.setText("No Following !");
                            txtnofollower.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), FollowingActivity.this);
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
            JSONArray posts = response.getJSONArray("FollowingList");
            followerArrayList = new ArrayList<Follower>();
            if (posts != null) {
                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.getJSONObject(i);
                    Follower follower = new Follower();
                    follower.setUserId(post.getString("userId"));
                    follower.setUserName(post.getString("firstName")+" "+post.getString("lastName"));
                    follower.setUserStatus(post.optString("userStatus"));
                    follower.setProfileImage(post.getString("profileImage"));
                    followerArrayList.add(follower);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void followuser(String userId, String singUserID) {
         if (CommanMethod.isNetworkAvailable(FollowingActivity.this)) {

            validateFollow(userId, singUserID);

        } else {
            CommanMethod.showAlert("No NetworkAvailable", FollowingActivity.this);
        }

    }

    protected void validateFollow(String signedUserId, String userId) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.add("", signedUserId);
        params.add("",userId);
        client.post(WebServices.FOLLOW, params,
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
                        Log.e("Response HomeBigImge", "" + responseCode.toString());
                        parseResultFollow(responseCode.toString());

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), FollowingActivity.this);

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }

                });
    }


    private void parseResultFollow(String result) {
        try {
            JSONObject response = new JSONObject(result);
            if(response!=null) {

                Log.e("",""+response.getString("responseMessage"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}