package com.app.shopchatmyworldra;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.MyApplication;
import com.app.shopchatmyworldra.constant.MyPreferences;

import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.zcw.togglebutton.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Vartulz on 10/12/2015.
 */
public class UserProfileActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private LinearLayout llmyorder;
    private LinearLayout lladdress;
    private LinearLayout llcontactus;
    private LinearLayout llBlockUser;
    private LinearLayout llLogOut;
    private Toolbar toolbar;
    private ImageView profileImage;
    private ImageView backgroundImage;
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView txtinvitefriend;
    private TextView hintfollowers;
    private TextView hintfollowing;
    private LinearLayout ll_friends;
    private LinearLayout ll_request;
    private LinearLayout llBlock;
    private LinearLayout llReeport;
    private TextView tvName;
    private TextView txt_followers;
    private TextView txt_following;
    private TextView txt_friends;
    private TextView txt_request;
    private TextView tvBlock;
    private String profileType;
    private String userImage;
    String profile;

    private ToggleButton swtich,swtichBlock;
    public Snackbar snackbar;

    /***************************Google pluse**************************************/
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private String UserId;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(UserProfileActivity.this);
        setContentView(R.layout.profile);
        getReferance();
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(UserProfileActivity.this, R.color.white));
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.expandedappbar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        setSupportActionBar(toolbar);
        collapsingToolbar.setTitle("");
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        UserId=getIntent().getStringExtra("UserId");
        hintfollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent next = new Intent(UserProfileActivity.this, FollowersActivity.class);
                next.putExtra("userId", UserId);
                startActivity(next);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        hintfollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent next = new Intent(UserProfileActivity.this, FollowingActivity.class);
                next.putExtra("userId", UserId);
                startActivity(next);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        txtinvitefriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogSureCancel(UserProfileActivity.this);
            }
        });

        ll_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(UserProfileActivity.this, FriendRequest.class);
                in.putExtra("userId", UserId);
                startActivity(in);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        ll_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(UserProfileActivity.this, FriendList.class);
                in.putExtra("userId", UserId);
                startActivity(in);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });
        llmyorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserId.equalsIgnoreCase(MyPreferences.getActiveInstance(UserProfileActivity.this).getUserId())) {
                    Intent in = new Intent(UserProfileActivity.this, OrderHistoryActivity.class);
                    startActivity(in);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }else {
                    Toast.makeText(UserProfileActivity.this,"Sorry Can't see Order",Toast.LENGTH_SHORT).show();
                }
            }
        });
        lladdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserId.equalsIgnoreCase(MyPreferences.getActiveInstance(UserProfileActivity.this).getUserId())) {
                    Intent in = new Intent(UserProfileActivity.this, ActivityAddress.class);
                    startActivity(in);
                    MyApplication.ActivityAddress=true;
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }else {
                Toast.makeText(UserProfileActivity.this,"Sorry Can't see Adress",Toast.LENGTH_SHORT).show();
            }
            }
        });

        llcontactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(UserProfileActivity.this, ActivityContactUs.class);
                startActivity(in);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        llBlockUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(UserProfileActivity.this, BlockListActivity.class);
                startActivity(in);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        llLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                MyPreferences.getActiveInstance(UserProfileActivity.this).setIsLoggedIn(false);
                MyPreferences.getActiveInstance(UserProfileActivity.this).setfacebookimage("");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                signOut();
                revokeAccess();
                LoginManager.getInstance().logOut();
                UserProfileActivity.this.finish();
                MainActivity.mActivity.finish();
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this/* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(AppIndex.API).build();
        if (CommanMethod.isOnline(UserProfileActivity.this)) {
            getProfile(UserId);
        } else {
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(UserProfileActivity.this, R.color.colorAccent));
            snackbar.show();
        }
        if(UserId.equalsIgnoreCase(MyPreferences.getActiveInstance(UserProfileActivity.this).getUserId())){
            llBlockUser.setVisibility(View.VISIBLE);
            llBlock.setVisibility(View.GONE);
            llLogOut.setVisibility(View.VISIBLE);
        }else {
            llBlockUser.setVisibility(View.GONE);
            llBlock.setVisibility(View.VISIBLE);
            llLogOut.setVisibility(View.GONE);

        }
            swtich.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
                @Override
                public void onToggle(boolean on) {
                    if (on) {
                        profile = "Private";
                        AlertDialogPrivate(UserProfileActivity.this, profile);
                    } else {
                        profile = "Public";
                        AlertDialogSureCancel(UserProfileActivity.this, profile);
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


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(UserProfileActivity.this);
                LayoutInflater factory = LayoutInflater.from(UserProfileActivity.this);
                View vi = factory.inflate(R.layout.zoom_image, null);
                alert.setView(vi);
                ImageView imageView = (ImageView) vi.findViewById(R.id.mImagId);

                if(userImage!=null&&!userImage.equals("")&&userImage.startsWith("graph"))
                {
                    Picasso.with(UserProfileActivity.this)
                            .load("https://"+userImage)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .placeholder(R.color.grey)
                            .error(R.color.grey)
                            .into(imageView);

                }else {

                    if (userImage!=null&&!userImage.equals("")) {
                        Picasso.with(UserProfileActivity.this)
                                .load(userImage)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .placeholder(R.color.grey)
                                .error(R.color.grey)
                                .into(imageView);
                    }
                }

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int sumthin) {
                        dlg.dismiss();
                    }
                });
                alert.show();

            }


        });


        swtichBlock.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    validateallBlock(UserId,"");

                } else {
                    validateallBlock(UserId,"");

                }

            }
        });
        llReeport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialogMessage(UserId, UserProfileActivity.this);

            }
        });

    }


    public void getReferance() {
        profileImage = (ImageView) findViewById(R.id.img_user);
        ll_friends = (LinearLayout) findViewById(R.id.ll_friends);
        tvName = (TextView) findViewById(R.id.tvName);
        snackbar = Snackbar.make(findViewById(R.id.fab), "No Internet  Connection", Snackbar.LENGTH_LONG);
        txt_followers = (TextView) findViewById(R.id.txt_followers);
        txt_following = (TextView) findViewById(R.id.txt_following);
        txt_friends = (TextView) findViewById(R.id.txt_friends);
        txt_request = (TextView) findViewById(R.id.txt_request);
        tvBlock = (TextView) findViewById(R.id.tvBlock);

        ll_request = (LinearLayout) findViewById(R.id.ll_request);
        llBlock = (LinearLayout) findViewById(R.id.llBlock);
        llReeport = (LinearLayout) findViewById(R.id.llReeport);
        hintfollowers = (TextView) findViewById(R.id.tv_hintfollowers);
        txtinvitefriend = (TextView) findViewById(R.id.txtinvitefriend);
        hintfollowing = (TextView) findViewById(R.id.txt_hintfollowing);
        backgroundImage = (ImageView) findViewById(R.id.relativeLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        llmyorder = (LinearLayout) findViewById(R.id.llmyorder);
        lladdress = (LinearLayout) findViewById(R.id.lladdress);
        llcontactus = (LinearLayout) findViewById(R.id.llcontactus);
        llLogOut = (LinearLayout) findViewById(R.id.llLogOut);
        llBlockUser = (LinearLayout) findViewById(R.id.llBlockUser);
        swtich = (ToggleButton) findViewById(R.id.swtich);
        swtichBlock = (ToggleButton) findViewById(R.id.swtichBlock);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result1 = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        }
        if (result1 != null) {
            if (result1.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //qrnum.setText(result.getContents());
                validateinvitefriend(result1.getContents());
            }
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        validateGetprofile(UserId);

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
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
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


    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        // updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        //updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d("&&&&&&&&", "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onStop() {
        super.onStop();

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
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
        mGoogleApiClient.disconnect();
    }


    public void AlertDialogSureCancel(final Activity activity, final String profile) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialogLayout = inflater.inflate(R.layout.dialog_public, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogLayout);
        final AlertDialog customAlertDialog = builder.create();
        customAlertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        customAlertDialog.show();
        final TextView Cancel = (TextView) dialogLayout.findViewById(R.id.tvCancel);
        final TextView tvYes = (TextView) dialogLayout.findViewById(R.id.tvYes);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAlertDialog.dismiss();
            }
        });

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAlertDialog.dismiss();
                setProfilePrivacy(profile);
            }
        });
    }


    public void AlertDialogPrivate(final Activity activity, final String profile) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialogLayout = inflater.inflate(R.layout.dialog_private, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogLayout);
        final AlertDialog customAlertDialog = builder.create();
        customAlertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        customAlertDialog.show();
        final TextView Cancel = (TextView) dialogLayout.findViewById(R.id.tvCancel);
        final TextView tvYes = (TextView) dialogLayout.findViewById(R.id.tvYes);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAlertDialog.dismiss();
            }
        });

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAlertDialog.dismiss();
                setProfilePrivacy(profile);
            }
        });
    }

    protected void setProfilePrivacy(String profile) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.add("profileType", profile);
        client.post(WebServices.updateprofileType, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        Log.e("", "Hash -> " + responseCode.toString());

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

    protected void getProfile(String userId) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.add("userId", userId);
        params.add("login_user_id", MyPreferences.getActiveInstance(UserProfileActivity.this).getUserId());
        client.post(WebServices.getprofileInfo, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        Log.e("", "Hash -> " + responseCode.toString());

                        try {
                            JSONObject response = new JSONObject(responseCode.toString());

                            if (response.getString("responseCode").equals("200")) {
                                profileType = response.getString("profileType");
                                if (profileType.equals("Public")) {
                                    swtich.setToggleOff();
                                } else {
                                    swtich.setToggleOn();
                                }
                                if (profileType.equals("Public")) {
                                    swtich.setToggleOff();
                                } else {
                                    swtich.setToggleOn();
                                }
                                if (response.getString("is_block").equals("Yes")) {

                                    swtichBlock.setToggleOn();
                                    tvBlock.setText("Un-Block");
                                } else {
                                    swtichBlock.setToggleOff();
                                    tvBlock.setText("Block");
                                }
                                txt_followers.setText(response.getString("followers"));
                                txt_following.setText(response.getString("following"));
                                txt_friends.setText(response.getString("friends"));
                                txt_request.setText(response.getString("friendsreq"));

                            } else {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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

    private void validateGetprofile(String userId) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId",userId);
        client.post(WebServices.GETUSERPROFILE, params,
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
                        parseResult(responseCode.toString());

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), UserProfileActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }

                });

    }


    private void parseResult(String result) {

        try {
            JSONObject response = new JSONObject(result);
           userImage = response.getString("profileImage");
            if(!response.getString("profileImage").equals("")&&response.getString("profileImage").startsWith("graph"))
            {
                Picasso.with(UserProfileActivity.this)
                        .load("https://"+response.getString("profileImage"))
                        .error(R.drawable.human)
                        .placeholder(R.drawable.human)
                        .into(profileImage);

            }else {

                if(!response.getString("profileImage").equals(""))
                {
                    Picasso.with(UserProfileActivity.this)
                            .load(response.getString("profileImage"))
                            .error(R.drawable.human)
                            .placeholder(R.drawable.human)
                            .into(profileImage);


                }

            }
            if (response.getString("coverImage") != null && !response.getString("coverImage").equals("")) {
                Picasso.with(UserProfileActivity.this)
                        .load(response.getString("coverImage"))
                        .error(R.color.gray)
                        .placeholder(R.color.gray)
                        .resize(400,400).centerCrop()
                        .into(backgroundImage);
        }
            tvName.setText(response.getString("firstName") +" "+response.getString("lastName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void AlertDialogSureCancel(final Activity activity) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialogLayout = inflater.inflate(R.layout.dilog_addfriendslist, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogLayout);
        final AlertDialog customAlertDialog = builder.create();
        customAlertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        customAlertDialog.show();
        final LinearLayout llSearchByName = (LinearLayout) dialogLayout.findViewById(R.id.llSearchByName);
        final LinearLayout llSearchByEmail = (LinearLayout) dialogLayout.findViewById(R.id.llSearchByEmail);
        final LinearLayout llSearchInTheList = (LinearLayout) dialogLayout.findViewById(R.id.llSearchInTheList);
        final LinearLayout llScatnQrCode = (LinearLayout) dialogLayout.findViewById(R.id.llScatnQrCode);
        final TextView tvYes = (TextView) dialogLayout.findViewById(R.id.tvYes);


        llSearchByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAlertDialog.dismiss();
                Intent intent=new Intent(UserProfileActivity.this,AllUserActivity.class);
                intent.putExtra("searchKey","searchName");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        llSearchByEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAlertDialog.dismiss();
                Intent intent=new Intent(UserProfileActivity.this,AllUserActivity.class);
                intent.putExtra("searchKey","searchEmail");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        llSearchInTheList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAlertDialog.dismiss();
                Intent intent=new Intent(UserProfileActivity.this,ActivityPublicUser.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        llScatnQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAlertDialog.dismiss();
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setPrompt("Scan a barcode or QRcode");
                integrator.initiateScan();

            }
        });
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAlertDialog.dismiss();

            }
        });
    }

    private void validateinvitefriend(String emailid) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(UserProfileActivity.this).getUserId());
        params.add("emailId", emailid);
        client.post(WebServices.sendrequest, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(UserProfileActivity.this, "");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        try {
                            JSONObject object = new JSONObject(responseCode.toString());
                            Log.e("Response", "--->>" + object.toString(2));
                            String message = object.getString("responseMessage");
                            if (object.getString("responseCode").equals("200")) {

                                finish();

                            } else {
                                CommanMethod.showAlert(message, UserProfileActivity.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(UserProfileActivity.this.getResources().getString(R.string.connection_error), UserProfileActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();
                    }

                });
    }
        public void AlertDialogMessage (final String userId1,final Activity activity){

            LayoutInflater inflater = LayoutInflater.from(activity);
            View dialogLayout = inflater.inflate(R.layout.message_layout, null);
            final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setView(dialogLayout);
            final AlertDialog customAlertDialog = builder.create();
            customAlertDialog.show();
            final EditText messageBox = (EditText) dialogLayout.findViewById(R.id.edit_chat_message);
            final TextView tvYes = (TextView) dialogLayout.findViewById(R.id.tvYes);
            final TextView tvCancel = (TextView) dialogLayout.findViewById(R.id.tvCancel);

            tvYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (messageBox.getText().toString().trim().length() > 0) {
                        String message = messageBox.getText().toString().trim();
                        validateallBlock(userId1, message);

                        customAlertDialog.dismiss();
                    } else {
                        CommanMethod.showAlert("Please write some messages", UserProfileActivity.this);
                    }


                }
            });
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customAlertDialog.dismiss();

                }
            });
        }




    protected void validateallBlock(String blockUserId,String report_text) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.add("login_user_id", MyPreferences.getActiveInstance(UserProfileActivity.this).getUserId());
        params.add("block_user_id", blockUserId);
        if(report_text!=""){

            params.add("report", "1");
            params.add("report_text", report_text);
        }
        client.post(WebServices.blockAndReport, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(UserProfileActivity.this,"");

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        Log.e("", "Hash -> " + responseCode.toString());
                        Log.e("", "On Success of NoTs");
                        ProgressBarDialog.dismissProgressDialog();

                        parseResultBlockReport(responseCode.toString());

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), UserProfileActivity.this);
                        ProgressBarDialog.dismissProgressDialog();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();
                    }

                });

    }

    private void parseResultBlockReport(String result) {

        try {
            JSONObject response = new JSONObject(result);
            if (response.getString("responseCode").equals("200"))
            {
                Toast.makeText(UserProfileActivity.this,response.getString("responseMessage"),Toast.LENGTH_SHORT).show();

            }else {
                CommanMethod.showAlert(response.getString("responseMessage"),UserProfileActivity.this);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }




}