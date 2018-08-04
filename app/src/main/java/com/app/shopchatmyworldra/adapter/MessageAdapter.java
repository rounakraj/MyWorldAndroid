package com.app.shopchatmyworldra.adapter;

import android.app.Activity;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.constant.FileOpen;
import com.app.shopchatmyworldra.downloadFile.CheckForSDCard;
import com.app.shopchatmyworldra.downloadFile.Utils;
import com.app.shopchatmyworldra.pojo.ChatList;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 03/08/18.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>
{
    List<ChatList> chatList ;
    Activity activity;
    private imageDownload callbackDownload;
    private playAudio callbackAudio;
    private SparseBooleanArray selectedItems;
    private File apkStorage = null;
    private File outputFile = null;

    public MessageAdapter(List<ChatList> chatList , Activity activity, imageDownload callbackDownload, playAudio callbackAudio) {
        this.chatList = chatList;
        this.activity=activity;
        this.callbackDownload=callbackDownload;
        this.callbackAudio=callbackAudio;
        selectedItems = new SparseBooleanArray();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_chat, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {

        holder.setIsRecyclable(false);
        final ChatList mData = chatList.get(position);
        String MyChat= mData.getChatFlag();

        //Get File if SD card is present
        if (new CheckForSDCard().isSDCardPresent()) {
            apkStorage = new File(
                    Environment.getExternalStorageDirectory() + "/"
                            + Utils.downloadDirectory);
        } else {
            Toast.makeText(activity, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();
        }

        if(MyChat.equalsIgnoreCase("0"))
        {
            holder.layout_chat_mine.setVisibility(View.VISIBLE);
            holder.layout_chat_others.setVisibility(View.GONE);
            holder.tvTime1.setText(mData.getChatDate());

            if(mData!=null&&!mData.getChatMsg().isEmpty()){
                holder.llBackgroundAudio.setVisibility(View.GONE);
                holder.llBackgroundVideo.setVisibility(View.GONE);
                holder.llBackgroundImage.setVisibility(View.GONE);

                holder.tv_message_mine.setText(mData.getChatMsg());

            }else if(mData!=null&&!mData.getChatFile().isEmpty()){

                holder.llBackgroundAudio.setVisibility(View.GONE);
                holder.llBackgroundVideo.setVisibility(View.GONE);
                holder.llBackgroundImage.setVisibility(View.VISIBLE);
                holder.tv_message_mine.setVisibility(View.GONE);

                    Picasso.with(activity).load(mData.getChatFile())
                            .error(R.color.greay)
                            .placeholder(R.color.greay)
                            .resize(400,400)
                            .centerCrop()
                            .into(holder.chat_imageView);

                callbackDownload.getUrl(chatList.get(position).getChatFile());
                outputFile = new File(apkStorage, chatList.get(position).getChatFile().replace(Utils.mainUrl, ""));
                if (outputFile!=null&&!outputFile.exists()) {
                    System.out.print("Ranjeetdoit"+"Yes>>>>>>>>>>");
                    holder.donut_progress.setVisibility(View.VISIBLE);

                }else {
                    if(holder.donut_progress!=null)
                        holder.donut_progress.setVisibility(View.GONE);
                    System.out.print("Ranjeetdoit"+"NO>>>>>>>>");

                }
            }else if(mData!=null&&!mData.getChatAudio().isEmpty()){

                holder.llBackgroundAudio.setVisibility(View.VISIBLE);
                holder.llBackgroundVideo.setVisibility(View.GONE);
                holder.llBackgroundImage.setVisibility(View.GONE);
                holder.tv_message_mine.setVisibility(View.GONE);

            }else if(mData!=null&&!mData.getChatVideo().isEmpty()){
                holder.llBackgroundAudio.setVisibility(View.GONE);
                holder.llBackgroundVideo.setVisibility(View.VISIBLE);
                holder.llBackgroundImage.setVisibility(View.GONE);
                holder.tv_message_mine.setVisibility(View.GONE);
                Picasso.with(activity).load(mData.getChatTime())
                        .error(R.color.greay)
                        .placeholder(R.color.greay)
                        .resize(400,400)
                        .centerCrop()
                        .into(holder.ivThumbnail);

                callbackDownload.getUrl(chatList.get(position).getChatVideo());
                outputFile = new File(apkStorage, chatList.get(position).getChatVideo().replace(Utils.mainUrl, ""));
                if (outputFile!=null&&!outputFile.exists()) {
                    System.out.print("Ranjeetdoit"+"Yes>>>>>>>>>>");
                    holder.donut_progress.setVisibility(View.VISIBLE);

                }else {
                    if(holder.donut_progress!=null)
                        holder.donut_progress.setVisibility(View.GONE);
                    System.out.print("Ranjeetdoit"+"NO>>>>>>>>");

                }
            }
        }
        else
        {
            holder.layout_chat_mine.setVisibility(View.GONE);
            holder.layout_chat_others.setVisibility(View.VISIBLE);
            holder.tvTime.setText(mData.getChatDate());

            if(mData!=null&&!mData.getChatMsg().isEmpty()){
                holder.llBackgroundAudio1.setVisibility(View.GONE);
                holder.llBackgroundVideo1.setVisibility(View.GONE);
                holder.llBackgroundImage1.setVisibility(View.GONE);
                holder.tv_message_other.setText(mData.getChatMsg());
            }else if(mData!=null&&!mData.getChatFile().isEmpty()){

                holder.llBackgroundAudio1.setVisibility(View.GONE);
                holder.llBackgroundVideo1.setVisibility(View.GONE);
                holder.llBackgroundImage1.setVisibility(View.VISIBLE);
                holder.tv_message_other.setVisibility(View.GONE);
                Picasso.with(activity).load(mData.getChatFile())
                        .error(R.color.greay)
                        .placeholder(R.color.greay)
                        .resize(400,400)
                        .centerCrop()
                        .into(holder.chat_imageView1);

                callbackDownload.getUrl(chatList.get(position).getChatFile());
                outputFile = new File(apkStorage, chatList.get(position).getChatFile().replace(Utils.mainUrl, ""));
                if (outputFile!=null&&!outputFile.exists()) {
                    System.out.print("Ranjeetdoit"+"Yes>>>>>>>>>>");
                    holder.donut_progress1.setVisibility(View.VISIBLE);

                }else {
                    if(holder.donut_progress1!=null)
                        holder.donut_progress1.setVisibility(View.GONE);
                    System.out.print("Ranjeetdoit"+"NO>>>>>>>>");

                }

            }else if(mData!=null&&!mData.getChatAudio().isEmpty()){
                holder.llBackgroundAudio1.setVisibility(View.VISIBLE);
                holder.llBackgroundVideo1.setVisibility(View.GONE);
                holder.llBackgroundImage1.setVisibility(View.GONE);
                holder.tv_message_other.setVisibility(View.GONE);
            }else if(mData!=null&&!mData.getChatVideo().isEmpty()){

                holder.llBackgroundAudio1.setVisibility(View.GONE);
                holder.llBackgroundVideo1.setVisibility(View.VISIBLE);
                holder.llBackgroundImage1.setVisibility(View.GONE);
                holder.tv_message_other.setVisibility(View.GONE);
                Picasso.with(activity).load(mData.getChatTime())
                        .error(R.color.greay)
                        .placeholder(R.color.greay)
                        .resize(400,400)
                        .centerCrop()
                        .into(holder.ivThumbnail1);

                callbackDownload.getUrl(chatList.get(position).getChatVideo());
                outputFile = new File(apkStorage, chatList.get(position).getChatVideo().replace(Utils.mainUrl, ""));
                if (outputFile!=null&&!outputFile.exists()) {
                    System.out.print("Ranjeetdoit"+"Yes>>>>>>>>>>");
                    holder.donut_progress1.setVisibility(View.VISIBLE);

                }else {
                    if(holder.donut_progress1!=null)
                        holder.donut_progress1.setVisibility(View.GONE);
                    System.out.print("Ranjeetdoit"+"NO>>>>>>>>");

                }
            }
        }

        holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOpen.openFile(activity, Uri.parse(chatList.get(position).getChatVideo()));

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        holder.btnPlayAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callbackAudio.audioUrl(chatList.get(position).getChatAudio(),holder.btnPlayAudio);
            }
        });
        holder.btnPlay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOpen.openFile(activity, Uri.parse(chatList.get(position).getChatVideo()));

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        holder.btnPlayAudio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callbackAudio.audioUrl(chatList.get(position).getChatAudio(),holder.btnPlayAudio);
            }
        });
        holder.chat_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOpen.openFile(activity, Uri.parse(chatList.get(position).getChatFile()));

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        holder.chat_imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOpen.openFile(activity, Uri.parse(chatList.get(position).getChatFile()));

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_message_other, tv_message_mine,singleName,senderName,tvTime,tvTime1;
        LinearLayout layout_chat_others,layout_chat_mine;
        LinearLayout llBackgroundAudio,llBackgroundAudio1;
        RelativeLayout llBackgroundImage,llBackgroundImage1;
        RelativeLayout llBackgroundVideo,llBackgroundVideo1;
        ImageButton btnPlayAudio,btnPlayAudio1;
        ImageView chat_imageView,chat_imageView1;
        ImageView ivThumbnail,ivThumbnail1;
        ImageView btnPlay,btnPlay1;
        DonutProgress donut_progress,donut_progress1;
        public MyViewHolder(View view)
        {
            super(view);
            tv_message_other = (TextView) view.findViewById(R.id.tv_message_other);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            tvTime1 = (TextView) view.findViewById(R.id.tvTime1);
            tv_message_mine = (TextView) view.findViewById(R.id.tv_message_mine);
            layout_chat_others=(LinearLayout)view.findViewById(R.id.layout_chat_others);
            layout_chat_mine=(LinearLayout)view.findViewById(R.id.layout_chat_mine);
            llBackgroundAudio=(LinearLayout)view.findViewById(R.id.llBackgroundAudio);
            llBackgroundAudio1=(LinearLayout)view.findViewById(R.id.llBackgroundAudio1);
            llBackgroundVideo=(RelativeLayout) view.findViewById(R.id.llBackgroundVideo);
            llBackgroundVideo1=(RelativeLayout) view.findViewById(R.id.llBackgroundVideo1);
            llBackgroundImage=(RelativeLayout) view.findViewById(R.id.llBackgroundImage);
            llBackgroundImage1=(RelativeLayout) view.findViewById(R.id.llBackgroundImage1);
            btnPlayAudio=(ImageButton) view.findViewById(R.id.btnPlayAudio);
            btnPlayAudio1=(ImageButton) view.findViewById(R.id.btnPlayAudio1);
            chat_imageView=(ImageView) view.findViewById(R.id.chat_imageView);
            chat_imageView1=(ImageView) view.findViewById(R.id.chat_imageView1);
            ivThumbnail=(ImageView) view.findViewById(R.id.ivThumbnail);
            ivThumbnail1=(ImageView) view.findViewById(R.id.ivThumbnail1);
            btnPlay=(ImageView) view.findViewById(R.id.btnPlay);
            btnPlay1=(ImageView) view.findViewById(R.id.btnPlay1);
            donut_progress=(DonutProgress) view.findViewById(R.id.donut_progress);
            donut_progress1=(DonutProgress) view.findViewById(R.id.donut_progress1);
        }
    }

    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        }
        else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }
    public interface imageDownload{

        public void getUrl(String url);
    }

    public interface playAudio{

        public void audioUrl(String url, ImageButton button);
    }


}
