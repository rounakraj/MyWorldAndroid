package com.app.shopchatmyworldra.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.UserChatMessageActivity;
import com.app.shopchatmyworldra.UserProfileActivity;
import com.app.shopchatmyworldra.pojo.FriendListResource;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by MMAD on 10-07-2017.
 */

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.AutoMobileServideViewHolder> {

    private List<FriendListResource> itemList;
    private Context context;
    int mpostion;
    callIdinterface callbackcallIdinterface;
    callIdvoiceinterface callbackcallIdvoiceinterface;
    public FriendListAdapter(Context context, List<FriendListResource> itemList,callIdinterface callbackcallIdinterface, callIdvoiceinterface callbackcallIdvoiceinterface) {
        this.itemList = itemList;
        this.context = context;
        this.callbackcallIdinterface = callbackcallIdinterface;
        this.callbackcallIdvoiceinterface = callbackcallIdvoiceinterface;
    }

    @Override
    public AutoMobileServideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendlist_item, null);
        AutoMobileServideViewHolder rcv = new AutoMobileServideViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(AutoMobileServideViewHolder holder, final int position) {

        holder.name.setText(itemList.get(position).getFirstName()+" "+itemList.get(position).getLastName() );
        holder.tv_stutaus.setText(itemList.get(position).getUserStatus());
        holder.ivVideocall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeRefernceId=itemList.get(position).getEmailId();
                callbackcallIdinterface.getPlaceReferId(placeRefernceId);
            }
        });


        if(itemList.get(position).getOnlineFlag().equals("0"))
        {
            holder.ivOnline.setColorFilter(ContextCompat.getColor(context,R.color.gray));
        }else {
            holder.ivOnline.setColorFilter(ContextCompat.getColor(context,R.color.rupee_color));
        }

        holder.ivcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeRefernceId=itemList.get(position).getEmailId();
                String userImage=itemList.get(position).getProfileImage();
                callbackcallIdvoiceinterface.getReferId(placeRefernceId,userImage);
            }
        });
        holder.ivMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, UserChatMessageActivity.class);
                intent.putExtra("Recipient",itemList.get(position).getEmailId());
                intent.putExtra("userName",itemList.get(position).getFirstName()+" "+itemList.get(position).getLastName());
                intent.putExtra("userId",itemList.get(position).getUserId());
                intent.putExtra("userImage",itemList.get(position).getProfileImage());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        if(itemList.get(position).getProfileImage()!=null&&!itemList.get(position).getProfileImage().equals("")&&itemList.get(position).getProfileImage().startsWith("graph"))
        {
            Picasso.with(context)
                    .load("https://"+itemList.get(position).getProfileImage())
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .placeholder(R.color.grey)
                    .error(R.color.grey)
                    .into( holder.iv_con);
        }else {

            if (itemList.get(position).getProfileImage()!=null&&!itemList.get(position).getProfileImage().equals("")) {
                Picasso.with(context)
                        .load(itemList.get(position).getProfileImage())
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .placeholder(R.color.grey)
                        .error(R.color.grey)
                        .into(holder.iv_con);
            }
        }


        holder.iv_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UserProfileActivity.class);
                intent.putExtra("UserId", itemList.get(position).getUserId());
                context.startActivity(intent);
            }

        });


    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    class AutoMobileServideViewHolder extends RecyclerView.ViewHolder  {

        public View view;
        public TextView name,tv_stutaus;
        public ImageView ivVideocall;
        public ImageView ivcall;
        public ImageView ivMessage;
        public ImageView iv_con;
        ImageView ivOnline;


        public AutoMobileServideViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.tv_name);
            tv_stutaus=(TextView)itemView.findViewById(R.id.tv_stutaus);
            ivVideocall=(ImageView) itemView.findViewById(R.id.ivVideocall);
            ivcall=(ImageView) itemView.findViewById(R.id.ivcall);
            ivMessage=(ImageView) itemView.findViewById(R.id.ivMessage);
            iv_con=(ImageView) itemView.findViewById(R.id.iv_con);
            ivOnline = (ImageView) itemView.findViewById(R.id.ivOnline);
        }
    }

    public interface callIdinterface{
        public void getPlaceReferId(String placecallEmailId);
    }


    public interface callIdvoiceinterface{
        public void getReferId(String placecallEmailId, String userImage);
    }



}