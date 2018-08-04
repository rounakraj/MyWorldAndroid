package com.app.shopchatmyworldra.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.shopchatmyworldra.AllUserActivity;
import com.app.shopchatmyworldra.BuyActivity;
import com.app.shopchatmyworldra.FriendListActivity;
import com.app.shopchatmyworldra.MainActivity;
import com.app.shopchatmyworldra.NewPostActivity;
import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.SellActivity;
import com.app.shopchatmyworldra.adapter.FutureBrandAdapter;
import com.app.shopchatmyworldra.adapter.NearbyAdapter;
import com.app.shopchatmyworldra.adapter.OnlineFriendAdapter;
import com.app.shopchatmyworldra.adapter.RecentChatAdapter;

import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.GPSTracker;
import com.app.shopchatmyworldra.constant.MyApplication;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.FriendListResource;
import com.app.shopchatmyworldra.pojo.FuturebrandList;
import com.app.shopchatmyworldra.pojo.Nearby;
import com.app.shopchatmyworldra.pojo.RecenChatResourse;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import cz.msebera.android.httpclient.Header;

/**
 * Created by MMAD on 06-07-2017.
 */

public class FragmentHome extends Fragment {

    private ImageView home_myImage, ivcamera, ivvideo;
    private ArrayList<Nearby> mnearlist = new ArrayList<Nearby>();
    private ArrayList<FuturebrandList> mfuturebrandList = new ArrayList<FuturebrandList>();
    private View view;
    private ImageView iBuyBanner;
    private ImageView ivSellBanner;
    private ImageView ivSocialBanner;
    RecyclerView mRecyclerView;
    RecyclerView mRecyclerVieRecentchat;
    RecyclerView reOnlineFriends;
    RecyclerView my_recycler_futurebrand;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.LayoutManager mLayoutManager1;
    RecyclerView.LayoutManager mLayoutManager2;
    RecyclerView.LayoutManager mLayoutManager3;
    NearbyAdapter mAdapter;
    FutureBrandAdapter mFutureBrandAdapter;
    RecentChatAdapter mRecentChatAdapter;
    OnlineFriendAdapter mOnlineFriendAdapter;
    private ArrayList<RecenChatResourse> mNearby = new ArrayList<>();
    private ArrayList<FriendListResource> friendListResource = new ArrayList<FriendListResource>();
    private Context mcontext;
    LinearLayout llCallNearbyPlace;
    TextView etstatus;
    LinearLayout llhomestatus;
    LinearLayout llCallRecetchat;
    LinearLayout llCallOnlineFriends;
    public Snackbar snackbar;
    double latitude, longitude;
    private GPSTracker gps;
    private Runnable runnable2;
    private Handler handler2 = new Handler();
    private boolean keepupdating = true;
    private BroadcastReceiver sendBroadcastReceiver;


    @Override
    public void onAttach(Context context) {
        mcontext = context;
        super.onAttach(context);

        if (CommanMethod.isOnline(mcontext)) {
            validategetproduct();
            validategetfeaturebrand();
        } else {
            View sbView = MainActivity.snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(mcontext, R.color.colorPrimary));
            MainActivity.snackbar.show();
        }

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmenthome, null);

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        gps = new GPSTracker(mcontext);
        getLatLong();

        llCallNearbyPlace = (LinearLayout) view.findViewById(R.id.llCallNearbyPlace);
        etstatus = (TextView) view.findViewById(R.id.etstatus);

        ivcamera = (ImageView) view.findViewById(R.id.ivcamera);
        iBuyBanner = (ImageView) view.findViewById(R.id.iBuyBanner);
        ivSellBanner = (ImageView) view.findViewById(R.id.ivSellBanner);
        ivSocialBanner = (ImageView) view.findViewById(R.id.ivSocialBanner);
        ivvideo = (ImageView) view.findViewById(R.id.ivvideo);
        snackbar = Snackbar.make(view.findViewById(R.id.fab), "No Internet  Connection", Snackbar.LENGTH_LONG);
        llhomestatus = (LinearLayout) view.findViewById(R.id.llhomestatus);
        llCallRecetchat = (LinearLayout) view.findViewById(R.id.llCallRecetchat);
        llCallOnlineFriends = (LinearLayout) view.findViewById(R.id.llCallOnlineFriends);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);


        mRecyclerVieRecentchat = (RecyclerView) view.findViewById(R.id.recentchat);
        mRecyclerVieRecentchat.setHasFixedSize(true);

        reOnlineFriends = (RecyclerView) view.findViewById(R.id.reOnlineFriends);
        reOnlineFriends.setHasFixedSize(true);

        my_recycler_futurebrand = (RecyclerView) view.findViewById(R.id.my_recycler_futurebrand);
        my_recycler_futurebrand.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new LinearLayoutManager(mcontext, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);

        mLayoutManager1 = new LinearLayoutManager(mcontext, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerVieRecentchat.setLayoutManager(mLayoutManager1);
        mRecyclerVieRecentchat.setNestedScrollingEnabled(false);

        mLayoutManager2 = new LinearLayoutManager(mcontext, LinearLayoutManager.HORIZONTAL, false);
        reOnlineFriends.setLayoutManager(mLayoutManager2);
        reOnlineFriends.setNestedScrollingEnabled(false);


        mLayoutManager3 = new LinearLayoutManager(mcontext, LinearLayoutManager.HORIZONTAL, false);
        my_recycler_futurebrand.setLayoutManager(mLayoutManager3);
        my_recycler_futurebrand.setNestedScrollingEnabled(false);
        DataUpdateBackgroundService();

        home_myImage = (ImageView) view.findViewById(R.id.home_myImage);
        home_myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent intent=new Intent(mcontext,FragmentMyPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);*/
                start_fragment(new FragmentMyPage1());
            }
        });

        try {
            if (!MyPreferences.getActiveInstance(mcontext).getprofileimg().equals("")) {
                Picasso.with(mcontext).load(MyPreferences.getActiveInstance(mcontext).getprofileimg())
                        .error(R.drawable.human)
                        .placeholder(R.drawable.human)
                        .resize(85, 85)
                        .into(home_myImage);
            } else {
                if (!MyPreferences.getActiveInstance(mcontext).getfacebookimage().equals("")) {

                    Picasso.with(mcontext)
                            .load("https://"+MyPreferences.getActiveInstance(mcontext).getfacebookimage())
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .placeholder(R.color.grey)
                            .error(R.color.grey)
                            .into(home_myImage);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        ivcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewPostActivity.class);
                intent.putExtra("statusmypage", "0");
                startActivity(intent);

            }
        });

        ivvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewPostActivity.class);
                intent.putExtra("statusmypage", "0");
                startActivity(intent);

            }
        });
        llhomestatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewPostActivity.class);
                intent.putExtra("statusmypage", "0");
                startActivity(intent);

            }
        });
        etstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewPostActivity.class);
                intent.putExtra("statusmypage", "0");
                startActivity(intent);

            }
        });
        runnable2 = new Runnable() {
            public void run() {
                validateFriendLisy();
                if (keepupdating)
                    handler2.postDelayed(runnable2, 10000);
            }
        };



        mOnlineFriendAdapter = new OnlineFriendAdapter(mcontext, friendListResource);
        reOnlineFriends.setAdapter(mOnlineFriendAdapter);
        iBuyBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext, BuyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        ivSellBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext, SellActivity.class);
                mcontext.startActivity(intent);
            }
        });
        ivSocialBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext, FriendListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        llCallRecetchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext, FriendListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        llCallOnlineFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext, AllUserActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        MainActivity.linemain.setVisibility(View.GONE);
        MainActivity.llTopLayout.setVisibility(View.VISIBLE);

        return view;

    }

    private void start_fragment(Fragment frag) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, frag);
        fragmentTransaction.commit();
    }


    @Override
    public void onResume() {
        super.onResume();
        keepupdating = true;
        handler2.postDelayed(runnable2, 100);

        if (CommanMethod.isOnline(mcontext)) {
            showRecentchat();
        } else {
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(mcontext, R.color.colorAccent));
            snackbar.show();
        }

    }

    // get product detaild
    private void validategetproduct() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userLat", String.valueOf(latitude));
        params.add("userLong", String.valueOf(longitude));
        params.add("userId", MyPreferences.getActiveInstance(mcontext).getUserId());
        client.post(WebServices.getnearbyproduct, params,
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
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), mcontext);
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
                llCallNearbyPlace.setVisibility(View.GONE);
                if (response.get("productUser1") instanceof JSONArray) {
                    llCallNearbyPlace.setVisibility(View.GONE);
                    mnearlist.clear();
                    Nearby mRide = null;
                    JSONArray jsonArray = null;
                    jsonArray = response.getJSONArray("productUser1");
                    JSONArray jsonArray1 = null;
                    jsonArray1 = response.getJSONArray("productUser2");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        mRide = new Nearby();
                        JSONObject obj = (JSONObject) jsonArray.get(i);

                        if (obj.getString("productName").toString().trim().equals("null") || obj.getString("productName").equals("")) {
                            mRide.setItemName("");
                        } else {
                            mRide.setItemName(obj.getString("productName").toString().trim());
                        }
                        if (obj.getString("productImage1").toString().trim().equals("null") || obj.getString("productImage1").equals("")) {
                            mRide.setItemImg("");
                        } else {
                            mRide.setItemImg(obj.getString("productImage1").toString().trim());
                        }
                        if (obj.getString("productId").toString().trim().equals("null") || obj.getString("productId").equals("")) {
                            mRide.setItemId("");
                        } else {
                            mRide.setItemId(obj.getString("productId").toString().trim());
                        }
                        if (obj.getString("userName").toString().trim().equals("null") || obj.getString("userName").equals("")) {
                            mRide.setItemId("");
                        } else {
                            mRide.setUserName(obj.getString("userName").toString().trim());
                        }

                        mnearlist.add(mRide);
                    }
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        mRide = new Nearby();
                        JSONObject obj = (JSONObject) jsonArray1.get(i);

                        if (obj.getString("productName").toString().trim().equals("null") || obj.getString("productName").equals("")) {
                            mRide.setItemName("");
                        } else {
                            mRide.setItemName(obj.getString("productName").toString().trim());
                        }
                        if (obj.getString("productImage1").toString().trim().equals("null") || obj.getString("productImage1").equals("")) {
                            mRide.setItemImg("");
                        } else {
                            mRide.setItemImg(obj.getString("productImage1").toString().trim());
                        }
                        if (obj.getString("productId").toString().trim().equals("null") || obj.getString("productId").equals("")) {
                            mRide.setItemId("");
                        } else {
                            mRide.setItemId(obj.getString("productId").toString().trim());
                        }
                        if (obj.getString("userName").toString().trim().equals("null") || obj.getString("userName").equals("")) {
                            mRide.setItemId("");
                        } else {
                            mRide.setUserName(obj.getString("userName").toString().trim());
                        }

                        mnearlist.add(mRide);
                    }
                    if (mnearlist != null) {
                        Collections.reverse(mnearlist);
                        mAdapter = new NearbyAdapter(getActivity(), mnearlist);
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                    }
                } else {

                    CommanMethod.showAlert("No Product Found.", mcontext);
                }
            } else {

                llCallNearbyPlace.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
// get feature brand

    private void validategetfeaturebrand() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.post(WebServices.getfeaturebrand, params,
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
                        try {
                            JSONObject object = new JSONObject(responseCode.toString());
                            Log.e("Response", "--->>" + object.toString(2));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        parseResultbrandfeatur(responseCode.toString());
                        ProgressBarDialog.dismissProgressDialog();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();                    }

                });

    }


    private void parseResultbrandfeatur(String result) {

        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {

                if (response.get("brandList") instanceof JSONArray) {
                    JSONArray jsonArray = null;
                    jsonArray = response.getJSONArray("brandList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = (JSONObject) jsonArray.get(i);
                        FuturebrandList objectbarand = new FuturebrandList();
                        if (obj.getString("brandImage") != null && !obj.getString("brandImage").equals("")) {
                            objectbarand.setBrandImage(obj.getString("brandImage"));
                        }
                        if (obj.getString("featureId") != null && !obj.getString("featureId").equals("")) {
                            objectbarand.setFeatureId(obj.getString("featureId"));
                        }
                        if (obj.getString("brandName") != null && !obj.getString("brandName").equals("")) {
                            objectbarand.setBrandName(obj.getString("brandName"));
                        }

                        mfuturebrandList.add(objectbarand);

                        if (mfuturebrandList != null) {
                            mFutureBrandAdapter = new FutureBrandAdapter(getActivity(), mfuturebrandList);
                            my_recycler_futurebrand.setAdapter(mFutureBrandAdapter);
                        } else {
                        }
                    }


                } else {

                    CommanMethod.showAlert("No Product Found.", mcontext);
                }
            } else {

                CommanMethod.showAlert(message, mcontext);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showRecentchat() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(mcontext).getUserId());
        client.post(WebServices.showRecentchat, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        //prgDialog.show();
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
                        showRecentchat(responseCode.toString());

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //prgDialog.hide();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        //prgDialog.hide();
                    }

                });

    }


    private void showRecentchat(String result) {

        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {
                mNearby.clear();
                JSONArray jsonArray = response.getJSONArray("recentList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    RecenChatResourse chatResourse = new RecenChatResourse();
                    chatResourse.setEmailId(object.getString("emailId"));
                    chatResourse.setUserId(object.getString("userId"));
                    chatResourse.setUserName(object.getString("userName"));
                    chatResourse.setFirstName(object.getString("firstName"));
                    chatResourse.setLastName(object.getString("lastName"));
                    chatResourse.setProfileImage(object.getString("profileImage"));
                    mNearby.add(chatResourse);
                }
                mRecyclerVieRecentchat.setVisibility(View.VISIBLE);
                mRecentChatAdapter = new RecentChatAdapter(mcontext, mNearby);
                mRecyclerVieRecentchat.setAdapter(mRecentChatAdapter);
                llCallRecetchat.setVisibility(View.GONE);
            } else {
                mRecyclerVieRecentchat.setVisibility(View.GONE);
                llCallRecetchat.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void validateFriendLisy() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(mcontext).getUserId());
        client.post(WebServices.getFriends, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        //prgDialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        parseResultOnline(responseCode.toString());

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //prgDialog.hide();
                    }

                    @Override
                    public void onFinish() {
                        //prgDialog.hide();
                        super.onFinish();
                    }

                });

    }


    private void parseResultOnline(String result) {
        try {
            JSONObject response = new JSONObject(result);
            if (response.getString("responseCode").equals("200")) {
                JSONArray jsonArray = response.getJSONArray("friendList");
                Log.e("FriendsOnline", "" + response.toString());
                friendListResource.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    FriendListResource item = new FriendListResource();
                    item.setUserId(object.getString("userId"));
                    item.setFirstName(object.getString("firstName"));
                    item.setLastName(object.getString("lastName"));
                    item.setMobileNo(object.getString("mobileNo"));
                    item.setEmailId(object.getString("emailId"));
                    item.setProfileImage(object.getString("profileImage"));
                    item.setFollowingStatus(object.getString("followingStatus"));
                    item.setFollowingStatus(object.getString("followingStatus"));
                    item.setOnlineFlag(object.getString("onlineFlag"));
                    Log.e("OnlineFlage", "" + object.getString("onlineFlag"));
                    friendListResource.add(item);
                }
                if (friendListResource != null) {

                    mOnlineFriendAdapter.notifyDataSetChanged();
                    llCallOnlineFriends.setVisibility(View.GONE);
                }

            }else {
                llCallOnlineFriends.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        keepupdating = false;
    }


    //*//***for getting lattitude and longitude****//*///
    private void getLatLong() {

        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            //drawMarkerforcurrentlocations(gps.getLatitude(), gps.getLongitude());
            Log.d("latitude....=", ">>>>>>>>>>>" + latitude);
            Log.d("longitude....=", ">>>>>>>>>>" + longitude);
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    public void DataUpdateBackgroundService() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("notification");
        sendBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //TODO  map updations work here

                if (MyApplication.Title.equals("user")) {
                    setRecentchat();
                }
            }
        };
        mcontext.registerReceiver(sendBroadcastReceiver, intentFilter);
    }



    protected void setRecentchat() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.add("userId1", MyPreferences.getActiveInstance(mcontext).getUserId());
        params.add("userId2", MyApplication.userId2);
        client.post(WebServices.setRecentchat, params,
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
                        Log.e("Recentchat", "" + responseCode.toString());

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


}