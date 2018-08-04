package com.app.shopchatmyworldra.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.TermsAndConditionActivity;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.WebServices;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

/**
 * Created by MMAD on 16-08-2017.
 */

public class InviteFriendFragment extends Fragment {
    private View view;
    private LinearLayout llMyRewards;
    private TextView tvTemsAndCondistion;
    private TextView tvQrcode;
    private TextView tvEarn;
    private Context mcontext;
    public final static int QRcodeWidth = 500 ;
    private static final String IMAGE_DIRECTORY = "/QRcodeDemonuts";
    Bitmap bitmap ;
    private StringBuilder stringBuilder;
    private ProgressBar progressBar;

    private ImageView imageView;
    private ImageView ivEmail;
    private ImageView ivFacebook;
    String path;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mcontext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.invitefriends, null);



        imageView=(ImageView)view.findViewById(R.id.imageView);
        llMyRewards = (LinearLayout) view.findViewById(R.id.llMyRewards);
        tvTemsAndCondistion = (TextView) view.findViewById(R.id.tvTemsAndCondistion);
        tvQrcode = (TextView) view.findViewById(R.id.tvQrcode);
        tvEarn = (TextView) view.findViewById(R.id.tvEarn);
        ivEmail = (ImageView) view.findViewById(R.id.ivEmail);
        ivFacebook = (ImageView) view.findViewById(R.id.ivFacebook);
        progressBar= (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        tvQrcode.setText("Qr code "+MyPreferences.getActiveInstance(mcontext).getEmailId());
        getMyrefer();
        tvTemsAndCondistion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext, TermsAndConditionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        llMyRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentMyRewards fragmentMyRewards = new FragmentMyRewards();
                start_fragment(fragmentMyRewards);
            }
        });


        ivEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailFile(mcontext);
            }
        });
        ivFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailFile(mcontext);
            }
        });
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

    }



    private void start_fragment(Fragment frag) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, frag);
        fragmentTransaction.commit();
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.

        if (!wallpaperDirectory.exists()) {
            Log.d("dirrrrrr", "" + wallpaperDirectory.mkdirs());
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();   //give read write permission
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(mcontext,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }
    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }




    private void validaterefer() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId",MyPreferences.getActiveInstance(getActivity()).getUserId());
        params.add("referCode",MyPreferences.getActiveInstance(mcontext).getEmailId());
        client.post(WebServices.updaterefercode, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                       //prgDialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        try {
                            JSONObject object = new JSONObject(responseCode.toString());
                            Log.e("Response", "--->>" + object.toString(2));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        parseResult(responseCode.toString());

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //prgDialog.hide();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), mcontext);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        //prgDialog.hide();
                    }

                });

    }


    private void parseResult(String result) {

        try {
            JSONObject response = new JSONObject(result);
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {

               // Toast.makeText(mcontext, message, Toast.LENGTH_SHORT).show();



            }else {

                CommanMethod.showAlert(message,getActivity());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void getMyrefer() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId", MyPreferences.getActiveInstance(mcontext).getUserId());
        client.post(WebServices.getMyrefer, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        //prgDialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject responseCode) {
                        super.onSuccess(statusCode, headers, responseCode);
                        parseResultRefer(responseCode.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //prgDialog.hide();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), mcontext);
                    }

                    @Override
                    public void onFinish() {
                        //prgDialog.hide();
                        super.onFinish();
                    }

                });

    }


    private void parseResultRefer(String result) {

        try {
            JSONObject response = new JSONObject(result);
            Log.e("inviteFriendFragment","resoponse"+response.toString(2));
            String message = response.getString("responseMessage");
            if (response.getString("responseCode").equals("200")) {

                tvEarn.setText("Get "+response.getString("earn"));

                try {
                    bitmap = TextToImageEncode(MyPreferences.getActiveInstance(mcontext).getEmailId());
                    imageView.setImageBitmap(bitmap);
                    path = saveImage(bitmap);  //give read write permission
                    //Toast.makeText(mcontext, "QRCode saved to -> "+path, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

                validaterefer();


            } else {

                CommanMethod.showAlert(message, mcontext);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void emailFile(Context activity) {

        stringBuilder = new StringBuilder();
        stringBuilder.append("Hey, \n"+"Check out My Worls application. \n https://playgeen.google.com/store/apps/details?id=com.app.shopchatmyworldra "+MyPreferences.getActiveInstance(mcontext).getEmailId()+MyPreferences.getActiveInstance(mcontext).getUserId() );

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT,"Myworld Qrcode");
        sendIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
        sendIntent.setType("text/plain");
        activity.startActivity(sendIntent);

    }

}
