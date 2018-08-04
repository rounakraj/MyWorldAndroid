package com.app.shopchatmyworldra.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.UserChatMessageActivity;
import com.app.shopchatmyworldra.pojo.FriendListResource;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by legacy on 05-Sep-17.
 */

public class OnlineFriendAdapter extends RecyclerView.Adapter<OnlineFriendAdapter.ViewHolder> {

    ArrayList<FriendListResource> mNearby;
    Context context;

    public OnlineFriendAdapter(Context context, ArrayList<FriendListResource> mNearby) {
        super();
        this.context = context;
        this.mNearby = mNearby;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_listonlinefriend, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.tvName.setText(mNearby.get(i).getFirstName());


        if(mNearby.get(i).getOnlineFlag().equals("0"))
        {
            viewHolder.ivOnline.setVisibility(View.GONE);
        }else {
            viewHolder.ivOnline.setVisibility(View.VISIBLE);
            viewHolder.ivOnline.setColorFilter(ContextCompat.getColor(context,R.color.rupee_color));
        }

        if(mNearby.get(i).getProfileImage()!=null&&!mNearby.get(i).getProfileImage().equals("")&&mNearby.get(i).getProfileImage().startsWith("graph"))
        {
            Picasso.with(context)
                    .load("https://"+mNearby.get(i).getProfileImage())
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .placeholder(R.color.grey)
                    .error(R.color.grey)
                    .into( viewHolder.recentchat_imageView);
        }else {

            if (mNearby.get(i).getProfileImage()!=null&&!mNearby.get(i).getProfileImage().equals("")) {
                Picasso.with(context)
                        .load(mNearby.get(i).getProfileImage())
                        .placeholder(R.color.grey)
                        .error(R.color.grey)
                        .into(viewHolder.recentchat_imageView);
            }
        }

        viewHolder.recentchat_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UserChatMessageActivity.class);
                intent.putExtra("Recipient",mNearby.get(i).getEmailId());
                intent.putExtra("userName",mNearby.get(i).getFirstName()+" "+mNearby.get(i).getLastName());
                intent.putExtra("userId",mNearby.get(i).getUserId());
                intent.putExtra("userImage",mNearby.get(i).getProfileImage());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mNearby.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvName;
        public CircleImageView recentchat_imageView;
        ImageView ivOnline;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            ivOnline = (ImageView) itemView.findViewById(R.id.ivOnline);
            recentchat_imageView = (CircleImageView) itemView.findViewById(R.id.recentchat_imageView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

        }
    }


    public interface ItemClickListener {

        public void onClick();
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
