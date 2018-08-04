package com.app.shopchatmyworldra.constant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by narendra on 6/20/2016.
 */
public class CommanMethod {


    /** Called for checking Internet connection */
    public static boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo;
        try {
            netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }

    public static void showAlert(String message, Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static void showAlertt(String message, Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }
    public static String Timeformate(String timestring)
    {
        String CurrentString = timestring;
        String[] separated = CurrentString.split("-");
        String[] splitdaytime = separated[2].split(" ");
        int selectedYear = Integer.parseInt(separated[0]);
        int selectedDay = Integer.parseInt(splitdaytime[0]);
        int selectedMonth = Integer.parseInt(separated[1]);
        splitdaytime[1] = splitdaytime[1].substring(0, splitdaytime[1].length() - 3);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, selectedYear);
        cal.set(Calendar.DAY_OF_MONTH, selectedDay);
        cal.set(Calendar.MONTH, selectedMonth-1);
        String format = new SimpleDateFormat("EEEE, MMM d, yyyy").format(cal.getTime());
        String finaldateformate=format+"  "+splitdaytime[1];
        System.out.println("formated day time == " + finaldateformate);
        return finaldateformate;
    }



    public static String [] value(String data)
    {
        String[] namesList=data.split("[,]+");

        return namesList;
    }

    public static String splitvalue(String data)
    {
        String value[];
        String returnvalue = null;
        value=data.split("[$]+");
        for(String name: value)
        {
            returnvalue=name;
        }

        return returnvalue;
    }


    public static String getAddress(Activity mactivity,double mlatitude, double mlongitude) {

        String city="";
        String state="";
        String zip="";
        String country="";
        String mUserLocation = "";
        Geocoder geocoder = new Geocoder(mactivity, Locale.getDefault());
        double latt = mlatitude;
        double longg = mlongitude;
        try {
            List<Address> addresses = geocoder.getFromLocation(latt, longg, 1);
            String mUserLocationNew = "";
            if (addresses.size() > 0) {
                mUserLocationNew = mUserLocationNew + addresses.get(0).getAddressLine(1).replace(",", "");
                for (int i = 0; i < 4; i++) { //Since it return only four value we declare this as static.
                    mUserLocation = mUserLocation + addresses.get(0).getAddressLine(i).replace(",", "") + ", ";
                }
            }
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            zip = addresses.get(0).getPostalCode();
            country = addresses.get(0).getCountryName();




        } catch (Exception ex) {

            ex.printStackTrace();

        }
        return  mUserLocation + "_" + city + "_" + state + "_" + country;
    }



    public static String getAddress1(Activity mactivity,double mlatitude, double mlongitude) {

        String city="";
        String state="";
        String zip="";
        String country="";
        String mUserLocation = "";
        Geocoder geocoder = new Geocoder(mactivity, Locale.getDefault());
        double latt = mlatitude;
        double longg = mlongitude;
        try {
            List<Address> addresses = geocoder.getFromLocation(latt, longg, 1);
            String mUserLocationNew = "";
            if (addresses.size() > 0) {
                mUserLocationNew = mUserLocationNew + addresses.get(0).getAddressLine(1).replace(",", "");
                for (int i = 0; i < 4; i++) { //Since it return only four value we declare this as static.
                    mUserLocation = mUserLocation + addresses.get(0).getAddressLine(i).replace(",", "") + ", ";
                }
            }
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            zip = addresses.get(0).getPostalCode();
            country = addresses.get(0).getCountryName();




        } catch (Exception ex) {

            ex.printStackTrace();

        }
        return  mUserLocation + "_" + city + "_" + state + "_" + country;
    }

    public  static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static String getDiference(long timeDifferenceMilliseconds) {

        long diffSeconds = timeDifferenceMilliseconds / 1000;
        long diffMinutes = timeDifferenceMilliseconds / (60 * 1000);
        long diffHours = timeDifferenceMilliseconds / (60*60*1000);
        long diffDays = timeDifferenceMilliseconds / (60*60*1000 * 24);
        long diffWeeks = timeDifferenceMilliseconds / (60*60*1000*24*7);
        long diffMonths = (long) (timeDifferenceMilliseconds / (60*60*1000*24*30.41666666));
        //long diffYears = (long) (timeDifferenceMilliseconds / (60  60  1000  24  365));

        if (diffSeconds < 1) {
            return "less than a second";
        } else if (diffMinutes < 1) {
            return diffSeconds + " seconds";
        } else if (diffHours < 1) {
            return diffMinutes + " min";
        } else if (diffDays < 1) {
            return diffHours + " hrs";
        } else if (diffWeeks < 1) {
            return diffDays + " days";
        } else if (diffMonths < 1) {
            return diffWeeks + " weeks";
        } else {
            return diffWeeks + " weeks";
        }/* else if (diffYears < 1) {
            return diffMonths + " months";
        } else {
            return diffYears + " years";
        }*/
        // }


    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void showSnack(View view, String message){
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static void showInfoDialog(Context ctx, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setMessage(msg);
        builder.setPositiveButton(ctx.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public  static URI ConvertToUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(),
                    url.getHost(), url.getPort(), url.getPath(),
                    url.getQuery(), url.getRef());
            //url = uri.toURL();
            return uri;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String date1(){
        Calendar calander;
        SimpleDateFormat simpledateformat;
        String Date;
        calander = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("MMMM dd, yyyy");
        Date = simpledateformat.format(calander.getTime());

        return Date;
    }


}
