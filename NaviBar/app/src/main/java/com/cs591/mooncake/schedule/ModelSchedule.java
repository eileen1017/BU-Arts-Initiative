package com.cs591.mooncake.schedule;

/**
 * Created by LinLi on 4/9/18.
 */

public class ModelSchedule {
    private String name;
    private String startt;
    private String endt;

    public ModelSchedule(String name, String startt, String endt) {
        this.name = name;
        this.startt = startt;
        this.endt = endt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartt() {
        return startt;
    }

    public void setStartt(String description) {
        this.startt = description;
    }

    public String getEndt() {
        return startt;
    }

    public void setEndt(String description) {
        this.startt = description;
    }
}
