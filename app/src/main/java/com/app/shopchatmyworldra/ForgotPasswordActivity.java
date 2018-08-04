package com.app.shopchatmyworldra;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class ForgotPasswordActivity extends AppCompatActivity {

    private RelativeLayout resetpass;
    private EditText input_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        resetpass=(RelativeLayout)findViewById(R.id.resetpass);
        input_otp=(EditText)findViewById(R.id.input_otp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommanMethod.isOnline(ForgotPasswordActivity.this))
                {

                    if(validationForgotPassword())
                    {
                        String emailId=input_otp.getText().toString().trim();
                        validateForgotPassword(emailId);
                    }
                }else {
                    Snackbar    snackbar = Snackbar.make(findViewById(R.id.fab), "No Internet  Connection", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(ContextCompat.getColor(ForgotPasswordActivity.this,R.color.colorPrimary));
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


    }


    private void validateForgotPassword(String emailId) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("emailId", emailId);
        client.post(WebServices.ForgotPassword, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(ForgotPasswordActivity.this,"");
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
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), ForgotPasswordActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();
                    }

                });

    }


    private void parseResult(String result) {

        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {
                MyPreferences.getActiveInstance(ForgotPasswordActivity.this).setEmailId(response.getString("emailId"));
                MyPreferences.getActiveInstance(ForgotPasswordActivity.this).setUserId(response.getString("userId"));
                MyPreferences.getActiveInstance(ForgotPasswordActivity.this).setOtp(response.getString("otp"));

                Intent in = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                startActivity(in);
                finish();
            }else {
                CommanMethod.showAlert(message,ForgotPasswordActivity.this);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private boolean validationForgotPassword() {

        if (input_otp.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Enter email.", ForgotPasswordActivity.this);
            return false;
        } else if (!CommanMethod.isEmailValid(input_otp.getText().toString().trim())) {
            CommanMethod.showAlert("Please enter valid email.", ForgotPasswordActivity.this);
            return false;
        }
        return true;
    }
}
