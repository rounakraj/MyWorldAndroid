package com.app.shopchatmyworldra.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.pojo.AlluserResource;
import com.app.shopchatmyworldra.pojo.User;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 29/07/18.
 */

public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.CustomViewHolder> { //tvEmailId
    private Context mContext;
    private ProgressDialog prgDialog;
    private List<User> user;
    private unblockuser callback;
    public BlockAdapter(Context context, List<User> user,unblockuser callback) {
        this.user = user;
        this.mContext = context;
        this.callback = callback;

    }

    @Override
    public BlockAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_block_list, viewGroup, false);
        BlockAdapter.CustomViewHolder viewHolder = new BlockAdapter.CustomViewHolder(view);

        prgDialog = new ProgressDialog(mContext);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BlockAdapter.CustomViewHolder customViewHolder, final int position) {



        if(user.get(position).getProfileImage()!=null&&!user.get(position).getProfileImage().equals("")&&user.get(position).getProfileImage().startsWith("graph"))
        {

            Picasso.with(mContext)
                    .load("https://"+user.get(position).getProfileImage())
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .placeholder(R.color.grey)
                    .error(R.color.grey)
                    .into(customViewHolder.imageView1);


        }else {

            if (user.get(position).getProfileImage()!=null&&!user.get(position).getProfileImage().equals("")) {
                Picasso.with(mContext)
                        .load(user.get(position).getProfileImage())
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .placeholder(R.color.grey)
                        .error(R.color.grey)
                        .into(customViewHolder.imageView1);

            }
        }

        customViewHolder.tvName.setText(user.get(position).getFirstName() + " " + user.get(position).getLastName());
        customViewHolder.tvEmailId.setText(user.get(position).getEmailId());

        customViewHolder.btnUnBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.getUserId(user.get(position).getUserId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return user.size();
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView1;
        protected TextView tvStutas, tvEmailId, tvName, btnUnBlock;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView1 = (ImageView) view.findViewById(R.id.iv_userimge);
            this.tvName = (TextView) view.findViewById(R.id.tv_username);
            this.btnUnBlock = (TextView) view.findViewById(R.id.btnUnBlock);
            this.tvEmailId = (TextView) view.findViewById(R.id.tvEmailId);

        }


    }

    public interface unblockuser{
        public void getUserId(String userId);
    }

    
}