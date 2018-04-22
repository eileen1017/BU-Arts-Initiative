package com.cs591.mooncake.SQLite;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.HashSet;
import java.util.Set;

public class SingleUser {
    private Set<Integer> scheduled;
    private String UID;
    private String userName;
    private Bitmap pic;
    private Uri picUrl;
    private Set<Integer> liked;
    private String splitter = ";";

    public SingleUser(){
        scheduled = new HashSet<>();
        liked = new HashSet<>();
    }

    public SingleUser(String splitter){
        scheduled = new HashSet<>();
        liked = new HashSet<>();
        this.splitter = splitter;
    }

    public Uri getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(Uri picUrl) {
        this.picUrl = picUrl;
    }

    public void addScheduled(int id) {
        scheduled.add(id);
    }

    public void removeScheduled(int id) {
        scheduled.remove(id);
    }

    public void addLiked(int id) {
        liked.add(id);
    }

    public void removeLiked(int id) {
        liked.remove(id);
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public Bitmap getPic() {
        return pic;
    }

    public Set<Integer> getLiked() {
        return liked;
    }

    public Set<Integer> getScheduled() {
        return scheduled;
    }

    public String getUID() {
        return UID;
    }

    public String getUserName() {
        return userName;
    }

    public String getScheduledString() {
        String res = "";
        if (scheduled != null) {
            for (Integer i : scheduled) {
                res += Integer.toString(i);
                res += splitter;
            }
        }
        return res;
    }

    public String getLikedString() {
        String res = "";
        if (liked != null) {
            for (Integer i : liked) {
                res += Integer.toString(i);
                res += splitter;
            }
        }
        return res;
    }

    public void setScheduledByString(String string) {
        scheduled.clear();
        for (String event : string.split(splitter)) {
            if(event.isEmpty()) continue;
            this.addScheduled(Integer.valueOf(event));
        }
    }

    public void setLikedByString(String string) {
        liked.clear();
        for (String event : string.split(splitter)) {
            if(event.isEmpty()) continue;
            this.addLiked(Integer.valueOf(event));
        }
    }
}
