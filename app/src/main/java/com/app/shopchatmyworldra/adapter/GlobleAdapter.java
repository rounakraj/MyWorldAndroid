package com.app.shopchatmyworldra.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.UserProfileActivity;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.FileOpen;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.pojo.MypageGlobalResources;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mac on 26/06/18.
 */

public class GlobleAdapter extends RecyclerView.Adapter<GlobleAdapter.ViewHolder> {

    private ArrayList<MypageGlobalResources> itemlist;
    private Context mContext;
    private sharFacebookGloble callback;
    private blockUser callbackBlockUser;

    private MediaController mediacontroller;
    private Uri uri;

    public GlobleAdapter(Context mcontext, ArrayList<MypageGlobalResources> itemlist, sharFacebookGloble callback,blockUser callbackBlockUser) {
        super();
        this.mContext = mcontext;
        this.itemlist = itemlist;
        this.callback = callback;
        this.callbackBlockUser = callbackBlockUser;
        mediacontroller = new MediaController(mcontext);

    }

    @Override
    public GlobleAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.mypage_global_itemlist, viewGroup, false);
        GlobleAdapter.ViewHolder viewHolder = new GlobleAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final GlobleAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.txtstatus.setText(itemlist.get(position).getGlobalStatus());
        viewHolder.txtname.setText(itemlist.get(position).getUserName());
        viewHolder.txtdate.setText(itemlist.get(position).getGlobalTime());



        if(itemlist.get(position).getProfileImage()!=null&&!itemlist.get(position).getProfileImage().equals("")&&itemlist.get(position).getProfileImage().startsWith("graph"))
        {

            Picasso.with(mContext)
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




        if (!itemlist.get(position).getGlobalFile().equals("") && itemlist.get(position).getGlobalFile().endsWith(".mp4")) {
            viewHolder.imgstas.setVisibility(View.GONE);
            viewHolder.llvideioview.setVisibility(View.VISIBLE);

        } else {
            if (!itemlist.get(position).getGlobalFile().equals("")) {
                viewHolder.imgstas.setVisibility(View.VISIBLE);
                viewHolder.llvideioview.setVisibility(View.GONE);
                Picasso.with(mContext)
                        .load(itemlist.get(position).getGlobalFile())
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .placeholder(R.color.grey)
                        .error(R.color.grey)
                        .fit()
                        .into(viewHolder.imgstas);
            }

        }
        viewHolder.imgstas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    FileOpen.openFile(mContext,Uri.parse(itemlist.get(position).getGlobalFile()));

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });



        mediacontroller.setAnchorView(viewHolder.videoview);

        if(itemlist.get(position).isContinuously()){
            viewHolder.videoview.setVisibility(View.VISIBLE);
            itemlist.get(position).setContinuously(true);
            uri=Uri.parse(itemlist.get(position).getGlobalFile());
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
                itemlist.get(position).setContinuously(true);
                viewHolder.btnconti.setVisibility(View.GONE);
                viewHolder.btnstop.setVisibility(View.VISIBLE);
                viewHolder.progressBar.setVisibility(View.VISIBLE);
                uri=Uri.parse(itemlist.get(position).getGlobalFile());
                viewHolder.videoview.setMediaController(null);
                //holder.videoview.setMediaController(mediacontroller);
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
                viewHolder.btnplay.setVisibility(View.VISIBLE);
                viewHolder.btnstop.setVisibility(View.GONE);
                viewHolder.progressBar.setVisibility(View.GONE);

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
        viewHolder.tvinvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateinvitefriend(itemlist.get(position).getEmailId());
            }
        });

        viewHolder.btnShareVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.getUrlforFacebook(itemlist.get(position).getGlobalFile());
            }
        });

        if(itemlist.get(position).getGlobalFile()!=""){
            viewHolder.fb_item_bottom.setVisibility(View.VISIBLE);
        }else {
            viewHolder.fb_item_bottom.setVisibility(View.GONE);

        }

        viewHolder.fb_Block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackBlockUser.getUserId("Block",itemlist.get(position).getUserId());
            }
        });
        viewHolder.fb_Reportcontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackBlockUser.getUserId("Report",itemlist.get(position).getUserId());
            }
        });
        viewHolder.btnFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    FileOpen.openFile(mContext,Uri.parse(itemlist.get(position).getGlobalFile()));

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
        protected TextView txtstatus, txtname, tvinvite, txtdate;
        protected ImageView imgstas, userrimg;
        protected VideoView videoview;
        protected RelativeLayout llvideioview;
        protected RelativeLayout llAllview1;
        protected LinearLayout fb_item_bottom;
        protected ImageView btnconti,btnstop,btnplay,ivThumbnail,btnFullScreen;
        private ProgressBar progressBar;
        private AppCompatTextView btnShareVideos;
        private AppCompatTextView fb_Block;
        private AppCompatTextView fb_Reportcontent;

        public ViewHolder(View itemView) {
            super(itemView);
            txtstatus = (TextView) itemView.findViewById(R.id.txtstatus);
            txtname = (TextView) itemView.findViewById(R.id.txtname);
            imgstas = (ImageView) itemView.findViewById(R.id.imgstas);
            userrimg = (ImageView) itemView.findViewById(R.id.userrimg);
            videoview = (VideoView) itemView.findViewById(R.id.videoview);
            llvideioview = (RelativeLayout) itemView.findViewById(R.id.llvideioview);
            llAllview1 = (RelativeLayout) itemView.findViewById(R.id.llAllview1);
            fb_item_bottom = (LinearLayout) itemView.findViewById(R.id.fb_item_bottom);
            tvinvite = (TextView) itemView.findViewById(R.id.btn_invitefriendlist);
            txtdate = (TextView) itemView.findViewById(R.id.txtdate);
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
            //this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            //clickListener.onClick(view, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
           // clickListener.onClick(view, getPosition(), true);
            return true;
        }
    }


    public interface ItemClickListener {

        void onClick(View view, int position, boolean isLongClick);
    }

    private void validateinvitefriend(String emailid) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(mContext).getUserId());
        params.add("emailId", emailid);
        client.post(WebServices.sendrequest, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        //ProgressBarDialog.showProgressBar(mContext,"");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        try {
                            JSONObject object = new JSONObject(responseCode.toString());
                            Log.e("Response", "--->>" + object.toString(2));
                            String message = object.getString("responseMessage");
                            if (object.getString("responseCode").equals("200")) {

                               /* Intent in=new Intent(mContext,FriendList.class);
                                mContext.startActivity(in);*/
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                            } else {
                                CommanMethod.showAlert(message, mContext);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();
                    }

                });

    }
    public interface sharFacebookGloble{

        public void getUrlforFacebook(String url);
    }

    public interface blockUser{

        public void getUserId(String type,String userId);
    }


}