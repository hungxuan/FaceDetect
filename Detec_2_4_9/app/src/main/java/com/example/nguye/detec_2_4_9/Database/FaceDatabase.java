package com.example.nguye.detec_2_4_9.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nguye.detec_2_4_9.Model.Face;
import com.example.nguye.detec_2_4_9.Helper;

import java.util.ArrayList;

public class FaceDatabase extends SQLiteOpenHelper {

    public FaceDatabase(Context context) {
        super(context, "FaceDatabase", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACS_TABLE = "CREATE TABLE  facedata ( id INTEGER PRIMARY KEY, photoScan BLOB )";
        db.execSQL(CREATE_CONTACS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS facedata");
        onCreate(db);
    }

    public void addFace(Face face){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", face.getId());
        values.put("photoScan", new Helper().getBytes(face.getPhotoScan()));

        db.insert("facedata", null, values);
        db.close();
    }

    public Face getFace(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Face face = new Face();
        Cursor cursor = db.query("facedata",
                new String[]{"id", "photoScan"}, "id = ?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if(cursor != null && cursor.moveToFirst()){
            face.setId(Integer.parseInt(cursor.getString(0)));
            face.setPhotoScan(new Helper().getImage(cursor.getBlob(1)));
            cursor.close();
        }
        return face;
    }

    public ArrayList<Face> getAllFaces(){
        ArrayList<Face> faceList = new ArrayList<Face>();

        String query = "SELECT * FROM facedata";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                Face face = new Face();
                face.setId(Integer.parseInt(cursor.getString(0)));
                face.setPhotoScan(new Helper().getImage(cursor.getBlob(1)));

                faceList.add(face);
            } while (cursor.moveToNext());
        }

        return faceList;
    }

    public int getFacesCount(){
        String query = "SELECT * FROM facedata";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        return cursor.getCount();
    }

    public int updateFace(Face face){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("photoScan", new Helper().getBytes(face.getPhotoScan()));

        return db.update("facedata", values, "id = ?", new String[] {String.valueOf(face.getId())});
    }

    // Deleting single detection
    public void deleteFace(Face face){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("facedata", "id = ?", new String[] {String.valueOf(face.getId())});
        db.close();
    }

    // Deleting all detections
    public void deleteAllFaces(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("facedata", null, null);
        db.close();
    }
}
