package com.app.shopchatmyworldra;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.WebServices;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by MMAD on 16-08-2017.
 */

public class TermsAndConditionActivity extends AppCompatActivity {

    private ProgressDialog prgDialog;
    WebView mWebView;
    public Snackbar snackbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.termsandcondition);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        prgDialog = new ProgressDialog(TermsAndConditionActivity.this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        mWebView = (WebView)findViewById(R.id.webview);
        // Enable Javascript
        WebSettings webSetting = mWebView.getSettings();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        webSetting.setBuiltInZoomControls(true);

        if (CommanMethod.isOnline(TermsAndConditionActivity.this)) {
            TermsAndCondition();

        } else {

            CommanMethod.showAlert("Please check your internate connection",TermsAndConditionActivity.this);
        }

        ImageView ivBackArrow = (ImageView) findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

    }


    private void TermsAndCondition() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.post(WebServices.termsCondition, params,
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
                        parseResult(responseCode.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        prgDialog.hide();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), TermsAndConditionActivity.this);
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
                String Details=response.getString("terms");
                mWebView.loadData(Details , "text/html",  "UTF-8");

            }else {

                CommanMethod.showAlert(message,TermsAndConditionActivity.this);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
