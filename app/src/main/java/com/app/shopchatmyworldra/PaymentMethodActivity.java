package com.app.shopchatmyworldra;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.OrderListPlace;
import com.app.shopchatmyworldra.pojo.SimilarList;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by legacy on 12-Aug-17.
 */

public class PaymentMethodActivity extends AppCompatActivity {
    private LinearLayout llCash;
    private LinearLayout llCard;
    private ImageView ivCheck;
    private ImageView ivCheck1;
    private Button btnSubmit;
    private String cartId;
    private String ProductId;
    private boolean payment;
    private String AddressId;
    private String amount;
    ArrayList<SimilarList> similarLists = new ArrayList<>();
    ArrayList<OrderListPlace> orderListPlace = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectpaymentmethod);
        // Call the function callInstamojo to start payment here
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        ivCheck = (ImageView) findViewById(R.id.ivCheck);
        ivCheck1 = (ImageView) findViewById(R.id.ivCheck1);
        llCash = (LinearLayout) findViewById(R.id.llCash);
        llCard = (LinearLayout) findViewById(R.id.llCard);

        cartId = getIntent().getStringExtra("cartId");
        ProductId = getIntent().getStringExtra("prductId");
        AddressId = getIntent().getStringExtra("addressId");
        amount = getIntent().getStringExtra("amount");

        llCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivCheck.setVisibility(View.VISIBLE);
                ivCheck1.setVisibility(View.GONE);
                payment=true;


            }
        });
        llCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivCheck.setVisibility(View.GONE);
                ivCheck1.setVisibility(View.VISIBLE);
                //payment=true;
                Intent intent = new Intent(PaymentMethodActivity.this, PaymentTypeActivity.class);
                intent.putExtra("cartId",cartId);
                intent.putExtra("prductId",ProductId);
                intent.putExtra("addressId",AddressId);
                intent.putExtra("amount",amount);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AddressId != null&&payment) {
                    validatePlaceOrder();
                }else {
                    CommanMethod.showAlert("Please select payment",PaymentMethodActivity.this);
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
                Intent intent=new Intent(PaymentMethodActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });

    }


    private void validatePlaceOrder() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(PaymentMethodActivity.this).getUserId());
        params.add("addressId", AddressId);
        //params.add("cartId",cartId);
        params.add("cartId", cartId.substring(0, cartId.length() - 1));
        client.post(WebServices.PlaceOrder, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(PaymentMethodActivity.this,"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        try {
                            JSONObject object = new JSONObject(responseCode.toString());
                            Log.e("orderResponse", "--->>" + object.toString(2));
                            String Message = object.getString("responseMessage");
                            if (object.getString("responseCode").equals("200")) {

                                JSONArray orderListjsonArray = object.getJSONArray("orderList");
                                JSONArray similarListjsonArray = object.getJSONArray("similarList");

                                for (int i = 0; i < orderListjsonArray.length(); i++) {
                                    JSONObject orderListObject = orderListjsonArray.getJSONObject(i);
                                    OrderListPlace place = new OrderListPlace();
                                    place.setOrderQuantity(orderListObject.getString("orderQuantity"));
                                    place.setOrderStatus(orderListObject.getString("orderStatus"));
                                    place.setOrderDate(orderListObject.getString("orderDate"));
                                    place.setPaymentType(orderListObject.getString("paymentType"));
                                    place.setProductName(orderListObject.getString("productName"));
                                    place.setProductImage1(orderListObject.getString("productImage1"));
                                    place.setProductId(orderListObject.getString("productId"));
                                    place.setProductPrice(orderListObject.getString("productPrice"));
                                    place.setProductsplPrice(orderListObject.getString("productsplPrice"));
                                    orderListPlace.add(place);

                                }
                                for (int j = 0; j < similarListjsonArray.length(); j++) {
                                    JSONObject similarObject = similarListjsonArray.getJSONObject(j);
                                    SimilarList list = new SimilarList();
                                    list.setProductId(similarObject.getString("productId"));
                                    list.setProductName(similarObject.getString("productName"));
                                    list.setProductImage1(similarObject.getString("productImage1"));
                                    list.setUserName(similarObject.getString("userName"));
                                    list.setProductPrice(similarObject.getString("productPrice"));
                                    list.setProductsplPrice(similarObject.getString("ProductsplPrice"));
                                    similarLists.add(list);
                                }
                                Toast.makeText(PaymentMethodActivity.this, Message, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PaymentMethodActivity.this, ActivityOrderPlaced.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("similarLists", similarLists);
                                intent.putExtra("orderListPlace", orderListPlace);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                MainActivity.tvCartItem.setText("0");
                                PaymentMethodActivity.this.finish();
                                ActivityAddress.activityAddress.finish();
                                CartlistActivity.cartlistActivity.finish();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), PaymentMethodActivity.this);

                    }

                    @Override
                    public void onFinish() {
                        ProgressBarDialog.dismissProgressDialog();
                        super.onFinish();
                    }

                });

    }

}
