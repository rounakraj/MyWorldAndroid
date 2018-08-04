package com.app.shopchatmyworldra.pojo;

/**
 * Created by MMAD on 10-07-2017.
 */

public class FriendListResource {

 private String userId="";
    private String firstName="";
    private String lastName="";
    private String mobileNo="";
    private String profileImage="";
    private String followingStatus="";
    private String emailId="";
    private String onlineFlag="";
    private String userStatus="";

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getOnlineFlag() {
        return onlineFlag;
    }

    public void setOnlineFlag(String onlineFlag) {
        this.onlineFlag = onlineFlag;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getFollowingStatus() {
        return followingStatus;
    }

    public void setFollowingStatus(String followingStatus) {
        this.followingStatus = followingStatus;
    }
}
