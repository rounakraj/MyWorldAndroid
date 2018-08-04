package com.app.shopchatmyworldra.pojo;

/**
 * Created by legacy on 08-Sep-17.
 */

public class UserChatMessage {

    private String id;
    private boolean isMe;
    private String message="";
    private Long userId;
    private String dateTime;
    private String image;
    private String chatFlag;
    private boolean itemFlag;
    private String chatVideo;
    private String chatTime;
    private String chatAudio;

    public String getChatAudio() {
        return chatAudio;
    }

    public void setChatAudio(String chatAudio) {
        this.chatAudio = chatAudio;
    }

    public String getChatVideo() {
        return chatVideo;
    }

    public void setChatVideo(String chatVideo) {
        this.chatVideo = chatVideo;
    }

    public String getChatTime() {
        return chatTime;
    }

    public void setChatTime(String chatTime) {
        this.chatTime = chatTime;
    }

    public String getChatFlag() {
        return chatFlag;
    }

    public void setChatFlag(String chatFlag) {
        this.chatFlag = chatFlag;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getIsme() {
        return isMe;
    }

    public void setMe(boolean isMe) {
        this.isMe = isMe;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDate() {
        return dateTime;
    }

    public void setDate(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isItemFlag() {
        return itemFlag;
    }

    public void setItemFlag(boolean itemFlag) {
        this.itemFlag = itemFlag;
    }
}
