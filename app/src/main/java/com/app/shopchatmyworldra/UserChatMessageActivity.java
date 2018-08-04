package com.app.shopchatmyworldra;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.app.shopchatmyworldra.Audio.AudioPlaybackActivity;
import com.app.shopchatmyworldra.ResponseParser.BlockParser;
import com.app.shopchatmyworldra.ResponseParser.ChatParser;
import com.app.shopchatmyworldra.adapter.MessageAdapter;
import com.app.shopchatmyworldra.com.skd.androidrecording.audio.AudioRecordingHandler;
import com.app.shopchatmyworldra.com.skd.androidrecording.audio.AudioRecordingThread;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.FilePath;
import com.app.shopchatmyworldra.constant.HttpClient;
import com.app.shopchatmyworldra.constant.MyApplication;
import com.app.shopchatmyworldra.constant.MyPreferences;


import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.StorageUtils;
import com.app.shopchatmyworldra.constant.WebServices;
import com.app.shopchatmyworldra.downloadFile.DownloadTask;
import com.app.shopchatmyworldra.pojo.ChatList;
import com.app.shopchatmyworldra.pojo.UserChatMessage;
import com.app.shopchatmyworldra.videochat.BaseActivity;
import com.app.shopchatmyworldra.videochat.CallScreenActivity;
import com.app.shopchatmyworldra.videochat.SinchService;
import com.app.shopchatmyworldra.videochat.VoiceCallActivity;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sinch.android.rtc.calling.Call;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;



/**
 * Created by legacy on 08-Sep-17.
 */

public class UserChatMessageActivity extends BaseActivity implements MessageAdapter.imageDownload,MessageAdapter.playAudio,View.OnClickListener, View.OnTouchListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener,ActionMode.Callback, RecyclerView.OnItemTouchListener{

    byte[] mData;
    byte[] inputData;
    byte[] Audiodata;
    private static final int SELECT_VIDEO = 3;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 1888;
    Bitmap mPreviewImage;
    private RecyclerView messagesContainer;
    private EditText chatText;
    TextView toolbartitle;
    private ImageView buttonSend;
    String userId2, username, recipientId;
    MessageAdapter adapter;
    String message = "";
    ImageView iv_camera;
    private ImageView ivcall, chat_imageView;
    private ImageView ivVideocall;
    private ImageView iv_con;
    private ImageView ivBackArrow;
    private ProgressBar progressBar;
    private Runnable runnable2;
    private Handler handler2 = new Handler();
    private boolean keepupdating = true;
    public Snackbar snackbar;
    private List<ChatList> chatHistory ;
    private int checkArraylistSize;
    private String onlineFlag = "";
    private String userImage = "";
    private  Toolbar toolbar;
    boolean hidden=true;
    LinearLayout mRevealView;
    ImageButton ib_gallery,ib_contacts,ib_location;
    ImageButton ib_video,ib_audio,ib_camera;
    StringBuilder stringBuilder =new StringBuilder();
    /*********************Audio*************************************/


    private ImageButton audioSendButton;
    private FrameLayout farmelayout;
    private LinearLayout chat_corrner;


    /*****************************Audiocode********************************/

    private AudioRecordingThread recordingThread;
    private boolean startRecording = true;
    private static String fileName = null;
   // private VisualizerView visualizerView;
    private MediaPlayer   mediaPlayer = null;
    private int mediaFileLengthInMilliseconds; // this value contains the song duration in milliseconds. Look at getDuration() method in MediaPlayer class

    private final Handler handler = new Handler();
    private TextView recordTimeText;
    private View recordPanel;
    private View slideText;
    private float startedDraggingX = -1;
    private float distCanMove = dp(80);
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private Timer timer;
    /************************************************************/
    GestureDetectorCompat gestureDetector;
    ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usermessger_activity);
        getRefrenceId();
       // Record to the external cache directory for visibility
        if (!StorageUtils.checkExternalStorageAvailable()) {
            CommanMethod.showInfoDialog(this, getString(R.string.noExtStorageAvailable));
            return;
        }
        fileName = StorageUtils.getFileName(true);


        MyApplication.Title = "";
        MainActivity.tvNotificationItem.setText("0");
        recipientId = getIntent().getStringExtra("Recipient");
        username = getIntent().getStringExtra("userName");
        userId2 = getIntent().getStringExtra("userId");
        userImage = getIntent().getStringExtra("userImage");
        toolbartitle.setText(username);

        mRevealView = (LinearLayout) findViewById(R.id.reveal_items);
        ib_audio=(ImageButton)findViewById(R.id.audio);
        ib_camera=(ImageButton)findViewById(R.id.camera);
        ib_contacts=(ImageButton)findViewById(R.id.contacts);
        ib_gallery=(ImageButton)findViewById(R.id.gallery);
        ib_location=(ImageButton)findViewById(R.id.location);
        ib_video=(ImageButton)findViewById(R.id.video);
        //visualizerView=(VisualizerView) findViewById(R.id.visualizerView);




        ib_audio.setOnClickListener(this);
        ib_camera.setOnClickListener(this);
        ib_contacts.setOnClickListener(this);
        ib_gallery.setOnClickListener(this);
        ib_location.setOnClickListener(this);
        ib_video.setOnClickListener(this);


        /*********************************Audio Record**********************************************************/
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);

        audioSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                record();

            }
        });

        /*******************************************************************************************************/
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        chatText.addTextChangedListener(watch);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommanMethod.isOnline(UserChatMessageActivity.this)) {
                    message = chatText.getEditableText().toString().trim();
                    if (mData != null) {
                        progressBar.setVisibility(View.VISIBLE);
                        new ServiceAsnc(mData).execute();
                    }  else if (inputData != null) {
                        progressBar.setVisibility(View.VISIBLE);
                        new ServiceAsnc(mData).execute();
                    } else if (!message.equals("")) {
                            progressBar.setVisibility(View.GONE);
                            chatText.setText("");
                            new ServiceAsnc(mData).execute();
                            hideKeyboard();
                        } else {
                            Toast.makeText(UserChatMessageActivity.this, "Please enter message.", Toast.LENGTH_SHORT).show();
                        }



                } else {
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(ContextCompat.getColor(UserChatMessageActivity.this, R.color.colorAccent));
                    snackbar.show();
                }

            }
        });

        ivcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = recipientId;
                if (userName.isEmpty()) {
                    Toast.makeText(UserChatMessageActivity.this, "Please enter a user to call", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    Call call = getSinchServiceInterface().callUser(userName);
                    String callId = call.getCallId();
                    Intent callScreen = new Intent(UserChatMessageActivity.this, VoiceCallActivity.class);
                    callScreen.putExtra(SinchService.CALL_ID, callId);
                    callScreen.putExtra("userImage",userImage);
                    startActivity(callScreen);
                }catch (Exception ex)
                {
                    CommanMethod.showAlert("Please logout then login",UserChatMessageActivity.this);
                }

            }
        });

        ivVideocall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = recipientId;
                if (userName.isEmpty()) {
                    Toast.makeText(UserChatMessageActivity.this, "Please enter a user to call", Toast.LENGTH_LONG).show();
                    return;
                }
                try{
                    Call call = getSinchServiceInterface().callUserVideo(userName);
                    String callId = call.getCallId();
                    Intent callScreen = new Intent(UserChatMessageActivity.this, CallScreenActivity.class);
                    callScreen.putExtra(SinchService.CALL_ID, callId);
                    startActivity(callScreen);
                }catch (Exception ex)
                {
                    CommanMethod.showAlert("Please logout then login",UserChatMessageActivity.this);
                }

            }
        });
        iv_camera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
            // selectImage();
                // finding X and Y co-ordinates
                int cx = (mRevealView.getLeft() + mRevealView.getRight());
                int cy = (mRevealView.getTop());

                // to find  radius when icon is tapped for showing layout
                int startradius=0;
                int endradius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());


                // performing circular reveal when icon will be tapped
                Animator animator = ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, startradius, endradius);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(400);

                //reverse animation
                // to find radius when icon is tapped again for hiding layout


                //  starting radius will be the radius or the extent to which circular reveal animation is to be shown
                int reverse_startradius = Math.max(mRevealView.getWidth(),mRevealView.getHeight());
                //endradius will be zero
                int reverse_endradius=0;


                // performing circular reveal for reverse animation
                Animator animate = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    animate = ViewAnimationUtils.createCircularReveal(mRevealView,cx,cy,reverse_startradius,reverse_endradius);
                }


                if(hidden) {

                    // to show the layout when icon is tapped
                    mRevealView.setVisibility(View.VISIBLE);
                    animator.start();
                    hidden = false;
                }
                else {

                    mRevealView.setVisibility(View.VISIBLE);

                    // to hide layout on animation end
                    animate.addListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mRevealView.setVisibility(View.INVISIBLE);
                            hidden = true;

                        }
                    });
                    animate.start();

                }

            }
        });

        runnable2 = new Runnable() {
            public void run() {
                validateGetMessage();
                if (keepupdating)
                    handler2.postDelayed(runnable2, 5000);
            }
        };



        if(userImage!=null&&!userImage.equals("")&&userImage.startsWith("graph"))
        {
                Picasso.with(UserChatMessageActivity.this)
                        .load("https://"+userImage)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .placeholder(R.color.grey)
                        .error(R.color.grey)
                        .into(iv_con);

        }else {

            if (userImage!=null&&!userImage.equals("")) {
                Picasso.with(UserChatMessageActivity.this)
                        .load(userImage)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .placeholder(R.color.grey)
                        .error(R.color.grey)
                        .into(iv_con);
            }
        }
        iv_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(UserChatMessageActivity.this);
                LayoutInflater factory = LayoutInflater.from(UserChatMessageActivity.this);
                View vi = factory.inflate(R.layout.zoom_image, null);
                alert.setView(vi);
                ImageView imageView = (ImageView) vi.findViewById(R.id.mImagId);

                if(userImage!=null&&!userImage.equals("")&&userImage.startsWith("graph"))
                {
                    Picasso.with(UserChatMessageActivity.this)
                            .load("https://"+userImage)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .placeholder(R.color.grey)
                            .error(R.color.grey)
                            .into(imageView);

                }else {

                    if (userImage!=null&&!userImage.equals("")) {
                        Picasso.with(UserChatMessageActivity.this)
                                .load(userImage)
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
        MyPreferences.getActiveInstance(UserChatMessageActivity.this).setbudge("");


       /* messagesContainer.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        messagesContainer.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode,int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = messagesContainer.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Selected");
                if(checkedCount==0)
                {
                    toolbar.setVisibility(View.VISIBLE);
                }else {
                    toolbar.setVisibility(View.GONE);
                }
                // Calls toggleSelection method from ListViewAdapter Class
                adapter.toggleSelection(position);
                System.out.println("checkId"+">>>>>>position"+position);


            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.delete:
                        toolbar.setVisibility(View.VISIBLE);
                        // Calls getSelectedIds method from ListViewAdapter Class
                        SparseBooleanArray selected = adapter.getSelectedIds();
                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                 int selecteditem = selected.keyAt(i);
                                // Remove selected items following the ids
                                System.out.println("checkId"+">>>>>>Ranjeet"+selecteditem);
                                //adapter.remove(selecteditem);
                                stringBuilder.append(chatHistory.get(selecteditem).getId());
                                stringBuilder.append(",");
                            }
                        }
                        deletChat(stringBuilder.toString());
                        // Close CAB
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.activity_main, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub
                //adapter.removeSelection();
                toolbar.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub

                return false;
            }
        });*/


        messagesContainer.addOnItemTouchListener(this);
        gestureDetector =
                new GestureDetectorCompat(this, new RecyclerViewDemoOnGestureListener());

    }


    @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    TextWatcher watch = new TextWatcher(){

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {
            // TODO Auto-generated method stub
            if(s.length()>0){
                farmelayout.setVisibility(View.VISIBLE);
            }else {
                farmelayout.setVisibility(View.GONE);

            }
        }};
    public void getRefrenceId()
    {
        toolbartitle = (TextView) findViewById(R.id.toolbar_title);
        buttonSend = (ImageView) findViewById(R.id.button_chat_send);
        iv_camera = (ImageView) findViewById(R.id.button_chat_attachment);
        messagesContainer = (RecyclerView) findViewById(R.id.messagesContainer);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(UserChatMessageActivity.this);
        layoutManager.setStackFromEnd(true);
        messagesContainer.setLayoutManager(layoutManager);
        messagesContainer.setItemAnimator(new DefaultItemAnimator());


        ivcall = (ImageView) findViewById(R.id.ivcall);
        ivVideocall = (ImageView) findViewById(R.id.ivVideocall);
        iv_con = (ImageView) findViewById(R.id.iv_con);
        ivBackArrow = (ImageView) findViewById(R.id.ivBackArrow);
        snackbar = Snackbar.make(findViewById(R.id.fab), "No Internet  Connection", Snackbar.LENGTH_LONG);
        chatText = (EditText) findViewById(R.id.edit_chat_message);
        chat_imageView = (ImageView) findViewById(R.id.chat_imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        farmelayout = (FrameLayout) findViewById(R.id.farmelayout);
        chat_corrner = (LinearLayout) findViewById(R.id.chatBox);
        recordTimeText = (TextView) findViewById(com.app.shopchatmyworldra.R.id.recording_time_text);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /****************************Audio Refrence*************************************/
        audioSendButton = (ImageButton) findViewById(R.id.chat_audio_send_button);

        /******************************************************************************/



    }


    /********************************Audio Record************************************************************/


    private void vibrate() {
        // TODO Auto-generated method stub
        try {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int dp(float value) {
        return (int) Math.ceil(1 * value);
    }

    private void record() {
        if (startRecording) {
            recordStart();
            audioSendButton.setImageResource(R.drawable.rec);
            chat_corrner.setVisibility(View.GONE);
            startTime = SystemClock.uptimeMillis();
            timer = new Timer();
            MyTimerTask myTimerTask = new MyTimerTask();
            timer.schedule(myTimerTask, 1000, 1000);

        }
        else {
            recordStop();

            if (timer != null) {
                timer.cancel();

            }
            chat_corrner.setVisibility(View.VISIBLE);
            messagesContainer.setVisibility(View.VISIBLE);

            if (recordTimeText.getText().toString().equals("00:00")) {
                return;
            }
            recordTimeText.setText("00:00");

            audioSendButton.setImageResource(R.drawable.mic_button_states);

        }
    }

    private void recordStart() {
        startRecording();
        vibrate();
        startRecording = false;
    }

    private void recordStop() {
        stopRecording();
        vibrate();

        try {
            InputStream iStream =   getContentResolver().openInputStream(Uri.fromFile(new File(fileName)));
            Audiodata = getBytes(iStream);
            new ServiceAsnc(mData).execute();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        startRecording = true;
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            final String hms = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(updatedTime)
                            - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                            .toHours(updatedTime)),
                    TimeUnit.MILLISECONDS.toSeconds(updatedTime)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                            .toMinutes(updatedTime)));
            long lastsec = TimeUnit.MILLISECONDS.toSeconds(updatedTime)
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                    .toMinutes(updatedTime));
            System.out.println(lastsec + " hms " + hms);
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        if (recordTimeText != null)
                            recordTimeText.setText(hms);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
            });
        }
    }



    private void startRecording() {
        recordingThread = new AudioRecordingThread(fileName, new AudioRecordingHandler() {
            @Override
            public void onFftDataCapture(final byte[] bytes) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        /*if (visualizerView != null) {
                            visualizerView.updateVisualizerFFT(bytes);
                        }*/
                    }
                });
            }

            @Override
            public void onRecordSuccess() {

            }

            @Override
            public void onRecordingError() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        recordStop();
                        CommanMethod.showInfoDialog(UserChatMessageActivity.this, getString(R.string.recordingError));
                    }
                });
            }

            @Override
            public void onRecordSaveError() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        recordStop();
                        CommanMethod.showInfoDialog(UserChatMessageActivity.this, getString(R.string.saveRecordError));
                    }
                });
            }
        });
        recordingThread.start();
    }

    private void stopRecording() {
        if (recordingThread != null) {
            recordingThread.stopRecording();
            recordingThread = null;
        }
    }


    /*******************************************************************************************************/


    private void validateGetMessage() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId1", MyPreferences.getActiveInstance(UserChatMessageActivity.this).getUserId());
        params.add("userId2", userId2);
        client.post(WebServices.showUserchat, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        Gson gson = new Gson();
                        ChatParser mChatParser = gson.fromJson(response.toString(), ChatParser.class);
                        parseResultgetMessage(mChatParser);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }

                });

    }


    private void parseResultgetMessage(ChatParser mChatParser) {

        chatHistory=mChatParser.getChatList();

        if (mChatParser.getChatList() != null && mChatParser.getChatList() .size()>checkArraylistSize||mChatParser.getChatList() .size()<checkArraylistSize) {
            checkArraylistSize = mChatParser.getChatList() .size();
            adapter = new MessageAdapter(mChatParser.getChatList() ,UserChatMessageActivity.this,UserChatMessageActivity.this,UserChatMessageActivity.this);
            messagesContainer.setAdapter(adapter);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        keepupdating = true;
        onlineFlag = "1";
        onlineFlag();
        handler2.postDelayed(runnable2, 5000);

        MyApplication.Title = "";
        MyPreferences.getActiveInstance(UserChatMessageActivity.this).setbudge("");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }


    public class ServiceAsnc extends AsyncTask<String, Void, String> {

        private String response;
        byte[] imageArray;
       // ProgressDialog mDialog;

        public ServiceAsnc(byte[] imageArray) {

            this.imageArray = imageArray;
        }

        @Override

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            response = callService();
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            // TODO Auto-generated method stub
            super.onPostExecute(response);
            if (response == null) {
                Toast.makeText(UserChatMessageActivity.this, "There is some problem with the server. Please try again after sometime.",
                        Toast.LENGTH_LONG).show();
            } else {
                progressBar.setVisibility(View.GONE);
                MyApplication.cropped = null;
                chat_imageView.setVisibility(View.GONE);
                farmelayout.setVisibility(View.GONE);
                setRecentchat();
            }

            Log.d("##########Response", "" + response);

            try {
                String success, msg, error_msg;
                if ((response != null) && !response.equals("")) {
                    Log.e("*******************", response);
                    JSONObject object = new JSONObject(response);
                    success = object.getString("responseCode");
                    inputData = null;
                    Audiodata=null;
                    mData = null;
                    message = "";
                    if (!success.equals("200")) {

                    }

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        private String callService() {
            // TODO Auto-generated method stub
            String url = WebServices.setUserchat;

            int time = (int) (System.currentTimeMillis());
            Timestamp tsTemp = new Timestamp(time);
            String ts =  tsTemp.toString();

            String Myworld = "Myworld" + ts;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            HttpClient client = new HttpClient(url);
            try {
                client.connectForMultipart();
                client.addFormPart("userId1", MyPreferences.getActiveInstance(UserChatMessageActivity.this).getUserId());
                client.addFormPart("userId2", userId2);
                Log.e("userId1",">>" +MyPreferences.getActiveInstance(UserChatMessageActivity.this).getUserId());
                Log.e("userId2",">>" +userId2);
                if (!message.equals("")) {
                    client.addFormPart("chatMsg", message);
                }
                if (mData != null) {
                    client.addFilePart("chatFile", Myworld +ts+ ".png", imageArray);
                }
                if (Audiodata != null) {
                    client.addFilePart("chatAudio", Myworld +".mp3", Audiodata);
                }
                if (inputData !=null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    mPreviewImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    client.addFilePart("thumbVideo", Myworld + ".png",byteArray);
                    client.addFilePart("chatVideo",  Myworld +".mp4", inputData);
                }
                client.finishMultipart();
                response = client.getResponse().toString();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return response;
        }

    }

    protected void setRecentchat() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.add("userId1", MyPreferences.getActiveInstance(UserChatMessageActivity.this).getUserId());
        params.add("userId2", userId2);
        client.post(WebServices.setRecentchat, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        Log.e("", "Hash -> " + responseCode.toString());
                        Log.e("", "On Success of NoTs");
                        Log.e("Recentchat", "" + responseCode.toString());

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();

                    }

                });

    }


    @Override
    public void onPause() {
        super.onPause();

        keepupdating = false;
        onlineFlag = "0";
        Log.e("onPause",">>>>>>>>>"+"onPause");
        onlineFlag();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(chatText.getWindowToken(), 0);
    }

    protected void onlineFlag() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.add("onlineFlag", onlineFlag);
        Log.e("onlineFlag",""+onlineFlag);
        params.add("userId", MyPreferences.getActiveInstance(UserChatMessageActivity.this).getUserId());
        client.post(WebServices.onlineFlag, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        Log.e("", "Hash -> " + responseCode.toString());
                        Log.e("", "On Success of NoTs");
                        Log.e("setOnlineFriend", "" + responseCode.toString());

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();

                    }

                });

    }

    @Override
    public void getUrl(String url) {
        if (isConnectingToInternet()) {
            if(MyApplication.isDownload == false){
                    new DownloadTask(UserChatMessageActivity.this, url);
            }
        }
    }


    private boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        private String TAG = "DownloadImage";
        private Bitmap downloadImageBitmap(String sUrl) {
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(sUrl).openStream();   // Download Image from URL
                bitmap = BitmapFactory.decodeStream(inputStream);       // Decode Bitmap
                inputStream.close();
            } catch (Exception e) {
                Log.d(TAG, "Exception 1, Something went wrong!");
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImageBitmap(params[0]);
        }

        protected void onPostExecute(Bitmap result) {
            saveImage(getApplicationContext(), result, "my_image.png");
        }
    }

    public void saveImage(Context context, Bitmap b, String imageName) {
        FileOutputStream foStream;
        try {
            foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 2, Something went wrong!");
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                System.out.println("SELECT_VIDEO");

                try {
                    farmelayout.setVisibility(View.VISIBLE);

                    Uri selectedImageUri = data.getData();
                    String pathresult = FilePath.getPath(this, selectedImageUri);
                    chat_imageView.setVisibility(View.VISIBLE);
                    mPreviewImage = ThumbnailUtils.createVideoThumbnail(pathresult, MediaStore.Images.Thumbnails.MINI_KIND);
                    chat_imageView.setImageBitmap(mPreviewImage);

                    InputStream iStream = getContentResolver().openInputStream(selectedImageUri);
                    inputData = getBytes(iStream);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }else if (requestCode==PICK_IMAGE_REQUEST && data!=null && data.getData()!=null) {

                Bitmap image = null;
                try {
                    farmelayout.setVisibility(View.VISIBLE);

                    image = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    chat_imageView.setVisibility(View.VISIBLE);
                    chat_imageView.setImageBitmap(image);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
                    mData = bytes.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                farmelayout.setVisibility(View.VISIBLE);

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                chat_imageView.setVisibility(View.VISIBLE);
                chat_imageView.setImageBitmap(photo);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
                mData = bytes.toByteArray();
            }
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.audio:
                Snackbar.make(v, "Audio Clicked", Snackbar.LENGTH_SHORT).show();
                mRevealView.setVisibility(View.INVISIBLE);
                hidden=true;
                break;
            case R.id.camera:
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                mRevealView.setVisibility(View.INVISIBLE);
                hidden=true;
                break;

            case R.id.location:
                Snackbar.make(v, "Location Clicked", Snackbar.LENGTH_SHORT).show();
                mRevealView.setVisibility(View.INVISIBLE);
                hidden=true;
                break;

            case R.id.contacts:
                Snackbar.make(v, "Contacts Clicked", Snackbar.LENGTH_SHORT).show();
                mRevealView.setVisibility(View.INVISIBLE);
                hidden=true;
                break;

            case R.id.video:
                Intent intent1 = new Intent();
                intent1.setType("video/*");
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent1, "Select a Video "), SELECT_VIDEO);
                //Snackbar.make(v, "Video Clicked", Snackbar.LENGTH_SHORT).show();
                mRevealView.setVisibility(View.INVISIBLE);
                hidden=true;
                break;

            case R.id.gallery:

                Intent intent2 = new Intent();
                intent2.setType("image/*");
                intent2.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent2, "Select Picture"), PICK_IMAGE_REQUEST);
                //Snackbar.make(v, "Gallery Clicked", Snackbar.LENGTH_SHORT).show();
                mRevealView.setVisibility(View.INVISIBLE);
                hidden=true;
                break;
            case R.id.messagesContainer: {
                int idx = messagesContainer.getChildPosition(v);
                if (actionMode != null) {
                    myToggleSelection(idx);
                }
                break;
            }

        }

    }

    @Override
    public void audioUrl(String url,ImageButton button) {

        Intent intent=new Intent(UserChatMessageActivity.this, AudioPlaybackActivity.class);
        intent.putExtra("arg_filename",url);
        startActivity(intent);

    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.seek_bar) {
            /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
            if (mediaPlayer.isPlaying()) {
                SeekBar sb = (SeekBar) v;
                int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                mediaPlayer.seekTo(playPositionInMillisecconds);
            }
        }
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mediaPlayer.release();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }


    protected void deletChat(String chatId) {
        System.out.println("checkId"+">>>>>>Call"+chatId.substring(0, chatId.length()-1));
        System.out.println("checkId"+">>>>>>CallUserId"+MyPreferences.getActiveInstance(UserChatMessageActivity.this).getUserId());

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams params = new RequestParams();
        params.add("chatId", chatId.substring(0, chatId.length()-1));
        params.add("userId", MyPreferences.getActiveInstance(UserChatMessageActivity.this).getUserId());
        client.post(WebServices.deletechats, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(UserChatMessageActivity.this,"");

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        Log.e("", "Hash -> " + responseCode.toString());
                        System.out.println("deleteMessage"+">>>>>>"+responseCode.toString());


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
    private void myToggleSelection(int idx) {
        adapter.toggleSelection(idx);
        String title = getString(R.string.selected_count, adapter.getSelectedItemCount());
        actionMode.setTitle(title);
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        toolbar.setVisibility(View.GONE);
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        this.actionMode = null;
        adapter.clearSelections();
        toolbar.setVisibility(View.VISIBLE);
    }
    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_delete:
                List<Integer> selectedItemPositions = adapter.getSelectedItems();
                int currPos;
                for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
                    currPos = selectedItemPositions.get(i);
                    stringBuilder.append(chatHistory.get(currPos).getChatId());
                    stringBuilder.append(",");
                }
                deletChat(stringBuilder.toString());
                toolbar.setVisibility(View.VISIBLE);
                actionMode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private class RecyclerViewDemoOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = messagesContainer.findChildViewUnder(e.getX(), e.getY());
            onClick(view);
            return super.onSingleTapConfirmed(e);
        }

        public void onLongPress(MotionEvent e) {
            View view = messagesContainer.findChildViewUnder(e.getX(), e.getY());
            if (actionMode != null) {
                return;
            }
            // Start the CAB using the ActionMode.Callback defined above
            actionMode = startActionMode(UserChatMessageActivity.this);
            int idx = messagesContainer.getChildPosition(view);
            myToggleSelection(idx);
            super.onLongPress(e);
        }
    }


}
