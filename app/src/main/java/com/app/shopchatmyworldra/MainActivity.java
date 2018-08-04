package com.app.shopchatmyworldra;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.shopchatmyworldra.constant.MyApplication;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.fragment.AddressFragment;
import com.app.shopchatmyworldra.fragment.BrowseCategoryFragment;
import com.app.shopchatmyworldra.fragment.FragmentBuy;
import com.app.shopchatmyworldra.fragment.FragmentHome;
import com.app.shopchatmyworldra.fragment.FragmentMyAccount;
import com.app.shopchatmyworldra.fragment.FragmentMyPage1;
import com.app.shopchatmyworldra.fragment.FragmentMyRewards;
import com.app.shopchatmyworldra.fragment.FragmentSell;
import com.app.shopchatmyworldra.fragment.FragmentWishList;
import com.app.shopchatmyworldra.fragment.TermsAndConditionFragment;
import com.app.shopchatmyworldra.videochat.BaseActivity;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {
    private LinearLayout llshare;
    private LinearLayout llMyworld;
    private LinearLayout llComeingsoon;
    private LinearLayout llcontact;
    private RelativeLayout llChat;

    public static View linemain;
    public static ImageView ivCart;
    public static ImageView ivNotification;
    public static ImageView ivHome;
    public static ImageView imageViewProfile;
    public static TextView tvCartItem;
    public static TextView tvNotificationItem;

    private static final int REQUEST_VIDEO_CAPTURE = 300;
    private static final int READ_REQUEST_CODE = 200;
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    String onlineFlag = "";
    private static final int CAMERA_REQUEST = 1888;
    private static final  int SELECT_VIDEO=4;
    private static final int PICK_IMAGE_REQUEST = 1;
    private BroadcastReceiver sendBroadcastReceiver;
    /***************************Google pluse**************************************/
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    public static Snackbar snackbar;
    private   byte[] imagepic;
    public FloatingActionButton fab;

    public static LinearLayout llTopLayout;
    private LinearLayout llHome;
    private LinearLayout llBuy;
    private LinearLayout llSell;
    private LinearLayout llwishlist;
    private LinearLayout llbadge;
    private View viewLine1;
    private View viewLine2;
    private View viewLine3;
    private View viewLine4;
    public static Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(MainActivity.this);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        DataUpdateBackgroundService();
        ivCart = (ImageView) findViewById(R.id.ivCart);
        ivNotification = (ImageView) findViewById(R.id.ivNotification);
        ivHome = (ImageView) findViewById(R.id.ivHome);
        llMyworld = (LinearLayout) findViewById(R.id.llMyworld);
        llbadge = (LinearLayout) findViewById(R.id.llbadge);
        llComeingsoon = (LinearLayout) findViewById(R.id.llComeingsoon);
        llcontact = (LinearLayout) findViewById(R.id.llcontact);
        llshare = (LinearLayout) findViewById(R.id.llshare);
        llChat = (RelativeLayout) findViewById(R.id.llChat);
        tvCartItem = (TextView) findViewById(R.id.tvCartItem);
        tvNotificationItem = (TextView) findViewById(R.id.tvNotificationItem);
        linemain = (View) findViewById(R.id.linemain);


        llTopLayout=(LinearLayout)findViewById(R.id.llTopLayout) ;
        llHome=(LinearLayout)findViewById(R.id.llHome) ;
        llBuy=(LinearLayout)findViewById(R.id.llBuy) ;
        llSell=(LinearLayout)findViewById(R.id.llSell) ;
        llwishlist=(LinearLayout)findViewById(R.id.llwishlist);
        viewLine1=(View)findViewById(R.id.viewLine1);
        viewLine2=(View)findViewById(R.id.viewLine2);
        viewLine3=(View)findViewById(R.id.viewLine3);
        viewLine4=(View)findViewById(R.id.viewLine4);

        fragmentReplace(new FragmentHome());
        mActivity=MainActivity.this;
        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentReplace(new FragmentHome());
                viewLine1.setVisibility(View.VISIBLE);
                viewLine2.setVisibility(View.GONE);
                viewLine3.setVisibility(View.GONE);
                viewLine4.setVisibility(View.GONE);
            }
        });

        llBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, BuyActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                fragmentReplace(new FragmentBuy());
                viewLine2.setVisibility(View.VISIBLE);
                viewLine1.setVisibility(View.GONE);
                viewLine3.setVisibility(View.GONE);
                viewLine4.setVisibility(View.GONE);
            }
        });

        llSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SellActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                fragmentReplace(new FragmentSell());

                viewLine3.setVisibility(View.VISIBLE);
                viewLine1.setVisibility(View.GONE);
                viewLine2.setVisibility(View.GONE);
                viewLine4.setVisibility(View.GONE);
            }
        });
        llwishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentReplace(new FragmentWishList());

                viewLine4.setVisibility(View.VISIBLE);
                viewLine1.setVisibility(View.GONE);
                viewLine2.setVisibility(View.GONE);
                viewLine3.setVisibility(View.GONE);
            }
        });


        snackbar = Snackbar.make(findViewById(R.id.fab), "No Internet  Connection", Snackbar.LENGTH_LONG);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartlistActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        if (!MyPreferences.getActiveInstance(MainActivity.this).getbudge().equals(""))
            tvNotificationItem.setText(MyPreferences.getActiveInstance(MainActivity.this).getbudge());
           ivNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.Title.equals("user")) {
                    Intent intent1 = new Intent(MainActivity.this, UserChatMessageActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent1.putExtra("userName", MyApplication.userNamechat);
                    intent1.putExtra("userId", MyApplication.userId2);
                    intent1.putExtra("Recipient", MyApplication.emilId);
                    startActivity(intent1);
                }
            }
        });
        if (MyApplication.Title.equals("user")) {
            Intent intent1 = new Intent(MainActivity.this, UserChatMessageActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra("userName", MyApplication.userNamechat);
            intent1.putExtra("userId", MyApplication.userId2);
            intent1.putExtra("Recipient", MyApplication.emilId);
            startActivity(intent1);
        }
        if (MyApplication.Title.equals("admin")) {
            Intent intent1 = new Intent(MainActivity.this, MessageActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
        }
        if (MyApplication.Title.equals("accept")) {
            Intent intent1 = new Intent(MainActivity.this, FriendListActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
        }
        if (MyApplication.Title.equals("request")) {
            Intent intent1 = new Intent(MainActivity.this, FriendRequest.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
        }
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentReplace(new FragmentHome());

                MainActivity.linemain.setVisibility(View.GONE);
                MainActivity.llTopLayout.setVisibility(View.VISIBLE);
                viewLine1.setVisibility(View.VISIBLE);
                viewLine2.setVisibility(View.GONE);
                viewLine3.setVisibility(View.GONE);
                viewLine4.setVisibility(View.GONE);


            }
        });


        llMyworld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentReplace(new FragmentMyPage1());
                MainActivity.linemain.setVisibility(View.VISIBLE);
                MainActivity.llTopLayout.setVisibility(View.GONE);

            }
        });

        llChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FriendListActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });
        llshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        llcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FriendList.class);
                intent.putExtra("userId",MyPreferences.getActiveInstance(MainActivity.this).getUserId());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        llComeingsoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityComeingsoon.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        imageViewProfile = (ImageView) headerView.findViewById(R.id.imageViewProfile);
        // menu_top = (ImageView) headerView.findViewById(R.id.menu_top);
        TextView textView = (TextView) headerView.findViewById(R.id.textView);
        textView.setText(MyPreferences.getActiveInstance(MainActivity.this).getEmailId());
        if (!MyPreferences.getActiveInstance(MainActivity.this).getprofileimg().equals("")) {
            Picasso.with(MainActivity.this)
                    .load(MyPreferences.getActiveInstance(MainActivity.this).getprofileimg())
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .placeholder(R.color.grey)
                    .error(R.color.grey)
                    .into(imageViewProfile);
        }
        if (!MyPreferences.getActiveInstance(MainActivity.this).getfacebookimage().equals("")) {

                Picasso.with(MainActivity.this)
                        .load("https://"+ MyPreferences.getActiveInstance(MainActivity.this).getfacebookimage())
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .placeholder(R.color.grey)
                        .error(R.color.grey)
                        .into(imageViewProfile);

        }
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                intent.putExtra("UserId",MyPreferences.getActiveInstance(MainActivity.this).getUserId());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        validateShowCart();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this/* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(AppIndex.API).build();


        if(MyPreferences.getActiveInstance(MainActivity.this).getbudge()=="")
        {
            llbadge.setVisibility(View.GONE);
        }else {
            llbadge.setVisibility(View.VISIBLE);
        }

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Toast.makeText(this, "Press once again to exit!", Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setQueryHint("Search...");
        search(searchView);
        return true;
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(MainActivity.this, AllSearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("search", query);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_postad) {

            Intent in = new Intent(MainActivity.this, PostsAdsActivity.class);
            startActivity(in);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        } else if (id == R.id.nav_MyAccount) {

            fragmentReplace(new FragmentMyAccount());
        } else if (id == R.id.nav_referandearn) {

            fragmentReplace(new FragmentMyRewards());
        }else if (id == R.id.nav_MyWorld) {

            fragmentReplace(new FragmentHome());
            viewLine1.setVisibility(View.VISIBLE);
            viewLine2.setVisibility(View.GONE);
            viewLine3.setVisibility(View.GONE);
            viewLine4.setVisibility(View.GONE);


        } else if (id == R.id.nav_friend) {
            Intent intent = new Intent(MainActivity.this, FriendList.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("userId",MyPreferences.getActiveInstance(MainActivity.this).getUserId());
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        } else if (id == R.id.nav_orderhistory) {

            Intent intent = new Intent(MainActivity.this, OrderHistoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        } else if (id == R.id.nav_addressbook) {


            fragmentReplace(new AddressFragment());

        } else if (id == R.id.nav_browse) {

            fragmentReplace(new BrowseCategoryFragment());
        } else if (id == R.id.nav_termcontion) {

            fragmentReplace(new TermsAndConditionFragment());
        } else if (id == R.id.nav_contactus) {

            Intent intent = new Intent(MainActivity.this, ActivityContactUs.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        } else if (id == R.id.nav_logout) {

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            MyPreferences.getActiveInstance(MainActivity.this).setIsLoggedIn(false);
            MyPreferences.getActiveInstance(MainActivity.this).setfacebookimage("");
            MyPreferences.getActiveInstance(MainActivity.this).setprofileimg("");
            MyPreferences.getActiveInstance(MainActivity.this).setEmailId("");
            startActivity(intent);
            if (getSinchServiceInterface()!=null&&!getSinchServiceInterface().isStarted()) {
                getSinchServiceInterface().stopClient();
            }
            signOut();
            revokeAccess();
            LoginManager.getInstance().logOut();
            MainActivity.this.finish();

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void validateShowCart() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(MainActivity.this).getUserId());
        client.post(WebServices.showCart, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        parseResult(responseCode.toString());

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


    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {
                JSONArray jsonArray = response.getJSONArray("cartList");
                int size = jsonArray.length();
                tvCartItem.setText(String.valueOf(size));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
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


    @Override
    public void onResume() {
        super.onResume();
        onlineFlag = "1";
        onlineFlag();
    }

    @Override
    public void onPause() {
        super.onPause();
        onlineFlag = "0";
        Log.e("onPause", ">>>>>>>>>" + "onPause");
        onlineFlag();
    }

    protected void onlineFlag() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.add("onlineFlag", onlineFlag);
        Log.e("onlineFlag", "" + onlineFlag);
        params.add("userId", MyPreferences.getActiveInstance(MainActivity.this).getUserId());
        client.post(WebServices.onlineFlag, params,
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
                        Log.e("", "On Success of NoTs");
                        Log.e("setOnlineFriend", "" + responseCode.toString());

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

    @Override
    protected void onDestroy() {
        unregisterReceiver(sendBroadcastReceiver);
        super.onDestroy();
    }

    public void DataUpdateBackgroundService() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("notification");
        sendBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //TODO  map updations work here

                if (MyApplication.Title.equals("user")) {

                    if(MyPreferences.getActiveInstance(MainActivity.this).getbudge()=="")
                    {
                        llbadge.setVisibility(View.GONE);

                    }else {
                        llbadge.setVisibility(View.VISIBLE);
                        tvNotificationItem.setText(MyPreferences.getActiveInstance(MainActivity.this).getbudge());

                    }

                }
                if (MyApplication.Title.equals("admin")) {
                    /*Intent intent1 = new Intent(MainActivity.this, MessageActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);*/
                }
                if (MyApplication.Title.equals("accept")) {
                    Intent intent1 = new Intent(MainActivity.this, FriendListActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                }
                if (MyApplication.Title.equals("request")) {
                    Intent intent1 = new Intent(MainActivity.this, FriendRequest.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                }


            }
        };
        registerReceiver(sendBroadcastReceiver, intentFilter);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                System.out.println("SELECT_VIDEO");
                try {
                    Uri selectedImageUri = data.getData();

                    Intent shareintent = new Intent(MainActivity.this, ShareFrendsActivity.class);
                    shareintent.putExtra("inputdata", selectedImageUri.toString());
                    startActivity(shareintent);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }else if (requestCode==PICK_IMAGE_REQUEST && data!=null && data.getData()!=null) {

                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
                    imagepic = bytes.toByteArray();
                    Intent intent = new Intent(MainActivity.this, ShareFrendsActivity.class);
                    intent.putExtra("data", imagepic);
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
                imagepic = bytes.toByteArray();

                Intent intent = new Intent(MainActivity.this, ShareFrendsActivity.class);
                intent.putExtra("data", imagepic);
                startActivity(intent);

            } else if(requestCode == REQUEST_VIDEO_CAPTURE){
                try {
                    Uri uriCaptureVideo = data.getData();
                    Intent shareintent = new Intent(MainActivity.this, ShareFrendsActivity.class);
                    shareintent.putExtra("inputdata",uriCaptureVideo.toString());
                    startActivity(shareintent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

    }


    private void selectImage() {
        final CharSequence[] items = {"Choose from Library", "Take Photo","Take Video","Choose Video",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {


                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);

                } else if (items[item].equals("Choose from Library")) {

                    Intent intent2 = new Intent();
                    intent2.setType("image/*");
                    intent2.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent2, "Select Picture"), PICK_IMAGE_REQUEST);

                }else if (items[item].equals("Take Video")) {
                    Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    if(videoCaptureIntent.resolveActivity(getPackageManager()) != null){
                        startActivityForResult(videoCaptureIntent, REQUEST_VIDEO_CAPTURE);
                    }

                } else if (items[item].equals("Choose Video")) {
                    Intent intentvideo = new Intent();
                    intentvideo.setType("video/*");
                    intentvideo.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intentvideo, "Select a Video "), SELECT_VIDEO);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }


    public void fragmentReplace(Fragment fragment){

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_frame, fragment);
        transaction.commit();
    }


}

