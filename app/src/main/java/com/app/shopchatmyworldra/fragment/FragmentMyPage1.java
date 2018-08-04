package com.app.shopchatmyworldra.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.app.shopchatmyworldra.MainActivity;
import com.app.shopchatmyworldra.NewPostActivity;
import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.adapter.GlobleAdapter;
import com.app.shopchatmyworldra.adapter.LocalUserAdapter;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.CropImage;
import com.app.shopchatmyworldra.constant.FileOpen;
import com.app.shopchatmyworldra.constant.HttpClient;
import com.app.shopchatmyworldra.constant.MyApplication;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.MypageGlobalResources;
import com.app.shopchatmyworldra.pojo.MypageUserResources;
import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;
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
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;

import static android.media.MediaPlayer.*;

/**
 * Created by mac on 26/06/18.
 */

public class FragmentMyPage1 extends Fragment implements GlobleAdapter.sharFacebookGloble,GlobleAdapter.blockUser,LocalUserAdapter.sharFacebookLocalUser,LocalUserAdapter.blockUser{
    private View view;
    ImageView ivcamera, coverimg, ivvideo, imgmystas, userimage;
    ImageView  proimg;
    LinearLayout llstatus_update,covercamera,procamera;
    private CoordinatorLayout container;
    String userTime;
    String mystatus, mypic, userproimg;
    TextView etstatus, txtmystatus;
    byte[] mData = new byte[0];
    byte[] mData2 = new byte[0];
    String img = " ";

    private VideoView videoview;
    private RelativeLayout llvideioview;
    protected ImageView btnconti, btnstop, btnplay;
    private ProgressBar progressBar;

    private MediaController mediacontroller;
    private Uri uri;
    RecyclerView recyclerViewuser, recyclerViewglobal;
    private ArrayList<MypageUserResources> userResources = new ArrayList<MypageUserResources>();
    private ArrayList<MypageGlobalResources> globalResources = new ArrayList<MypageGlobalResources>();

    private LocalUserAdapter userAdapter;
    private GlobleAdapter globalAdapter;

    private Activity mcontext;
    private TextView textView, mydate,tvName;
    public Snackbar snackbar;
    CallbackManager callbackManager;
    private ShareDialog shareDialog;


    @Override
    public void onAttach(Activity activity) {
        mcontext=activity;
        super.onAttach(activity);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mypage, null);


        MainActivity.linemain.setVisibility(View.VISIBLE);
        MainActivity.llTopLayout.setVisibility(View.GONE);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(mcontext);
        mediacontroller = new MediaController(mcontext);

        ivcamera = (ImageView)view.findViewById(R.id.ivcamera);
        proimg = (ImageView)view.findViewById(R.id.proimg);
        coverimg = (ImageView)view.findViewById(R.id.coverimg);
        ivvideo = (ImageView) view.findViewById(R.id.ivvideo);
        imgmystas = (ImageView) view.findViewById(R.id.imgmystas);
        userimage = (ImageView) view.findViewById(R.id.userimage);

        procamera = (LinearLayout) view.findViewById(R.id.procamera);
        covercamera = (LinearLayout) view.findViewById(R.id.covercamera);
        proimg = (ImageView) view.findViewById(R.id.proimg);

        txtmystatus = (TextView) view.findViewById(R.id.txtmystatus);
        textView = (TextView) view.findViewById(R.id.textView);
        etstatus = (TextView) view.findViewById(R.id.etstatus);
        mydate = (TextView) view.findViewById(R.id.mydate);
        tvName = (TextView) view.findViewById(R.id.tvName);
        llstatus_update = (LinearLayout) view.findViewById(R.id.llstatus_update);
        container = (CoordinatorLayout) view.findViewById(R.id.container);


        btnconti = (ImageView) view.findViewById(R.id.btnconti);
        btnstop = (ImageView) view.findViewById(R.id.btnstop);
        btnplay = (ImageView) view.findViewById(R.id.btnplay);
        progressBar = (ProgressBar) view.findViewById(R.id.progrss);
        llvideioview = (RelativeLayout) view.findViewById(R.id.llvideioview);
        videoview = (VideoView) view.findViewById(R.id.videoview);
        mediacontroller.setAnchorView(videoview);

        recyclerViewuser = (RecyclerView) view.findViewById(R.id.recycler_user);
        recyclerViewglobal = (RecyclerView) view.findViewById(R.id.recycler_global);

        LinearLayoutManager llm = new LinearLayoutManager(mcontext);
        recyclerViewuser.setLayoutManager(llm);
        recyclerViewuser.setHasFixedSize(true);
        recyclerViewuser.setNestedScrollingEnabled(false);

        LinearLayoutManager llm1 = new LinearLayoutManager(mcontext);
        recyclerViewglobal.setLayoutManager(llm1);
        recyclerViewglobal.setHasFixedSize(true);
        recyclerViewglobal.setNestedScrollingEnabled(false);


        ivcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(mcontext, NewPostActivity.class);
                in.putExtra("statusmypage", "1");
                startActivity(in);
            }
        });

        ivvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mcontext, NewPostActivity.class);
                in.putExtra("statusmypage", "1");
                startActivity(in);

            }
        });

        llstatus_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mcontext, NewPostActivity.class);
                in.putExtra("statusmypage", "1");
                startActivity(in);
            }
        });

        procamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext, CropImage.class);
                startActivity(intent);
                img = "0";


            }
        });
        covercamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext, CropImage.class);
                startActivity(intent);
                img = "1";
            }
        });

        videoview.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoview.pause();
            }
        });

        videoview.setOnPreparedListener(new OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
                videoview.setBackgroundColor(0);

            }
        });
        btnconti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnconti.setVisibility(View.GONE);
                btnstop.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                uri = Uri.parse(mypic);
                videoview.setMediaController(null);
                videoview.setVideoURI(uri);
                videoview.start();
                videoview.setVisibility(View.VISIBLE);


            }
        });

        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoview.pause();
                progressBar.setVisibility(View.GONE);
                btnplay.setVisibility(View.VISIBLE);
                btnstop.setVisibility(View.GONE);

            }
        });
        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnplay.setVisibility(View.GONE);
                btnstop.setVisibility(View.VISIBLE);
                videoview.setVisibility(View.VISIBLE);
                videoview.start();

            }
        });

        tvName.setText(MyPreferences.getActiveInstance(mcontext).getFirstName()+" "+ MyPreferences.getActiveInstance(mcontext).getLastName());



        imgmystas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FileOpen.openFile(mcontext,Uri.parse(mypic));

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        return view;
    }


    @Override
    public void onResume() {

        super.onResume();
        if (CommanMethod.isOnline(mcontext)) {
            validateshowuserstatus();
            validateshowglobalstatus();
        } else {
            CommanMethod.showSnack(container,mcontext.getResources().getString(R.string.error_no_internet));
        }
        if (MyApplication.cropped != null) {
            FragmentMyPage1.ProfilePosttsAsynTask pro = new FragmentMyPage1.ProfilePosttsAsynTask();
            pro.execute();

            if (img.equals("0")) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                MyApplication.cropped.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                mData = bytes.toByteArray();
                Bitmap decodedByte = BitmapFactory.decodeByteArray(mData, 0, mData.length);
                proimg.setImageBitmap(decodedByte);

                MainActivity.imageViewProfile.setImageBitmap(decodedByte);

            } else if (img.equals("1")) {
                ByteArrayOutputStream bytes1 = new ByteArrayOutputStream();
                MyApplication.cropped.compress(Bitmap.CompressFormat.JPEG, 100, bytes1);
                mData2 = bytes1.toByteArray();
                Bitmap decodedByte1 = BitmapFactory.decodeByteArray(mData2, 0, mData2.length);
                coverimg.setImageBitmap(decodedByte1);

            }
        }


    }

    @Override
    public void getUserId(String type, String userId) {
        if(type.equalsIgnoreCase("Block")){

            validateallBlock(userId,"");

        }else {
            AlertDialogMessage(userId,mcontext);
        }
    }



    @Override
    public void getLocalUserId(String type, String userId) {

        if(type.equalsIgnoreCase("Block")){

            validateallBlock(userId,"");

        }else {
            AlertDialogMessage(userId,mcontext);
        }

    }


    //status update
    public class ProfilePosttsAsynTask extends AsyncTask<String, Void, String> {
        private Dialog mDialog;
        private String response;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            System.out.println("in asynctask");


            super.onPreExecute();
            mDialog = ProgressDialog.show(mcontext, "", "Loading...", true);
            mDialog.setCancelable(false);


        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            response = callService();
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            // TODO Auto-generated method stub
            super.onPostExecute(response);

            if (mDialog != null) {
                mDialog.dismiss();
            }
            Log.d("Response", "" + response);

            //responseCode
            JSONObject object;

            if (response != null) {
                try {
                    object = new JSONObject(response);
                    String success = object.getString("responseCode");
                    String responseMessage = object.getString("responseMessage");
                    Log.d("object", "" + object);
                    if (success.equals("200")) {
                        mDialog.dismiss();
                        MyApplication.cropped = null;
                        Toast.makeText(mcontext, responseMessage,
                                Toast.LENGTH_LONG).show();
                    } else {
                        CommanMethod.showAlert(responseMessage, mcontext);
                        MyApplication.cropped = null;

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                CommanMethod.showAlert(getResources().getString(R.string.connection_error),mcontext);

            }
        }

        private String callService() {
            String url = WebServices.updateuserImage;
            HttpClient client = new HttpClient(url);
            Log.e("before connection", "" + url);
            try {

                client.connectForMultipart();
                client.addFormPart("userId", MyPreferences.getActiveInstance(mcontext).getUserId());
                if (mData != null&&mData.length>0) {
                    client.addFilePart("image1", ".jpg", mData);
                }
                if (mData2 != null&&mData2.length>0) {
                    client.addFilePart("image2", ".jpg", mData2);
                }

                Log.d("client", client.toString());
                client.finishMultipart();
                response = client.getResponse().toString();
                Log.d("responseVideoImage", response);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return response;
        }
    }


    // Assyntask show user status

    private void validateshowuserstatus() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(mcontext).getUserId());
        client.post(WebServices.showuserstatus, params,
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
                            Log.e("userstatusResponse", "--->>" + object.toString(2));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        parseResult(responseCode.toString());

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), mcontext);

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
            if (response.getString("responseCode").equals("200")) {

                userResources.clear();
                mystatus = response.getString("userStatus");
                userTime = response.getString("userTime");
                userproimg = response.getString("profileImage");
                txtmystatus.setText(response.getString("userStatus"));
                mydate.setText(userTime);

                if (!response.getString("userFile").equals("") && response.getString("userFile").endsWith(".mp4")) {
                    llvideioview.setVisibility(View.VISIBLE);
                    imgmystas.setVisibility(View.GONE);
                    mypic = response.getString("userFile");

                } else {
                    if (!response.getString("userFile").equals("")) {
                        imgmystas.setVisibility(View.VISIBLE);
                        llvideioview.setVisibility(View.GONE);
                        mypic = response.getString("userFile");
                        Picasso.with(mcontext)
                                .load(mypic)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .placeholder(R.color.grey)
                                .error(R.color.grey)
                                .fit()
                                .into(imgmystas);
                    }

                }

                if (!response.getString("coverImage").equals("")) {
                    Picasso.with(mcontext)
                            .load(response.getString("coverImage"))
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .placeholder(R.color.grey)
                            .error(R.color.grey)
                            .fit()
                            .into(coverimg);
                }
                if(!response.getString("profileImage").equals("")&&response.getString("profileImage").startsWith("graph"))
                {
                    Picasso.with(mcontext)
                            .load("https://"+response.getString("profileImage"))
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .placeholder(R.color.grey)
                            .error(R.color.grey)
                            .fit()
                            .into(userimage);

                    Picasso.with(mcontext)
                            .load("https://"+response.getString("profileImage"))
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .placeholder(R.color.grey)
                            .error(R.color.grey)
                            .fit()
                            .into(proimg);
                }else {

                    if (!response.getString("profileImage").equals("")) {
                        Picasso.with(mcontext)
                                .load(response.getString("profileImage"))
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .placeholder(R.color.grey)
                                .error(R.color.grey)
                                .fit()
                                .into(proimg);

                        Picasso.with(mcontext)
                                .load(response.getString("profileImage"))
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .placeholder(R.color.grey)
                                .error(R.color.grey)
                                .fit()
                                .into(userimage);
                    }
                }
                if (!response.getString("userFile").equals("")) {
                    Picasso.with(mcontext)
                            .load(response.getString("userFile"))
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .placeholder(R.color.grey)
                            .error(R.color.grey)
                            .fit()
                            .into(imgmystas);
                }

                JSONArray jsonArray = response.getJSONArray("statusList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    MypageUserResources item = new MypageUserResources();


                    if (object.optString("userStatus").toString().trim().equals("null") || object.optString("userStatus").equals("")) {
                        item.setUserStatus("");
                    } else {
                        item.setUserStatus(object.optString("userStatus"));
                    }
                    if (object.optString("userFile").toString().trim().equals("null") || object.optString("userFile").equals("")) {
                        item.setUserFile("");
                    } else {
                        item.setUserFile(object.getString("userFile"));
                        Log.e("userFile", "" + object.getString("userFile"));
                    }
                    if (object.optString("userName").toString().trim().equals("null") || object.optString("userName").equals("")) {
                        item.setUserName("");
                    } else {
                        item.setUserName(object.getString("userName"));
                    }
                    if (object.optString("userId").toString().trim().equals("null") || object.optString("userId").equals("")) {
                        item.setUserId("");
                    } else {
                        item.setUserId(object.optString("userId"));
                    }
                    if (object.optString("profileImage").toString().trim().equals("null") || object.optString("profileImage").equals("")) {
                        item.setProfileImage("");
                    } else {
                        item.setProfileImage(object.optString("profileImage"));
                    }
                    if (object.optString("userTime").toString().trim().equals("null") || object.optString("userTime").equals("")) {
                        item.setUserTime("");
                    } else {
                        item.setUserTime(object.optString("userTime"));
                    }


                    userResources.add(item);
                }
                if (userResources != null) {
                    userAdapter = new LocalUserAdapter(mcontext, userResources,FragmentMyPage1.this,FragmentMyPage1.this);
                    recyclerViewuser.setAdapter(userAdapter);

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
// global show status

    private void validateshowglobalstatus() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(mcontext).getUserId());
        client.post(WebServices.showglobalstatus, params,
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
                            Log.e("globalstatusResponse", "--->>" + object.toString(2));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        parseResultglobal(responseCode.toString());

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //prgDialog.hide();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error),mcontext);

                    }

                    @Override
                    public void onFinish() {
                        //prgDialog.hide();
                        super.onFinish();
                    }

                });

    }

    private void parseResultglobal(String result) {
        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {
                globalResources.clear();
                JSONArray jsonArray = response.getJSONArray("statusList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    MypageGlobalResources item = new MypageGlobalResources();

                    if (object.optString("globalStatus").toString().trim().equals("null") || object.optString("globalStatus").equals("")) {
                        item.setGlobalStatus("");
                    } else {
                        item.setGlobalStatus(object.optString("globalStatus"));
                    }
                    if (object.optString("globalFile").toString().trim().equals("null") || object.optString("globalFile").equals("")) {
                        item.setGlobalFile("");
                    } else {
                        item.setGlobalFile(object.getString("globalFile"));

                    }
                    if (object.optString("userId").toString().trim().equals("null") || object.optString("userId").equals("")) {
                        item.setUserId("");
                    } else {
                        item.setUserId(object.optString("userId"));
                    }
                    if (object.optString("userName").toString().trim().equals("null") || object.optString("userName").equals("")) {
                        item.setUserName("");
                    } else {
                        item.setUserName(object.optString("userName"));
                    }
                    if (object.optString("profileImage").toString().trim().equals("null") || object.optString("profileImage").equals("")) {
                        item.setProfileImage("");
                    } else {
                        item.setProfileImage(object.optString("profileImage"));
                    }
                    if (object.optString("emailId").toString().trim().equals("null") || object.optString("emailId").equals("")) {
                        item.setEmailId("");
                    } else {
                        item.setEmailId(object.optString("emailId"));
                    }
                    if (object.optString("globalTime").toString().trim().equals("null") || object.optString("globalTime").equals("")) {
                        item.setGlobalTime("");
                    } else {
                        item.setGlobalTime(object.optString("globalTime"));
                    }
                    globalResources.add(item);
                }
                if (globalResources != null) {

                    globalAdapter = new GlobleAdapter(mcontext, globalResources,FragmentMyPage1.this,FragmentMyPage1.this);
                    recyclerViewglobal.setAdapter(globalAdapter);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void getUrlLocalFacebook(String url) {
        try{
            if(url.endsWith(".mp4")){
                shareStutusOnfacebook(Uri.parse(url));

            }else if(url.endsWith(".jpeg")||url.endsWith(".jpg")){
                shareImageOnfacebook(CommanMethod.getBitmapFromURL(url));

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    @Override
    public void getUrlforFacebook(String url) {
        try{
            if(url.endsWith(".mp4")){
                shareStutusOnfacebook(Uri.parse(url));

            }else if(url.endsWith(".jpeg")||url.endsWith(".jpg")){

                shareImageOnfacebook(CommanMethod.getBitmapFromURL(url));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    public void shareVideoOnfacebook(Uri uri){

        ShareVideo video = new ShareVideo.Builder()
                .setLocalUrl(uri)
                .build();
        ShareVideoContent content = new ShareVideoContent.Builder()
                .setVideo(video)
                .build();

        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);

    }


    public void shareImageOnfacebook(Bitmap image){

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        if (ShareDialog.canShow(SharePhotoContent.class)) {
            SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();

            shareDialog.show(sharePhotoContent);
        }
    }

    public void shareStutusOnfacebook(Uri uri){

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("MyWorld")
                    .setContentDescription("App")
                    .setContentUrl(uri)
                    .build();

            shareDialog.show(linkContent);
        }
    }


    protected void validateallBlock(String blockUserId,String report_text) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.add("login_user_id", MyPreferences.getActiveInstance(mcontext).getUserId());
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
                        ProgressBarDialog.showProgressBar(mcontext,"");

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
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), mcontext);
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
                Toast.makeText(mcontext,response.getString("responseMessage"),Toast.LENGTH_SHORT).show();

            }else {
                CommanMethod.showAlert(response.getString("responseMessage"),mcontext);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    public void AlertDialogMessage(final String userId1,final Activity activity) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialogLayout = inflater.inflate(R.layout.message_layout, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogLayout);
        final AlertDialog customAlertDialog = builder.create();
        customAlertDialog.show();
        final EditText messageBox = (EditText) dialogLayout.findViewById(R.id.edit_chat_message);
        final TextView tvYes = (TextView) dialogLayout.findViewById(R.id.tvYes);
        final TextView tvCancel = (TextView) dialogLayout.findViewById(R.id.tvCancel);
        final TextView tvBlock = (TextView) dialogLayout.findViewById(R.id.tvBlock);

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageBox.setVisibility(View.VISIBLE);
                if(messageBox.getText().toString().trim().length()>0)
                {
                    String message = messageBox.getText().toString().trim();
                    validateallBlock(userId1,message);

                    customAlertDialog.dismiss();
                }else {
                    CommanMethod.showSnack(v,"Please write some messages");
                }


            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAlertDialog.dismiss();

            }
        });
        tvBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageBox.setVisibility(View.GONE);
                validateallBlock(userId1,"");

            }
        });
    }




}

