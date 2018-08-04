package com.app.shopchatmyworldra;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by legacy on 19-Aug-17.
 */

public class AddAdressActivity extends AppCompatActivity{

    private EditText etmobile;
    private EditText etHouseno;
    private EditText etStreet;
    private EditText etlandmark;
    private EditText etcity;
    private EditText etstate;
    private EditText etpincode;
    private EditText etcountry;
    private String AddressId;
    private CheckBox chktype;
    private LinearLayout ll_save;
    private String concatenateUrl;
    public Snackbar snackbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addadress);
        getReferanceId();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        snackbar = Snackbar.make(findViewById(R.id.fab), "No Internet  Connection", Snackbar.LENGTH_LONG);
        String mobileNo=getIntent().getStringExtra("mobileNo");
        String HouseNo=getIntent().getStringExtra("HouseNo");
        String Landmark=getIntent().getStringExtra("Landmark");
        String City=getIntent().getStringExtra("City");
        String State=getIntent().getStringExtra("State");
        String Country=getIntent().getStringExtra("Country");
        String Pincode=getIntent().getStringExtra("Pincode");
        String DefaultType=getIntent().getStringExtra("DefaultType");
        AddressId=getIntent().getStringExtra("AddressId");

        if(mobileNo!=null&&HouseNo!=null&&Landmark!=null&&City!=null&&State!=null&&Country!=null&&Pincode!=null&&DefaultType!=null&&AddressId!=null)
        {

            etmobile.setText(mobileNo);
            etHouseno.setText(HouseNo);
            etStreet.setText(City);
            etlandmark.setText(Landmark);
            etcity.setText(City);
            etcountry.setText(Country);
            etstate.setText(State);
            etpincode.setText(Pincode);
            concatenateUrl=WebServices.UPDATEADDRESS;
        }else {

            concatenateUrl=WebServices.Add_Address;
        }
        if (DefaultType!=null &&DefaultType.equals("1"))
        {
            chktype.setChecked(true);
        }else {
            chktype.setChecked(false);
        }

        ll_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommanMethod.isOnline(AddAdressActivity.this)) {

                    if(isvalidationAdrees()) {

                        String mobileno = etmobile.getText().toString().trim();
                        String houseno = etHouseno.getText().toString().trim();
                        String street = etStreet.getText().toString().trim();
                        String Landmark = etlandmark.getText().toString().trim();
                        String city = etcity.getText().toString().trim();
                        String country = etcountry.getText().toString().trim();
                        String state = etstate.getText().toString().trim();
                        String picode = etpincode.getText().toString().trim();
                        String addType;
                        if (chktype.isChecked()) {
                            addType = "1";
                        } else {
                            addType = "0";
                        }

                        validateAddAddress(mobileno, houseno, street, Landmark, city, country, state, picode, addType);
                    }
                } else {
                    View sbView =snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(ContextCompat.getColor(AddAdressActivity.this,R.color.colorAccent));
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
                Intent intent=new Intent(AddAdressActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });


    }
    public void getReferanceId()
    {
        ll_save=(LinearLayout)findViewById(R.id.ll_save);
        etmobile=(EditText) findViewById(R.id.etmobile);
        etHouseno=(EditText) findViewById(R.id.etHouseno);
        etStreet=(EditText) findViewById(R.id.etStreet);
        etlandmark=(EditText) findViewById(R.id.etlandmark);
        etcity=(EditText) findViewById(R.id.etcity);
        etstate=(EditText) findViewById(R.id.etstate);
        etpincode=(EditText) findViewById(R.id.etpincode);
        etcountry=(EditText) findViewById(R.id.etcountry);
        chktype=(CheckBox) findViewById(R.id.chktype);


    }


    private void validateAddAddress(String mobileNo,String houseNo,String street,String landmark,String city,String state,String pincode,String country,String add_type) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(AddAdressActivity.this).getUserId());
        params.add("mobileNo", mobileNo);
        params.add("houseNo", houseNo);
        params.add("street", street);
        params.add("landmark", landmark);
        params.add("city", city);
        params.add("state", state);
        if(AddressId!=null)
        {
            params.add("addressId",AddressId);
        }
        params.add("pincode", pincode);
        params.add("country", country);
        params.add("add_type", add_type);
        params.add("defaultType", add_type);
        client.post(concatenateUrl, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(AddAdressActivity.this,"");
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
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), AddAdressActivity.this);
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
            Log.e("Addaddress",""+response.toString(2));
            if (response.getString("responseCode").equals("200")) {

                CommanMethod.showAlert(message,AddAdressActivity.this);
                AddAdressActivity.this.finish();

            }else {
                CommanMethod.showAlert(message,AddAdressActivity.this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private boolean isvalidationAdrees() {

        if (etmobile.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter your mobile no.",AddAdressActivity.this);
            return false;
        }else if (etHouseno.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter your house no.",AddAdressActivity.this);
            return false;
        }else if (etStreet.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter your street.",AddAdressActivity.this);
            return false;
        }else if (etlandmark.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter your landmark.",AddAdressActivity.this);
            return false;
        }else if (etcity.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter your city.",AddAdressActivity.this);
            return false;
        }else if (etstate.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter your state.",AddAdressActivity.this);
            return false;
        }else if (etcountry.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter your country.",AddAdressActivity.this);
            return false;
        }else if (etpincode.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter your pincode.",AddAdressActivity.this);
            return false;
        }
        return true;

    }
}
