package com.cs591.mooncake.schedule;

/**
 * Created by LinLi on 4/9/18.
 */

public class ModelSchedule {
    private String name;
    private String description;

    public ModelSchedule(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
