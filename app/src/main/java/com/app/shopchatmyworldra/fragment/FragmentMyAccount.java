package com.app.shopchatmyworldra.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.shopchatmyworldra.MainActivity;
import com.app.shopchatmyworldra.R;
import com.app.shopchatmyworldra.constant.CommanMethod;
import com.app.shopchatmyworldra.constant.CropImage;
import com.app.shopchatmyworldra.constant.HttpClient;
import com.app.shopchatmyworldra.constant.MyApplication;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.Header;

/**
 * Created by MMAD on 18-08-2017.
 */

public class FragmentMyAccount  extends Fragment {
    private View view;
    EditText etfirstname,etlastname,etusername,etemail,etmobile,etstatus;
    private Context mcontext;
    private TextView profileedit;
    private LinearLayout ll_update;
    ImageView procamera,covercamera,proimg,coverimage;
    byte[] mData = new byte[0];
    byte[] mData2 = new byte[0];
    String img=" ";
    private ProgressBar progressBar1;
    private ProgressBar progressBar;
    @Override
    public void onAttach(Context context) {
        mcontext=context;
        super.onAttach(context);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.myaccount_fragment, null);



        MainActivity.linemain.setVisibility(View.VISIBLE);
        MainActivity.llTopLayout.setVisibility(View.GONE);

        etfirstname=(EditText)view.findViewById(R.id.etfirstname);
        etlastname=(EditText)view.findViewById(R.id.etlastname);
        etusername=(EditText)view.findViewById(R.id.etusername);
        etemail=(EditText)view.findViewById(R.id.etemail);
        etmobile=(EditText)view.findViewById(R.id.etmobile);
        etstatus=(EditText)view.findViewById(R.id.etstatus);
        procamera=(ImageView)view.findViewById(R.id.procamera);
        covercamera=(ImageView)view.findViewById(R.id.covercamera);
        proimg=(ImageView)view.findViewById(R.id.proimg);
        coverimage=(ImageView)view.findViewById(R.id.coverimage);
        progressBar1=(ProgressBar) view.findViewById(R.id.progressBar1);
        progressBar=(ProgressBar) view.findViewById(R.id.progressBar);

        etfirstname.setEnabled(false);
        etlastname.setEnabled(false);
        etusername.setEnabled(false);
        etemail.setEnabled(false);
        etmobile.setEnabled(false);
        etstatus.setEnabled(false);
        if (CommanMethod.isOnline(mcontext)) {
            validateGetprofile();
        } else {
            View sbView = MainActivity.snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(mcontext, R.color.colorPrimary));
            MainActivity.snackbar.show();
        }


        ll_update = (LinearLayout) view.findViewById(R.id.ll_update);
        ll_update.setClickable(false);


        profileedit = (TextView) view.findViewById(R.id.profileedit);
        profileedit.setTextColor(getResources().getColor(R.color.red));
        profileedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileedit.setTextColor(getResources().getColor(R.color.green));
                etfirstname.setEnabled(true);
                etlastname.setEnabled(true);
                etusername.setEnabled(false);
                etemail.setEnabled(false);
                etmobile.setEnabled(true);
                etstatus.setEnabled(true);
                ll_update.setClickable(true);


            }
        });

        ll_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 updateAsynTask update =new updateAsynTask();
                 update.execute();
            }
        });

        procamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), CropImage.class);
                startActivity(intent);

               img="0";


            }
        });

        covercamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), CropImage.class);
                startActivity(intent);
                img="1";


            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MyApplication.cropped!=null){
                if(img.equals("0")){
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    MyApplication.cropped.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    mData = bytes.toByteArray();
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(mData, 0, mData.length);

                    proimg.setImageBitmap(decodedByte);

                    MainActivity.imageViewProfile.setImageBitmap(decodedByte);

                }else if(img.equals("1")){
                    ByteArrayOutputStream bytes1 = new ByteArrayOutputStream();
                    MyApplication.cropped.compress(Bitmap.CompressFormat.JPEG, 100, bytes1);
                    mData2 = bytes1.toByteArray();
                    Bitmap decodedByte1 = BitmapFactory.decodeByteArray(mData2, 0, mData2.length);
                    coverimage.setImageBitmap(decodedByte1);

                }else {

                }

            }


    }

    private void validateGetprofile() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("userId",MyPreferences.getActiveInstance(mcontext).getUserId());
        client.post(WebServices.GETUSERPROFILE, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressBarDialog.showProgressBar(getActivity(),"");
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
                        ProgressBarDialog.dismissProgressDialog();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressBarDialog.dismissProgressDialog();
                        CommanMethod.showAlert(getResources().getString(R.string.connection_error), mcontext);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressBarDialog.dismissProgressDialog();
                    }

                });

    }


    private void parseResult(String result) {

        try {
            JSONObject response = new JSONObject(result);
            if (response.getString("firstName") != null && !response.getString("firstName").equals("")) {
                etfirstname.setText(response.getString("firstName"));
            }
            if (response.getString("lastName") != null && !response.getString("lastName").equals("")) {
                etlastname.setText(response.getString("lastName"));
            }
            if (response.getString("userName") != null && !response.getString("userName").equals("")) {
                etusername.setText(response.getString("userName"));
            }
            if (response.getString("emailId") != null && !response.getString("emailId").equals("")) {
                etemail.setText(response.getString("emailId"));
            }
            if(!response.getString("profileImage").equals("")&&response.getString("profileImage").startsWith("graph"))
            {
                try {
                    Bitmap mBitmap = getFacebookProfilePicture("https://"+response.getString("profileImage"));
                    proimg.setImageBitmap(mBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {

                if(response.getString("profileImage")!=null&&!response.getString("profileImage").equals(""))
                {
                    progressBar1.setVisibility(View.VISIBLE);
                    Picasso.with(mcontext)
                            .load(response.getString("profileImage"))
                            .error(R.color.gray)
                            .placeholder(R.color.gray)
                            .resize(320, 230)
                            .into(proimg, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {

                                    if (progressBar1 != null) {
                                        progressBar1.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onError() {
                                    if (progressBar1 != null) {
                                        progressBar1.setVisibility(View.GONE);
                                    }
                                }
                            });

                    Picasso.with(getActivity())
                            .load(response.getString("profileImage"))
                            .error(R.color.gray)
                            .placeholder(R.color.gray)
                        /*.resize(b, b)
                        .centerInside()*/
                            .into(MainActivity.imageViewProfile);
                    MyPreferences.getActiveInstance(getActivity()).setprofileimg(response.getString("profileImage"));

                }

            }
            if (response.getString("coverImage") != null && !response.getString("coverImage").equals("")) {

                progressBar.setVisibility(View.VISIBLE);
                Picasso.with(mcontext)
                        .load(response.getString("coverImage"))
                        .error(R.color.gray)
                        .placeholder(R.color.gray)
                        .resize(320, 230)
                        .into(coverimage, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {

                                if (progressBar != null) {
                                    progressBar.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onError() {
                                if (progressBar != null) {
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });


                MyPreferences.getActiveInstance(getActivity()).setcoverimg(response.getString("coverImage"));
            }

            if (response.getString("mobileNo") != null && !response.getString("mobileNo").equals("")) {
                etmobile.setText(response.getString("mobileNo"));
            }

           /* String message = response.getString("responseMessage");
           if (response.getString("responseCode").equals("200")) {
                // CommanMethod.showAlert(message,mactivity);
                if(response.getString("firstName")!=null&&response.getString("firstName").equals(""))
                {
                    etfirstname.setText(response.getString("firstName"));
                } if(response.getString("lastName")!=null&&response.getString("lastName").equals(""))
                {
                    etlastname.setText(response.getString("lastName"));
                }
                if(response.getString("userName")!=null&&response.getString("userName").equals(""))
                {
                    etusername.setText(response.getString("userName"));
                }
                if(response.getString("emailId")!=null&&response.getString("emailId").equals(""))
                {
                    etemail.setText(response.getString("emailId"));
                }
               *//* if(response.getString("mobileNo")!=null&&response.getString("mobileNo").equals(""))
                {
                    etmobile.setText(response.getString("mobileNo"));
                }
                if(response.getString("userStatus")!=null&&response.getString("userStatus").equals(""))
                {
                    etstatus.setText(response.getString("userStatus"));
                }*//*



            }else {

                CommanMethod.showAlert(message,getActivity());
            }*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public class updateAsynTask extends AsyncTask<String, Void, String> {
        private Dialog mDialog;
        private String response;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            System.out.println("in asynctask");


            super.onPreExecute();
            mDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
            mDialog.setCancelable(false);


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

            if (mDialog != null) {
                mDialog.dismiss();
            }
            Log.d("Response", "" + response);

            //responseCode
            JSONObject object;

            if (response != null) {
                try {
                    object = new JSONObject(response);
                    String success = object.getString("responseCode");
                    String responseMessage = object.getString("responseMessage");
                    Log.d("object", "" + object);
                    if (success.equals("200")) {
                        MyApplication.cropped=null;
                        mData =null;
                        mData2 =null;
                        CommanMethod.showAlert(responseMessage,getActivity());
                        validateGetprofile();
                        etfirstname.setEnabled(false);
                        etlastname.setEnabled(false);
                        etusername.setEnabled(false);
                        etemail.setEnabled(false);
                        etmobile.setEnabled(false);
                        etstatus.setEnabled(false);
                        profileedit.setTextColor(getResources().getColor(R.color.red));
                    } else {
                        CommanMethod.showAlert(responseMessage, getActivity());

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                CommanMethod.showAlert(getResources().getString(R.string.connection_error), getActivity());

            }
        }

        private String callService() {
            String url = WebServices.UpdateUserProfile;
            HttpClient client = new HttpClient(url);
            Log.e("before connection", "" + url);
            try {

                client.connectForMultipart();
                client.addFormPart("userId",MyPreferences.getActiveInstance(mcontext).getUserId());
                client.addFormPart("firstName",etfirstname.getText().toString().trim());
                client.addFormPart("lastName",etlastname.getText().toString().trim());
                client.addFormPart("mobileNo", etmobile.getText().toString().trim());
                client.addFormPart("userStatus",etstatus.getText().toString().trim());
                if(mData!=null&&mData.length!=0){
                    client.addFilePart("image1", ".jpg", mData);
                }
                if(mData2!=null&&mData2.length!=0){
                    client.addFilePart("image2", ".jpg", mData2);
                }



                Log.d("client", client.toString());
                client.finishMultipart();
                response = client.getResponse().toString();
                Log.d("response", response);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return response;
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




