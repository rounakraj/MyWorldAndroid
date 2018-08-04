package com.app.shopchatmyworldra;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.shopchatmyworldra.adapter.FriendAdapter;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.Friend;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by MMAD on 04-09-2017.
 */

public class FriendList  extends AppCompatActivity {
    FriendAdapter adapter;
    ArrayList<Friend> followerArrayList;
    private RecyclerView mRecyclerView;
    ImageView addfriend;
    TextView toolbartitle,txtnofollower;
    public Snackbar snackbar;
    private LinearLayout llCallbox;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        llCallbox = (LinearLayout) findViewById(R.id.llCallbox);
        toolbartitle=(TextView)findViewById(R.id.toolbar_title);
        toolbartitle.setText("Friends");
        txtnofollower=(TextView)findViewById(R.id.txtnofollower);
        addfriend=(ImageView) findViewById(R.id.addfriend);
        snackbar = Snackbar.make(findViewById(R.id.fab), "No Internet  Connection", Snackbar.LENGTH_LONG);
        userID = getIntent().getStringExtra("userId");

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(FriendList.this));

        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogSureCancel(FriendList.this);
            }
        });

        if (CommanMethod.isOnline(FriendList.this)) {

            validateFriendList(userID);
        } else {
            View sbView =snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(FriendList.this,R.color.colorAccent));
            snackbar.show();
        }

        ImageView ivBackArrow = (ImageView) findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        llCallbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialogSureCancel(FriendList.this);
            }
        });

        ImageView ivHome=(ImageView)findViewById(R.id.ivHome);
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FriendList.this,MainActivity.class);
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
        client.post(WebServices.getFriends, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(FriendList.this,"");

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        Log.e("", "Hash -> " + responseCode.toString());
                        Log.e("", "On Success of NoTs");
                        parseResult(responseCode.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), FriendList.this);
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

            if (response.getString("responseCode").equals("200")) {
                JSONArray posts = response.getJSONArray("friendList");
                followerArrayList = new ArrayList<>();
                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.getJSONObject(i);
                    Friend follower = new Friend();
                    follower.setUserId(post.getString("userId"));
                    follower.setUserName(post.getString("firstName"));
                    follower.setLastName(post.getString("lastName"));
                    follower.setProfileImage(post.getString("profileImage"));
                    follower.setFollowed(post.getString("followingStatus"));
                    follower.setUserStatus(post.optString("userStatus"));
                    follower.setEmail(post.getString("emailId"));
                    followerArrayList.add(follower);
                }

                if (followerArrayList != null) {
                    adapter = new FriendAdapter(FriendList.this, followerArrayList,userID);
                    mRecyclerView.setAdapter(adapter);
                    llCallbox.setVisibility(View.GONE);
                }

            }else {
                llCallbox.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    public void AlertDialogSureCancel(final Activity activity) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialogLayout = inflater.inflate(R.layout.dilog_addfriendslist, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogLayout);
        final AlertDialog customAlertDialog = builder.create();
        customAlertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        customAlertDialog.show();
        final LinearLayout llSearchByName = (LinearLayout) dialogLayout.findViewById(R.id.llSearchByName);
        final LinearLayout llSearchByEmail = (LinearLayout) dialogLayout.findViewById(R.id.llSearchByEmail);
        final LinearLayout llSearchInTheList = (LinearLayout) dialogLayout.findViewById(R.id.llSearchInTheList);
        final LinearLayout llScatnQrCode = (LinearLayout) dialogLayout.findViewById(R.id.llScatnQrCode);
        final TextView tvYes = (TextView) dialogLayout.findViewById(R.id.tvYes);


        llSearchByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAlertDialog.dismiss();
                Intent intent=new Intent(FriendList.this,AllUserActivity.class);
                intent.putExtra("searchKey","searchName");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        llSearchByEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAlertDialog.dismiss();
                Intent intent=new Intent(FriendList.this,AllUserActivity.class);
                intent.putExtra("searchKey","searchEmail");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        llSearchInTheList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAlertDialog.dismiss();
                Intent intent=new Intent(FriendList.this,ActivityPublicUser.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        llScatnQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAlertDialog.dismiss();
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setPrompt("Scan a barcode or QRcode");
                integrator.initiateScan();

            }
        });
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAlertDialog.dismiss();

            }
        });
    }


     @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //qrnum.setText(result.getContents());
                validateinvitefriend(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }



    private void validateinvitefriend(String emailid) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(FriendList.this).getUserId());
        params.add("emailId", emailid);
        client.post(WebServices.sendrequest, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(FriendList.this,"");

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        try {
                            JSONObject object = new JSONObject(responseCode.toString());
                            Log.e("Response", "--->>" + object.toString(2));
                            String message = object.getString("responseMessage");
                            if (object.getString("responseCode").equals("200")) {

                                finish();

                            } else {
                                CommanMethod.showAlert(message, FriendList.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(FriendList.this.getResources().getString(R.string.connection_error), FriendList.this);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();
                    }

                });

    }




}