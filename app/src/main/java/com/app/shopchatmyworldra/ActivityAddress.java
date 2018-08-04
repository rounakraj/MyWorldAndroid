package com.app.shopchatmyworldra;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.shopchatmyworldra.adapter.AddressAdapter;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.MyApplication;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.AddressResources;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by legacy on 21-Aug-17.
 */

public class ActivityAddress extends AppCompatActivity implements AddressAdapter.addressId1,AddressAdapter.addressId,AddressAdapter.setaddressDefault{

    private View view;
    RecyclerView recyclerView;
    private ArrayList<AddressResources> Resources;
    private AddressAdapter mAdapter;
    private Button btnNext;
    private FloatingActionButton fab;
    private String cartId,sendcartId;
    private String ProductId;
    private String AddressId;
    private String amount;
    private int Position;
    public Snackbar snackbar;
    public static ActivityAddress activityAddress;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        activityAddress=this;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_rewards);
        btnNext=(Button)findViewById(R.id.btnNext);

        //llm.setOrientation(LinearLayoutManager.VERTICAL);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        cartId=getIntent().getStringExtra("cartId");
        ProductId=getIntent().getStringExtra("prductId");
        amount=getIntent().getStringExtra("amount");
        LinearLayoutManager llm = new LinearLayoutManager(ActivityAddress.this);
        recyclerView.setLayoutManager(llm);
        Resources = new ArrayList<>();
        mAdapter = new AddressAdapter(ActivityAddress.this, Resources,ActivityAddress.this,ActivityAddress.this,ActivityAddress.this);
        recyclerView.setAdapter(mAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        snackbar = Snackbar.make(findViewById(R.id.fab1), "No Internet  Connection", Snackbar.LENGTH_LONG);
        if (cartId!=null&&cartId.endsWith(",")) {
            sendcartId = cartId.substring(0, cartId.length() - 1);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivityAddress.this, AddAdressActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        if(MyApplication.ActivityAddress)
        {
            btnNext.setVisibility(View.GONE);
        }else {
            btnNext.setVisibility(View.VISIBLE);
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(AddressId!=null&&Resources.get(Position).getDefaultType().equals("1"))
                {
                    Intent intent=new Intent(ActivityAddress.this,PaymentMethodActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("cartId",cartId);
                    intent.putExtra("prductId",ProductId);
                    intent.putExtra("addressId",AddressId);
                    intent.putExtra("amount",amount);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }else {
                    CommanMethod.showAlert("Please select address.",ActivityAddress.this);
                }

            }
        });
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
                Intent intent=new Intent(ActivityAddress.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();

        if (CommanMethod.isOnline(ActivityAddress.this)) {

            validateGETADDRESS();
        } else {
            View sbView =snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(ActivityAddress.this,R.color.colorAccent));
            snackbar.show();
        }


    }

    private void validateGETADDRESS() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(ActivityAddress.this).getUserId());
        client.post(WebServices.Get_Address, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(ActivityAddress.this,"");

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
                        ProgressBarDialog.dismissProgressDialog();


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), ActivityAddress.this);

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
                JSONArray jsonArray=response.getJSONArray("addressList");
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject object=jsonArray.getJSONObject(i);
                    AddressResources item = new AddressResources();
                    item.setAddressId(object.getString("addressId"));
                    item.setAddressNo("Address "+i++);
                    item.setMobileNo(object.getString("mobileNo"));
                    item.setHouseNo(object.getString("houseNo"));
                    item.setStreet(object.getString("street"));
                    item.setLandmark(object.getString("landmark"));
                    item.setCity(object.getString("city"));
                    item.setState(object.getString("state"));
                    item.setPincode(object.getString("pincode"));
                    item.setCountry(object.getString("country"));
                    item.setAdd_type(object.getString("add_type"));
                    item.setDefaultType(object.getString("defaultType"));
                    Resources.add(item);
                }
                if(Resources!=null)
                {
                    mAdapter.notifyDataSetChanged();

                }

            }else {
                CommanMethod.showAlert(message,ActivityAddress.this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void getAddressId1(String addressId, int mposition) {
        AddressId=addressId;
        Position=mposition;
    }

    @Override
    public void getAddressId(String addressId, int mposition) {
        REMOVEADDRESS(addressId,mposition);
    }

    @Override
    public void getsetaddressDefault(String addressId, int mposition) {
        setaddressDefault(addressId,mposition);
    }


    private void REMOVEADDRESS(String addressId,final int mposition) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(ActivityAddress.this).getUserId());
        params.add("addressId", addressId);
        client.post(WebServices.REMOVEADDRESS, params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(ActivityAddress.this,"");
                    }
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        try {
                            JSONObject object = new JSONObject(responseCode.toString());
                            Log.e("ReemoveAddressResponse", "--->>" + object.toString(2));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        parseResultReemove(responseCode.toString(),mposition);
                        ProgressBarDialog.dismissProgressDialog();

                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), ActivityAddress.this);
                    }
                    @Override
                    public void onFinish() {
                        ProgressBarDialog.dismissProgressDialog();
                        super.onFinish();
                    }

                });

    }
    private void parseResultReemove(String result,int mposition) {
        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {

                validateGETADDRESS();
            }else {
                CommanMethod.showAlert(message,ActivityAddress.this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void setaddressDefault(String addressId,final int mposition) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(ActivityAddress.this).getUserId());
        params.add("addressId", addressId);
        client.post(WebServices.setaddressDefault, params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(ActivityAddress.this,"");
                    }
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        try {
                            JSONObject object = new JSONObject(responseCode.toString());
                            Log.e("ReemoveAddressResponse", "--->>" + object.toString(2));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        parseResultsetaddressDefault(responseCode.toString(),mposition);
                        ProgressBarDialog.dismissProgressDialog();

                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), ActivityAddress.this);
                    }
                    @Override
                    public void onFinish() {
                        ProgressBarDialog.dismissProgressDialog();
                        super.onFinish();
                    }

                });
    }

    private void parseResultsetaddressDefault(String result,int mposition) {
        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {

                CommanMethod.showAlert(message,ActivityAddress.this);
                validateGETADDRESS();
            }else {
                CommanMethod.showAlert(message,ActivityAddress.this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
