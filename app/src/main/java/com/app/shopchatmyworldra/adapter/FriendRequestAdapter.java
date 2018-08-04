package com.app.shopchatmyworldra.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.pojo.FriendRequestResources;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by MMAD on 05-09-2017.
 */

public class FriendRequestAdapter  extends RecyclerView.Adapter<FriendRequestAdapter.CustomViewHolder> {
    private ArrayList<FriendRequestResources> friends;
    private Context mContext;

    private ProgressDialog prgDialog;
    public static Typeface my_font;
    FriendRequestCallback callback;
    private String userId;
    public FriendRequestAdapter(Context context, ArrayList<FriendRequestResources> followers,String userId,FriendRequestCallback callback) {
        this.friends = followers;
        this.mContext = context;
        this.callback = callback;
        this.userId = userId;
    }

    @Override
    public FriendRequestAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friend_request_item, viewGroup, false);

        FriendRequestAdapter.CustomViewHolder viewHolder = new FriendRequestAdapter.CustomViewHolder(view);

        prgDialog = new ProgressDialog(mContext);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FriendRequestAdapter.CustomViewHolder customViewHolder, final int i) {

        final FriendRequestResources friend = friends.get(i);

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

        customViewHolder.btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.regectUserId(friend.getUserId());
            }
        });


        customViewHolder.tvName.setText(friend.getUserName() );
        customViewHolder.tvName.setTypeface(my_font);
        customViewHolder.btn_accept.setTypeface(my_font);
        customViewHolder.btn_reject.setTypeface(my_font);


        if(userId.equalsIgnoreCase(MyPreferences.getActiveInstance(mContext).getUserId())){
            customViewHolder.btn_reject.setVisibility(View.VISIBLE);
            customViewHolder.btn_accept.setVisibility(View.VISIBLE);
        }else {
            customViewHolder.btn_reject.setVisibility(View.GONE);
            customViewHolder.btn_accept.setVisibility(View.GONE);
        }


        customViewHolder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callback.acceptUserId(friend.getUserId());
                /*if (friend.getFollowed().equals("0")) {
                    customViewHolder.btn_accept.setText("Invite friend");
                    customViewHolder.btn_accept.setBackgroundResource(R.drawable.box1);
                    customViewHolder.btn_accept.setTextColor(Color.parseColor("#808080"));
                    customViewHolder.btn_accept.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                    //customViewHolder.tvFllow.setTag(2);
                    //validatefollow(MyPreferences.getActiveInstance(mContext).getUserId(), friend.getUserId(), i);
                } else {

                    customViewHolder.btn_accept.setText("sent");
                    customViewHolder.btn_accept.setTextColor(Color.parseColor("#40a6e8"));
                    customViewHolder.btn_accept.setBackgroundResource(R.drawable.box);
                    customViewHolder.btn_accept.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                    //customViewHolder.tvFllow.setTag(1);
                    // validateunfollow(MyPreferences.getActiveInstance(mContext).getUserId(), friend.getUserId(), i);
                }*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != friends ? friends.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView1;
        protected TextView tvPostName, tvName, btn_accept, btn_reject;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView1 = (ImageView) view.findViewById(R.id.iv_userimge);
            this.tvName = (TextView) view.findViewById(R.id.tv_username);
            this.btn_accept = (TextView) view.findViewById(R.id.btn_accept);
            this.btn_reject = (TextView) view.findViewById(R.id.btn_reject);

        }
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


    public interface FriendRequestCallback
    {
        public void acceptUserId(String userId1);
        public void regectUserId(String userId1);
    }

}
