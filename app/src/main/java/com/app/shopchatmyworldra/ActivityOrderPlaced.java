package com.app.shopchatmyworldra;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.shopchatmyworldra.adapter.OrderPlaceAdapter;
import com.app.shopchatmyworldra.adapter.SimalarProductAdapter;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.NestedListView;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.OrderListPlace;
import com.app.shopchatmyworldra.pojo.SimilarList;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by legacy on 05-Sep-17.
 */

public class ActivityOrderPlaced extends AppCompatActivity {
    RecyclerView mRecyclerView;
    private ImageView facebook;
    private ImageView twiter;
    private ImageView watsup;
    private EditText etFeedback;
    private RatingBar ratingbar;
    TextView textView3;
    RecyclerView.LayoutManager mLayoutManager;
    SimalarProductAdapter simalarProductAdapter;
    OrderPlaceAdapter orderPlaceAdapter;
    ArrayList<SimilarList> similarList = new ArrayList<>();
    ArrayList<OrderListPlace> resources = new ArrayList<>();
    NestedListView recycler_orderlist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderplaced);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        textView3 = (TextView) findViewById(R.id.textView3);
        etFeedback = (EditText) findViewById(R.id.etFeedback);
        mRecyclerView = (RecyclerView) findViewById(R.id.reSimilarProduct);
        mRecyclerView.setHasFixedSize(true);
        // The number of Columns
        mLayoutManager = new LinearLayoutManager(ActivityOrderPlaced.this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        facebook = (ImageView) findViewById(R.id.facebook);
        twiter = (ImageView) findViewById(R.id.twiter);
        watsup = (ImageView) findViewById(R.id.watsup);
        ratingbar = (RatingBar) findViewById(R.id.ratingbar);
        recycler_orderlist = (NestedListView) findViewById(R.id.recycler_orderlist);
        recycler_orderlist.setEnabled(false);

        similarList=(ArrayList<SimilarList>) getIntent().getSerializableExtra("similarLists");
        resources=(ArrayList<OrderListPlace>) getIntent().getSerializableExtra("orderListPlace");

        simalarProductAdapter = new SimalarProductAdapter(ActivityOrderPlaced.this, similarList);
        mRecyclerView.setAdapter(simalarProductAdapter);

        orderPlaceAdapter = new OrderPlaceAdapter(ActivityOrderPlaced.this, resources);
        recycler_orderlist.setAdapter(orderPlaceAdapter);

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/path-to-your-image.jpg"));
                startActivity(Intent.createChooser(share, "Share Image"));

            }
        });

        watsup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/path-to-your-image.jpg"));
                share.setPackage("com.whatsapp");//package name of the app
                startActivity(Intent.createChooser(share, "Share Image"));
            }
        });
        twiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/path-to-your-image.jpg"));
                startActivity(Intent.createChooser(share, "Share Image"));
            }
        });

        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ActivityOrderPlaced.this, OrderHistoryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        etFeedback.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    String rating = String.valueOf(ratingbar.getRating());
                    String feedmesseage = etFeedback.getText().toString().trim();
                    if(feedmesseage!=null)
                    {
                        addFeedback(rating, feedmesseage);
                    }else {
                        CommanMethod.showAlert("Please fill feedback.",ActivityOrderPlaced.this);
                    }


                }
                return false;
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
                Intent intent=new Intent(ActivityOrderPlaced.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                 overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

    }


    private void addFeedback(String feedRating, String feedmesseage) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(ActivityOrderPlaced.this).getUserId());
        params.add("feedRating", feedRating);
        params.add("feedMsg", feedmesseage);
        client.post(WebServices.addFeedback, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(ActivityOrderPlaced.this,"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        try {
                            JSONObject object = new JSONObject(responseCode.toString());
                            Log.e("AddressResponse", "--->>" + object.toString(2));
                            ProgressBarDialog.dismissProgressDialog();

                            etFeedback.setText("");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), ActivityOrderPlaced.this);

                    }

                    @Override
                    public void onFinish() {
                        ProgressBarDialog.dismissProgressDialog();
                        super.onFinish();
                    }

                });
    }
}
