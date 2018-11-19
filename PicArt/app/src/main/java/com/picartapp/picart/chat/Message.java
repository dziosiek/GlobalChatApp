package com.picartapp.picart.chat;

public class Message {
    String contents, uid;
    long time;

    public Message(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Message(String contents, String uid, long time) {
        this.contents = contents;
        this.uid = uid;
        this.time = time;
    }

    public String getContents() {
        return contents;
    }

    public String getUid() {
        return uid;
    }

    public long getTime() {
        return time;
    }


}
