package com.example.nguye.detec_2_4_9;

import android.app.Application;

import com.example.nguye.detec_2_4_9.Database.ConfirmDatabase;
import com.example.nguye.detec_2_4_9.Database.FaceDatabase;

public class MyApplication extends Application {

    private FaceDatabase faceDatabase;
    private ConfirmDatabase confirmDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        faceDatabase = new FaceDatabase(this);
        confirmDatabase = new ConfirmDatabase(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        faceDatabase.close();
        confirmDatabase.close();
    }

    public FaceDatabase getFaceDatabase(){
        return faceDatabase;
    }

    public ConfirmDatabase getConfirmDatabase() {
        return confirmDatabase;
    }
}
