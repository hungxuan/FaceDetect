package com.example.nguye.detec_2_4_9.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nguye.detec_2_4_9.Model.Face;
import com.example.nguye.detec_2_4_9.Database.FaceDatabase;
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

public class Create extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener {
    private CameraBridgeViewBase openCvCameraView;
    private CascadeClassifier faceDetect;
    private Mat grayscaleImage;
    private int absoluteFaceSize;

    private Button btnScan, btnList;
    private EditText edId;
    private boolean bClick = false;

    private FaceDatabase db = new FaceDatabase(this);

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

    private void initializeOpenCVDependencies() {
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
        setContentView(R.layout.activity_create);

        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        db = ((MyApplication) getApplication()).getFaceDatabase();

        openCvCameraView = new JavaCameraView(this, -1);
        openCvCameraView = findViewById(R.id.camera_create);
        edId = findViewById(R.id.edId);
        btnScan = findViewById(R.id.btnScan);
        btnList = findViewById(R.id.btnList);

        openCvCameraView.setCvCameraViewListener(this);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edId.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "Please fill your id!", Toast.LENGTH_SHORT).show();
                else
                    bClick = true;
            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Create.this, ListFace.class);
                startActivity(intent);
            }
        });
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
            if(bClick){
                    final Rect tmpFacesArray = faceshumansArray[i];
                    Create.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Mat tmp = tmpInputFrame.submat(tmpFacesArray);

                            try {
                                Bitmap bmp = Bitmap.createBitmap(tmp.cols(), tmp.rows(), Bitmap.Config.ARGB_8888);
                                Utils.matToBitmap(tmp, bmp);

                                int before = db.getFacesCount();
                                db.addFace(new Face(Integer.parseInt(edId.getText().toString()), bmp));
                                int after = db.getFacesCount();
                                if(before != after)
                                    Toast.makeText(getApplicationContext(), "Create new user successfully", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getApplicationContext(), "Error! Try again", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.d("MainError", "Loi");
                            }
                        }
                    });
//                if (! checkId(Integer.parseInt(edId.getText().toString()))){
//                    final Rect tmpFacesArray = faceshumansArray[i];
//                    Create.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Mat tmp = tmpInputFrame.submat(tmpFacesArray);
//
//                            try {
//                                Bitmap bmp = Bitmap.createBitmap(tmp.cols(), tmp.rows(), Bitmap.Config.ARGB_8888);
//                                Utils.matToBitmap(tmp, bmp);
//
//                                int before = db.getFacesCount();
//                                db.addFace(new Face(Integer.parseInt(edId.getText().toString()), bmp));
//                                int after = db.getFacesCount();
//                                if(before != after)
//                                    Toast.makeText(getApplicationContext(), "Create new user successfully", Toast.LENGTH_SHORT).show();
//                                else
//                                    Toast.makeText(getApplicationContext(), "Error! Try again", Toast.LENGTH_SHORT).show();
//                            } catch (Exception e) {
//                                Log.d("MainError", "Loi");
//                            }
//                        }
//                    });
//                }
//                else
//                    Toast.makeText(getApplicationContext(), "Error! Complict Id", Toast.LENGTH_SHORT).show();
            }
        }
        bClick = false;
        return inputFrame;
    }

    public boolean checkId(int id){
        boolean status = false;
        for(int i=0; i <= db.getFacesCount(); i++){
            if (id == db.getFace(i).getId()){
                status = true;
                break;
            }
        }
        return status;
    }

    @Override
    protected void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, baseLoaderCallback);
    }
}
