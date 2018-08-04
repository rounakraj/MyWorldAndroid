package com.app.shopchatmyworldra.pojo;

/**
 * Created by MMAD on 01-09-2017.
 */

public class MypageGlobalResources {

    private String userId="";
    private String userName="";
    private String globalStatus="";
    private String globalFile="";
    private String profileImage="";
    private String emailId="";
    private String globalTime="";
    private boolean isContinuously;

    public boolean isContinuously() {
        return isContinuously;
    }

    public void setContinuously(boolean continuously) {
        isContinuously = continuously;
    }

    public String getGlobalTime() {
        return globalTime;
    }

    public void setGlobalTime(String globalTime) {
        this.globalTime = globalTime;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
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

    public String getGlobalStatus() {
        return globalStatus;
    }

    public void setGlobalStatus(String globalStatus) {
        this.globalStatus = globalStatus;
    }

    public String getGlobalFile() {
        return globalFile;
    }

    public void setGlobalFile(String globalFile) {
        this.globalFile = globalFile;
    }
}
