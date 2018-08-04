package com.app.shopchatmyworldra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mac on 04/08/18.
 */

public class ChatList {

    @SerializedName("chatMsg")
    @Expose
    private String chatMsg;
    @SerializedName("chatId")
    @Expose
    private String chatId;
    @SerializedName("chatFile")
    @Expose
    private String chatFile;
    @SerializedName("chatVideo")
    @Expose
    private String chatVideo;
    @SerializedName("chatAudio")
    @Expose
    private String chatAudio;
    @SerializedName("chatTime")
    @Expose
    private String chatTime;
    @SerializedName("chatDate")
    @Expose
    private String chatDate;
    @SerializedName("chatFlag")
    @Expose
    private String chatFlag;

    public String getChatMsg() {
        return chatMsg;
    }

    public void setChatMsg(String chatMsg) {
        this.chatMsg = chatMsg;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatFile() {
        return chatFile;
    }

    public void setChatFile(String chatFile) {
        this.chatFile = chatFile;
    }

    public String getChatVideo() {
        return chatVideo;
    }

    public void setChatVideo(String chatVideo) {
        this.chatVideo = chatVideo;
    }

    public String getChatAudio() {
        return chatAudio;
    }

    public void setChatAudio(String chatAudio) {
        this.chatAudio = chatAudio;
    }

    public String getChatTime() {
        return chatTime;
    }

    public void setChatTime(String chatTime) {
        this.chatTime = chatTime;
    }

    public String getChatDate() {
        return chatDate;
    }

    public void setChatDate(String chatDate) {
        this.chatDate = chatDate;
    }

    public String getChatFlag() {
        return chatFlag;
    }

    public void setChatFlag(String chatFlag) {
        this.chatFlag = chatFlag;
    }

}
