package com.app.shopchatmyworldra;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

/**
 * Created by legacy on 18-Aug-17.
 */

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText etNewPassword;
    private EditText etConfirmpasword;
    private EditText etOtp;
    private TextView tvOtpnumber;
    private ImageView ivPasswordVissiableReEnter;
    private ImageView ivPasswordReInVissiable;
    private ImageView ivPasswordVissiable;
    private ImageView ivPasswordInVissiable;
    Button tvresendCode;
    TextView timer;
    private Button resetpass;
    private BroadcastReceiver sendBroadcastReceiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        //BackgroundServiceSmsReader();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etConfirmpasword = (EditText) findViewById(R.id.etConfirmpasword);
        etOtp = (EditText) findViewById(R.id.etOtp);
        tvOtpnumber = (TextView) findViewById(R.id.tvOtpnumber);
        resetpass = (Button) findViewById(R.id.resetpass);
        tvresendCode=(Button) findViewById( R.id.tvresendCode );

        ivPasswordVissiableReEnter=(ImageView) findViewById( R.id.ivPasswordVissiableReEnter );
        ivPasswordVissiable=(ImageView) findViewById( R.id.ivPasswordVissiable );
        ivPasswordInVissiable=(ImageView) findViewById( R.id.ivPasswordInVissiable );
        ivPasswordReInVissiable=(ImageView) findViewById( R.id.ivPasswordReInVissiable );

        ivPasswordVissiable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ivPasswordInVissiable.setVisibility(View.VISIBLE);
                ivPasswordVissiable.setVisibility(View.GONE);
                etNewPassword.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

        ivPasswordInVissiable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivPasswordVissiable.setVisibility(View.VISIBLE);
                ivPasswordInVissiable.setVisibility(View.GONE);
                etNewPassword.setTransformationMethod(null);
            }
        });

        ivPasswordReInVissiable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ivPasswordVissiableReEnter.setVisibility(View.VISIBLE);
                ivPasswordReInVissiable.setVisibility(View.GONE);
                etConfirmpasword.setTransformationMethod(null);
            }
        });

        ivPasswordVissiableReEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ivPasswordVissiableReEnter.setVisibility(View.GONE);
                ivPasswordReInVissiable.setVisibility(View.VISIBLE);

                etConfirmpasword.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommanMethod.isOnline(ResetPasswordActivity.this))
                {
                    if(isvalidationResetPassword())
                    {
                        String otp=etOtp.getText().toString().trim();
                        String password=etNewPassword.getText().toString().trim();
                        validateResetPassword(otp,password);
                    }
                }else {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.fab), "No Internet  Connection", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(ContextCompat.getColor(ResetPasswordActivity.this,R.color.colorPrimary));
                    snackbar.show();
                }
            }
        });


        timer = (TextView) findViewById( R.id.textView1 );
        getTimer();

        tvresendCode.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateparseResend();
            }
        } );

        ImageView ivBackArrow = (ImageView) findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

    }



    private void validateResetPassword(String otp, String newPassword) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("otp", otp);
        params.add("newPassword", newPassword);
        params.add("userId", MyPreferences.getActiveInstance(ResetPasswordActivity.this).getUserId());
        client.post(WebServices.RESEPASSWORD, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(ResetPasswordActivity.this,"");
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
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), ResetPasswordActivity.this);
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
                CommanMethod.showAlert(message, ResetPasswordActivity.this);
                Intent intent = new Intent(ResetPasswordActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                ResetPasswordActivity.this.finish();
            } else {
                CommanMethod.showAlert(message, ResetPasswordActivity.this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void validateparseResend() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("emailId", MyPreferences.getActiveInstance(ResetPasswordActivity.this).getEmailId());
        client.post(WebServices.ForgotPassword, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(ResetPasswordActivity.this,"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        parseResultResend(responseCode.toString());

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), ResetPasswordActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();

                    }

                });

    }

    private void parseResultResend(String result) {

        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {
                getTimer();
                MyPreferences.getActiveInstance(ResetPasswordActivity.this).setEmailId(response.getString("emailId"));
                MyPreferences.getActiveInstance(ResetPasswordActivity.this).setUserId(response.getString("userId"));
                MyPreferences.getActiveInstance(ResetPasswordActivity.this).setOtp(response.getString("otp"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private boolean isvalidationResetPassword() {

        if (etNewPassword.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter your new password.", ResetPasswordActivity.this);
            return false;
        }else if (etConfirmpasword.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter your Confirm password.", ResetPasswordActivity.this);
            return false;
        } else if (!etConfirmpasword.getText().toString().equals(etConfirmpasword.getText().toString())) {
            CommanMethod.showAlert("Password mismatch.", ResetPasswordActivity.this);
            return false;
        }else if (etOtp.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter your Otp", ResetPasswordActivity.this);
            return false;
        }else if (!etOtp.getText().toString().trim().equals(MyPreferences.getActiveInstance(ResetPasswordActivity.this).getOtp())) {
            CommanMethod.showAlert("Your otp number not match.", ResetPasswordActivity.this);
            return false;
        }
        return true;

    }


   /* public void BackgroundServiceSmsReader() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("smsreader");
        sendBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //TODO  map updations work here
                etOtp.setText(MyPreferences.getActiveInstance(ResetPasswordActivity.this).getOtp());

            }
        };
        registerReceiver(sendBroadcastReceiver, intentFilter);
    }
*/

    public void getTimer()
    {

        new CountDownTimer(180000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                tvresendCode.setVisibility(View.GONE);
                resetpass.setVisibility(View.VISIBLE);
                timer.setText(""+String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                timer.setText("");
                tvresendCode.setVisibility(View.VISIBLE);
                resetpass.setVisibility(View.GONE);
            }
        }.start();

    }

}
