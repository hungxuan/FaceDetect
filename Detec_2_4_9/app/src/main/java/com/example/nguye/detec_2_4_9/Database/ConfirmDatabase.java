package com.example.nguye.detec_2_4_9.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nguye.detec_2_4_9.Model.Student;

import java.util.ArrayList;

public class ConfirmDatabase extends SQLiteOpenHelper {
    public ConfirmDatabase(Context context) {
        super(context, "ConfirmDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACS_TABLE = "CREATE TABLE student ( student INTEGER PRIMARY KEY, date VARCHAR, time VARCHAR )";
        db.execSQL(CREATE_CONTACS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS student");
        onCreate(db);
    }

    public void addStudent(Student student){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("student", student.getStudent());
        values.put("date", student.getDate());
        values.put("time", student.getTime());

        db.insert("student", null, values);
        db.close();
    }

    public ArrayList<Student> getAllStudents(){
        ArrayList<Student> studentList = new ArrayList<>();

        String query = "SELECT * FROM student";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                Student student = new Student();
                student.setStudent(Integer.parseInt(cursor.getString(0)));
                student.setDate(cursor.getString(1));
                student.setTime(cursor.getString(2));

                studentList.add(student);
            } while (cursor.moveToNext());
        }

        return studentList;
    }

    public ArrayList<Student> getStudentDate(String date){
        ArrayList<Student> studentList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM student WHERE date='" + date + "'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do{
                Student student = new Student();
                student.setStudent(Integer.parseInt(cursor.getString(0)));
                student.setDate(cursor.getString(1));
                student.setTime(cursor.getString(2));

                studentList.add(student);
            } while (cursor.moveToNext());
        }

        return studentList;
    }

    public ArrayList<String> getDateList(){
        ArrayList<String> dateList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT DISTINCT date FROM student";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do{
                dateList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        return  dateList;
    }
}


