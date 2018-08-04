package com.app.shopchatmyworldra;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.app.shopchatmyworldra.adapter.CartListAdapter;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.MyApplication;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.CartListResourse;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by legacy on 18-Aug-17.
 */

public class CartlistActivity extends AppCompatActivity implements CartListAdapter.removeCart,CartListAdapter.movetowishlist,CartListAdapter.addQuanty {

    private RecyclerView mRecyclerView;
    private ArrayList<CartListResourse> cartListResourses=new ArrayList<>();
    private CartListAdapter mAdapter;
    private Button btnProced;
    private Button btnCountinue;
    private TextView tvCartEmapty,txttotalItem;
    public static TextView txttotalprice;
    StringBuilder stringBuilder;
    StringBuilder stringBuilder1;
    String totalprie,spltotalprie;
    int price,mPosition;
    public Snackbar snackbar;
    public static CartlistActivity cartlistActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartlist);
        cartlistActivity=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        btnProced = (Button) findViewById(R.id.btnProced);
        btnCountinue = (Button) findViewById(R.id.btnCountinue);
        tvCartEmapty = (TextView) findViewById(R.id.tvCartEmapty);
        txttotalprice = (TextView) findViewById(R.id.txttotalprice);
        txttotalItem = (TextView) findViewById(R.id.txttotalItem);
        LinearLayoutManager llm = new LinearLayoutManager(CartlistActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        snackbar = Snackbar.make(findViewById(R.id.fab), "No Internet  Connection", Snackbar.LENGTH_LONG);

        btnProced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cartListResourses.size()==0)
                {
                    CommanMethod.showAlert(" Cart is empty please add item",CartlistActivity.this);
                }else if(cartListResourses.get(mPosition).getQuantity().equals("Quantity")){

                    CommanMethod.showAlert("Please select quantity",CartlistActivity.this);
                }else{

                    Intent intent=new Intent(CartlistActivity.this,ActivityAddress.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("cartId",stringBuilder.toString());
                    intent.putExtra("prductId",stringBuilder1.toString());
                    intent.putExtra("amount",totalprie);
                    startActivity(intent);
                    MyApplication.ActivityAddress=false;
                }

            }
        });
        if (CommanMethod.isOnline(CartlistActivity.this)) {

            validateShowCart();
        } else {
            View sbView =snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(CartlistActivity.this,R.color.colorAccent));
            snackbar.show();
        }

        ImageView ivBackArrow = (ImageView) findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        btnCountinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        ImageView ivHome=(ImageView)findViewById(R.id.ivHome);
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CartlistActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });



    }

    private void validateShowCart() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(CartlistActivity.this).getUserId());
        client.post(WebServices.showCart, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(CartlistActivity.this,"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        try {
                            JSONObject object = new JSONObject(responseCode.toString());
                            Log.e("showCart", "--->>" + object.toString(2));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        parseResult(responseCode.toString());

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), CartlistActivity.this);
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
                cartListResourses.clear();
                tvCartEmapty.setVisibility(View.GONE);
                btnCountinue.setVisibility(View.GONE);
                btnProced.setVisibility(View.VISIBLE);
                stringBuilder=new StringBuilder();
                stringBuilder1=new StringBuilder();

                JSONArray jsonArray=response.getJSONArray("cartList");
                int size=jsonArray.length();
                MainActivity.tvCartItem.setText(String.valueOf(size));
                totalprie=response.getString("totalPrice");
                spltotalprie=response.getString("totalsplPrice");

                txttotalprice.setText("Total Price:- "+totalprie);
                txttotalprice.setVisibility(View.VISIBLE);
                txttotalItem.setText("ITEMS"+"("+String.valueOf(size)+")");
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject object=jsonArray.getJSONObject(i);
                    CartListResourse resource=new CartListResourse();
                    resource.setCartId(object.getString("cartId"));
                    stringBuilder.append(object.getString("cartId"));
                    stringBuilder.append(",");
                    resource.setQuantity(object.getString("quantity"));
                    resource.setProductId(object.getString("productId"));
                    stringBuilder1.append(object.getString("productId"));
                    stringBuilder1.append(",");
                    resource.setProductName(object.getString("productName"));
                    resource.setUserName(object.getString("userName"));
                    resource.setProductImage1(object.getString("productImage1"));
                    resource.setProductPrice(object.getString("productPrice"));
                    resource.setProductsplPrice(object.getString("productsplPrice"));
                    resource.setSinglePrice(object.getString("singlePrice"));
                    resource.setSinglesplPrice(object.getString("singlesplPrice"));
                    cartListResourses.add(resource);
                }
                MainActivity.tvCartItem.setText(String.valueOf(cartListResourses.size()));
                mAdapter = new CartListAdapter(CartlistActivity.this, cartListResourses,CartlistActivity.this,CartlistActivity.this,CartlistActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                if(cartListResourses!=null)
                {


                }

            }else {
                tvCartEmapty.setVisibility(View.VISIBLE);
                btnCountinue.setVisibility(View.VISIBLE);
                btnProced.setVisibility(View.GONE);
                 tvCartEmapty.setText(message);
                cartListResourses.clear();
                MainActivity.tvCartItem.setText(String.valueOf(cartListResourses.size()));
                mAdapter = new CartListAdapter(CartlistActivity.this, cartListResourses,CartlistActivity.this,CartlistActivity.this,CartlistActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                txttotalItem.setText("ITEMS"+"("+String.valueOf(cartListResourses.size())+")");
                txttotalprice.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void getcartId(String cartId,int position,String productprice) {
        validateReemoveCartlist(cartId,position);
        price=Integer.parseInt(productprice);



    }

    @Override
    public void getmovewishlist(String productid,String username,int position) {
        ADDWISHLIST(productid,username,position);
    }

    private void validateReemoveCartlist(String cartId,final int position) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(CartlistActivity.this).getUserId());
        params.add("cartId", cartId);
        client.post(WebServices.removeCart, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(CartlistActivity.this,"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        parseResultReemove(responseCode.toString(),position);


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), CartlistActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        ProgressBarDialog.dismissProgressDialog();
                        super.onFinish();
                    }

                });

    }

    private void parseResultReemove(String result,int position) {
        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {

                //CommanMethod.showAlert(message,CartlistActivity.this);
                cartListResourses.remove(position);
                if(cartListResourses.size()==0){
                    txttotalprice.setVisibility(View.GONE);
                    txttotalItem.setVisibility(View.GONE);
                    tvCartEmapty.setVisibility(View.VISIBLE);
                    btnCountinue.setVisibility(View.VISIBLE);
                    btnProced.setVisibility(View.GONE);
                    tvCartEmapty.setText(message);

                }
                txttotalprice.setText("Total Price:- "+(Integer.parseInt(totalprie)-price));
                mAdapter.notifyDataSetChanged();
                int size=cartListResourses.size();
                MainActivity.tvCartItem.setText(String.valueOf(cartListResourses.size()));
                txttotalItem.setText("ITEMS"+"("+String.valueOf(size)+")");

            }else {
                CommanMethod.showAlert(message,CartlistActivity.this);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void ADDWISHLIST(String productId, String Name,final int position) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(CartlistActivity.this).getUserId());
        params.add("productId", productId);
        params.add("userName",Name);
        client.post(WebServices.ADDWISHLIST, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(CartlistActivity.this,"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        parseResult1(responseCode.toString(),position);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), CartlistActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        ProgressBarDialog.dismissProgressDialog();
                        super.onFinish();
                    }

                });

    }


    private void parseResult1(String result,int position) {

        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {
                CommanMethod.showAlert(message,CartlistActivity.this);
                Log.e("CartList",">>>>>>>>"+response.getString("wishId").toString());
                validateShowCart();
            }else {

                CommanMethod.showAlert(message,CartlistActivity.this);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void getCartId(String cartId, String quantity,int position) {
        mPosition=position;
        editcart(cartId,quantity);
    }
    private void editcart(String cartId, String quantity) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(CartlistActivity.this).getUserId());
        params.add("cartId",cartId);
        params.add("quantity",quantity);
        client.post(WebServices.editcart, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(CartlistActivity.this,"");

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        editparseResult1(responseCode.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), CartlistActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        ProgressBarDialog.dismissProgressDialog();
                        super.onFinish();
                    }

                });

    }


    private void editparseResult1(String result) {

        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {
                //CommanMethod.showAlert(message,context);
                totalprie=response.getString("totalPrice");
                spltotalprie=response.getString("totalsplPrice");

                txttotalprice.setText("Total Price:- "+spltotalprie);

            }else {

                CommanMethod.showAlert(message,CartlistActivity.this);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
