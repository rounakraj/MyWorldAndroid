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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.app.shopchatmyworldra.adapter.SubCategoryAdapter;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.MyApplication;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.PRODUCTBYSUBCAT;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by legacy on 16-Aug-17.
 */

public class ActivitySortBy extends AppCompatActivity implements SubCategoryAdapter.categorybyId {
    private View view;
    private ArrayList<PRODUCTBYSUBCAT> subCategoryResources;
    SubCategoryAdapter mAdapter;
    RecyclerView recyclerView;
    String subcatId;
    //private RadioGroup radioSort;
    //private RadioButton radioChild;
    private LinearLayout llFilter;
    private LinearLayout llSortbyLayout;
    private RadioButton rbLowToHigh;
    private RadioButton rbHighToLow;
    private RadioButton rbPopular;
    private RadioButton rbNew;

    private LinearLayout llSortBy;
    private LinearLayout btnSend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sortby);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        subcatId = getIntent().getStringExtra("subcatId");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        llFilter = (LinearLayout) findViewById(R.id.llFilter);
        //radioSort = (RadioGroup) findViewById(R.id.radioSort);
        llSortbyLayout = (LinearLayout) findViewById(R.id.llSortbyLayout);

        rbLowToHigh = (RadioButton) findViewById(R.id.rbLowToHigh);
        rbHighToLow = (RadioButton) findViewById(R.id.rbHighToLow);
        rbPopular = (RadioButton) findViewById(R.id.rbPopular);
        rbNew = (RadioButton) findViewById(R.id.rbNew);



        llSortBy = (LinearLayout) findViewById(R.id.llSortBy);
        btnSend = (LinearLayout) findViewById(R.id.btnSend);
        GridLayoutManager llm = new GridLayoutManager(ActivitySortBy.this, 2);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        subCategoryResources = new ArrayList<>();
        mAdapter = new SubCategoryAdapter(ActivitySortBy.this, subCategoryResources, ActivitySortBy.this);
        recyclerView.setAdapter(mAdapter);

        llFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitySortBy.this, ActivityFilter.class);
                intent.putExtra("subcatId", subcatId);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        llSortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llSortbyLayout.getVisibility() == View.VISIBLE) {
                    // Its visible
                    llSortbyLayout.setVisibility(View.GONE);
                    //btnSend.setVisibility(View.GONE);
                } else {
                    // Either gone or invisible
                    //btnSend.setVisibility(View.VISIBLE);
                    llSortbyLayout.setVisibility(View.VISIBLE);

                }
            }
        });

        rbLowToHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortProduct("Low To High");
                rbLowToHigh.setChecked(true);
                rbHighToLow.setChecked(false);
                rbPopular.setChecked(false);
                rbNew.setChecked(false);
            }
        });
        rbHighToLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortProduct("High To Low");

                rbLowToHigh.setChecked(false);
                rbHighToLow.setChecked(true);
                rbPopular.setChecked(false);
                rbNew.setChecked(false);
            }
        });
        rbPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortProduct("Popular");
                rbLowToHigh.setChecked(false);
                rbHighToLow.setChecked(false);
                rbPopular.setChecked(true);
                rbNew.setChecked(false);
            }
        });
        rbNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortProduct("New");
                rbLowToHigh.setChecked(false);
                rbHighToLow.setChecked(false);
                rbPopular.setChecked(false);
                rbNew.setChecked(true);
            }
        });

     /*   btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId=radioSort.getCheckedRadioButtonId();
                radioChild=(RadioButton)findViewById(selectedId);
                Toast.makeText(ActivitySortBy.this,radioChild.getText(),Toast.LENGTH_SHORT).show();

                Toast.makeText(ActivitySortBy.this, radioChild.getText(), Toast.LENGTH_SHORT).show();

            }
        });*/
        validateSubGETCATGORY();
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
                Intent intent=new Intent(ActivitySortBy.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

    }

    private void validateSubGETCATGORY() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("subcatId", subcatId);
        client.post(WebServices.GETPRODUCTBYSUBCAT, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(ActivitySortBy.this,"");
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
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), ActivitySortBy.this);
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
                subCategoryResources.clear();
                JSONArray jsonArray = response.getJSONArray("productServer");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    PRODUCTBYSUBCAT resource = new PRODUCTBYSUBCAT();
                    resource.setProductId(object.getString("productId"));
                    resource.setProductName(object.getString("productName"));
                    resource.setProductImage1(object.getString("productImage1"));
                    resource.setUserName(object.getString("userName"));
                    resource.setProductPrice(object.getString("productPrice"));
                    resource.setPercent(object.getString("Percent"));

                    subCategoryResources.add(resource);
                }
                JSONArray jsonArray1 = response.getJSONArray("productUser");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject object = jsonArray1.getJSONObject(i);
                    PRODUCTBYSUBCAT resource = new PRODUCTBYSUBCAT();
                    resource.setProductId(object.getString("productId"));
                    resource.setProductName(object.getString("productName"));
                    resource.setProductImage1(object.getString("productImage1"));
                    resource.setUserName(object.getString("userName"));
                    resource.setProductPrice(object.getString("productPrice"));
                    resource.setPercent(object.getString("Percent"));
                    subCategoryResources.add(resource);
                }
                if (subCategoryResources != null) {
                    mAdapter.notifyDataSetChanged();

                }

            } else {
                CommanMethod.showAlert(message, ActivitySortBy.this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void getId(String productId, String userName) {
        Intent intent = new Intent(ActivitySortBy.this, ProductDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("productId", productId);
        intent.putExtra("userName", userName);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }


    @Override
    public void onResume() {
        super.onResume();

        if (MyApplication.ActivityFilter.equals("1")) {
            Filter();
        }
    }

    private void Filter() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("minPrice", MyApplication.minPrice);
        params.add("maxPrice", MyApplication.maxPrice);
        params.add("brandId", MyApplication.brandId);
        params.add("location", MyApplication.location);
        params.add("subcatId", MyApplication.subcatId);
        client.post(WebServices.filterProduct, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(ActivitySortBy.this,"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        parseResultFilter(responseCode.toString());
                        ProgressBarDialog.dismissProgressDialog();


                        MyApplication.ActivityFilter = "0";
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), ActivitySortBy.this);
                    }

                    @Override
                    public void onFinish() {
                        ProgressBarDialog.dismissProgressDialog();
                        super.onFinish();
                    }

                });

    }

    private void parseResultFilter(String result) {
        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            Log.e("responseMessageFillter", ">>>" + response.toString(2));
            if (response.getString("responseCode").equals("200")) {
                subCategoryResources.clear();
                MyApplication.minPrice = "";
                MyApplication.maxPrice = "";
                MyApplication.brandId = "";
                MyApplication.location = "";
                MyApplication.subcatId = "";
                MyApplication.ActivityFilter = "";
                JSONArray jsonArray = response.getJSONArray("productServer");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    PRODUCTBYSUBCAT resource = new PRODUCTBYSUBCAT();
                    resource.setProductId(object.getString("productId"));
                    resource.setProductName(object.getString("productName"));
                    resource.setProductImage1(object.getString("productImage1"));
                    resource.setProductPrice(object.getString("productPrice"));
                    resource.setUserName(object.getString("userName"));
                    resource.setPercent(object.getString("Percent"));

                    subCategoryResources.add(resource);
                }
                JSONArray jsonArray1 = response.getJSONArray("productUser");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject object = jsonArray1.getJSONObject(i);
                    PRODUCTBYSUBCAT resource = new PRODUCTBYSUBCAT();
                    resource.setProductId(object.getString("productId"));
                    resource.setProductName(object.getString("productName"));
                    resource.setProductImage1(object.getString("productImage1"));
                    resource.setProductPrice(object.getString("productPrice"));
                    resource.setUserName(object.getString("userName"));

                    subCategoryResources.add(resource);
                }
                if (subCategoryResources != null) {
                    mAdapter.notifyDataSetChanged();

                }

            } else {
                CommanMethod.showAlert(message, ActivitySortBy.this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void sortProduct(String sortby) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("sort_type", sortby);
        params.add("subcatId", subcatId);
        client.post(WebServices.sortProduct, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(ActivitySortBy.this,"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        parseResultsortProduct(responseCode.toString());
                        ProgressBarDialog.dismissProgressDialog();


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), ActivitySortBy.this);
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
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {
                subCategoryResources.clear();
                llSortbyLayout.setVisibility(View.GONE);
                btnSend.setVisibility(View.GONE);
                JSONArray jsonArray = response.getJSONArray("productAll");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    PRODUCTBYSUBCAT resource = new PRODUCTBYSUBCAT();
                    resource.setProductId(object.getString("productId"));
                    resource.setProductName(object.getString("productName"));
                    resource.setProductImage1(object.getString("productImage1"));
                    resource.setProductPrice(object.getString("productPrice"));
                    resource.setUserName(object.getString("userName"));
                    resource.setPercent(object.getString("Percent"));

                    subCategoryResources.add(resource);
                }
               /* JSONArray jsonArray1 = response.getJSONArray("productUser");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject object = jsonArray1.getJSONObject(i);
                    PRODUCTBYSUBCAT resource = new PRODUCTBYSUBCAT();
                    resource.setProductId(object.getString("productId"));
                    resource.setProductName(object.getString("productName"));
                    resource.setProductImage1(object.getString("productImage1"));
                    resource.setProductPrice(object.getString("productPrice"));
                    resource.setUserName(object.getString("userName"));
                    resource.setPercent(object.getString("Percent"));

                    subCategoryResources.add(resource);
                }*/
                if (subCategoryResources != null) {
                    mAdapter.notifyDataSetChanged();

                }

            } else {
                CommanMethod.showAlert(message, ActivitySortBy.this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
