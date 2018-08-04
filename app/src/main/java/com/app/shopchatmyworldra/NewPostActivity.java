package com.app.shopchatmyworldra;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.app.shopchatmyworldra.constant.CommanMethod;

import com.app.shopchatmyworldra.constant.CropImage;
import com.app.shopchatmyworldra.constant.HttpClient;
import com.app.shopchatmyworldra.constant.MyApplication;
import com.app.shopchatmyworldra.constant.MyPreferences;
import com.app.shopchatmyworldra.constant.ProgressBarDialog;
import com.app.shopchatmyworldra.constant.WebServices;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by MMAD on 09-09-2017.
 */

public class NewPostActivity extends AppCompatActivity {

    ImageView camera, video, setimg;
    private Button post;
    EditText etstatus;
    String status, from;

    byte[] mData =new byte[0];
    byte[] inputData = new byte[0];

    VideoView videoview;
    public Snackbar snackbar;

    private ShareDialog shareDialog;
    private int PICK_IMAGE_REQUEST=1;
    private int CAMERA_IMAGE_REQUEST=2;
    private int CROP_IMAGE_REQUEST=3;
    private static final int SELECT_VIDEO = 4;
    private Dialog dialog;
    private ImageView ivImageView;
    private Uri selectedImageUri;
    private Bitmap image;
    private TextView tvSave;
    private TextView tvCrop;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the SDK before executing any other operations,
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_statusupdate);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        shareDialog = new ShareDialog(this);  // intialize facebook shareDialog.
        videoview=(VideoView)findViewById(R.id.videoview);
        snackbar = Snackbar.make(findViewById(R.id.fab), "No Internet  Connection", Snackbar.LENGTH_LONG);
        from = getIntent().getStringExtra("statusmypage");
        camera = (ImageView) findViewById(R.id.camera);
        setimg = (ImageView) findViewById(R.id.setimg);
        video = (ImageView) findViewById(R.id.video);

        post = (Button) findViewById(R.id.post);

        etstatus = (EditText) findViewById(R.id.etstatus);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(NewPostActivity.this, CropImage.class);
                startActivity(intent);*/
                openImagePickerDialog();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommanMethod.isOnline(NewPostActivity.this)) {

                    status = etstatus.getText().toString().trim();
                    if (from.equals("1")) {
                        if(validationpost()){
                            StatusPosttsAsynTask statussupdate = new StatusPosttsAsynTask();
                            statussupdate.execute();
                        }

                    } else{
                        if(validationpost()){

                            StatusglobalPosttsAsynTask gstatussupdate = new StatusglobalPosttsAsynTask();
                            gstatussupdate.execute();
                        }


                    }
                } else {
                    View sbView =snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(ContextCompat.getColor(NewPostActivity.this,R.color.colorAccent));
                    snackbar.show();
                }





            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseVideo();
            }
        });

        ImageView ivBackArrow = (ImageView) findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        ImageView ivHome=(ImageView)findViewById(R.id.ivHome);
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NewPostActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    //status update
    public class StatusPosttsAsynTask extends AsyncTask<String, Void, String> {
        private String response;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            System.out.println("in asynctask");
            super.onPreExecute();
            ProgressBarDialog.showProgressBar(NewPostActivity.this,"");



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
            ProgressBarDialog.dismissProgressDialog();
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
                        ProgressBarDialog.dismissProgressDialog();
                        Toast.makeText(NewPostActivity.this, responseMessage,
                                Toast.LENGTH_LONG).show();
                        NewPostActivity.this.finish();

                    } else {
                        CommanMethod.showAlert(responseMessage, NewPostActivity.this);

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                CommanMethod.showAlert(getResources().getString(R.string.connection_error), NewPostActivity.this);

            }
        }

        private String callService() {
            String url = WebServices.userstatusupdate;
            HttpClient client = new HttpClient(url);
            Log.e("before connection", "" + url);
            try {

                client.connectForMultipart();
                client.addFormPart("userId", MyPreferences.getActiveInstance(NewPostActivity.this).getUserId());
                client.addFormPart("userStatus", status);
                if (mData.length > 0) {
                    client.addFilePart("userFile", ".jpeg", mData);

                }
                if(inputData.length>0)
                {
                    client.addFilePart("userFile", ".mp4", inputData);
                }


                Log.d("client", client.toString());
                client.finishMultipart();
                response = client.getResponse().toString();
                Log.d("responseVideoImage", response);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return response;
        }
    }


    //globalstatus update
    public class StatusglobalPosttsAsynTask extends AsyncTask<String, Void, String> {

        private String response;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            System.out.println("in asynctask");


            super.onPreExecute();
            ProgressBarDialog.showProgressBar(NewPostActivity.this,"");



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

            ProgressBarDialog.dismissProgressDialog();

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
                        mData=null;
                        inputData=null;

                        Toast.makeText(NewPostActivity.this, responseMessage, Toast.LENGTH_SHORT).show();
                        NewPostActivity.this.finish();
                    } else {
                        CommanMethod.showAlert(responseMessage, NewPostActivity.this);

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                CommanMethod.showAlert(getResources().getString(R.string.connection_error), NewPostActivity.this);

            }
        }

        private String callService() {
            String url = WebServices.globlstatusupdate;
            //String url = "http://13.126.105.58/Supu/site/webservices";
            HttpClient client = new HttpClient(url);
            Log.e("before connection", "" + url);
            try {

                client.connectForMultipart();
                client.addFormPart("userId", MyPreferences.getActiveInstance(NewPostActivity.this).getUserId());
                client.addFormPart("globalStatus", status);
                if (mData.length > 0) {
                    client.addFilePart("globalFile", ".jpeg", mData);

                }
                if(inputData.length>0)
                {
                    client.addFilePart("globalFile", ".mp4", inputData);
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


    private void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                System.out.println("SELECT_VIDEO");

                try {
                    Uri selectedImageUri = data.getData();

                    videoview.setVideoURI(selectedImageUri);
                    videoview.start();
                    setimg.setVisibility(View.GONE);
                    videoview.setVisibility(View.VISIBLE);

                    InputStream iStream = getContentResolver().openInputStream(selectedImageUri);
                    inputData = getBytes(iStream);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (requestCode==PICK_IMAGE_REQUEST && data!=null && data.getData()!=null) {

                    tvSave.setVisibility(View.VISIBLE);
                    tvCrop.setVisibility(View.VISIBLE);
                    videoview.setVisibility(View.GONE);
                    setimg.setVisibility(View.VISIBLE);
                    selectedImageUri = data.getData();
                    ivImageView.setImageURI(selectedImageUri);


            }else if (requestCode==CROP_IMAGE_REQUEST && data!=null && data.getData()!=null) {
                try {
                    videoview.setVisibility(View.GONE);
                    setimg.setVisibility(View.VISIBLE);
                    selectedImageUri = data.getData();
                    setimg.setImageURI(selectedImageUri);

                    InputStream iStream =   getContentResolver().openInputStream(selectedImageUri);
                    mData = getBytes(iStream);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else if (requestCode==CAMERA_IMAGE_REQUEST) {


                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
                    try {
                        tvSave.setVisibility(View.VISIBLE);
                        tvCrop.setVisibility(View.VISIBLE);
                        selectedImageUri = Uri.fromFile(file);
                        videoview.setVisibility(View.GONE);
                        setimg.setVisibility(View.VISIBLE);
                        ivImageView.setImageURI(selectedImageUri);

                    } catch (ActivityNotFoundException aNFE) {
                        String errorMessage = "Sorry - your device doesn't support the crop action!";
                    }

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



    private boolean validationpost() {
        if (etstatus.getText().toString().trim().length()==0&&mData.length==0&&inputData.length==0) {
            CommanMethod.showAlert("Please add  new post.", NewPostActivity.this);
            return false;
        }else if(inputData.length>5242880)
        {
            CommanMethod.showAlert("Please upload video less than 5mb.", NewPostActivity.this);
            return false;
        }
        return true;

    }


    public void openImagePickerDialog() {
        dialog = new Dialog(NewPostActivity.this, android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image_picker);
        LinearLayout container = (LinearLayout) dialog.findViewById(R.id.container);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView ivCamera = (ImageView) dialog.findViewById(R.id.ivCamera);
        ivImageView= (ImageView) dialog.findViewById(R.id.ivImageView);
        final ImageView ivGallery = (ImageView) dialog.findViewById(R.id.ivGallery);

        TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
        tvSave = (TextView) dialog.findViewById(R.id.tvSave);
        tvCrop = (TextView) dialog.findViewById(R.id.tvCrop);

        //MINIMIZE THE DIALOG
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setimg.setImageURI(selectedImageUri);
                    dialog.dismiss();

                    InputStream iStream = getContentResolver().openInputStream(selectedImageUri);
                    mData = getBytes(iStream);
                }catch (Exception ex){
                    ex.printStackTrace();
                }


            }
        });  //MINIMIZE THE DIALOG
        tvCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropCapturedImage(selectedImageUri);
                dialog.dismiss();
            }
        }); tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //TAKE THE IMAGE FROM CAMERA
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
                //dialog.dismiss();
            }
        });


        //GET THE IMAGE FROM GALLERY
        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST);
                    //Crop.pickImage(RequestProductActivity.this, Image1RequestCode);


                //dialog.dismiss();
            }
        });

        dialog.show();
    }

    //create helping method cropCapturedImage(Uri picUri)
    public void cropCapturedImage(Uri picUri) {
        //call the standard crop action intent
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        //indicate image type and Uri of image
        cropIntent.setDataAndType(picUri, "image/*");
        //set crop properties
        cropIntent.putExtra("crop", "true");
        //indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        //indicate output X and Y
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        //retrieve data on return
        cropIntent.putExtra("return-data", true);
        //start the activity - we handle returning in onActivityResult
        startActivityForResult(cropIntent, CROP_IMAGE_REQUEST);
    }
}
