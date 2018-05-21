package com.example.nguye.detec_2_4_9.Activity;

import android.app.Activity;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nguye.detec_2_4_9.Adapter.StudentAdapter;
import com.example.nguye.detec_2_4_9.Database.ConfirmDatabase;
import com.example.nguye.detec_2_4_9.Model.Student;
import com.example.nguye.detec_2_4_9.MyApplication;
import com.example.nguye.detec_2_4_9.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ListStudent extends AppCompatActivity {
    private ListView listView;
    private Button btnExport;
    private Spinner spinner;

    private StudentAdapter studentAdapter;
    private ConfirmDatabase confirmDatabase;
    private ArrayList<Student> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_students);

        confirmDatabase = ((MyApplication) getApplication()).getConfirmDatabase();
        data = confirmDatabase.getAllStudents();

        listView = findViewById(R.id.list_students);
        studentAdapter = new StudentAdapter(this, R.layout.student, data);
        listView.setAdapter(studentAdapter);

        spinner = findViewById(R.id.spinner);
        controlSpiner();

        btnExport = findViewById(R.id.btnExport);
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FILE_NAME = "attendance_" + spinner.getSelectedItem().toString() + ".csv";
                String content = "" + spinner.getSelectedItem().toString() + "\n";
                ArrayList<Student> listStudent = confirmDatabase.getStudentDate(spinner.getSelectedItem().toString());
                for(int i=0; i < listStudent.size(); i++){
                    content += listStudent.get(i).getStudent() + "     " + listStudent.get(i).getDate() + "     " + listStudent.get(i).getTime() + "\n";
                }

                try {
                    BufferedWriter writer = new BufferedWriter(
                        new FileWriter(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FILE_NAME)));
                    writer.write(content);
                    Toast.makeText(getApplicationContext(), "Export successfully!!!", Toast.LENGTH_SHORT).show();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public StudentAdapter setStudentAdapter(ArrayList<Student> content){
        studentAdapter = new StudentAdapter( this, R.layout.student, content);
        return studentAdapter;
    }

    protected void controlSpiner(){
        ArrayList<String> content = confirmDatabase.getDateList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, content);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String date = spinner.getSelectedItem().toString();
                ArrayList<Student> content = confirmDatabase.getStudentDate(date);
                studentAdapter = setStudentAdapter(content);
                listView.setAdapter(studentAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
