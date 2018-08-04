package com.app.shopchatmyworldra.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.shopchatmyworldra.MainActivity;
import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.adapter.WishListRecyclerViewAdapter;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.WishListResources;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by MMAD on 09-07-2017.
 */
public class FragmentWishList extends Fragment implements WishListRecyclerViewAdapter.dislikecall, WishListRecyclerViewAdapter.movetocart {
    private View view;
    private ArrayList<WishListResources> serviceListData;
    private WishListRecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;
    private Activity mcontext;
    private LinearLayout llCallbox;
    private TextView tvYourCartIsEmaty;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        mcontext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_wish_list, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        llCallbox = (LinearLayout) view.findViewById(R.id.llCallbox);
        tvYourCartIsEmaty = (TextView) view.findViewById(R.id.tvYourCartIsEmaty);


        GridLayoutManager llm = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        serviceListData = new ArrayList<>();

        mAdapter = new WishListRecyclerViewAdapter(getActivity(), serviceListData, FragmentWishList.this, FragmentWishList.this);
        recyclerView.setAdapter(mAdapter);

        if (CommanMethod.isOnline(mcontext)) {
            GETWISHLIST();
        } else {
            View sbView = MainActivity.snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(mcontext, R.color.colorPrimary));
            MainActivity.snackbar.show();
        }
        llCallbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent=new Intent(mcontext, BuyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(intent);*/
            }
        });

        return view;

    }

    private void GETWISHLIST() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(mcontext).getUserId());
        client.post(WebServices.GETWISHLIST, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(getActivity(),"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        parseResult(responseCode.toString());
                        ProgressBarDialog.dismissProgressDialog();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
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

                JSONArray jsonArray = response.getJSONArray("productlist");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    WishListResources resources = new WishListResources();
                    if (object.getString("productId") != null && !object.getString("productId").equals("")) {
                        resources.setProductId(object.getString("productId"));
                    }
                    if (object.getString("productName") != null && !object.getString("productName").equals("")) {
                        resources.setProductName(object.getString("productName"));
                    }

                    if (object.getString("productImage1") != null && !object.getString("productImage1").equals("")) {
                        resources.setProductImage1(object.getString("productImage1"));
                    }
                    if (object.getString("userName") != null && !object.getString("userName").equals("")) {
                        resources.setUserName(object.getString("userName"));
                    }
                    if (object.getString("wishId") != null && !object.getString("wishId").equals("")) {
                        resources.setWishId(object.getString("wishId"));
                    }
                    if (object.getString("productPrice") != null && !object.getString("productPrice").equals("")) {
                        resources.setProductPrice(object.getString("productPrice"));
                    }
                    if (object.getString("Percent") != null && !object.getString("Percent").equals("")) {
                        resources.setPercent(object.getString("Percent"));
                    }
                    serviceListData.add(resources);
                }
                mAdapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);
                llCallbox.setVisibility(View.GONE);

            } else {
                recyclerView.setVisibility(View.GONE);
                tvYourCartIsEmaty.setText(message);
                llCallbox.setVisibility(View.VISIBLE);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getIdNamedislike(String productId, String Name, int Position) {

        DISLIKEWISHLIST(productId, Name, Position);
    }

    private void DISLIKEWISHLIST(String productId, String Name, final int Position) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(mcontext).getUserId());
        params.add("productId", productId);
        params.add("userName", Name);
        client.post(WebServices.DISLIKEWISHLIST, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(getActivity(),"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        parseResult1(responseCode.toString(), Position);
                        ProgressBarDialog.dismissProgressDialog();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                    }

                    @Override
                    public void onFinish() {
                        ProgressBarDialog.dismissProgressDialog();
                        super.onFinish();
                    }

                });
    }


    private void parseResult1(String result, int Position) {

        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {
                CommanMethod.showAlert(message, getActivity());
                serviceListData.remove(Position);
                mAdapter.notifyDataSetChanged();

            } else {

                CommanMethod.showAlert(message, getActivity());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void movecart(String productId, String quantity) {
        if (quantity.equals("0")) {
            CommanMethod.showAlert("Please select Quantity.", mcontext);
        } else {
            validateAddCart(productId, quantity);
        }
    }

    private void validateAddCart(String productId, final String quantity) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("productId", productId);
        params.add("userId", MyPreferences.getActiveInstance(mcontext).getUserId());
        params.add("quantity", quantity);
        client.post(WebServices.addtoCart, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(getActivity(),"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        ProgressBarDialog.dismissProgressDialog();
                        try {
                            JSONObject object = new JSONObject(responseCode.toString());
                            Log.e("Response", "--->>" + object.toString(2));
                            String message = object.getString("responseMessage");
                            if (object.getString("responseCode").equals("200")) {
                                Toast.makeText(mcontext, message, Toast.LENGTH_SHORT).show();
                                int cart = Integer.parseInt(MainActivity.tvCartItem.getText().toString());
                                int totarcart = cart + 1;
                                MainActivity.tvCartItem.setText(String.valueOf(totarcart));

                            } else {
                                CommanMethod.showAlert(message, mcontext);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();
                    }

                });

    }


}
