package com.app.shopchatmyworldra.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.app.shopchatmyworldra.pojo.UserChatMessage;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by legacy on 08-Sep-17.
 */

public class UserChatAdapter extends BaseAdapter {

    private List<UserChatMessage> chatMessages;
    private Activity context;
    private  imageDownload callback;
    private  playAudio audiocallback;
    private  imageZoome imagecallback;
    private File apkStorage = null;
    private File outputFile = null;
    private Timer timer;
    private LayoutInflater mInflater;
    private static final int MESSAGE_ = 0;
    private static final int PICTURE_ = 1;
    private static final int VIDEO_ = 2;
    private static final int Audio_= 3;
    private SparseBooleanArray mSelectedItemsIds;

    public UserChatAdapter( Activity context,imageDownload callback,playAudio audiocallback, ArrayList<UserChatMessage> chatMessages,imageZoome imagecallback) {
        this.context = context;
        this.chatMessages = chatMessages;
        this.callback = callback;
        this.audiocallback = audiocallback;
        this.imagecallback = imagecallback;
        timer = new Timer();
        mSelectedItemsIds = new SparseBooleanArray();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getItemViewType(int position) {
        UserChatMessage dataSet=null;
        try {
            dataSet = (UserChatMessage) chatMessages.get(position);

        }catch (Exception ex){
            ex.printStackTrace();
        }

        if(dataSet!=null&&!dataSet.getMessage().isEmpty()){
            return 0;
        }else if(dataSet!=null&&!dataSet.getImage().isEmpty()){
            return 1;
        }else if(dataSet!=null&&!dataSet.getChatTime().isEmpty()){
            return 2;
        }else if(dataSet!=null&&!dataSet.getChatAudio().isEmpty()){
            return 3;
        }
        else{
            return 4;
        }


    }
    @Override
    public  int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getCount() {
        if (chatMessages != null) {
            return chatMessages.size();
        } else {
            return 0;
        }
    }

    @Override
    public UserChatMessage getItem(int position) {
        if (chatMessages != null&&chatMessages.size()>0) {
            return chatMessages.get(position);
        } else {
            return null;
        }
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        final ViewHolder holder;
        UserChatMessage chatMessage = getItem(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case MESSAGE_:
                    convertView = mInflater.inflate(R.layout.layout_message, null);
                    setAlignmentMessage(convertView,holder, chatMessage.getChatFlag());
                    break;
                case PICTURE_:
                    convertView = mInflater.inflate(R.layout.layout_image, null);
                    setAlignmentPICTURE(convertView,holder, chatMessage.getChatFlag());
                    setProgressbar(holder);
                    break;
                case Audio_:
                    convertView = mInflater.inflate(R.layout.layout_audio, null);
                    setAlignmentAUDIO(convertView,holder, chatMessage.getChatFlag());
                    holder.llBackgroundAudio = (LinearLayout) convertView.findViewById(R.id.llBackgroundAudio);

                    holder.btnPlayAudio = (ImageButton) convertView.findViewById(R.id.btnPlayAudio);
                    holder.seek_bar = (SeekBar) convertView.findViewById(R.id.seek_bar);
                    break;
                case VIDEO_:
                    convertView = mInflater.inflate(R.layout.layout_video, null);
                    setAlignmentVIDEO(convertView,holder, chatMessage.getChatFlag());
                    holder.llBackgroundImage = (RelativeLayout) convertView.findViewById(R.id.llBackgroundImage);

                    setProgressbar(holder);
                    break;

            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        //Get File if SD card is present
        if (new CheckForSDCard().isSDCardPresent()) {

            apkStorage = new File(
                    Environment.getExternalStorageDirectory() + "/"
                            + Utils.downloadDirectory);
        } else {
            Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();
        }


        if(type == MESSAGE_){
            setMessage(convertView,holder,chatMessage,position);

        }else if(type == PICTURE_){
            setPicture(convertView,holder,chatMessage,position);

        }else if(type == VIDEO_){
            setVideo(convertView,holder,chatMessage,position);

        }else if(type == Audio_){
            setAudio(convertView,holder,chatMessage,position);
            holder.btnPlayAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    audiocallback.audioUrl(chatMessages.get(position).getChatAudio(),holder.btnPlayAudio,holder.seek_bar);
                }
            });


        }

        return convertView;
    }

    public void add(UserChatMessage message) {
        chatMessages.add(message);
    }

    public void add(List<UserChatMessage> messages) {
        chatMessages.addAll(messages);
    }

    private void setProgressbar(final ViewHolder holder){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        holder. donut_progress.setProgress(holder.donut_progress.getProgress() + 1);
                        if(holder.donut_progress.getProgress()==100){
                            holder.donut_progress.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }, 5000, 100);

    }


    private void setMessage(View view, ViewHolder holder, final UserChatMessage chatMessage, final int position){
        holder.txtInfo = (TextView) view.findViewById(R.id.txtInfo);
        holder.txtMessage = (TextView) view.findViewById(R.id.txtMessage);
        holder.txtInfo.setText(chatMessage.getDate());
        if(!chatMessage.getMessage().equals(""))
        {
            holder.txtMessage.setText(chatMessage.getMessage());
        }
    }

    private void setPicture(View view, ViewHolder holder, final UserChatMessage chatMessage, final int position){
        holder.txtInfo = (TextView) view.findViewById(R.id.txtInfo);
        holder.chat_imageView1 = (ImageView) view.findViewById(R.id.chat_imageView1);
        holder.txtInfo.setText(chatMessage.getDate());
        holder.donut_progress = (DonutProgress) view.findViewById(R.id.donut_progress);

        //Create New File if not present
        if (outputFile!=null&&!outputFile.exists()) {
            System.out.print("Ranjeetdoit"+"Yes>>>>>>>>>>");
            holder.donut_progress.setVisibility(View.VISIBLE);

        }else {
            if(holder.donut_progress!=null)
            holder.donut_progress.setVisibility(View.GONE);
            System.out.print("Ranjeetdoit"+"NO>>>>>>>>");

        }
        if(chatMessage.getImage()!=null&&!chatMessage.getImage().equals("")&&filename(chatMessage.getImage()).equalsIgnoreCase("png")||filename(chatMessage.getImage()).equalsIgnoreCase("jpg")){
            callback.getUrl(chatMessage.getImage());
            outputFile = new File(apkStorage, chatMessage.getImage().replace(Utils.mainUrl, ""));
            Picasso.with(context).load(chatMessage.getImage())
                    .error(R.color.greay)
                    .placeholder(R.color.greay)
                    .resize(400,400)
                    .centerCrop()
                    .into(holder.chat_imageView1);
        }

        holder.chat_imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imagecallback.zoom(chatMessage.getImage());
                try {
                    FileOpen.openFile(context, Uri.parse(chatMessage.getImage()));

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }
    private void setVideo(View view, ViewHolder holder, final UserChatMessage chatMessage, final int position){
        holder.txtInfo = (TextView) view.findViewById(R.id.txtInfo);
        holder.thumnail = (ImageView) view.findViewById(R.id.chat_imageView);
        holder.btnPlay = (ImageView) view.findViewById(R.id.btnPlay);
        holder.txtInfo.setText(chatMessage.getDate());
        holder.donut_progress = (DonutProgress) view.findViewById(R.id.donut_progress);

        //Create New File if not present
        if (outputFile!=null&&!outputFile.exists()) {
            System.out.print("Ranjeetdoit"+"Yes>>>>>>>>>>");
             holder.donut_progress.setVisibility(View.VISIBLE);

        }else {
            holder.donut_progress.setVisibility(View.GONE);
            System.out.print("Ranjeetdoit"+"NO>>>>>>>>");

        }
        if(!chatMessage.getChatTime().equals("")){
            callback.getUrl(chatMessage.getChatVideo());
            outputFile = new File(apkStorage, chatMessage.getChatVideo().replace(Utils.mainUrl, ""));
            Picasso.with(context).load(chatMessage.getChatTime())
                    .error(R.color.greay)
                    .placeholder(R.color.greay)
                    .resize(400,400)
                    .centerCrop()
                    .into(holder.thumnail);
        }

        holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent intent = new Intent(context, ActivityVideoView.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("videourl",chatMessage.getChatVideo());
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);*/
                try {
                    FileOpen.openFile(context, Uri.parse(chatMessage.getChatVideo()));

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }
    private void setAudio(View view, final ViewHolder holder, final UserChatMessage chatMessage, final int position){

        holder.txtInfo = (TextView) view.findViewById(R.id.txtInfo);
       /* holder.btnPlayAudio = (ImageButton) view.findViewById(R.id.btnPlayAudio);
        holder.seek_bar = (SeekBar) view.findViewById(R.id.seek_bar);*/
        holder.txtInfo.setText(chatMessage.getDate());
        //holder.seek_bar.setId(position);
       /*holder.btnPlayAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audiocallback.audioUrl(chatMessages.get(position).getChatAudio(),holder.btnPlayAudio,holder.seek_bar);
            }
        });
*/


    }

    private void setAlignmentMessage(View view,ViewHolder holder, String isMe) {

        holder.contentWithBG = (LinearLayout) view.findViewById(R.id.contentWithBackground);
        holder.content = (LinearLayout) view.findViewById(R.id.content);
        holder.txtMessage = (TextView) view.findViewById(R.id.txtMessage);
        holder.txtInfo = (TextView) view.findViewById(R.id.txtInfo);

        if (isMe.equals("0")) {
            holder.contentWithBG.setBackgroundResource(R.drawable.grey_bubble);
            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);

            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtInfo.setLayoutParams(layoutParams);



        } else {

            holder.contentWithBG.setBackgroundResource(R.drawable.msg_out);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);

            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtInfo.setLayoutParams(layoutParams);

        }
    }
    private void setAlignmentPICTURE(View view,ViewHolder holder, String isMe) {

        holder.llBackgroundImage1 = (RelativeLayout) view.findViewById(R.id.llBackgroundImage1);
        holder.content = (LinearLayout) view.findViewById(R.id.content);
        holder.chatImageView = (ImageView) view.findViewById(R.id.chat_imageView1);
        holder.txtInfo = (TextView) view.findViewById(R.id.txtInfo);

        if (isMe.equals("0")) {
            holder.llBackgroundImage1.setBackgroundResource(R.drawable.grey_bubble);
            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.llBackgroundImage1.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.llBackgroundImage1.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);

           /* layoutParams = (LinearLayout.LayoutParams) holder.chatImageView.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.chatImageView.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtInfo.setLayoutParams(layoutParams);*/



        } else {

            holder.llBackgroundImage1.setBackgroundResource(R.drawable.msg_out);
            holder.chatImageView.setBackgroundResource(R.drawable.msg_out);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.llBackgroundImage1.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.llBackgroundImage1.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);

        }
    }
    private void setAlignmentVIDEO(View view,ViewHolder holder, String isMe) {

        holder.content = (LinearLayout) view.findViewById(R.id.content);
        holder.thumnail = (ImageView) view.findViewById(R.id.chat_imageView);
        holder.txtInfo = (TextView) view.findViewById(R.id.txtInfo);

        if (isMe.equals("0")) {
            holder.llBackgroundImage.setBackgroundResource(R.drawable.grey_bubble);
            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.llBackgroundImage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.llBackgroundImage.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);


        } else {

            holder.llBackgroundImage.setBackgroundResource(R.drawable.msg_out);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.llBackgroundImage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.llBackgroundImage.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);
            holder.chat_imageView1.setBackgroundResource(R.drawable.msg_out);

        }
    }

    private void setAlignmentAUDIO(View view,ViewHolder holder, String isMe) {

        holder.content = (LinearLayout) view.findViewById(R.id.content);
        holder.txtInfo = (TextView) view.findViewById(R.id.txtInfo);

        if (isMe.equals("0")) {
            holder.llBackgroundAudio.setBackgroundResource(R.drawable.grey_bubble);
            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.llBackgroundAudio.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.llBackgroundAudio.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);


            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtInfo.setLayoutParams(layoutParams);



        } else {

            holder.llBackgroundAudio.setBackgroundResource(R.drawable.grey_bubble);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.llBackgroundAudio.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.llBackgroundAudio.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtInfo.setLayoutParams(layoutParams);



        }
    }
    public void remove(UserChatMessage object) {
        chatMessages.remove(object);
        notifyDataSetChanged();
    }

    public List<UserChatMessage> getWorldPopulation() {
        return chatMessages;
    }

    public void toggleSelection(int position) {
      selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        //notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);

    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
     static class ViewHolder {
        public TextView txtMessage;
        public TextView txtInfo;
        public ImageView chatImageView,btnPlay,thumnail;
        public ImageView chat_imageView1;
        public LinearLayout content;
        public LinearLayout contentWithBG;
        public LinearLayout llBackgroundAudio;
        public RelativeLayout llBackgroundImage;
        public RelativeLayout llBackgroundImage1;
        public DonutProgress donut_progress;
        public SeekBar seek_bar;
        public ImageButton btnPlayAudio;
    }

    public interface imageDownload{

        public void getUrl(String url);
    }

    public interface playAudio{

        public void audioUrl(String url, ImageButton button, SeekBar seekBar);
    }
    public interface imageZoome{

        public void zoom(String url);
    }

    public String filename(String file){
        int dotposition= file.lastIndexOf(".");
        String ext = file.substring(dotposition + 1, file.length());
        return ext;
    }

}

