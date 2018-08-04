package com.app.shopchatmyworldra;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by legacy on 16-Aug-17.
 */

public class ActivityContactUs extends AppCompatActivity {
    ImageView imgcall, imgfacebook, imggmail, imgtwiter, ivEmail;
    TextView txtmobnumber, txtemailid, txtaddress, txtcompanyname;
    private String mFacebook="";
    private String mGmail="";
    private String toemailId="";
    private String mTwitter="";
    private StringBuilder stringBuilder;
    public Snackbar snackbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        imgcall = (ImageView) findViewById(R.id.imgcall);
        imgfacebook = (ImageView) findViewById(R.id.imgfacebook);
        imggmail = (ImageView) findViewById(R.id.imggmail);
        imgtwiter = (ImageView) findViewById(R.id.imgtwiter);
        ivEmail = (ImageView) findViewById(R.id.ivEmail);


        txtmobnumber = (TextView) findViewById(R.id.txtmobnumber);
        txtemailid = (TextView) findViewById(R.id.txtemailid);
        txtaddress = (TextView) findViewById(R.id.txtaddress);
        txtcompanyname = (TextView) findViewById(R.id.txtcompanyname);
        snackbar = Snackbar.make(findViewById(R.id.fab), "No Internet  Connection", Snackbar.LENGTH_LONG);


        imgcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + txtmobnumber.getText().toString().trim()));
                if (ActivityCompat.checkSelfPermission(ActivityContactUs.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
            }
        });
        ivEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] TO = {toemailId};
                String[] CC = {""};
                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"");
                intent.putExtra(Intent.EXTRA_TEXT,"" );
                intent.putExtra(Intent.EXTRA_EMAIL, TO);
                intent.putExtra(Intent.EXTRA_CC, CC);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");;
                intent.setData(Uri.parse("mailto:")); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(intent);
            }
        });
        imgfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mFacebook.equals("")) {
                    Uri uri = Uri.parse(mFacebook); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

            }
        });

        imggmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mGmail.equals("")) {
                    Uri uri = Uri.parse(mGmail); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

            }
        });

        imgtwiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mTwitter.equals("")) {
                    Uri uri = Uri.parse(mTwitter); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

            }
        });

        if (CommanMethod.isOnline(ActivityContactUs.this)) {

            validatecontactus();
        } else {
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(ActivityContactUs.this, R.color.colorAccent));
            snackbar.show();
        }


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
                Intent intent=new Intent(ActivityContactUs.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });
    }


    private void validatecontactus() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.post(WebServices.contactus, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(ActivityContactUs.this,"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        try {
                            JSONObject object = new JSONObject(responseCode.toString());
                            Log.e("Response", "--->>" + object.toString(2));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        parseResult(responseCode.toString());
                        ProgressBarDialog.dismissProgressDialog();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), ActivityContactUs.this);
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

            if (response.getString("companyName") != null && !response.getString("companyName").equals("")) {
                txtcompanyname.setText(response.getString("companyName"));
            }
            if (response.getString("contactNumber") != null && !response.getString("contactNumber").equals("")) {
                txtmobnumber.setText(response.getString("contactNumber"));
            }
            if (response.getString("contactEmail") != null && !response.getString("contactEmail").equals("")) {
                txtemailid.setText(response.getString("contactEmail"));
                toemailId=response.getString("contactEmail");
            }
            if (response.getString("contactAddress") != null && !response.getString("contactAddress").equals("")) {
                txtaddress.setText(response.getString("contactAddress"));
            }
            if (response.getString("facebook") != null && !response.getString("facebook").equals("")) {

                mFacebook=response.getString("facebook");
            }
            if (response.getString("google") != null && !response.getString("google").equals("")) {
                mGmail=response.getString("google");
            }
            if (response.getString("twitter") != null && !response.getString("twitter").equals("")) {
                mTwitter=response.getString("twitter");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {toemailId};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ActivityContactUs.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }



}
