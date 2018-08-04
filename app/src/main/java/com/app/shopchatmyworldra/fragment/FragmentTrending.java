package com.app.shopchatmyworldra.fragment;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.adapter.TrendingAdapter;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.TrendingResources;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FragmentTrending extends Fragment {
    private ArrayList<TrendingResources> serviceListData = new ArrayList<TrendingResources>();
    TrendingAdapter mAdapter;
    RecyclerView recyclerView;
    public Snackbar snackbar;
    private String onlineFlag = "";
    private View view;
    private Activity mactivity;
    @Override
    public void onAttach(Activity activity) {
        mactivity = activity;
        super.onAttach(activity);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_trending, null);
        //snackbar = Snackbar.make(view.findViewById(R.id.fab), "No Internet  Connection", Snackbar.LENGTH_LONG);
        if (CommanMethod.isOnline(mactivity)) {

            GETtrendingitem();
        } else {
            /*View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(mactivity, R.color.colorAccent));
            snackbar.show();*/
        }


        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);

        GridLayoutManager llm = new GridLayoutManager(mactivity, 2);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        return view;
    }

    private void GETtrendingitem() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(mactivity).getUserId());
        client.post(WebServices.gettrendingProduct, params,
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

                    JSONArray jsonArray = response.getJSONArray("productAll");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TrendingResources mRide = new TrendingResources();
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
                    if (serviceListData != null) {
                        mAdapter = new TrendingAdapter(mactivity, serviceListData);
                        recyclerView.setAdapter(mAdapter);
                    }

            }else {
                CommanMethod.showAlert(message,mactivity);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onResume() {
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
        Log.e("onlineFlag", "" + onlineFlag);
        params.add("userId", MyPreferences.getActiveInstance(mactivity).getUserId());
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


}
