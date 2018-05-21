package com.example.nguye.detec_2_4_9.Model;

import android.graphics.Bitmap;

public class Face {
    private int id;
    private Bitmap photoScan;

    public Face(){

    }

    public Face(Bitmap photoScan){
        this.photoScan = photoScan;
    }

    public Face(int id, Bitmap photoScan){
        this.id = id;
        this.photoScan = photoScan;
    }

    public int getId() {
        return id;
    }
    public Bitmap getPhotoScan() {
        return photoScan;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPhotoScan(Bitmap photoScan) {
        this.photoScan = photoScan;
    }
}

