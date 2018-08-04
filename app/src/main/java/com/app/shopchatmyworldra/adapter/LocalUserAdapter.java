package com.app.shopchatmyworldra.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.UserProfileActivity;
import com.app.shopchatmyworldra.constant.FileOpen;
import com.app.shopchatmyworldra.pojo.MypageUserResources;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by mac on 26/06/18.
 */

public class LocalUserAdapter extends RecyclerView.Adapter<LocalUserAdapter.ViewHolder> {

    private ArrayList<MypageUserResources> itemlist;
    private Context mContext;
    private MediaController mediacontroller;
    private Uri uri;
    private sharFacebookLocalUser callback;
    private blockUser callbackBlockUser;

    public LocalUserAdapter(Context mcontext, ArrayList<MypageUserResources> itemlist,sharFacebookLocalUser callback,blockUser callbackBlockUser) {
        super();
        this.mContext = mcontext;
        this.itemlist = itemlist;
        this.callback = callback;
        this.callbackBlockUser = callbackBlockUser;
        mediacontroller = new MediaController(mcontext);

    }

    @Override
    public LocalUserAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.mypageuser_itmelist, viewGroup, false);
        LocalUserAdapter.ViewHolder viewHolder = new LocalUserAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final LocalUserAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.txtstatus.setText(itemlist.get(position).getUserStatus());
        viewHolder.txtname.setText(itemlist.get(position).getUserName());

        viewHolder.txtuserdate.setText(itemlist.get(position).getUserTime());

        if(itemlist.get(position).getProfileImage()!=null&&!itemlist.get(position).getProfileImage().equals("")&&itemlist.get(position).getProfileImage().startsWith("graph"))
        { Picasso.with(mContext)
                .load("https://"+itemlist.get(position).getProfileImage())
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .placeholder(R.color.grey)
                .error(R.color.grey)
                .fit()
                .into(viewHolder.userrimg);

        }else {
            if (itemlist.get(position).getProfileImage()!=null&&!itemlist.get(position).getProfileImage().equals("")) {
                Picasso.with(mContext)
                        .load(itemlist.get(position).getProfileImage())
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .placeholder(R.color.grey)
                        .error(R.color.grey)
                        .fit()
                        .into(viewHolder.userrimg);

            }
        }

        viewHolder.userrimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, UserProfileActivity.class);
                intent.putExtra("UserId",itemlist.get(position).getUserId());
                mContext.startActivity(intent);
            }

        });



        if(!itemlist.get(position).getUserFile().equals("")&&itemlist.get(position).getUserFile().endsWith(".mp4"))
        {
            viewHolder.llvideioview.setVisibility(View.VISIBLE);
            viewHolder.imgstas.setVisibility(View.GONE);


        }else {
            if(!itemlist.get(position).getUserFile().equals(""))
            {
                viewHolder.imgstas.setVisibility(View.VISIBLE);
                viewHolder.llvideioview.setVisibility(View.GONE);
                Picasso.with(mContext)
                        .load(itemlist.get(position).getUserFile())
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .placeholder(R.color.grey)
                        .error(R.color.grey)
                        .fit()
                        .into(viewHolder.imgstas);
            }

        }

        mediacontroller.setAnchorView(viewHolder.videoview);

        if(itemlist.get(position).isContinuously()){
            viewHolder.videoview.setVisibility(View.VISIBLE);
            itemlist.get(position).setContinuously(true);
            uri=Uri.parse(itemlist.get(position).getUserFile());
            viewHolder.videoview.setMediaController(null);
            viewHolder.videoview.setVideoURI(uri);
            viewHolder.videoview.requestFocus();
            viewHolder.videoview.start();
        }else {
            viewHolder.videoview.setVisibility(View.GONE);
            viewHolder.btnconti.setVisibility(View.VISIBLE);
            viewHolder.btnstop.setVisibility(View.GONE);
        }
        viewHolder.videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(itemlist.get(position).isContinuously()){
                    viewHolder.videoview.setVisibility(View.VISIBLE);
                    viewHolder.videoview.start();
                }else {
                    viewHolder.videoview.setVisibility(View.GONE);
                }
            }
        });
        viewHolder.videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                viewHolder.progressBar.setVisibility(View.GONE);
                viewHolder.videoview.setBackgroundColor(0);
            }
        });
        viewHolder.btnconti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //isContinuously = true;
                itemlist.get(position).setContinuously(true);
                viewHolder.btnconti.setVisibility(View.GONE);
                viewHolder.btnstop.setVisibility(View.VISIBLE);
                viewHolder.progressBar.setVisibility(View.VISIBLE);
                uri=Uri.parse(itemlist.get(position).getUserFile());
                //holder.videoview.setMediaController(mediacontroller);
                viewHolder.videoview.setMediaController(null);
                viewHolder.videoview.setVideoURI(uri);
                viewHolder.videoview.requestFocus();
                viewHolder.videoview.start();
                viewHolder.videoview.setVisibility(View.VISIBLE);


            }
        });
        viewHolder.btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.videoview.pause();
                viewHolder.progressBar.setVisibility(View.GONE);
                viewHolder.btnplay.setVisibility(View.VISIBLE);
                viewHolder.btnstop.setVisibility(View.GONE);

            }
        });
        viewHolder.btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.btnplay.setVisibility(View.GONE);
                viewHolder.btnstop.setVisibility(View.VISIBLE);
                viewHolder.videoview.start();

            }
        });

        if(itemlist.get(position).getUserFile()!=""){
            viewHolder.fb_item_bottom.setVisibility(View.VISIBLE);
        }else {
            viewHolder.fb_item_bottom.setVisibility(View.GONE);

        }
        viewHolder.btnShareVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.getUrlLocalFacebook(itemlist.get(position).getUserFile());
            }
        });
        viewHolder.fb_Block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackBlockUser.getLocalUserId("Block",itemlist.get(position).getUserId());
            }
        });
        viewHolder.fb_Reportcontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackBlockUser.getLocalUserId("Report",itemlist.get(position).getUserId());
            }
        });

        viewHolder.imgstas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOpen.openFile(mContext,Uri.parse(itemlist.get(position).getUserFile()));

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        viewHolder.btnFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    FileOpen.openFile(mContext,Uri.parse(itemlist.get(position).getUserFile()));

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        private ItemClickListener clickListener;
        protected  TextView txtstatus,txtname,txtuserdate;
        protected ImageView imgstas,userrimg;
        protected VideoView videoview;
        protected RelativeLayout llvideioview;
        protected LinearLayout llAllView;
        protected ImageView btnconti,btnstop,btnplay,ivThumbnail,btnFullScreen;
        private ProgressBar progressBar;
        protected LinearLayout fb_item_bottom;
        private AppCompatTextView btnShareVideos;
        private AppCompatTextView fb_Block;
        private AppCompatTextView fb_Reportcontent;

        public ViewHolder(View itemView) {
            super(itemView);
            txtstatus = (TextView) itemView.findViewById(R.id.txtstatus);
            txtuserdate = (TextView) itemView.findViewById(R.id.txtuserdate);
            txtname = (TextView) itemView.findViewById(R.id.txtname);
            imgstas = (ImageView) itemView.findViewById(R.id.imgstas);
            userrimg = (ImageView) itemView.findViewById(R.id.userrimg);
            videoview=(VideoView)itemView.findViewById(R.id.videoview);
            llvideioview=(RelativeLayout) itemView.findViewById(R.id.llvideioview);
            llAllView=(LinearLayout) itemView.findViewById(R.id.llAllView);
            fb_item_bottom = (LinearLayout) itemView.findViewById(R.id.fb_item_bottom);

            btnconti = (ImageView) itemView.findViewById(R.id.btnconti);
            btnstop = (ImageView) itemView.findViewById(R.id.btnstop);
            btnplay = (ImageView) itemView.findViewById(R.id.btnplay);
            ivThumbnail = (ImageView) itemView.findViewById(R.id.ivThumbnail);
            btnFullScreen = (ImageView) itemView.findViewById(R.id.btnFullScreen);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progrss);

            btnShareVideos = (AppCompatTextView) itemView.findViewById(R.id.btnShareVideos);
            fb_Block = (AppCompatTextView) itemView.findViewById(R.id.fb_Block);
            fb_Reportcontent = (AppCompatTextView) itemView.findViewById(R.id.fb_Reportcontent);


            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            //clickListener.onClick(view, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            //clickListener.onClick(view, getPosition(), true);
            return true;
        }
    }


    public interface ItemClickListener {

        void onClick(View view, int position, boolean isLongClick);
    }


    public interface sharFacebookLocalUser{

        public void getUrlLocalFacebook(String url);
    }

    public interface blockUser{

        public void getLocalUserId(String type,String userId);
    }

}
