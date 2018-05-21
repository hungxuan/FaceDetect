package com.example.nguye.detec_2_4_9.Model;

public class Diff {
    private int id;
    private double diff;

    public Diff(int id, double diff){
        this.id = id;
        this.diff = diff;
    }

    public int getId(){
        return id;
    }
    public double getDiff(){
        return diff;
    }
}
