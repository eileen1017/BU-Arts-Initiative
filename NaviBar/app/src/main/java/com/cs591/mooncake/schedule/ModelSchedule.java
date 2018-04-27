package com.cs591.mooncake.schedule;

import java.util.List;

/**
 * Created by LinLi on 4/9/18.
 */

public class ModelSchedule {
    private List<Integer> scheduledint;

    public ModelSchedule(List<Integer> scheduledint) {
        this.scheduledint = scheduledint;

    }

    public List<Integer> getScheduledint() {
        return scheduledint;
    }

    public void setScheduledint(List<Integer> scheduledint) {
        this.scheduledint = scheduledint;
    }


}
