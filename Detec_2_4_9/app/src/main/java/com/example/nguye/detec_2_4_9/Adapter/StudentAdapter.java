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

import com.example.nguye.detec_2_4_9.Model.Student;
import com.example.nguye.detec_2_4_9.R;

import java.util.List;

public class StudentAdapter extends ArrayAdapter<Student> {
    Activity context;
    int resource;
    List<Student> objects;

    public StudentAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Student> objects) {
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
        Student student = this.objects.get(position);

        TextView txtMSSV = row.findViewById(R.id.txtMSSV);
        TextView txtDate = row.findViewById(R.id.txtDate);
        TextView txtTime = row.findViewById(R.id.txtTime);

        txtMSSV.setText("" + student.getStudent());
        txtDate.setText(student.getDate());
        txtTime.setText(student.getTime());

        return row;
    }
}
