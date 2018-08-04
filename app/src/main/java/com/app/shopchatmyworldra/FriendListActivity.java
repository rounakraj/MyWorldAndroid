package com.app.shopchatmyworldra;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.shopchatmyworldra.adapter.FriendListAdapter;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.MyApplication;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.FriendListResource;
import com.app.shopchatmyworldra.videochat.BaseActivity;
import com.app.shopchatmyworldra.videochat.CallScreenActivity;
import com.app.shopchatmyworldra.videochat.SinchService;
import com.app.shopchatmyworldra.videochat.VoiceCallActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sa90.materialarcmenu.ArcMenu;
import com.sa90.materialarcmenu.StateChangeListener;
import com.sinch.android.rtc.calling.Call;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;

public class FriendListActivity extends BaseActivity implements FriendListAdapter.callIdinterface, FriendListAdapter.callIdvoiceinterface {
    private RecyclerView mRecyclerView;
    private ArrayList<FriendListResource> friendListResource = new ArrayList<FriendListResource>();
    FriendListAdapter mAdapter;
    public Snackbar snackbar;
    ImageView addfriend;
    public static TextView tvNotificationItem;
    String onlineFlag = "";
    private ImageView ivBackArrow;
    private LinearLayout llCallbox;
    private ArcMenu arcMenuAndroid;
    private FloatingActionButton photolibrary;
    private FloatingActionButton camera;
    private FloatingActionButton video;
    private FloatingActionButton videogallery;
    private FloatingActionButton textMessage;

    private static final int REQUEST_VIDEO_CAPTURE = 300;
    private static final int CAMERA_REQUEST = 1888;
    private static final  int SELECT_VIDEO=4;
    private static final int PICK_IMAGE_REQUEST = 1;
    private   byte[] imagepic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        MyApplication.Title = "";
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_friend);
        tvNotificationItem = (TextView) findViewById(R.id.tvNotificationItem);
        addfriend = (ImageView) findViewById(R.id.addfriend);
        photolibrary = (FloatingActionButton) findViewById(R.id.photolibrary);
        camera = (FloatingActionButton) findViewById(R.id.camera);
        video = (FloatingActionButton) findViewById(R.id.video);
        videogallery = (FloatingActionButton) findViewById(R.id.videogallery);
        textMessage = (FloatingActionButton) findViewById(R.id.textMessage);
        snackbar = Snackbar.make(findViewById(R.id.fab), "No Internet  Connection", Snackbar.LENGTH_LONG);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(FriendListActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        snackbar = Snackbar.make(findViewById(R.id.fab), "No Internet  Connection", Snackbar.LENGTH_LONG);
        if (!MyPreferences.getActiveInstance(FriendListActivity.this).getbudge().equals(""))
            tvNotificationItem.setText(MyPreferences.getActiveInstance(FriendListActivity.this).getbudge());
        if (CommanMethod.isOnline(FriendListActivity.this)) {
            validateFriendLisy();
        } else {
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(FriendListActivity.this, R.color.colorAccent));
            snackbar.show();
        }

        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogSureCancel(FriendListActivity.this);
            }
        });
        ivBackArrow = (ImageView) findViewById(R.id.ivBackArrow);
        llCallbox = (LinearLayout) findViewById(R.id.llCallbox);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });


        llCallbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialogSureCancel(FriendListActivity.this);
            }
        });

        ImageView ivHome=(ImageView)findViewById(R.id.ivHome);
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FriendListActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        arcMenuAndroid = (ArcMenu) findViewById(R.id.arcmenu_android_example_layout);
        arcMenuAndroid.setStateChangeListener(new StateChangeListener() {
            @Override
            public void onMenuOpened() {
                //TODO something when menu is opened
            }
            @Override
            public void onMenuClosed() {
                //TODO something when menu is closed
            }
        });



        photolibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(arcMenuAndroid.isMenuOpened()){
                    Intent intent2 = new Intent();
                    intent2.setType("image/*");
                    intent2.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent2, "Select Picture"), PICK_IMAGE_REQUEST);
                    arcMenuAndroid.toggleMenu();
                }
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(arcMenuAndroid.isMenuOpened()){

                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    arcMenuAndroid.toggleMenu();
                }
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(arcMenuAndroid.isMenuOpened()){
                    Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    if(videoCaptureIntent.resolveActivity(getPackageManager()) != null){
                        startActivityForResult(videoCaptureIntent, REQUEST_VIDEO_CAPTURE);
                    }
                    arcMenuAndroid.toggleMenu();
                }
            }
        });
        videogallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(arcMenuAndroid.isMenuOpened()){
                    arcMenuAndroid.toggleMenu();

                    Intent intentvideo = new Intent();
                    intentvideo.setType("video/*");
                    intentvideo.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intentvideo, "Select a Video "), SELECT_VIDEO);
                }
            }
        });

        textMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(arcMenuAndroid.isMenuOpened()){
                    arcMenuAndroid.toggleMenu();
                    AlertDialogMessage(FriendListActivity.this);
                }
            }
        });


    }

    /*@Override
    public void onDestroy() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        super.onDestroy();
    }
*/

    private void validateFriendLisy() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(FriendListActivity.this).getUserId());
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
                        parseResult(responseCode.toString());

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                       // prgDialog.hide();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), FriendListActivity.this);
                    }

                    @Override
                    public void onFinish() {
                       // prgDialog.hide();
                        super.onFinish();
                    }

                });

    }


    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            if (response.getString("responseCode").equals("200")) {
                JSONArray jsonArray = response.getJSONArray("friendList");
                friendListResource = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    FriendListResource item = new FriendListResource();
                    item.setUserId(object.getString("userId"));
                    item.setFirstName(object.getString("firstName"));
                    item.setLastName(object.getString("lastName"));
                    item.setMobileNo(object.getString("mobileNo"));
                    item.setEmailId(object.getString("emailId"));
                    item.setUserStatus(object.optString("userStatus"));
                    item.setProfileImage(object.getString("profileImage"));
                    item.setFollowingStatus(object.getString("followingStatus"));
                    friendListResource.add(item);
                }
                if (friendListResource != null&&friendListResource.size()>0) {
                    mAdapter = new FriendListAdapter(FriendListActivity.this, friendListResource, FriendListActivity.this, FriendListActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                    llCallbox.setVisibility(View.GONE);
                }
            }else {
                llCallbox.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void getPlaceReferId(String placecallEmailId) {

        String userName = placecallEmailId;
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a user to call", Toast.LENGTH_LONG).show();
            return;
        }
        try {

            Call call = getSinchServiceInterface().callUserVideo(userName);
            String callId = call.getCallId();
            Intent callScreen = new Intent(FriendListActivity.this, CallScreenActivity.class);
            callScreen.putExtra(SinchService.CALL_ID, callId);
            callScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(callScreen);
        } catch (Exception ex) {
            CommanMethod.showAlert("Please logout then login", FriendListActivity.this);
        }


    }


    @Override
    public void getReferId(String placecallEmailId, String userImage) {

        String userName = placecallEmailId;
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a user to call", Toast.LENGTH_LONG).show();
            return;
        }
        try {

            Call call = getSinchServiceInterface().callUser(userName);
            String callId = call.getCallId();
            Intent callScreen = new Intent(FriendListActivity.this, VoiceCallActivity.class);
            callScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            callScreen.putExtra(SinchService.CALL_ID, callId);
            callScreen.putExtra("userImage", userImage);
            startActivity(callScreen);

        } catch (Exception ex) {
            CommanMethod.showAlert("Please logout then login", FriendListActivity.this);
        }

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
        params.add("userId", MyPreferences.getActiveInstance(FriendListActivity.this).getUserId());
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
                       // AppUtils.stopWaitingDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), FriendListActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        //AppUtils.stopWaitingDialog();
                    }

                });

    }

    public void AlertDialogMessage(final Activity activity) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialogLayout = inflater.inflate(R.layout.layout_dialoge, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogLayout);
        final AlertDialog customAlertDialog = builder.create();
        customAlertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        customAlertDialog.show();
        final EditText messageBox = (EditText) dialogLayout.findViewById(R.id.edit_chat_message);
        final TextView tvYes = (TextView) dialogLayout.findViewById(R.id.tvYes);
        final TextView tvCancel = (TextView) dialogLayout.findViewById(R.id.tvCancel);




        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageBox.getText().toString().trim().length()>0)
                {
                    String message = messageBox.getText().toString().trim();
                    Intent shareintent = new Intent(FriendListActivity.this, ShareFrendsActivity.class);
                    shareintent.putExtra("message",message);
                    startActivity(shareintent);
                    customAlertDialog.dismiss();
                }else {
                    CommanMethod.showAlert("Please write some messages", FriendListActivity.this);
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
                Intent intent=new Intent(FriendListActivity.this,AllUserActivity.class);
                intent.putExtra("searchKey","searchName");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                customAlertDialog.dismiss();

            }
        });
        llSearchByEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAlertDialog.dismiss();
                Intent intent=new Intent(FriendListActivity.this,AllUserActivity.class);
                intent.putExtra("searchKey","searchEmail");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });
        llSearchInTheList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAlertDialog.dismiss();
                Intent intent=new Intent(FriendListActivity.this,ActivityPublicUser.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //qrnum.setText(result.getContents());
                validateinvitefriend(result.getContents());
            }
        }  if (requestCode == SELECT_VIDEO) {
            System.out.println("SELECT_VIDEO");
            try {
                Uri selectedImageUri = data.getData();

                Intent shareintent = new Intent(FriendListActivity.this, ShareFrendsActivity.class);
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
                Intent intent = new Intent(FriendListActivity.this, ShareFrendsActivity.class);
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

            Intent intent = new Intent(FriendListActivity.this, ShareFrendsActivity.class);
            intent.putExtra("data", imagepic);
            startActivity(intent);

        } else if(requestCode == REQUEST_VIDEO_CAPTURE){
            try {
                Uri uriCaptureVideo = data.getData();
                Intent shareintent = new Intent(FriendListActivity.this, ShareFrendsActivity.class);
                shareintent.putExtra("inputdata",uriCaptureVideo.toString());
                startActivity(shareintent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }



    private void validateinvitefriend(String emailid) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(FriendListActivity.this).getUserId());
        params.add("emailId", emailid);
        client.post(WebServices.sendrequest, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(FriendListActivity.this,"");

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
                                CommanMethod.showAlert(message, FriendListActivity.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(FriendListActivity.this.getResources().getString(R.string.connection_error), FriendListActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();
                    }

                });

    }

}
