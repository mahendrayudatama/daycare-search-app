package com.example.kidcare.Model;

import java.util.ArrayList;
import java.util.List;

public class ModelActivity {
    String activity;
    String time;
    String noted;
    String status;
    String urlVideo;
    String urlImage;
    ArrayList<ModelUrl> modelImageUrlList;
    ArrayList<ModelUrl> modelVideoUrlList;

    public ArrayList<ModelUrl> getModelImageUrlList() {
        return modelImageUrlList;
    }

    public ArrayList<ModelUrl> getModelVideoUrlList() {
        return modelVideoUrlList;
    }

    public ModelActivity(String activity, String time, String noted, String status, String urlImage, String urlVideo, ArrayList<ModelUrl> modelImageUrlList, ArrayList<ModelUrl> modelVideoUrlList) {
        this.activity = activity;
        this.time = time;
        this.noted = noted;
        this.status = status;
        this.urlImage = urlImage;
        this.urlVideo = urlVideo;
        this.modelImageUrlList = modelImageUrlList;
        this.modelVideoUrlList = modelVideoUrlList;

    }


    public String getUrlVideo() { return urlVideo; }
    public String getUrlImage() { return urlImage; }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNoted() {
        return noted;
    }

    public void setNoted(String noted) {
        this.noted = noted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
