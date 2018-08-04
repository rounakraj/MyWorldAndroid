package com.app.shopchatmyworldra;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.shopchatmyworldra.adapter.DealsAdapter;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.GPSTracker;
import com.app.shopchatmyworldra.constant.MyApplication;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.DealsResources;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by legacy on 17-Nov-17.
 */

public class SellActivity extends AppCompatActivity {

    private ImageView ivBackArrow;
    private TextView tvCartItem;
    RecyclerView recyclerView;
    private ArrayList<DealsResources> serviceListData = new ArrayList<DealsResources>();
    DealsAdapter mAdapter;
    private GPSTracker gps;
    double latitude, longitude;
    private ImageView ivAddPoast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        gps = new GPSTracker(SellActivity.this);
        getLatLong();

        ivBackArrow = (ImageView) findViewById(R.id.ivBackArrow);

        ivAddPoast = (ImageView)findViewById(R.id.ivAddPoast);
        tvCartItem = (TextView) findViewById(R.id.tvCartItem);

        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                MyApplication.isBackSell=true;

            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);

        GridLayoutManager llm = new GridLayoutManager(SellActivity.this, 2);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);




        if(CommanMethod.isOnline(SellActivity.this))
        {
            validateShowCart();
            GETSellProduct();
        }else {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.fab), "No Internet  Connection", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(SellActivity.this,R.color.colorPrimary));
            snackbar.show();
        }


        ivAddPoast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(SellActivity.this, PostsAdsActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });



        ImageView ivHome=(ImageView)findViewById(R.id.ivHome);
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_lowtohigh:
                sortProduct("Low To High");
                return true;
            case R.id.action_hightolow:
                sortProduct("High To Low");
                return true;
            case R.id.action_popular:
                sortProduct("Popular");
                return true;
            case R.id.action_new:
                sortProduct("New");
                return true;
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    private void validateShowCart() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(SellActivity.this).getUserId());
        client.post(WebServices.showCart, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        parseResult(responseCode.toString());

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


    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {
                JSONArray jsonArray = response.getJSONArray("cartList");
                int size = jsonArray.length();
                tvCartItem.setText(String.valueOf(size));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void GETSellProduct() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userLat", String.valueOf(latitude));
        params.add("userLong", String.valueOf(longitude));
        params.add("userId", MyPreferences.getActiveInstance(SellActivity.this).getUserId());
        client.post(WebServices.getnearbyproduct, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        ProgressBarDialog.showProgressBar(SellActivity.this,"");
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        ProgressBarDialog.dismissProgressDialog();
                        parseResultSell(responseCode.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), SellActivity.this);
                        ProgressBarDialog.dismissProgressDialog();
                        Log.e("MessageRanjeet",">>>>>>>"+errorResponse.toString());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();
                    }

                });

    }


    private void parseResultSell(String result) {

        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {
                if (response.get("productUser1") instanceof JSONArray) {

                    DealsResources mRide = null;
                    JSONArray jsonArray = null;
                    jsonArray = response.getJSONArray("productUser1");
                    JSONArray jsonArray1 = null;
                    jsonArray1 = response.getJSONArray("productUser2");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        mRide = new DealsResources();
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
                            mRide.setPrice("INR " + obj.getString("productPrice").toString().trim());
                        }

                        serviceListData.add(mRide);
                    }
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        mRide = new DealsResources();
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
                            mRide.setPrice("INR " + obj.getString("productPrice").toString().trim());
                        }
                        serviceListData.add(mRide);
                    }
                    if (serviceListData != null) {
                        mAdapter = new DealsAdapter(SellActivity.this, serviceListData);
                        recyclerView.setAdapter(mAdapter);
                    }
                } else {

                    CommanMethod.showAlert("No Product Found.", SellActivity.this);
                }
            } else {

                CommanMethod.showAlert(message, SellActivity.this);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private void sortProduct(String sortby) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("sort_type", sortby);
        client.post(WebServices.sortsellProduct, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        ProgressBarDialog.showProgressBar(SellActivity.this,"");
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        ProgressBarDialog.dismissProgressDialog();
                        parseResultsortProduct(responseCode.toString());


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), SellActivity.this);
                        ProgressBarDialog.dismissProgressDialog();
                    }

                    @Override
                    public void onFinish() {
                        ProgressBarDialog.dismissProgressDialog();
                        super.onFinish();
                    }

                });

    }

    private void parseResultsortProduct(String result) {
        try {
            JSONObject response = new JSONObject(result);
            Log.e("SortBySell",">>>>>>"+response.toString(2));
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {
                serviceListData.clear();
                JSONArray jsonArray = response.getJSONArray("productAll");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    DealsResources resource = new DealsResources();
                    resource.setProductid(object.getString("productId"));
                    resource.setName(object.getString("productName"));
                    resource.setImage(object.getString("productImage1"));
                    resource.setPrice(object.getString("productPrice"));
                    resource.setUsername(object.getString("userName"));
                    resource.setDiscounttext(object.getString("Percent"));
                    serviceListData.add(resource);
                }
                if (serviceListData != null) {
                    mAdapter.notifyDataSetChanged();

                }

            } else {
                CommanMethod.showAlert(message, SellActivity.this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //*//***for getting lattitude and longitude****//*///
    private void getLatLong() {

        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            //drawMarkerforcurrentlocations(gps.getLatitude(), gps.getLongitude());
            Log.d("latitude....=", ">>>>>>>>>>>" + latitude);
            Log.d("longitude....=", ">>>>>>>>>>" + longitude);
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

}
