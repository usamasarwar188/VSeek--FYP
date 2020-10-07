package com.example.vseek.models;


import android.net.Uri;

import java.io.Serializable;

public class Video  {
    private final Uri uri;
    private final String name;
    private final int duration;
    private final int size;



    boolean labeled;
    VideoLabels videoLabels;

    public Video(Uri uri, String name, int duration, int size) {
        this.uri = uri;
        this.name = name;
        this.duration = duration;
        this.size = size;
        this.labeled=false;
    }

    public Video(Uri uri, String name, int duration, int size, boolean labeled, VideoLabels videoLabels) {
        this.uri = uri;
        this.name = name;
        this.duration = duration;
        this.size = size;
        this.labeled=labeled;
        this.videoLabels=videoLabels;
    }


    public Uri getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public int getSize() {
        return size;
    }


    public VideoLabels getVideoLabels() {
        return videoLabels;
    }

    public void setVideoLabels(VideoLabels videoLabels) {
        this.videoLabels = videoLabels;
    }

    public boolean isLabeled() {
        return labeled;
    }

    public void setLabeled(boolean labeled) {
        this.labeled = labeled;
    }

}
