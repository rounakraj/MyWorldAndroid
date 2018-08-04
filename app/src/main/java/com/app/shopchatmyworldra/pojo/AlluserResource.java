package com.app.shopchatmyworldra.pojo;

/**
 * Created by MMAD on 05-09-2017.
 */

public class AlluserResource {

    private String userName;
    private String emailId;
    private String ProfileImage;
    public String userStatus;
    public String UserId;
    public String lastName;
    public String is_invited;

    public String getIs_invited() {
        return is_invited;
    }

    public void setIs_invited(String is_invited) {
        this.is_invited = is_invited;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
