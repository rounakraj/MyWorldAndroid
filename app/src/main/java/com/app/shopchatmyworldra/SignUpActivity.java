package com.app.shopchatmyworldra;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.videochat.BaseActivity;
import com.app.shopchatmyworldra.videochat.SinchService;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sinch.android.rtc.SinchError;


import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SignUpActivity extends BaseActivity implements SinchService.StartFailedListener, GoogleApiClient.OnConnectionFailedListener {
    private TextView txt_register, txt_signin,tvTermsAndCondiont;
    EditText input_fname, input_lastname, input_email, input_password, input_refcode;
    String firstname, lastname, emai, password, senrefcode;
    private String getqrcode="";
    LoginButton fbloginButton;
    private CallbackManager callbackManager;

    /***************************Google pluse**************************************/
    SignInButton signInButton;
    private static final int RC_SIGN_IN = 9001;
    private ProgressDialog mProgressDialog;
    private GoogleApiClient mGoogleApiClient;
    private String deviceId;
    private CheckBox chkBox;
    public Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(SignUpActivity.this);
        setContentView(R.layout.activity_sign_up);
        getRefrenceId();
        deviceId = FirebaseInstanceId.getInstance().getToken();
        System.out.println("deviceId=" + deviceId);


        Intent intent = getIntent();
        snackbar = Snackbar.make(findViewById(R.id.fab), "No Internet  Connection", Snackbar.LENGTH_LONG);
        getqrcode = intent.getStringExtra("qrcode");
        if (getqrcode != null&&!getqrcode.equals("")) {
            input_refcode.setText(getqrcode);
            input_refcode.setVisibility(View.VISIBLE);
        }

        txt_register = (TextView) findViewById(R.id.txt_register);
        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (CommanMethod.isOnline(SignUpActivity.this)) {
                    if (validationRegistration()) {
                        firstname = input_fname.getText().toString().trim();
                        lastname = input_lastname.getText().toString().trim();
                        emai = input_email.getText().toString().trim();
                        password = input_password.getText().toString().trim();
                        senrefcode = input_refcode.getText().toString().trim();
                        validateRegistration();
                    }
                } else {
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.colorAccent));
                    snackbar.show();
                }


            }
        });

        txt_signin = (TextView) findViewById(R.id.txt_signin);
        tvTermsAndCondiont = (TextView) findViewById(R.id.tvTermsAndCondiont);
        txt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent in = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(in);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();
            }
        });

        //facebook callback
        fbloginButton.setReadPermissions("email");
        callbackManager = CallbackManager.Factory.create();
        fbloginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                Log.v("LoginActivity", response.toString());
                                String name = null, email = null, id = null, link = null;
                                try {
                                    JSONObject data = response.getJSONObject();
                                    email = data.getString("email");
                                    name = data.getString("name");
                                    id = data.getString("id");
                                    link = data.getString("link");
                                    link = link.replace("https://www.facebook.com/app_scoped_user_id/", "");
                                    link = link.replace("/", "");
                                    Log.e("profilelink", "" + link);
                                    link = "graph.facebook.com/" + link + "/picture?height=500";
                                    MyPreferences.getActiveInstance(SignUpActivity.this).setfacebookimage(link);
                                    MyPreferences.getActiveInstance(SignUpActivity.this).setEmailId(email);
                                    validateSignupForSocialMedia(name, email, link);
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday,link");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                // TODO Auto-generated method stub

            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(AppIndex.API).build();
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();

            }
        });

        tvTermsAndCondiont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, TermsAndConditionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void getRefrenceId() {

        input_fname = (EditText) findViewById(R.id.input_fname);
        input_lastname = (EditText) findViewById(R.id.input_lastname);
        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);
        input_refcode = (EditText) findViewById(R.id.input_refcode);
        fbloginButton = (LoginButton) findViewById(R.id.login_button);
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        chkBox = (CheckBox) findViewById(R.id.chkBox);
        snackbar = Snackbar.make(findViewById(R.id.fab), "No Internet  Connection", Snackbar.LENGTH_LONG);
    }

    private void validateRegistration() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("firstName", firstname);
        params.add("lastName", lastname);
        params.add("emailId", emai);
        params.add("password", password);
        params.add("deviceId", deviceId);
        params.add("referCode", "");
        params.add("anotherCode", getqrcode);
        params.add("tag", "android");
        client.post(WebServices.Registration, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(SignUpActivity.this,"");
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

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), SignUpActivity.this);

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();                    }

                });

    }


    private void parseResult(String result) {

        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {
                LoginActivity.mActivity.finish();
                Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
                MyPreferences.getActiveInstance(SignUpActivity.this).setEmailId(response.getString("emailId"));
                MyPreferences.getActiveInstance(SignUpActivity.this).setUserId(response.getString("userId"));
                MyPreferences.getActiveInstance(SignUpActivity.this).setOtp(response.getString("otp"));

                Intent in = new Intent(SignUpActivity.this, VerifyMobileActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            } else {
                CommanMethod.showAlert(message, SignUpActivity.this);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private boolean validationRegistration() {


        if (input_fname.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter your first name.", SignUpActivity.this);
            return false;
        } else if (input_lastname.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter your last name.", SignUpActivity.this);
            return false;
        } else if (input_email.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter your email.", SignUpActivity.this);
            return false;
        } else if (!CommanMethod.isEmailValid(input_email.getText().toString().trim())) {
            CommanMethod.showAlert("Please enter valid Email", SignUpActivity.this);
            return false;
        } else if (input_password.getText().toString().trim().length() == 0) {
            CommanMethod.showAlert("Please enter your password.", SignUpActivity.this);
            return false;
        } else if (!chkBox.isChecked()) {
            CommanMethod.showAlert("Click the terms & conditon.", SignUpActivity.this);
            return false;
        }
        return true;

    }

    //this method is invoked when the connection is established with the SinchService
    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
    }

    //Invoked when just after the service is connected with Sinch
    @Override
    public void onStarted() {

    }

    //Login is Clicked to manually to connect to the Sinch Service
    private void loginClicked(String userName) {

        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
            return;
        }

        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userName);
        } else {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        ProgressBarDialog.showProgressBar(SignUpActivity.this,"");    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d("SIGN in", "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            //showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    //hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.app.shopchatmyworldra/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("&&&s", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            Log.e("information", "" + personName + " " + personId + "" + personPhoto + "" + personEmail);
            ProgressBarDialog.dismissProgressDialog();
            MyPreferences.getActiveInstance(SignUpActivity.this).setEmailId(personEmail);
            if(personPhoto!=null)
            {
                validateSignupForSocialMedia(personName, personEmail, personPhoto.toString());
                MyPreferences.getActiveInstance(SignUpActivity.this).setprofileimg(personPhoto.toString());
            }else {
                validateSignupForSocialMedia(personName, personEmail, "");
                MyPreferences.getActiveInstance(SignUpActivity.this).setprofileimg("");
            }

        } else {
            // Signed out, show unauthenticated UI.

        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d("&&&&&&&&", "onConnectionFailed:" + connectionResult);
    }



    @Override
    public void onStop() {
        super.onStop();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.app.shopchatmyworldra/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
        mGoogleApiClient.disconnect();
    }

    private void validateSignupForSocialMedia(String fullName, final String emailId, String image) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("fullName", fullName);
        params.add("emailId", emailId);
        params.add("image", image);
        params.add("deviceId", deviceId);
        params.add("device","android");
        params.add("tag", "android");
        client.post(WebServices.registerUserSocialMedia, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
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
                        parseResultSignInActivity(responseCode.toString());
                        loginClicked(emailId);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }

                });

    }

    private void parseResultSignInActivity(String result) {
        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {
                MyPreferences.getActiveInstance(SignUpActivity.this).setIsLoggedIn(true);
                MyPreferences.getActiveInstance(SignUpActivity.this).setUserId(response.getString("userId"));
                Intent in = new Intent(SignUpActivity.this, MainActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                SignUpActivity.this.finish();
                LoginActivity.mActivity.finish();
            } else {
                CommanMethod.showAlert(message, SignUpActivity.this);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}



