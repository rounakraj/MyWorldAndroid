package com.app.shopchatmyworldra;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import cz.msebera.android.httpclient.Header;
import instamojo.library.InstapayListener;
import instamojo.library.InstamojoPay;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.IntentFilter;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.widget.Button;

import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.OrderListPlace;
import com.app.shopchatmyworldra.pojo.SimilarList;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

/**
 * Created by bhupeshkathuria on 21/01/18.
 */

public class PaymentTypeActivity extends AppCompatActivity {
    private AppCompatEditText nameBox, emailBox, phoneBox, amountBox, descriptionBox;
    public  Snackbar snackbar;
    private  String amount;
    private String cartId;
    private String ProductId;
    private String AddressId;
    ArrayList<SimilarList> similarLists = new ArrayList<>();
    ArrayList<OrderListPlace> orderListPlace = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_information);
        // Call the function callInstamojo to start payment here
        amount = getIntent().getStringExtra("amount");

        cartId = getIntent().getStringExtra("cartId");
        ProductId = getIntent().getStringExtra("prductId");
        AddressId = getIntent().getStringExtra("addressId");

        nameBox = (AppCompatEditText) findViewById(R.id.name);
        nameBox.setSelection(nameBox.getText().toString().trim().length());
        emailBox = (AppCompatEditText) findViewById(R.id.email);
        emailBox.setSelection(emailBox.getText().toString().trim().length());
        phoneBox = (AppCompatEditText) findViewById(R.id.phone);
        phoneBox.setSelection(phoneBox.getText().toString().trim().length());
        amountBox = (AppCompatEditText) findViewById(R.id.amount);
        amountBox.setSelection(amountBox.getText().toString().trim().length());
        descriptionBox = (AppCompatEditText) findViewById(R.id.description);
        descriptionBox.setSelection(descriptionBox.getText().toString().trim().length());
        snackbar = Snackbar.make(findViewById(R.id.fab), "No Internet  Connection", Snackbar.LENGTH_LONG);
        Button button = (Button) findViewById(R.id.pay);



        amountBox.setText(amount);
        amountBox.setEnabled(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommanMethod.isOnline(PaymentTypeActivity.this)) {
                    if (validationPaymet()) {
                        String EmialId = emailBox.getText().toString().trim();
                        String phone = phoneBox.getText().toString().trim();
                        String amount = amountBox.getText().toString().trim();
                        String purpose = descriptionBox.getText().toString().trim();
                        String buyername = nameBox.getText().toString().trim();
                        callInstamojoPay(EmialId,phone,amount,purpose,buyername);
                    }
                } else {
                    View sbView =snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(ContextCompat.getColor(PaymentTypeActivity.this,R.color.colorAccent));
                    snackbar.show();
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
                Intent intent=new Intent(PaymentTypeActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });


    }
    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
        final Activity activity = this;
        InstamojoPay instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        registerReceiver(instamojoPay, filter);
        JSONObject pay = new JSONObject();
        try {
            pay.put("email", email);
            pay.put("phone", phone);
            pay.put("purpose", purpose);
            pay.put("amount", amount);
            pay.put("name", buyername);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initListener();
        instamojoPay.start(activity, pay, listener);
    }

    InstapayListener listener;


    private void initListener() {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG)
                        .show();

                validatePlaceOrder();
                try{

                    JSONObject object = new JSONObject(response.toString());

                    String status=object.getString("success");
                    String orderId=object.getString("orderId");
                    String paymentId=object.getString("paymentId");



                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }

            @Override
            public void onFailure(int code, String reason) {
                Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG)
                        .show();
            }
        };
    }



    private void validatePlaceOrder() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(PaymentTypeActivity.this).getUserId());
        params.add("addressId", AddressId);
        //params.add("cartId",cartId);
        params.add("cartId", cartId.substring(0, cartId.length() - 1));
        client.post(WebServices.PlaceOrder, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(PaymentTypeActivity.this,"");
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
                                Toast.makeText(PaymentTypeActivity.this, Message, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PaymentTypeActivity.this, ActivityOrderPlaced.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("similarLists", similarLists);
                                intent.putExtra("orderListPlace", orderListPlace);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                MainActivity.tvCartItem.setText("0");
                                PaymentTypeActivity.this.finish();
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
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), PaymentTypeActivity.this);

                    }

                    @Override
                    public void onFinish() {
                        ProgressBarDialog.dismissProgressDialog();
                        super.onFinish();
                    }

                });

    }





    private boolean validationPaymet() {
        if (nameBox.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter your name.", PaymentTypeActivity.this);
            return false;

        }else if (emailBox.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter your email.", PaymentTypeActivity.this);
            return false;
        }else if (!CommanMethod.isEmailValid(emailBox.getText().toString().trim())) {
            CommanMethod.showAlert("Please enter valid Email", PaymentTypeActivity.this);
            return false;
        }else if (phoneBox.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter your phone no.", PaymentTypeActivity.this);
            return false;
        }else if (descriptionBox.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter your purpose.", PaymentTypeActivity.this);
            return false;
        }
        return true;

    }




}
