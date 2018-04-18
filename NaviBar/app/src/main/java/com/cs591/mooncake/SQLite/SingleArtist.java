package com.cs591.mooncake.SQLite;

import android.graphics.Bitmap;

public class SingleArtist {
    private int id;
    private String name;
    private Bitmap pic;
    private String country;
    private String website;
    private String bios;

    SingleArtist(){

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Bitmap getPic() {
        return pic;
    }

    public int getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public String getWebsite() {
        return website;
    }

    public String getBios() {
        return bios;
    }

    public void setBios(String bios) {
        this.bios = bios;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
