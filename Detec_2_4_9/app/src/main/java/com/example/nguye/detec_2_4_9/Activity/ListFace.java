package com.example.nguye.detec_2_4_9.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.nguye.detec_2_4_9.Adapter.FacesAdapter;
import com.example.nguye.detec_2_4_9.Database.FaceDatabase;
import com.example.nguye.detec_2_4_9.Model.Face;
import com.example.nguye.detec_2_4_9.MyApplication;
import com.example.nguye.detec_2_4_9.R;

import java.util.ArrayList;

public class ListFace extends AppCompatActivity {
    private ListView listView;
    private FacesAdapter facesAdapter;
    private ArrayList<Face> data;
    private FaceDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_faces);

        db = ((MyApplication) getApplication()).getFaceDatabase();
        data = db.getAllFaces();
        System.out.println("Count " + db.getFacesCount());

        listView = findViewById(R.id.list_faces);
        facesAdapter = new FacesAdapter(this, R.layout.face, data);
        listView.setAdapter(facesAdapter);
    }
}
