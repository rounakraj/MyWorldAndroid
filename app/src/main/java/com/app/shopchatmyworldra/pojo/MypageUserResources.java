package com.app.shopchatmyworldra.pojo;

/**
 * Created by MMAD on 01-09-2017.
 */

public class MypageUserResources {
    private String userId="";
    private String userStatus="";
    private String userFile="";
    private String userName="";
    private String profileImage="";
    private String userTime="";
    private boolean isContinuously;

    public boolean isContinuously() {
        return isContinuously;
    }

    public void setContinuously(boolean continuously) {
        isContinuously = continuously;
    }

    public String getUserTime() {
        return userTime;
    }

    public void setUserTime(String userTime) {
        this.userTime = userTime;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserFile() {
        return userFile;
    }

    public void setUserFile(String userFile) {
        this.userFile = userFile;
    }
}
