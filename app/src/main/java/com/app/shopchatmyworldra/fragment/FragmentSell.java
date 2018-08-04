package com.app.shopchatmyworldra.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.shopchatmyworldra.MainActivity;
import com.app.shopchatmyworldra.PostsAdsActivity;
import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.adapter.NewItemAdapter;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.NewItemResources;
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
 * Created by legacy on 01-Sep-17.
 */

public class FragmentSell extends Fragment {
    private View view;
    private ArrayList<NewItemResources> serviceListData = new ArrayList<NewItemResources>();
    NewItemAdapter mAdapter;
    RecyclerView recyclerView;
    private ProgressDialog prgDialog;
    private Activity mcontext;
    private ImageView ivAddPoast;
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        mcontext=context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sell, null);

        prgDialog = new ProgressDialog(mcontext);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);


        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        ivAddPoast = (ImageView) view.findViewById(R.id.ivAddPoast);

        GridLayoutManager llm = new GridLayoutManager(mcontext, 2);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        if (CommanMethod.isOnline(mcontext)) {
            GETPRODUCTNEWITEM();
        } else {
            View sbView = MainActivity.snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(mcontext, R.color.colorPrimary));
            MainActivity.snackbar.show();
        }


        ivAddPoast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(mcontext, PostsAdsActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(in);
                mcontext.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ivAddPoast.setVisibility(View.VISIBLE);
    }

    private void GETPRODUCTNEWITEM() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("show_type", "new");
        params.add("userId", MyPreferences.getActiveInstance(mcontext).getUserId());
        client.post(WebServices.GETPRODUCT, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        //prgDialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        parseResult(responseCode.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //prgDialog.hide();
                        //CommanMethod.showAlert(getResources().getString(R.string.connection_error), mcontext);
                    }

                    @Override
                    public void onFinish() {
                        //prgDialog.hide();
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
                        }
                        if (obj.getString("Percent").toString().trim().equals("null") || obj.getString("Percent").equals("")) {
                            mRide.setPrice("");
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
                        } if (obj.getString("Percent").toString().trim().equals("null") || obj.getString("Percent").equals("")) {
                            mRide.setPrice("");
                        } else {
                            mRide.setPercent(obj.getString("Percent").toString().trim());
                        }
                        serviceListData.add(mRide);
                    }
                    if (serviceListData != null) {
                        Collections.reverse(serviceListData);
                        mAdapter = new NewItemAdapter(mcontext, serviceListData);
                        recyclerView.setAdapter(mAdapter);
                    } else {
                    }
                } else {
                    CommanMethod.showAlert("No Product Found.", mcontext);
                }
            } else {

                CommanMethod.showAlert(message, mcontext);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
