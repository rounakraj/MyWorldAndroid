package com.app.shopchatmyworldra.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.UserProfileActivity;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.Friend;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.Header;

/**
 * Created by MMAD on 04-09-2017.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.CustomViewHolder> {
    private ArrayList<Friend> friends;
    private Context mContext;
    private String userId;
    private ProgressDialog prgDialog;
    public FriendAdapter(Context context, ArrayList<Friend> followers,String userId) {
        this.friends = followers;
        this.userId = userId;
        this.mContext = context;

    }

    @Override
    public FriendAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friendlistitem,viewGroup, false);

        FriendAdapter.CustomViewHolder viewHolder = new FriendAdapter.CustomViewHolder(view);

        prgDialog = new ProgressDialog(mContext);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FriendAdapter.CustomViewHolder customViewHolder, final int i) {

        final Friend friend = friends.get(i);

        if(friends.get(i).getProfileImage()!=null&&!friends.get(i).getProfileImage().equals("")&&friends.get(i).getProfileImage().startsWith("graph"))
        {
            try {
                Bitmap mBitmap = getFacebookProfilePicture("https://"+friends.get(i).getProfileImage());
                customViewHolder.imageView1.setImageBitmap(mBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {

            if (friends.get(i).getProfileImage()!=null&&!friends.get(i).getProfileImage().equals("")) {
                Picasso.with(mContext)
                        .load(friends.get(i).getProfileImage())
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .placeholder(R.color.grey)
                        .error(R.color.grey)
                        .into(customViewHolder.imageView1);
            }
        }


        customViewHolder.imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                LayoutInflater factory = LayoutInflater.from(mContext);
                View vi = factory.inflate(R.layout.zoom_image, null);
                alert.setView(vi);
                ImageView imageView = (ImageView) vi.findViewById(R.id.mImagId);

                if(friends.get(i).getProfileImage()!=null&&!friends.get(i).getProfileImage().equals("")&&friends.get(i).getProfileImage().startsWith("graph"))
                {
                    try {
                        Bitmap mBitmap = getFacebookProfilePicture("https://"+friends.get(i).getProfileImage());
                        imageView.setImageBitmap(mBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {

                    if (friends.get(i).getProfileImage()!=null&&!friends.get(i).getProfileImage().equals("")) {
                        Picasso.with(mContext)
                                .load(friends.get(i).getProfileImage())
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

        customViewHolder.btn_unfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateunfriend(MyPreferences.getActiveInstance(mContext).getUserId(),friend.getUserId());
            }
        });

        customViewHolder.tvName.setText(friend.getUserName()+" "+friend.getLastName());
        customViewHolder.tvStatus.setText(friend.getUserStatus());


        if(friend.getFollowed().equals("0"))
        {
            customViewHolder.tvFllow.setText("Follow");
            customViewHolder.tvFllow.setTextColor(Color.parseColor("#BFBDBD"));
            customViewHolder.tvFllow.setBackgroundResource(R.drawable.box1);
            customViewHolder.tvFllow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            customViewHolder.tvFllow.setTag(2);

        }else {
            customViewHolder.tvFllow.setText("Unfollow");
            customViewHolder.tvFllow.setTextColor(Color.parseColor("#40A6E8"));
            customViewHolder.tvFllow.setBackgroundResource(R.drawable.box);
            customViewHolder.tvFllow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            customViewHolder.tvFllow.setTag(1);
        }
        if(userId.equalsIgnoreCase(MyPreferences.getActiveInstance(mContext).getUserId())){
            customViewHolder.tvFllow.setVisibility(View.VISIBLE);
            customViewHolder.btn_unfriend.setVisibility(View.VISIBLE);
        }else {
            customViewHolder.tvFllow.setVisibility(View.GONE);
            customViewHolder.btn_unfriend.setVisibility(View.GONE);
        }

        customViewHolder.tvFllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (friend.getFollowed().equals("0"))
                {
                    customViewHolder.tvFllow.setText("Follow");
                    customViewHolder.tvFllow.setBackgroundResource(R.drawable.box1);
                    customViewHolder.tvFllow.setTextColor(Color.parseColor("#808080"));
                    customViewHolder.tvFllow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                    //customViewHolder.tvFllow.setTag(2);
                    validatefollow(friends.get(i).getUserId(),i);

                }else {

                    customViewHolder.tvFllow.setText("Unfollow");
                    customViewHolder.tvFllow.setTextColor(Color.parseColor("#40a6e8"));
                    customViewHolder.tvFllow.setBackgroundResource(R.drawable.box);
                    customViewHolder.tvFllow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                    //customViewHolder.tvFllow.setTag(1);
                    validateunfollow(friends.get(i).getUserId(),i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != friends ? friends.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView1;
        protected TextView tvStatus,tvName,tvFllow,btn_unfriend;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView1 = (ImageView) view.findViewById(R.id.iv_userimge);
            this.tvName=(TextView) view.findViewById(R.id.tv_username);
            this.tvStatus=(TextView) view.findViewById(R.id.tvStatus);
            this.tvFllow=(TextView)view.findViewById(R.id.btn_followfriendlist);
            this.btn_unfriend=(TextView)view.findViewById(R.id.btn_unfriend);

        }
    }





    private void validateunfriend(String userId1,String userId2) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        params.add("userId1",userId1);
        params.add("userId2",userId2);

        client.post(WebServices.unfriend, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        prgDialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        try {
                            JSONObject object = new JSONObject(responseCode.toString());
                            Log.e("Response", "--->>" + object.toString(2));
                            String message=object.getString("responseMessage");
                            if(object.getString("responseCode").equals("200"))
                            {

                                Intent in=new Intent(mContext,UserProfileActivity.class);
                                mContext.startActivity(in);
                                Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();

                            }else {
                                CommanMethod.showAlert(message,mContext);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        prgDialog.hide();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        prgDialog.hide();
                    }

                });

    }


    private void validatefollow(String userId2,final int positon) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId1",MyPreferences.getActiveInstance(mContext).getUserId());
        params.add("userId2",userId2);
        client.post(WebServices.FOLLOW, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        prgDialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        try {
                            JSONObject object = new JSONObject(responseCode.toString());
                            Log.e("Response", "--->>" + object.toString(2));
                            String message=object.getString("responseMessage");
                            if(object.getString("responseCode").equals("200"))
                            {
                                friends.get(positon).setFollowed("1");
                                Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();

                            }else {
                                CommanMethod.showAlert(message,mContext);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        prgDialog.hide();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        prgDialog.hide();
                    }

                });

    }



    private void validateunfollow(String userId2,final int positon) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId1",MyPreferences.getActiveInstance(mContext).getUserId());
        params.add("userId2",userId2);
        client.post(WebServices.Unfollow, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        prgDialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        try {
                            JSONObject object = new JSONObject(responseCode.toString());
                            Log.e("Response", "--->>" + object.toString(2));
                            String message=object.getString("responseMessage");
                            if(object.getString("responseCode").equals("200"))
                            {

                                friends.get(positon).setFollowed("0");
                                Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();

                            }else {
                                CommanMethod.showAlert(message,mContext);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        prgDialog.hide();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        prgDialog.hide();
                    }

                });

    }

    public static Bitmap getFacebookProfilePicture(String url) throws IOException {
        Bitmap bitmap = null;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL imageURL = new URL(url);
            HttpsURLConnection conn1 = (HttpsURLConnection) imageURL.openConnection();
            HttpsURLConnection.setFollowRedirects(true);
            conn1.setInstanceFollowRedirects(true);
            bitmap = BitmapFactory.decodeStream(conn1.getInputStream());
        }
        return bitmap;
    }

}
