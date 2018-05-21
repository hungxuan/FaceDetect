package com.example.nguye.detec_2_4_9.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nguye.detec_2_4_9.Database.ConfirmDatabase;
import com.example.nguye.detec_2_4_9.Database.FaceDatabase;
import com.example.nguye.detec_2_4_9.Helper;
import com.example.nguye.detec_2_4_9.Model.Diff;
import com.example.nguye.detec_2_4_9.Model.Student;
import com.example.nguye.detec_2_4_9.MyApplication;
import com.example.nguye.detec_2_4_9.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Check extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener {
    private CameraBridgeViewBase openCvCameraView;
    private CascadeClassifier faceDetect;
    private Mat grayscaleImage;
    private int absoluteFaceSize;

    private Button btnConfirm, btnCompare;
    private ImageView imConfirm;
    private boolean bClick = false, bCompare = false;

    private FaceDatabase db = new FaceDatabase(this);
    private ConfirmDatabase confirmDatabase = new ConfirmDatabase(this);

    private BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS:
                    initializeOpenCVDependencies();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    private void initializeOpenCVDependencies(){
        try{
            InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt.xml");
            FileOutputStream os  = new FileOutputStream(mCascadeFile);

            byte []buffer = new byte[4096];
            int bytesRead;
            while((bytesRead = is.read(buffer)) != -1){
                os.write(buffer, 0, bytesRead);
            }

            faceDetect = new CascadeClassifier(mCascadeFile.getAbsolutePath());

            is.close();
            os.close();
        }
        catch (Exception e){
            Log.e("OpenCvActivity", "Error loading cascade", e);
        }

        openCvCameraView.enableView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        db = ((MyApplication) getApplication()).getFaceDatabase();

        openCvCameraView = new JavaCameraView(this, -1);
        openCvCameraView = findViewById(R.id.camera_check);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCompare = findViewById(R.id.btnCompare);
        imConfirm = findViewById(R.id.imConfirm);

        openCvCameraView.setCvCameraViewListener(this);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bClick = true;
                bCompare = true;
            }
        });

        btnCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap image = ((BitmapDrawable) imConfirm.getDrawable()).getBitmap();
                boolean flag = false;
                ArrayList<Diff> listResult = new ArrayList<>();
                if (db.getFacesCount() > 0){
                    for (int i=1; i <= db.getFacesCount(); i++){
                        if (db.getFace(i).getPhotoScan() == null){
                            System.out.println("Null " + i);
                        }
                        else {
                            double result = new Helper().confirm(image, db.getFace(i).getPhotoScan());
                            System.out.println("Diff: " + result);
                            if(result < 15) {
                                flag = true;
                                listResult.add(new Diff(db.getFace(i).getId(), result));
                            }
                        }
                    }

                    if(flag){
                        int mssv = getMin(listResult);
                        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                        DateFormat df = new SimpleDateFormat("h:mm a");
                        String time = df.format(Calendar.getInstance().getTime());

                        Student student = new Student(db.getFace(mssv).getId(), date, time);

                        confirmDatabase.addStudent(student);
                        Toast.makeText(getApplicationContext(), "Hello " + db.getFace(mssv).getId(), Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Not found!!", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Database null!!!", Toast.LENGTH_SHORT).show();
//                if (bCompare)
//                    compare();
//                else
//                    Toast.makeText(getApplicationContext(), "Please capture image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getMin(ArrayList<Diff> list){
        int id =  list.get(0).getId();

        for (int i=0; i < list.size()-1; i++){
            if (list.get(i).getDiff() < list.get(i+1).getDiff()) {
                id = list.get(i).getId();
                Log.d("Diff", "getMin: " + id);
            }else {
                id = list.get(i+1).getId();
                Log.d("Diff", "getMin: " + id);
            }
        }
        return  id;
    }

    public void compare(){
        Bitmap image = ((BitmapDrawable) imConfirm.getDrawable()).getBitmap();
        boolean flag = false;
        if (db.getFacesCount() > 0){
            for (int i=1; i <= db.getFacesCount(); i++){
                double result = new Helper().confirm(image, db.getFace(i).getPhotoScan());
                if(result > 0) {
                    flag = true;
                    Toast.makeText(getApplicationContext(), "Hello " + db.getFace(i).getId(), Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            if(flag)
                Toast.makeText(getApplicationContext(), "Not found!!!", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getApplicationContext(), "Database null!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        grayscaleImage = new Mat(height, width, CvType.CV_8UC4);
        absoluteFaceSize = (int) (height * 0.2);
    }

    @Override
    public void onCameraViewStopped() {
        grayscaleImage.release();
    }

    @Override
    public Mat onCameraFrame(Mat inputFrame) {
        Imgproc.cvtColor(inputFrame, grayscaleImage, Imgproc.CV_RGBA2mRGBA);

        final MatOfRect faceshumans = new MatOfRect();

        try{
            faceDetect.detectMultiScale(grayscaleImage, faceshumans, 1.1, 2, 2, new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }catch (Exception ex){
            ex.printStackTrace();
        }

        final Rect[] faceshumansArray = faceshumans.toArray();
        final int len = faceshumansArray.length;
        final Mat tmpInputFrame = inputFrame;

        for (int i=0; i < len; i++){
            Core.rectangle(inputFrame, faceshumansArray[i].tl(), faceshumansArray[i].br(), new Scalar(0, 255, 0, 255),1);
            if(bClick == true){
                final Rect tmpFacesArray = faceshumansArray[i];
                Check.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Mat tmp = tmpInputFrame.submat(tmpFacesArray);
                        try {
                            Bitmap bmp = Bitmap.createBitmap(tmp.cols(), tmp.rows(), Bitmap.Config.ARGB_8888);
                            Utils.matToBitmap(tmp, bmp);
                            imConfirm.setImageBitmap(bmp);
                        } catch (Exception e) {
                            Log.d("MainError", "Error!!");
                        }
                    }
                });
            }
        }
        bClick = false;
        return inputFrame;
    }

    @Override
    protected void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, baseLoaderCallback);
    }
}

