package com.cs591.mooncake.like;

/**
 * Created by LinLi on 4/8/18.
 */

public class ModelLike {

    private int image;
    private String name;

    public ModelLike(int image, String name) {
        this.image = image;
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
