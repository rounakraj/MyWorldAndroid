package com.app.shopchatmyworldra;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sinch.android.rtc.SinchError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends BaseActivity implements SinchService.StartFailedListener,GoogleApiClient.OnConnectionFailedListener{

    private Button signin,btn_sinup;
    private ImageView img_qrcode;
    EditText qrnum;

    /***************************************************************************/
    private CallbackManager callbackManager;
    LoginButton fbloginButton;
    private ProgressDialog prgDialog;
    /***************************Google pluse**************************************/
    SignInButton signInButton;
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private String deviceId;
    public static Activity mActivity;

    private String[] permissions = new String[]
            {
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.RECORD_AUDIO,

            };
    private int REQUEST_ID_MULTIPLE_PERMISSIONS = 8877;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(LoginActivity.this);
        setContentView(R.layout.activity_login);
        mActivity=this;
        checkPermissions();
        prgDialog = new ProgressDialog(LoginActivity.this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        deviceId= FirebaseInstanceId.getInstance().getToken();
        System.out.println("deviceId="+deviceId);
        signin=(Button) findViewById(R.id.btn_sign);
        btn_sinup=(Button) findViewById(R.id.btn_sinup);
        qrnum=(EditText)findViewById(R.id.et_qr);
        img_qrcode=(ImageView)findViewById(R.id.img_qrcode);
        fbloginButton = (LoginButton) findViewById(R.id.login_button);
        signInButton = (SignInButton)findViewById(R.id.sign_in_button);
        img_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanNow(v);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in =new Intent(LoginActivity.this,SignInActivity.class);
                startActivity(in);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        btn_sinup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in =new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(in);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
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
                                    loginClicked(email);
                                    MyPreferences.getActiveInstance(LoginActivity.this).setEmailId(email);
                                    MyPreferences.getActiveInstance(LoginActivity.this).setfacebookimage(link);
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

    }




    public void scanNow(View view){


        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a barcode or QRcode");
        integrator.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //qrnum.setText(result.getContents());
                Intent in =new Intent(LoginActivity.this,SignUpActivity.class);
                in.putExtra("qrcode",result.getContents());
                startActivity(in);
                MyPreferences.getActiveInstance(LoginActivity.this).setMobileNo(result.getContents());
                //tvScanFormat.setText(result.getFormatName());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result1 = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result1);
        }

    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        ProgressBarDialog.showProgressBar(LoginActivity.this, "");

    }

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
            if(personPhoto!=null)
            {
                validateSignupForSocialMedia(personName, personEmail, personPhoto.toString());
                MyPreferences.getActiveInstance(LoginActivity.this).setprofileimg(personPhoto.toString());
            }else {
                validateSignupForSocialMedia(personName, personEmail, "");
                MyPreferences.getActiveInstance(LoginActivity.this).setprofileimg("");
            }

            loginClicked(personEmail);
            MyPreferences.getActiveInstance(LoginActivity.this).setEmailId(personEmail);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
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

    private void validateSignupForSocialMedia(String fullName, String emailId, String image) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("fullName", fullName);
        params.add("emailId", emailId);
        params.add("image", image);
        params.add("device","android");
        params.add("deviceId",deviceId);
        client.post(WebServices.registerUserSocialMedia, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(LoginActivity.this,"");
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
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), LoginActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();                    }

                });

    }
    private void parseResultSignInActivity(String result) {
        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {
                MyPreferences.getActiveInstance(LoginActivity.this).setIsLoggedIn(true);
                MyPreferences.getActiveInstance(LoginActivity.this).setUserId(response.getString("userId"));
                Intent in = new Intent(LoginActivity.this, MainActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                LoginActivity.this.finish();

            } else {
                CommanMethod.showAlert(message, LoginActivity.this);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //this method is invoked when the connection is established with the SinchService
    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
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

        if (getSinchServiceInterface()!=null&&!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userName);
        } else {
        }
    }


    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            result = ContextCompat.checkSelfPermission(mActivity, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(mActivity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
}
