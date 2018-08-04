package com.app.shopchatmyworldra.ResponseParser;

import com.app.shopchatmyworldra.pojo.ChatList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mac on 04/08/18.
 */

public class ChatParser {

    @SerializedName("responseCode")
    @Expose
    private String responseCode;
    @SerializedName("responseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("chatList")
    @Expose
    private List<ChatList> chatList = null;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public List<ChatList> getChatList() {
        return chatList;
    }

    public void setChatList(List<ChatList> chatList) {
        this.chatList = chatList;
    }

}
