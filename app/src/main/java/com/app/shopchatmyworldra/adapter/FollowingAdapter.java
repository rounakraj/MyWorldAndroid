package com.app.shopchatmyworldra.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.pojo.Follower;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by MMAD on 04-09-2017.
 */

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.CustomViewHolder> {
    private ArrayList<Follower> followers;
    private Context mContext;
    FollwerCallback callback;
    public static Typeface my_font;
    public FollowingAdapter(Context context, ArrayList<Follower> followers, FollwerCallback callback) {
        this.followers = followers;
        this.mContext = context;
        this.callback=callback;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_followingitem,viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, final int position) {

        if(followers.get(position).getProfileImage()!=null&&!followers.get(position).getProfileImage().equals("")&&followers.get(position).getProfileImage().startsWith("graph"))
        {
            try {
                Bitmap mBitmap = getFacebookProfilePicture("https://"+followers.get(position).getProfileImage());
                customViewHolder.imageView1.setImageBitmap(mBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {

            if (followers.get(position).getProfileImage()!=null&&!followers.get(position).getProfileImage().equals("")) {
                Picasso.with(mContext)
                        .load(followers.get(position).getProfileImage())
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

                if(followers.get(position).getProfileImage()!=null&&!followers.get(position).getProfileImage().equals("")&&followers.get(position).getProfileImage().startsWith("graph"))
                {
                    try {
                        Bitmap mBitmap = getFacebookProfilePicture("https://"+followers.get(position).getProfileImage());
                        imageView.setImageBitmap(mBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {

                    if (followers.get(position).getProfileImage()!=null&&!followers.get(position).getProfileImage().equals("")) {
                        Picasso.with(mContext)
                                .load(followers.get(position).getProfileImage())
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


        customViewHolder.tvName.setText(followers.get(position).getUserName());
        customViewHolder.tvStatus.setText(followers.get(position).getUserStatus());




        customViewHolder.tvFllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String singUserId= MyPreferences.getActiveInstance(mContext).getUserId();
                callback.followuser(followers.get(position).getUserId(),singUserId);
                if (Integer.parseInt(customViewHolder.tvFllow.getTag().toString())==1)
                {
                    customViewHolder.tvFllow.setText("Follow");
                    customViewHolder.tvFllow.setBackgroundResource(R.drawable.box1);
                    customViewHolder.tvFllow.setTextColor(Color.parseColor("#808080"));
                    customViewHolder.tvFllow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                    customViewHolder.tvFllow.setTag(2);
                }else {

                    customViewHolder.tvFllow.setText("Following");
                    customViewHolder.tvFllow.setTextColor(Color.parseColor("#40a6e8"));
                    customViewHolder.tvFllow.setBackgroundResource(R.drawable.box);
                    customViewHolder.tvFllow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                    customViewHolder.tvFllow.setTag(1);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != followers ? followers.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView1;
        protected TextView tvStatus,tvName,tvFllow;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView1 = (ImageView) view.findViewById(R.id.iv_userimge);
            this.tvName=(TextView) view.findViewById(R.id.tv_username);
            this.tvStatus=(TextView) view.findViewById(R.id.tvStatus);
            this.tvFllow=(TextView)view.findViewById(R.id.btn_follow);

        }
    }

    public interface FollwerCallback
    {
        public void followuser(String userId, String singUserID);
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
