package com.app.shopchatmyworldra;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.shopchatmyworldra.adapter.AllUserAdapter;

import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.AlluserResource;
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

public class AllUserActivity  extends AppCompatActivity implements AllUserAdapter.Invaite, AllUserAdapter.UnInvaite{
    AllUserAdapter adapter;
    ArrayList<AlluserResource> followerArrayList;
    private RecyclerView mRecyclerView;
    TextView toolbartitle,txtnofollower;
    public Snackbar snackbar;
    private String onlineFlag = "";
    private String searchKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_friend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        searchKey=getIntent().getStringExtra("searchKey");
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        toolbartitle=(TextView)findViewById(R.id.toolbar_title);
        toolbartitle.setText("Friends Request");
        txtnofollower=(TextView)findViewById(R.id.txtnofollower);
        snackbar = Snackbar.make(findViewById(R.id.fab), "No Internet  Connection", Snackbar.LENGTH_LONG);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(AllUserActivity.this));

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
                Intent intent=new Intent(AllUserActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });



    }

    protected void validatealluser(String searchKeydata) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.add(searchKey,searchKeydata);
        params.add("userId",MyPreferences.getActiveInstance(AllUserActivity.this).getUserId());
        client.post(WebServices.alluser, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(AllUserActivity.this,"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        Log.e("", "Hash -> " + responseCode.toString());
                        Log.e("", "On Success of NoTs");
                        parseResult(responseCode.toString());
                        if (followerArrayList != null) {
                            adapter = new AllUserAdapter(AllUserActivity.this, followerArrayList,AllUserActivity.this,AllUserActivity.this);
                            mRecyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), AllUserActivity.this);
                        ProgressBarDialog.dismissProgressDialog();
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
            if (response.getString("responseCode").equals("200"))
            {
                JSONArray posts = response.getJSONArray("User");
                followerArrayList = new ArrayList<AlluserResource>();
                if (posts != null) {
                    for (int i = 0; i < posts.length(); i++) {
                        JSONObject post = posts.getJSONObject(i);
                        AlluserResource follower = new AlluserResource();
                        follower.setUserId(post.getString("userId"));
                        follower.setUserId(post.getString("userId"));
                        follower.setUserName(post.getString("firstName"));
                        follower.setLastName(post.getString("lastName"));
                        follower.setProfileImage(post.getString("profileImage"));
                        follower.setUserStatus(post.optString("userStatus"));
                        follower.setEmailId(post.getString("emailId"));
                        follower.setIs_invited(post.getString("is_invited"));
                        followerArrayList.add(follower);
                    }
                }
            }else {
                CommanMethod.showAlert(response.getString("responseMessage"),AllUserActivity.this);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setQueryHint("Search...");
        search(searchView);


        return true;
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (CommanMethod.isOnline(AllUserActivity.this)) {
                    validatealluser(query);
                } else {
                    View sbView =snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(ContextCompat.getColor(AllUserActivity.this,R.color.colorAccent));
                    snackbar.show();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AllUserActivity.this.finish();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onlineFlag = "1";
        onlineFlag();
    }

    @Override
    public void onPause() {
        super.onPause();
        onlineFlag = "0";
        onlineFlag();
    }

    protected void onlineFlag() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.add("onlineFlag", onlineFlag);
        Log.e("onlineFlag",""+onlineFlag);
        params.add("userId", MyPreferences.getActiveInstance(AllUserActivity.this).getUserId());
        client.post(WebServices.onlineFlag, params,
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
                        Log.e("setOnlineFriend", "" + responseCode.toString());

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


    @Override
    public void getEmailForInviteFriends(String EmailId) {
        validateinvitefriend(EmailId);
    }


    private void validateinvitefriend(String emailid) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(AllUserActivity.this).getUserId());
        params.add("emailId", emailid);
        client.post(WebServices.sendrequest, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(AllUserActivity.this,"");
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
                                CommanMethod.showAlert(message, AllUserActivity.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(AllUserActivity.this.getResources().getString(R.string.connection_error), AllUserActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();
                    }

                });

    }

    @Override
    public void getUnInviteFriends(String UserId) {
        validateUninvitefriend(UserId);
    }

    private void validateUninvitefriend(String UserId) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(AllUserActivity.this).getUserId());
        params.add("userId2", UserId);
        client.post(WebServices.unInvite, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(AllUserActivity.this,"");
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

                                //Toast.makeText()

                            } else {
                                CommanMethod.showAlert(message, AllUserActivity.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(AllUserActivity.this.getResources().getString(R.string.connection_error), AllUserActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();
                    }

                });

    }

}