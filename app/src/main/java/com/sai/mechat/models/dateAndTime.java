package com.sai.mechat.models;

public class dateAndTime {
    long previousLoginTime;

    public dateAndTime(long previousLoginTime) {
        this.previousLoginTime = previousLoginTime;
    }

    public long getPreviousLoginTime() {
        return previousLoginTime;
    }

    public void setPreviousLoginTime(long previousLoginTime) {
        this.previousLoginTime = previousLoginTime;
    }
}
