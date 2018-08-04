package com.app.shopchatmyworldra;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.MyApplication;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.BrandResources;
import com.app.shopchatmyworldra.pojo.SubCategoryResources;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by legacy on 02-Sep-17.
 */

public class ActivityFilter extends AppCompatActivity{

    private LinearLayout llApplay;
    ArrayList<SubCategoryResources> subcategorytypeList = new ArrayList<SubCategoryResources>();
    ArrayList<String> brandlist = new ArrayList<String>();
    ArrayList<BrandResources> brandtypeList = new ArrayList<BrandResources>();
    Spinner spin_brand;
 /*   TextView tvMin;
    TextView tvMax;*/
    EditText etLocation;
    private String subcatId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        subcatId=getIntent().getStringExtra("subcatId");
        llApplay=(LinearLayout)findViewById(R.id.llApplay);
        spin_brand = (Spinner) findViewById(R.id.spin_brand);
      /*  tvMax = (TextView) findViewById(R.id.tvMax);
        tvMin = (TextView) findViewById(R.id.tvMin);*/
        etLocation = (EditText) findViewById(R.id.etLocation);
        // Setup the new range seek bar

        // Seek bar for which we will set text color in code
        RangeSeekBar rangeSeekBarTextColorWithCode = (RangeSeekBar) findViewById(R.id.rangeSeekBarTextColorWithCode);
        rangeSeekBarTextColorWithCode.setTextAboveThumbsColorResource(android.R.color.holo_blue_bright);
        rangeSeekBarTextColorWithCode.setRangeValues(0,100000);
        rangeSeekBarTextColorWithCode.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                Log.e("value", minValue + "  " + maxValue);
              /*  tvMin.setText("Min " + minValue);
                tvMax.setText("Max " + maxValue);*/
                MyApplication.minPrice=String.valueOf(minValue);
                MyApplication.maxPrice=String.valueOf(maxValue);


            }

        });

        spin_brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if(pos>0)
                {
                    MyApplication.brandId=brandtypeList.get(pos-1).getBrandId();


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        llApplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.location=etLocation.getText().toString().trim();
                MyApplication.ActivityFilter="1";
                MyApplication.subcatId=subcatId;
                ActivityFilter.this.finish();


            }
        });
        ValidateGetBrand();

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
                Intent intent=new Intent(ActivityFilter.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });


    }

    // Get Brand AsynTask

    protected void ValidateGetBrand() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.add("subcatId", subcatId);
        client.post(WebServices.GetBrand, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(ActivityFilter.this,"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.e("Response", "--->>" + response.toString());
                        parseResultbrand(response.toString(), ActivityFilter.this);
                        ProgressBarDialog.dismissProgressDialog();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), ActivityFilter.this);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();

                    }

                });

    }

    private void parseResultbrand(String response, Context LognActivity) {

        JSONObject object;

        if (response != null) {
            try {

                object = new JSONObject(response);
                String success = object.getString("responseCode");
                String message = object.getString("responseMessage");

                Log.d("object", "" + object);
                if (success.equals("200")) {

                    Log.d("success", "" + success);
                    try {
                        JSONArray jsonarry = object.getJSONArray("brand");
                        brandlist.clear();
                        brandtypeList.clear();
                        brandlist.add("Select Brand Type");
                        for (int i = 0; i < jsonarry.length(); i++) {
                            BrandResources details = new BrandResources();

                            JSONObject jsonObject2 = jsonarry.getJSONObject(i);
                            details.setBrandId(jsonObject2.getString("brandId"));
                            details.setBrandName(jsonObject2.getString("brandName"));

                            brandtypeList.add(details);
                            brandlist.add(jsonObject2.getString("brandName"));
                        }

                        Log.d("brandlist", "" + brandlist.size());

                        ArrayAdapter spin_brandlist = new ArrayAdapter(this, android.R.layout.simple_spinner_item, brandlist);
                        spin_brandlist.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_brand.setAdapter(spin_brandlist);


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
