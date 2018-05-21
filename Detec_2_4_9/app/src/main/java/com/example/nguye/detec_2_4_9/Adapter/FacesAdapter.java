package com.example.nguye.detec_2_4_9.Adapter;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nguye.detec_2_4_9.Model.Face;
import com.example.nguye.detec_2_4_9.R;

import java.util.List;

public class FacesAdapter extends ArrayAdapter<Face>{
    Activity context;
    int resource;
    List<Face> objects;

    public FacesAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Face> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);
        Face face = this.objects.get(position);

        TextView txtId = row.findViewById(R.id.faceId);
        ImageView imgPhotoScaned = row.findViewById(R.id.faceIM);

        imgPhotoScaned.setImageBitmap(face.getPhotoScan());
        txtId.setText("ID : "+ face.getId());

        return row;
    }
}
