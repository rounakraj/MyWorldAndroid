package com.app.shopchatmyworldra.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.shopchatmyworldra.AddAdressActivity;
import com.app.shopchatmyworldra.MainActivity;
import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.adapter.AddressAdapter;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.AddressResources;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by MMAD on 16-08-2017.
 */

public class AddressFragment  extends Fragment implements AddressAdapter.addressId,AddressAdapter.addressId1,AddressAdapter.setaddressDefault{
    private View view;
    RecyclerView recyclerView;
    private ArrayList<AddressResources> Resources;
    private AddressAdapter mAdapter;
    private Context mcontext;
    private ProgressDialog prgDialog;
    private FloatingActionButton fab;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mcontext=context;
    }

    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.address_book, null);

        MainActivity.linemain.setVisibility(View.VISIBLE);
        MainActivity.llTopLayout.setVisibility(View.GONE);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_rewards);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        prgDialog = new ProgressDialog(mcontext);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        Resources = new ArrayList<>();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcontext, AddAdressActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (CommanMethod.isOnline(mcontext)) {
            validateGETADDRESS();
        } else {
            View sbView = MainActivity.snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(mcontext, R.color.colorPrimary));
            MainActivity.snackbar.show();
        }

    }

    private void validateGETADDRESS() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(mcontext).getUserId());
        client.post(WebServices.Get_Address, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        prgDialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        try {
                            JSONObject object = new JSONObject(responseCode.toString());
                            Log.e("AddressResponse", "--->>" + object.toString(2));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        parseResult(responseCode.toString());

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        prgDialog.hide();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), mcontext);
                    }

                    @Override
                    public void onFinish() {
                        prgDialog.hide();
                        super.onFinish();
                    }

                });

    }


    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {
                Resources.clear();
                JSONArray jsonArray=response.getJSONArray("addressList");
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject object=jsonArray.getJSONObject(i);
                    AddressResources item = new AddressResources();
                    item.setAddressId(object.getString("addressId"));
                    item.setMobileNo(object.getString("mobileNo"));
                    item.setHouseNo(object.getString("houseNo"));
                    item.setStreet(object.getString("street"));
                    item.setLandmark(object.getString("landmark"));
                    item.setCity(object.getString("city"));
                    item.setState(object.getString("state"));
                    item.setPincode(object.getString("pincode"));
                    item.setCountry(object.getString("country"));
                    item.setAdd_type(object.getString("add_type"));
                    item.setDefaultType(object.getString("defaultType"));
                    Resources.add(item);
                }
                if(Resources!=null)
                {
                    mAdapter = new AddressAdapter(getActivity(), Resources,AddressFragment.this,AddressFragment.this,AddressFragment.this);
                    recyclerView.setAdapter(mAdapter);

                }

            }else {
                CommanMethod.showAlert(message,mcontext);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getAddressId(String addressId,int mposition) {
        REMOVEADDRESS(addressId,mposition);
    }

    private void REMOVEADDRESS(String addressId,final int mposition) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(mcontext).getUserId());
        params.add("addressId", addressId);
        client.post(WebServices.REMOVEADDRESS, params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        prgDialog.show();
                    }
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        try {
                            JSONObject object = new JSONObject(responseCode.toString());
                            Log.e("ReemoveAddressResponse", "--->>" + object.toString(2));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        parseResultReemove(responseCode.toString(),mposition);

                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        prgDialog.hide();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), mcontext);
                    }
                    @Override
                    public void onFinish() {
                        prgDialog.hide();
                        super.onFinish();
                    }

                });

    }
    private void parseResultReemove(String result,int mposition) {
        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {

                CommanMethod.showAlert(message,mcontext);
                validateGETADDRESS();
            }else {
                CommanMethod.showAlert(message,mcontext);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getAddressId1(String addressId, int mposition) {

    }

    @Override
    public void getsetaddressDefault(String addressId, int mposition) {

        setaddressDefault(addressId,mposition);

    }

    private void setaddressDefault(String addressId,final int mposition) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(mcontext).getUserId());
        params.add("addressId", addressId);
        client.post(WebServices.setaddressDefault, params,
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
                        try {
                            JSONObject object = new JSONObject(responseCode.toString());
                            Log.e("setaddressDefault", "--->>" + object.toString(2));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        parseResultsetaddressDefault(responseCode.toString(),mposition);

                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //prgDialog.hide();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), mcontext);
                    }
                    @Override
                    public void onFinish() {
                        //prgDialog.hide();
                        super.onFinish();
                    }

                });
    }

    private void parseResultsetaddressDefault(String result,int mposition) {
        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {

                CommanMethod.showAlert(message,mcontext);
                validateGETADDRESS();
            }else {
                CommanMethod.showAlert(message,mcontext);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
