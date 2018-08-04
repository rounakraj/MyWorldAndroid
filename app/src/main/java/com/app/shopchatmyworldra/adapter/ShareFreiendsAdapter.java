package com.app.shopchatmyworldra.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.pojo.Friend;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by legacy on 02-Nov-17.
 */

public class ShareFreiendsAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<Friend> itemList;
    private Context mContext;
    public ShareFreiendsAdapter(Context mcontext, ArrayList<Friend> itemList) {
        this.mContext = mcontext;
        this.itemList = itemList;
        mInflater = (LayoutInflater) mcontext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listrow_share, null);
            holder = new ViewHolder();

            holder.name=(TextView)convertView.findViewById(R.id.tv_name);
            holder.tv_stutaus=(TextView)convertView.findViewById(R.id.tv_stutaus);
            holder.iv_con=(CircleImageView)convertView.findViewById(R.id.iv_con);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.name.setText(itemList.get(position).getUserName());
        holder.tv_stutaus.setText(itemList.get(position).getUserStatus());


        if(itemList.get(position).getProfileImage()!=null&&!itemList.get(position).getProfileImage().equals("")&&itemList.get(position).getProfileImage().startsWith("graph"))
        {

            Picasso.with(mContext)
                    .load("https://"+itemList.get(position).getProfileImage())
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .placeholder(R.color.grey)
                    .error(R.color.grey)
                    .into(holder.iv_con);


        }else {

            if (itemList.get(position).getProfileImage()!=null&&!itemList.get(position).getProfileImage().equals("")) {
                Picasso.with(mContext)
                        .load(itemList.get(position).getProfileImage())
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .placeholder(R.color.grey)
                        .error(R.color.grey)
                        .into(holder.iv_con);

            }
        }


        return convertView;
    }

    public class ViewHolder {
        public TextView name,tv_stutaus;
        public CircleImageView iv_con;



    }

}
