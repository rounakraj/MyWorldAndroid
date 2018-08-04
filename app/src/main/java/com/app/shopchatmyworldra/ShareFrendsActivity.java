package com.app.shopchatmyworldra;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.shopchatmyworldra.adapter.ShareFreiendsAdapter;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.FilePath;
import com.app.shopchatmyworldra.constant.HttpClient;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.Friend;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by legacy on 02-Nov-17.
 */

public class ShareFrendsActivity extends AppCompatActivity {

    private LinearLayout llbottom;
    private ListView listviewFriends;
    private TextView tvShreName;
    private LinearLayout llCallbox;
    private ImageView ivSend;
    StringBuilder stringBuilder = new StringBuilder();
    private ShareFreiendsAdapter adapter;
    TextView toolbartitle;
    public Snackbar snackbar;
    private   byte [] thePic = null;
    private  byte [] mvideoData = null;
    private  Bitmap mPreviewImage ;
    ArrayList<Friend> followerArrayList;
    ArrayList<String> stringArrayList = new ArrayList<>();
    byte[] imageArray1, imageArray,imagearrythumb,inputData;
    String selectedImageUri;
    String message;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharelistview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        snackbar = Snackbar.make(findViewById(R.id.fab), "No Internet  Connection", Snackbar.LENGTH_LONG);

        llbottom = (LinearLayout) findViewById(R.id.llbottom);
        listviewFriends = (ListView) findViewById(R.id.listviewFriends);
        tvShreName = (TextView) findViewById(R.id.tvShreName);
        llCallbox = (LinearLayout) findViewById(R.id.llCallbox);
        ivSend = (ImageView) findViewById(R.id.ivSend);
        final Intent intent = getIntent();
        thePic = (byte[]) intent.getByteArrayExtra("data");

        Bundle extras = getIntent().getExtras();
        message = getIntent().getStringExtra("message");

        if (extras != null && extras.containsKey("inputdata")) {
            selectedImageUri = extras.getString("inputdata");
            try{
                String pathresult = FilePath.getPath(this, Uri.parse(selectedImageUri));
                mPreviewImage = ThumbnailUtils.createVideoThumbnail(pathresult, MediaStore.Images.Thumbnails.MINI_KIND);
                InputStream iStream = getContentResolver().openInputStream(Uri.parse(selectedImageUri));
                mvideoData = getBytes(iStream);

            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        if (CommanMethod.isOnline(ShareFrendsActivity.this)) {

            validateFriendList();
        } else {
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(ShareFrendsActivity.this, R.color.colorAccent));
            snackbar.show();
        }
        ImageView ivHome=(ImageView)findViewById(R.id.ivHome);
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(ShareFrendsActivity.this,MainActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent);

            }
        });

        listviewFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                view.setSelected(true);
                ImageView ivImageView = (ImageView) view.findViewById(R.id.ivImageView);

                if (ivImageView.getVisibility() == View.VISIBLE) {
                    // Its visible
                    ivImageView.setVisibility(View.GONE);
                    if(stringArrayList.contains(followerArrayList.get(position).getUserId()))
                    {
                        stringArrayList.remove(followerArrayList.get(position).getUserId());
                    }
                } else {
                    // Either gone or invisible
                    if(!stringArrayList.contains(followerArrayList.get(position).getUserId()))
                    {
                        stringArrayList.add(followerArrayList.get(position).getUserId());
                    }
                    ivImageView.setVisibility(View.VISIBLE);
                }
                if (stringArrayList.size() > 0) {
                    llbottom.setVisibility(View.VISIBLE);
                } else {
                    llbottom.setVisibility(View.GONE);
                }
            }
        });

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(thePic!=null){
                    imageArray1 = thePic;
                }else {
                    if(mPreviewImage!=null) {
                        imagearrythumb = sendImage(mPreviewImage);
                        inputData = mvideoData;
                    }
                }


                for (String e : stringArrayList) {
                    stringBuilder.append(e);
                    stringBuilder.append(",");
                }

                Log.e("UiserId","call"+stringBuilder.toString());
                ShareFrendsActivity.AddPosttsAsynTask addpost = new ShareFrendsActivity.AddPosttsAsynTask();
                addpost.execute();
            }
        });

        ImageView ivBackArrow = (ImageView) findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        llCallbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareFrendsActivity.this, AllUserActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

    }

    public byte[] sendImage(Bitmap bm) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
        imageArray = bos.toByteArray();
        return imageArray;
    }

    public class AddPosttsAsynTask extends AsyncTask<String, Void, String> {
        private String response;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            System.out.println("in asynctask");


            super.onPreExecute();
            ProgressBarDialog.showProgressBar(ShareFrendsActivity.this,"");


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
            ProgressBarDialog.dismissProgressDialog();

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
                        ShareFrendsActivity.this.finish();

                    } else {
                        CommanMethod.showAlert(responseMessage, ShareFrendsActivity.this);

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                CommanMethod.showAlert(getResources().getString(R.string.connection_error), ShareFrendsActivity.this);

            }
        }

        private String callService() {
            String url = WebServices.sendBulkImage;
            HttpClient client = new HttpClient(url);
            Log.e("before connection", "" + url);
            try {
                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();
                String Myworld = "Myworld" + ts;
                client.connectForMultipart();
                client.addFormPart("userId1", MyPreferences.getActiveInstance(ShareFrendsActivity.this).getUserId());
                client.addFormPart("alluserId", stringBuilder.substring(0, stringBuilder.length() - 1));
                if (imageArray1 != null) {
                    client.addFilePart("chatFile", ".png", imageArray1);
                }
                if (inputData != null) {
                    client.addFilePart("thumbVideo", Myworld + ".png",imagearrythumb);
                    client.addFilePart("chatVideo",  Myworld +".mp4", inputData);
                }if(message!=null)
                {
                    client.addFormPart("chatMsg", message);
                }
                Log.d("client", client.toString());
                client.finishMultipart();
                response = client.getResponse().toString();
                Log.d("response", response);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return response;
        }
    }

    protected void validateFriendList() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(ShareFrendsActivity.this).getUserId());
        client.post(WebServices.getFriends, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(ShareFrendsActivity.this,"");

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        Log.e("", "Hash -> " + responseCode.toString());
                        Log.e("", "On Success of NoTs");
                        parseResult(responseCode.toString());

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), ShareFrendsActivity.this);
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
            JSONArray posts = response.getJSONArray("friendList");

            if (response.getString("responseCode").equals("200")) {
                followerArrayList = new ArrayList<Friend>();
                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.getJSONObject(i);
                    Friend follower = new Friend();
                    follower.setUserId(post.getString("userId"));
                    follower.setUserName(post.getString("firstName"));
                    follower.setLastName(post.getString("lastName"));
                    follower.setProfileImage(post.getString("profileImage"));
                    follower.setFollowed(post.getString("followingStatus"));
                    follower.setUserStatus(post.optString("userStatus"));
                    follower.setEmail(post.getString("emailId"));
                    followerArrayList.add(follower);
                }

                if (followerArrayList != null && followerArrayList.size() > 0) {
                    adapter = new ShareFreiendsAdapter(ShareFrendsActivity.this, followerArrayList);
                    listviewFriends.setAdapter(adapter);
                    llCallbox.setVisibility(View.GONE);
                } else {
                    llCallbox.setVisibility(View.VISIBLE);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }




}
