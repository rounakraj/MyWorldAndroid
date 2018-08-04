package com.app.shopchatmyworldra.constant;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class MyPreferences {
    private static MyPreferences preferences = null;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;
    private String flag = "flag";
    private String isLogedIn = "isLogedIn";
    private String userId = "userId";
    private String otp = "otp";
    private String emailId = "emailId";
    private String firstName = "firstName";
    private String lastName = "lastName";
    private String mobileNo = "mobileNo";
    private String bookingId = "bookingId";
    private String qrcode="qrcode";
    private String profileimg="profileimg";
    private String coverimg="coverimg";
    private String logintime="logintime";

    private String budge="budge";
    private String facebookimage = "facebookimage";
    public MyPreferences(Context context) {
        setmPreferences(PreferenceManager.getDefaultSharedPreferences(context));
    }


    public SharedPreferences getmPreferences() {
        return mPreferences;
    }

    public void setmPreferences(SharedPreferences mPreferences) {
        this.mPreferences = mPreferences;
    }


    public static MyPreferences getActiveInstance(Context context) {
        if (preferences == null) {
            preferences = new MyPreferences(context);
        }
        return preferences;
    }

    public boolean getIsLoggedIn() {
        return mPreferences.getBoolean(this.isLogedIn, false);
    }

    public void setIsLoggedIn(boolean isLoggedin) {
        editor = mPreferences.edit();
        editor.putBoolean(this.isLogedIn, isLoggedin);
        editor.commit();
    }

    public boolean getflag() {
        return mPreferences.getBoolean(this.flag, false);
    }

    public void setflag(boolean flag) {
        editor = mPreferences.edit();
        editor.putBoolean(this.flag, flag);
        editor.commit();
    }

    public void setlogintime(String logintime) {
        editor = mPreferences.edit();
        editor.putString(this.logintime, logintime);
        //editor.putString(this.ASMID, ASMID);
        editor.commit();
    }

    public String getlogintime() {
        return mPreferences.getString(this.logintime, "");
        //return mPreferences.getString(this.ASMID,"");
    }

    public String getUserId() {
        return mPreferences.getString(this.userId, "");

    }


    public void setUserId(String userId) {
        editor = mPreferences.edit();
        editor.putString(this.userId, userId);
        editor.commit();
    }
    public String getOtp() {
        return mPreferences.getString(this.otp, "");

    }


    public void setOtp(String otp) {
        editor = mPreferences.edit();
        editor.putString(this.otp, otp);
        editor.commit();
    }
    public String getEmailId() {
        return mPreferences.getString(this.emailId, "");

    }


    public void setEmailId(String emailId) {
        editor = mPreferences.edit();
        editor.putString(this.emailId, emailId);
        editor.commit();
    }


    public String getfacebookimage() {
        return mPreferences.getString(this.facebookimage, "");
    }

    public void setfacebookimage(String facebookimage) {
        editor = mPreferences.edit();
        editor.putString(this.facebookimage, facebookimage);
        editor.commit();
    }

    public void setFirstName(String firstName) {
        editor = mPreferences.edit();
        editor.putString(this.firstName, firstName);
        //editor.putString(this.ASMID, ASMID);
        editor.commit();
    }

    public String getFirstName() {
        return mPreferences.getString(this.firstName, "");
        //return mPreferences.getString(this.ASMID,"");
    }
    public String getLastName() {
        return mPreferences.getString(this.lastName, "");
        //return mPreferences.getString(this.ASMID,"");
    }

    public void setLastName(String lastName) {
        editor = mPreferences.edit();
        editor.putString(this.lastName, lastName);
        //editor.putString(this.ASMID, ASMID);
        editor.commit();
    }
    public String getbookingId() {
        return mPreferences.getString(this.bookingId, "");
        //return mPreferences.getString(this.ASMID,"");
    }

    public void setbookingId(String bookingId) {
        editor = mPreferences.edit();
        editor.putString(this.bookingId, bookingId);
        //editor.putString(this.ASMID, ASMID);
        editor.commit();
    }

    public String getMobileNo() {
        return mPreferences.getString(this.mobileNo, "");
        //return mPreferences.getString(this.ASMID,"");
    }

    public void setMobileNo(String mobileNo) {
        editor = mPreferences.edit();
        editor.putString(this.mobileNo, mobileNo);
        //editor.putString(this.ASMID, ASMID);
        editor.commit();
    }
    public String getqrcode() {
        return mPreferences.getString(this.qrcode, "");
        //return mPreferences.getString(this.ASMID,"");
    }

    public void setqrcode(String qrcode) {
        editor = mPreferences.edit();
        editor.putString(this.qrcode, qrcode);
        //editor.putString(this.ASMID, ASMID);
        editor.commit();
    }


    public String getprofileimg() {
        return mPreferences.getString(this.profileimg, "");
        //return mPreferences.getString(this.ASMID,"");
    }

    public void setprofileimg(String profileimg) {
        editor = mPreferences.edit();
        editor.putString(this.profileimg, profileimg);
        //editor.putString(this.ASMID, ASMID);
        editor.commit();
    }



    public String getcoverimg() {
        return mPreferences.getString(this.coverimg, "");
        //return mPreferences.getString(this.ASMID,"");
    }

    public void setcoverimg(String coverimg) {
        editor = mPreferences.edit();
        editor.putString(this.coverimg, coverimg);
        //editor.putString(this.ASMID, ASMID);
        editor.commit();
    }
    public String getbudge() {
        return mPreferences.getString(this.budge, "");
        //return mPreferences.getString(this.ASMID,"");
    }

    public void setbudge(String budge) {
        editor = mPreferences.edit();
        editor.putString(this.budge, budge);
        editor.commit();
    }

}
