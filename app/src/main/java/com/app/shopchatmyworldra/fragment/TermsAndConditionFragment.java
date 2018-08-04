package com.app.shopchatmyworldra.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.app.shopchatmyworldra.MainActivity;
import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
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

public class TermsAndConditionFragment extends Fragment {
    private View view;
    WebView mWebView;
    public Snackbar snackbar;
    private Toolbar toolbar;
    private Activity mactivity;
    @Override
    public void onAttach(Activity activity) {
        mactivity = activity;
        super.onAttach(activity);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.termsandcondition, null);

        MainActivity.linemain.setVisibility(View.VISIBLE);
        MainActivity.llTopLayout.setVisibility(View.GONE);
        toolbar=(Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);

        mWebView = (WebView)view.findViewById(R.id.webview);
        // Enable Javascript
        WebSettings webSetting = mWebView.getSettings();

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        webSetting.setBuiltInZoomControls(true);

        if (CommanMethod.isOnline(mactivity)) {
            TermsAndCondition();

        } else {
            View sbView = MainActivity.snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(mactivity, R.color.colorPrimary));
            MainActivity.snackbar.show();
        }
        return view;
    }





    private void TermsAndCondition() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.post(WebServices.termsCondition, params,
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
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), mactivity);
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
                String Details=response.getString("terms");
                mWebView.loadData(Details , "text/html",  "UTF-8");

            }else {

                CommanMethod.showAlert(message,getActivity());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
