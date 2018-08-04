package com.app.shopchatmyworldra.downloadFile;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.app.shopchatmyworldra.constant.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SONU on 29/10/15.
 */
public class DownloadTask {

    private static final String TAG = "Download Task";
    private Context context;
    private Button buttonText;
    private String downloadUrl = "", downloadFileName = "";
    public DownloadTask(Context context, String downloadUrl) {
        try{
            this.context = context;
            this.downloadUrl = downloadUrl;
            MyApplication.isDownload = true;
            downloadFileName = downloadUrl.replace(Utils.mainUrl, "");//Create file name by picking download file name from URL

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        //Start Downloading Task
        new DownloadingTask().execute();
    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //buttonText.setEnabled(false);
            //buttonText.setText(R.string.downloadStarted);//Set Button Text when download started
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    //buttonText.setEnabled(true);
                    //buttonText.setText(R.string.downloadCompleted);//If Download completed then change button text
                    MyApplication.isDownload = false;
                } else {
                   // buttonText.setText(R.string.downloadFailed);//If download failed change button text
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //buttonText.setEnabled(true);
                            //buttonText.setText(R.string.downloadAgain);//Change button text again after 3sec
                        }
                    }, 3000);

                    Log.e(TAG, "Download Failed");
                    MyApplication.isDownload = false;

                }
            } catch (Exception e) {
                e.printStackTrace();

                //Change button text if exception occurs
                //buttonText.setText(R.string.downloadFailed);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //buttonText.setEnabled(true);
                        //buttonText.setText(R.string.downloadAgain);
                    }
                }, 3000);
                Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

            }


            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(downloadUrl);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.connect();
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());

                }
                if (new CheckForSDCard().isSDCardPresent()) {

                    apkStorage = new File(
                            Environment.getExternalStorageDirectory() + "/"
                                    + Utils.downloadDirectory);
                } else
                    Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    Log.e(TAG, "Directory Created.");
                }

                outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                    Log.e("RanjeetCheck", "File Created");

               }
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();

                byte[] buffer = new byte[1024];
                int len1 = 0;//init length
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();

            } catch (Exception e) {
                e.printStackTrace();
                outputFile = null;
                MyApplication.isDownload = false;
                Log.e(TAG, "Download Error Exception " + e.getMessage());
            }

            return null;
        }
    }

}
