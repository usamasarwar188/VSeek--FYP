package com.example.vseek.models;

import java.io.Serializable;
import java.util.ArrayList;

public class VideoLabels {

    public ArrayList<String> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<String> labels) {
        this.labels = labels;
    }

    public int getVideoID() {
        return videoID;
    }

    public void setVideoID(int videoID) {
        this.videoID = videoID;
    }

    ArrayList<String> labels;
    int videoID;

    public VideoLabels(ArrayList<String> labels) {
        this.labels = labels;
    }


}
