package com.app.shopchatmyworldra;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.app.shopchatmyworldra.adapter.SubAdapter;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.SubCategoryResources;
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

public class ActivitySubCategory extends AppCompatActivity{

    ArrayList<SubCategoryResources> arrayList;
    SubAdapter subAdapter;
    private String catId;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager llm = new LinearLayoutManager(ActivitySubCategory.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        catId = getIntent().getStringExtra("catId");
        ValidateGetsubCategory();

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
                Intent intent=new Intent(ActivitySubCategory.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });


    }


    protected void ValidateGetsubCategory() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.add("catId", catId);
        client.post(WebServices.GETSUBCATEGORY, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(ActivitySubCategory.this,"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.e("Response", "--->>" + response.toString());
                        parseResultsubCatlist(response.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), ActivitySubCategory.this);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();

                    }

                });

    }

    private void parseResultsubCatlist(String response) {
        JSONObject object;
        if (response != null) {
            try {

                object = new JSONObject(response);
                String success = object.getString("responseCode");
                String message = object.getString("responseMessage");
                Log.d("object", "" + object);
                if (success.equals("200")) {
                    arrayList=new ArrayList<>();
                    try {
                        JSONArray jsonarry = object.getJSONArray("subcategory");
                        for (int i = 0; i < jsonarry.length(); i++) {
                            SubCategoryResources details = new SubCategoryResources();
                            JSONObject jsonObject2 = jsonarry.getJSONObject(i);
                            details.setSubcatId(jsonObject2.getString("subcatId"));
                            details.setSubcatName(jsonObject2.getString("subcatName"));
                            arrayList.add(details);
                        }

                        if(arrayList!=null)
                        {
                            subAdapter=new SubAdapter(ActivitySubCategory.this,arrayList);
                            mRecyclerView.setAdapter(subAdapter);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
