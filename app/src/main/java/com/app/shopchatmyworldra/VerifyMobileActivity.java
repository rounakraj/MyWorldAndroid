package com.app.shopchatmyworldra;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.WebServices;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class VerifyMobileActivity extends AppCompatActivity {

    private Button btn_submit;
    EditText inputOtp;
    ProgressDialog progressDialog;
    Toolbar toolbar;
    String Otp;
    TextView tvresendCode;
    TextView tvMessage,timer;
    public Snackbar snackbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_mobile);


        progressDialog = new ProgressDialog(VerifyMobileActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btn_submit=(Button) findViewById(R.id.btn_submit);
        inputOtp=(EditText)findViewById( R.id.inputOtp );
        tvresendCode=(TextView) findViewById( R.id.tvresendCode );
        tvMessage=(TextView) findViewById( R.id.tvMessage );
        timer=(TextView) findViewById( R.id.textView1 );
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getTimer();
        tvresendCode.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateparseResend();
            }
        } );


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommanMethod.isOnline(getApplicationContext()))
                {
                    if(validationRegistration())
                    {
                         Otp=inputOtp.getText().toString().trim();
                        validateVerifyMobileNum(Otp);
                    }

                }else {
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor( ContextCompat.getColor(VerifyMobileActivity.this,R.color.colorPrimary));
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

    private void validateVerifyMobileNum(String Otp) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance( VerifyMobileActivity.this ).getUserId());
        params.add("otp", Otp);
        client.post(WebServices.Verifymobilenumber, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        progressDialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        parseResult(responseCode.toString());

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        progressDialog.hide();
                    }

                    @Override
                    public void onFinish() {
                        progressDialog.hide();
                        super.onFinish();
                    }

                });

    }

    private void parseResult(String result) {

        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {
                Toast.makeText(VerifyMobileActivity.this,message,Toast.LENGTH_SHORT).show();
                Intent in =new Intent(VerifyMobileActivity.this,SignInActivity.class);
                startActivity(in);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private void validateparseResend() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("emailId", MyPreferences.getActiveInstance(VerifyMobileActivity.this).getEmailId());
        client.post(WebServices.ForgotPassword, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        progressDialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        parseResultResend(responseCode.toString());

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        progressDialog.hide();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        progressDialog.hide();
                    }

                });

    }

    private void parseResultResend(String result) {

        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {
                getTimer();
                Toast.makeText(VerifyMobileActivity.this,message,Toast.LENGTH_SHORT).show();
                MyPreferences.getActiveInstance(VerifyMobileActivity.this).setEmailId(response.getString("emailId"));
                MyPreferences.getActiveInstance(VerifyMobileActivity.this).setUserId(response.getString("userId"));
                MyPreferences.getActiveInstance(VerifyMobileActivity.this).setOtp(response.getString("otp"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private boolean validationRegistration() {
        if (!inputOtp.getText().toString().trim().equals(MyPreferences.getActiveInstance(VerifyMobileActivity.this).getOtp())) {
            CommanMethod.showAlert( "Please enter correct otp.", VerifyMobileActivity.this );
            return false;
        }
        return true;
    }



    public void getTimer()
    {

        new CountDownTimer(180000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                tvresendCode.setVisibility(View.GONE);
                btn_submit.setVisibility(View.VISIBLE);
                timer.setText(""+String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                timer.setText("");
                btn_submit.setVisibility(View.GONE);
                tvresendCode.setVisibility(View.VISIBLE);
            }
        }.start();

    }
}
