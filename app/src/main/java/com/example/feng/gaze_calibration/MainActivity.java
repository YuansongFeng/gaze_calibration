package com.example.feng.gaze_calibration;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button btnCali1;
    private Button btnCali2;
    private Button btnCali3;
    private Button btnCali4;
    private Button btnCali5;
    private Button btnCali6;
    private Button btnCali7;
    private Button btnCali8;
    private Button btnCali9;
    private static ArrayList<Button> buttonList;
    private static int order = 0;
    private static final int REQUEST_CAMERA = 1;
    private ImageView checkPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonList = new ArrayList<Button>();
        btnCali1 = (Button) findViewById(R.id.btnCali1);
        btnCali2 = (Button) findViewById(R.id.btnCali2);
        btnCali3 = (Button) findViewById(R.id.btnCali3);
        btnCali4 = (Button) findViewById(R.id.btnCali4);
        btnCali5 = (Button) findViewById(R.id.btnCali5);
        btnCali6 = (Button) findViewById(R.id.btnCali6);
        btnCali7 = (Button) findViewById(R.id.btnCali7);
        btnCali8 = (Button) findViewById(R.id.btnCali8);
        btnCali9 = (Button) findViewById(R.id.btnCali9);
        buttonList.add(btnCali1);
        buttonList.add(btnCali2);
        buttonList.add(btnCali3);
        buttonList.add(btnCali4);
        buttonList.add(btnCali5);
        buttonList.add(btnCali6);
        buttonList.add(btnCali7);
        buttonList.add(btnCali8);
        buttonList.add(btnCali9);
        checkPhoto = (ImageView)findViewById(R.id.photoView);

        btnCali5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBtnVisible(btnCali1);
                cameraCapture();
            }
        });
        btnCali1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBtnVisible(btnCali2);
                cameraCapture();
            }
        });
        btnCali2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBtnVisible(btnCali3);
                cameraCapture();
            }
        });
        btnCali3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBtnVisible(btnCali6);
                cameraCapture();
            }
        });
        btnCali6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBtnVisible(btnCali9);
                cameraCapture();
            }
        });
        btnCali9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBtnVisible(btnCali8);
                cameraCapture();
            }
        });
        btnCali8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBtnVisible(btnCali7);
                cameraCapture();
            }
        });
        btnCali7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBtnVisible(btnCali4);
                cameraCapture();
            }
        });
        btnCali4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraCapture();
            }
        });
        for(int i=0; i<buttonList.size();i++){
            if(!buttonList.get(i).equals(btnCali5)){
                buttonList.get(i).setEnabled(false);
            }
        }

    }


    /*@Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults){

    }*/

    private void setBtnVisible(Button btn){
        for(int i=0; i<buttonList.size();i++){
            if(!buttonList.get(i).equals(btn)){
                buttonList.get(i).setBackgroundColor(Color.BLACK);
                buttonList.get(i).setEnabled(false);
            }else{
                buttonList.get(i).setBackgroundColor(Color.WHITE);
                buttonList.get(i).setEnabled(true);
            }
        }
    }
    private void cameraCapture(){
        boolean result = Utility.checkPermission(MainActivity.this);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(result){
            startActivityForResult(intent,REQUEST_CAMERA);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_CAMERA){
                onCaptureImageResult(data);
            }
        }
    }
    private void onCaptureImageResult(Intent data){
        Bitmap thumbnail = (Bitmap)data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG,100,bytes); // Quality is from 0 to 100

        File dest = new File(Environment.getExternalStorageDirectory(),System.currentTimeMillis()+".jpg");
        FileOutputStream fo;
        try{
            dest.createNewFile();
            fo = new FileOutputStream(dest);
            fo.write(bytes.toByteArray()); // Bytes is the array containing the compressed image
            fo.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        checkPhoto.setImageBitmap(thumbnail);
    }
}
