package com.app.shopchatmyworldra.videochat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.shopchatmyworldra.R;
import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallListener;
import com.sinch.android.rtc.calling.CallState;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by legacy on 23-Aug-17.
 */

public class VoiceCallActivity extends BaseActivity {

    static final String TAG = CallScreenActivity.class.getSimpleName();
    static final String CALL_START_TIME = "callStartTime";
    static final String ADDED_LISTENER = "addedListener";

    private AudioPlayer mAudioPlayer;
    private Timer mTimer;
    private VoiceCallActivity.UpdateCallDurationTask mDurationTask;
    private TextView tvName;
    private String mCallId;
    private long mCallStart = 0;
    private boolean mAddedListener = false;
    private ImageView iv_con;
    private ImageView iv_con1;
    private ImageView speakerButton;
    private ImageView micButton;
    private TextView mCallDuration;
    private TextView mCallState;
    private TextView mCallerName;
    private String userImage = "";
    private boolean isSpekar;
    private boolean isMic;
    private class UpdateCallDurationTask extends TimerTask {

        @Override
        public void run() {
            VoiceCallActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateCallDuration();
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong(CALL_START_TIME, mCallStart);
        savedInstanceState.putBoolean(ADDED_LISTENER, mAddedListener);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mCallStart = savedInstanceState.getLong(CALL_START_TIME);
        mAddedListener = savedInstanceState.getBoolean(ADDED_LISTENER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        mAudioPlayer = new AudioPlayer(this);
        mCallDuration = (TextView) findViewById(R.id.callDuration);
        mCallerName = (TextView) findViewById(R.id.tvName);
        mCallState = (TextView) findViewById(R.id.callState);
        tvName = (TextView) findViewById(R.id.tvName);
        iv_con = (ImageView) findViewById(R.id.iv_con);
        iv_con1 = (ImageView) findViewById(R.id.iv_con1);

         speakerButton =(ImageView) findViewById(R.id.speakerButton);
         micButton = (ImageView) findViewById(R.id.micButton);

        ImageView endCallButton = (ImageView) findViewById(R.id.hangupButton);
        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);
        userImage = getIntent().getStringExtra("userImage");

        if (savedInstanceState == null) {
            mCallStart = System.currentTimeMillis();
        }

        speakerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isSpekar)
                {
                    isSpekar =false;

                }else {
                    isSpekar =true;
                }
                if(isSpekar)
                {
                    setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
                    AudioController audioController = getSinchServiceInterface().getAudioController();
                    audioController.enableSpeaker();
                    speakerButton.setImageResource(R.drawable.spekar);

                }else {
                    setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
                    AudioController audioController = getSinchServiceInterface().getAudioController();
                    audioController.disableSpeaker();
                    speakerButton.setImageResource(R.drawable.spekar_off);
                }


            }
        });

        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isMic)
                {
                    isMic =false;

                }else {
                    isMic =true;
                }
                if(isMic)
                {
                    setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
                    AudioController audioController = getSinchServiceInterface().getAudioController();
                    audioController.mute();
                    micButton.setImageResource(R.drawable.mute_mic);

                }else {
                    setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
                    AudioController audioController = getSinchServiceInterface().getAudioController();
                    audioController.unmute();
                    micButton.setImageResource(R.drawable.mic);
                }


            }
        });




        endCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endCall();
            }
        });

        if(userImage!=null&&!userImage.equals("")&&userImage.startsWith("graph"))
        {
            try {
                Bitmap mBitmap = getFacebookProfilePicture("https://"+userImage);
                iv_con.setImageBitmap(mBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            if (userImage!=null&&!userImage.equals("")) {
                Picasso.with(VoiceCallActivity.this)
                        .load(userImage)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .placeholder(R.color.grey)
                        .error(R.color.grey)
                        .into(iv_con);
                iv_con1.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            if (!mAddedListener) {
                tvName.setText(call.toString());
                call.addCallListener(new SinchCallListener());
                mAddedListener = true;
            }
        } else {
            Log.e(TAG, "Started with invalid callId, aborting.");
            finish();
        }

        updateUI();
    }

    //method to update video feeds in the UI
    private void updateUI() {
        if (getSinchServiceInterface() == null) {
            return; // early
        }
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            mCallerName.setText(call.getRemoteUserId());
            mCallState.setText(call.getState().toString());
            if (call.getState() == CallState.ESTABLISHED) {
                //when the call is established, addVideoViews configures the video to  be shown
            }


        }
    }

    //stop the timer when call is ended
    @Override
    public void onStop() {
        super.onStop();
        mDurationTask.cancel();
        mTimer.cancel();
    }

    //start the timer for the call duration here
    @Override
    public void onStart() {
        super.onStart();
        mTimer = new Timer();
        mDurationTask = new VoiceCallActivity.UpdateCallDurationTask();
        mTimer.schedule(mDurationTask, 0, 500);
        updateUI();
    }

    @Override
    public void onBackPressed() {
        // User should exit activity by ending call, not by going back.
    }

    //method to end the call
    private void endCall() {
        mAudioPlayer.stopProgressTone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();

    }

    private String formatTimespan(long timespan) {
        long totalSeconds = timespan / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }

    //method to update live duration of the call
    private void updateCallDuration() {
        if (mCallStart > 0) {
            mCallDuration.setText(formatTimespan(System.currentTimeMillis() - mCallStart));
        }
    }

    private class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {

            CallEndCause cause = endedCall.getDetails().getEndCause();
            Log.d(TAG, "Call ended. Reason: " + cause.toString());
            mCallDuration.setVisibility(View.GONE);
            mAudioPlayer.stopProgressTone();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            if (cause.toString().equals("DENIED")) {
                mCallState.setText("Busy");
            } else if (cause.toString().equals("CANCELED")) {
                mCallState.setText("Busy");
            } else if (cause.toString().equals("OTHER_DEVICE_ANSWERED")) {
                mCallState.setText("On another call");
            } else if (cause.toString().equals("TIMEOUT")) {
                mCallState.setText("Out of range try again");
            } else if (cause.toString().equals("HUNG_UP")) {
                endCall();
            }

               /* for (CallEndCause callStatus : CallEndCause.values())
                {
                    System.out.println("voicecallCheck"+callStatus);

                }
*/


        }

        @Override
        public void onCallEstablished(Call establishedCall) {
            mCallState.setText("connected");

            mCallDuration.setVisibility(View.VISIBLE);
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            AudioController audioController = getSinchServiceInterface().getAudioController();
            audioController.disableSpeaker();
            mAudioPlayer.stopProgressTone();

        }

        @Override
        public void onCallProgressing(Call progressingCall) {
            mCallState.setText("ringing...");
            CallState cause = progressingCall.getState();
            Log.d(TAG, "Call progressingCall. Reason: " + progressingCall.toString());
            mAudioPlayer.playProgressTone();

        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
        }
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



















