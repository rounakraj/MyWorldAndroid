package com.app.shopchatmyworldra;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.shopchatmyworldra.adapter.OrderHistoryAdapter;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.OrderHistoryResources;
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
 * Created by MMAD on 16-08-2017.
 */

public class OrderHistoryActivity extends AppCompatActivity {
    private View view;
    RecyclerView recyclerView;
    private ArrayList<OrderHistoryResources> Resources=new ArrayList<OrderHistoryResources>();
    private OrderHistoryAdapter mAdapter;
    public Snackbar snackbar;
    private LinearLayout llCallbox;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_rewards);
        llCallbox = (LinearLayout)findViewById(R.id.llCallbox);
        LinearLayoutManager llm = new LinearLayoutManager(OrderHistoryActivity.this);
        recyclerView.setLayoutManager(llm);
        snackbar = Snackbar.make(findViewById(R.id.fab), "No Internet  Connection", Snackbar.LENGTH_LONG);
        if (CommanMethod.isOnline(OrderHistoryActivity.this)) {

            validateorderhostory();
        } else {
            View sbView =snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(OrderHistoryActivity.this,R.color.colorAccent));
            snackbar.show();
        }

        mAdapter = new OrderHistoryAdapter(OrderHistoryActivity.this, Resources);
        recyclerView.setAdapter(mAdapter);

        ImageView ivBackArrow = (ImageView) findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

       /* llCallbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderHistoryActivity.this, BuyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });*/

        ImageView ivHome=(ImageView)findViewById(R.id.ivHome);
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderHistoryActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });


    }



    private void validateorderhostory() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(OrderHistoryActivity.this).getUserId());
        client.post(WebServices.OrderHistory, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(OrderHistoryActivity.this,"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        try {
                            JSONObject object = new JSONObject(responseCode.toString());
                            Log.e("AddressResponse", "--->>" + object.toString(2));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        parseResult(responseCode.toString());

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), OrderHistoryActivity.this);
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
                Resources.clear();
                JSONArray jsonArray=response.getJSONArray("orderList");
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject object=jsonArray.getJSONObject(i);
                    OrderHistoryResources item = new OrderHistoryResources();

                    item.setOrderId(object.getString("orderId"));
                    item.setOrderQuantity(object.getString("orderQuantity"));
                    item.setOrderStatus(object.getString("orderStatus"));
                    item.setOrderCreated(object.getString("orderCreated"));
                    item.setDeliveron(object.getString("orderDate"));
                    item.setHouseNo(object.getString("houseNo"));
                    item.setStreet(object.getString("street"));
                    item.setProductImage1(object.getString("productImage1"));
                    item.setPaymentType(object.getString("paymentType"));
                    item.setLandmark(object.getString("landmark"));
                    item.setCity(object.getString("city"));
                    item.setState(object.getString("state"));
                    item.setPincode(object.getString("pincode"));
                    item.setCountry(object.getString("country"));
                    item.setProductName(object.getString("productName"));
                    item.setProductId(object.getString("productId"));
                    item.setProductPrice(object.getString("productPrice"));
                    item.setProductsplPrice(object.getString("ProductsplPrice"));
                    item.setProcessingDate(object.getString("processingDate"));
                    item.setDispatchDate(object.getString("dispatchDate"));
                    item.setShippingDate(object.getString("shippingDate"));
                    item.setDeliveredDate(object.getString("deliveredDate"));
                    Resources.add(item);
                }
                if(Resources!=null)
                {
                    Collections.reverse(Resources);
                    mAdapter.notifyDataSetChanged();

                }
                llCallbox.setVisibility(View.GONE);

            }else {
                llCallbox.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



}
