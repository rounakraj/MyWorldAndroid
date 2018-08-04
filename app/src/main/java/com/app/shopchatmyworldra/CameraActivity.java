package com.app.shopchatmyworldra;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by legacy on 02-Nov-17.
 */

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;
    Camera mCamera;
    boolean mPreviewRunning;
    Button btncapture;
    String fullPath;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        btncapture=(Button) findViewById(R.id.btncapture);
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        btncapture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                mCamera.takePicture(null, null, mPictureCallback);

            }
        });
    }


    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] imageData, Camera c) {

            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData , 0, imageData .length);
            saveImageToExternalStorage(bitmap);
            Toast.makeText(getApplicationContext(),"Image stored succesfully at "+fullPath, Toast.LENGTH_LONG).show();
            mCamera.startPreview();
        }
    };
    public void saveImageToExternalStorage(Bitmap image) {
        //image=scaleCenterCrop(image,200,200);
        fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        try
        {
            File dir = new File(fullPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            OutputStream fOut = null;
            File file = new File(fullPath, "photo_marina200000.jpg");

            if(file.exists())
                file.delete();

            file.createNewFile();
            fOut = new FileOutputStream(file);
            // 100 means no compression, the lower you go, the stronger the compression
            image.compress(Bitmap.CompressFormat.JPEG, 50, fOut);

            fOut.flush();
            fOut.close();
        }
        catch (Exception e)
        {
            Log.e("saveToExternalStorage()", e.getMessage());
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera=Camera.open();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //mCamera.setDisplayOrientation(180);
        }  if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mCamera.setDisplayOrientation(90);
        }


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w,
                               int h) {
        if (mPreviewRunning) {
            mCamera.stopPreview();
        }

        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();

        //Camera.Parameters p = mCamera.getParameters();
     /*   p.setPreviewSize(w, h);
        mCamera.setParameters(p);*/

        //Camera.Size previewSize =
                parameters.setPreviewSize(w, h);
        mCamera.setParameters(parameters);
        mCamera.startPreview();
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();
        mPreviewRunning = true;

    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mPreviewRunning = false;
        mCamera.release();

    }
}
