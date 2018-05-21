package com.example.nguye.detec_2_4_9.Model;

public class Student {
    private int student;
    private String date, time;

    public Student(){
    }

    public Student(int student, String date, String time){
        this.student = student;
        this.date = date;
        this.time = time;
    }

    public int getStudent() {
        return student;
    }
    public String getDate() {return date;}
    public String getTime() {
        return time;
    }

    public void setStudent(int student) {
        this.student = student;
    }
    public void setDate(String date){
        this.date = date;
    }
    public void setTime(String time) {
        this.time = time;
    }
}
