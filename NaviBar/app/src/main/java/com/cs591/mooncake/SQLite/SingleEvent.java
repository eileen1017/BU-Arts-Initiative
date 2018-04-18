package com.cs591.mooncake.SQLite;

import android.graphics.Bitmap;

public class SingleEvent {
    private int ID;
    private String name;
    private String address;
    private Bitmap pic;
    private String artist;
    private String start;
    private String end;
    private int date;
    private String type;


    SingleEvent(){

    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getName() {
        return name;
    }

    public Bitmap getPic() {
        return pic;
    }

    public int getDate() {
        return date;
    }

    public int getID() {
        return ID;
    }

    public String getAddress() {
        return address;
    }

    public String getArtist() {
        return artist;
    }

    public String getEnd() {
        return end;
    }

    public String getStart() {
        return start;
    }

}
