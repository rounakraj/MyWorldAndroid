package com.app.shopchatmyworldra;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.app.shopchatmyworldra.adapter.NewItemAdapter;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.NewItemResources;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by legacy on 02-Sep-17.
 */

public class AllSearchActivity extends AppCompatActivity {

    private ArrayList<NewItemResources> serviceListData = new ArrayList<NewItemResources>();
    NewItemAdapter mAdapter;
    RecyclerView recyclerView;

    String search;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allserch);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        search=getIntent().getStringExtra("search");


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        GridLayoutManager llm = new GridLayoutManager(AllSearchActivity.this, 2);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        GETPRODUCTNEWITEM();

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
                Intent intent=new Intent(AllSearchActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });




    }



    private void GETPRODUCTNEWITEM() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("search", search);
        client.post(WebServices.searchProduct, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(AllSearchActivity.this,"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        parseResult(responseCode.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), AllSearchActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        ProgressBarDialog.dismissProgressDialog();
                        super.onFinish();
                    }

                });

    }


    private void parseResult(String result) {

        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {

                if (response.get("productServer") instanceof JSONArray) {

                    NewItemResources mRide = null;
                    JSONArray jsonArray = null;
                    jsonArray = response.getJSONArray("productServer");
                    JSONArray jsonArray1 = null;
                    jsonArray1 = response.getJSONArray("productUser");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        mRide = new NewItemResources();
                        JSONObject obj = (JSONObject) jsonArray.get(i);

                        if (obj.getString("productName").toString().trim().equals("null") || obj.getString("productName").equals("")) {
                            mRide.setName("");
                        } else {
                            mRide.setName(obj.getString("productName").toString().trim());
                        }
                        if (obj.getString("productImage1").toString().trim().equals("null") || obj.getString("productImage1").equals("")) {
                            mRide.setImage("");
                        } else {
                            mRide.setImage(obj.getString("productImage1").toString().trim());
                        }
                        if (obj.getString("productId").toString().trim().equals("null") || obj.getString("productId").equals("")) {
                            mRide.setProductid("");
                        } else {
                            mRide.setProductid(obj.getString("productId").toString().trim());
                        }
                        if (obj.getString("userName").toString().trim().equals("null") || obj.getString("userName").equals("")) {
                            mRide.setUsername("");
                        } else {
                            mRide.setUsername(obj.getString("userName").toString().trim());
                        }
                        if (obj.getString("productPrice").toString().trim().equals("null") || obj.getString("productPrice").equals("")) {
                            mRide.setPrice("");
                        } else {
                            mRide.setPrice(obj.getString("productPrice").toString().trim());
                        } if (obj.getString("Percent").toString().trim().equals("null") || obj.getString("Percent").equals("")) {
                            mRide.setPercent("");
                        } else {
                            mRide.setPercent(obj.getString("Percent").toString().trim());
                        }

                        serviceListData.add(mRide);
                    }
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        mRide = new NewItemResources();
                        JSONObject obj = (JSONObject) jsonArray1.get(i);

                        if (obj.getString("productName").toString().trim().equals("null") || obj.getString("productName").equals("")) {
                            mRide.setName("");
                        } else {
                            mRide.setName(obj.getString("productName").toString().trim());
                        }
                        if (obj.getString("productImage1").toString().trim().equals("null") || obj.getString("productImage1").equals("")) {
                            mRide.setImage("");
                        } else {
                            mRide.setImage(obj.getString("productImage1").toString().trim());
                        }
                        if (obj.getString("productId").toString().trim().equals("null") || obj.getString("productId").equals("")) {
                            mRide.setProductid("");
                        } else {
                            mRide.setProductid(obj.getString("productId").toString().trim());
                        }
                        if (obj.getString("userName").toString().trim().equals("null") || obj.getString("userName").equals("")) {
                            mRide.setUsername("");
                        } else {
                            mRide.setUsername(obj.getString("userName").toString().trim());
                        }
                        if (obj.getString("productPrice").toString().trim().equals("null") || obj.getString("productPrice").equals("")) {
                            mRide.setPrice("");
                        } else {
                            mRide.setPrice(obj.getString("productPrice").toString().trim());
                        }
                        serviceListData.add(mRide);
                    }
                    if (serviceListData != null) {
                        mAdapter = new NewItemAdapter(AllSearchActivity.this, serviceListData);
                        recyclerView.setAdapter(mAdapter);
                    } else {
                    }
                } else {

                    CommanMethod.showAlert("No Product Found.", AllSearchActivity.this);
                }
            } else {

                CommanMethod.showAlert(message, AllSearchActivity.this);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}