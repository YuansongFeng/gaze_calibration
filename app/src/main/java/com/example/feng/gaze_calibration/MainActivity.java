package com.example.feng.gaze_calibration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.util.Size;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.jar.Manifest;


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
    private CameraDevice mCameraDevice;
    private String mCameraId;
    private Semaphore mCameraLock = new Semaphore(1);
    private ImageReader mImageReader;
    private File mFile;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private CameraCaptureSession mCaptureSession;
    private static final String TAG = "Gaze_Calibration";

    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            mCameraLock.release(); // Here the release means that the Camera can be CLOSED.
            mCameraDevice = cameraDevice;
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraLock.release();
            cameraDevice.close();
            ;
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            mCameraLock.release();
            mCameraDevice.close();
            mCameraDevice = null;
        }
    };

    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener
            = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            mBackgroundHandler.post(new ImageSaver(reader.acquireNextImage(), mFile));
        }
    };


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
        checkPhoto = (ImageView) findViewById(R.id.photoView);

        btnCali5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewFile(5);
                setBtnVisible(btnCali1);
                cameraCapture();
            }
        });
        btnCali1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewFile(1);
                setBtnVisible(btnCali2);
                cameraCapture();
            }
        });
        btnCali2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewFile(2);
                setBtnVisible(btnCali3);
                cameraCapture();
            }
        });
        btnCali3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewFile(3);
                setBtnVisible(btnCali6);
                cameraCapture();
            }
        });
        btnCali6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewFile(6);
                setBtnVisible(btnCali9);
                cameraCapture();
            }
        });
        btnCali9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewFile(9);
                setBtnVisible(btnCali8);
                cameraCapture();
            }
        });
        btnCali8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewFile(8);
                setBtnVisible(btnCali7);
                cameraCapture();
            }
        });
        btnCali7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewFile(7);
                setBtnVisible(btnCali4);
                cameraCapture();
            }
        });
        btnCali4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewFile(4);
                cameraCapture();
                stopBackGroundThread();
                closeCamera();
                btnCali4.setBackgroundColor(Color.BLACK);
                btnCali4.setEnabled(false);
            }
        });
        for (int i = 0; i < buttonList.size(); i++) {
            if (!buttonList.get(i).equals(btnCali5)) {
                buttonList.get(i).setEnabled(false);
            }
        }
        startBackGroundThread();
        openCamera();

    }

    private void createNewFile(int order){
        mFile = new File(Environment.getExternalStorageDirectory(), order+".jpg");
        if(mFile.exists()){
            mFile.delete();
        }
        mFile.deleteOnExit();
    }
    private void setBtnVisible(Button btn) {
        for (int i = 0; i < buttonList.size(); i++) {
            if (!buttonList.get(i).equals(btn)) {
                buttonList.get(i).setBackgroundColor(Color.BLACK);
                buttonList.get(i).setEnabled(false);
            } else {
                buttonList.get(i).setBackgroundColor(Color.WHITE);
                buttonList.get(i).setEnabled(true);
            }
        }
    }
    private void startBackGroundThread(){
        mBackgroundThread = new HandlerThread("GazeCalibration");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }
    private void stopBackGroundThread(){
        mBackgroundThread.quitSafely();
        try{
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    private void openCamera(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            String[] requests = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, requests, REQUEST_CAMERA);
            return;
        }
        // Obtain the mCameraDevice here
        CameraManager manager = (CameraManager) getSystemService(CAMERA_SERVICE);
        Size largest = null;
        try {
            if (!mCameraLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Timeout!");
            }
            int cam_counter = 0;
            mCameraId = manager.getCameraIdList()[0];
            while(cam_counter<manager.getCameraIdList().length){
                String cameraID = manager.getCameraIdList()[cam_counter];
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraID);
                if(characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT){
                    mCameraId = cameraID;
                break;}
                else{
                    cam_counter += 1;
                }
            }
            manager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            largest = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new CompareSizesByArea());

            mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(), ImageFormat.JPEG, 2);
            mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mBackgroundHandler);

            mCameraDevice.createCaptureSession(Arrays.asList(mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(CameraCaptureSession session) {
                            if (null == mCameraDevice) return;
                            mCaptureSession = session;
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession session) {

                        }
                    }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        }

    private void closeCamera(){
        try{
            mCameraLock.acquire();
            if(mCaptureSession != null){
                mCaptureSession.close();
                mCaptureSession = null;
            }
            if(mCameraDevice != null){
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if(mImageReader != null){
                mImageReader.close();
                mImageReader = null;
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }finally{
            mCameraLock.release();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    new AlertDialog.Builder(this).setMessage("Error Getting Permission")
                            .setPositiveButton(android.R.string.ok, null).show();
                    break;
                } else {
                    Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void cameraCapture() {
        try {
            if (null == mCameraDevice) return;
            final CaptureRequest.Builder captureBuilder =
                    mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            CameraCaptureSession.CaptureCallback CaptureCallback
                    = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request,
                                               @NonNull TotalCaptureResult result) {
                    /*Thread UIthread = new Thread(){
                        @Override
                        public void run(){
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showPhoto();
                                }
                            });
                            super.run();
                        }
                    };
                    UIthread.start();*/
                    mBackgroundHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showPhoto();
                                }
                            });
                        }
                    });

                }
            };
            //mCaptureSession.stopRepeating();
            mCaptureSession.capture(captureBuilder.build(), CaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    private void showPhoto(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(mFile.getAbsolutePath(), options);
        checkPhoto.setImageBitmap(bitmap);
        if(mFile.exists())Log.d(TAG,"mFile exists.",null);
        else Log.d(TAG,"mFile not exists.",null);
        Toast.makeText(this,"File Path"+mFile.getAbsolutePath(),Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onPause(){
        super.onPause();
        if(mCameraDevice != null){
            closeCamera();
        }
    }

    private static class ImageSaver implements Runnable {
        private final Image mImage;
        private final File mFile;

        public ImageSaver(Image image, File file) {
            mImage = image;
            mFile = file;
            Log.d(TAG,"Store the File at location "+mFile.getAbsolutePath(),null);
        }

        @Override
        public void run() {
            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            FileOutputStream fo = null;
            try {
                fo = new FileOutputStream(mFile);
                fo.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mImage.close();
            }

        }
    }

    public static class CompareSizesByArea implements Comparator<Size> {
        @Override
        public int compare(Size lhs, Size rhs) {
            return lhs.getWidth() * lhs.getHeight() - rhs.getWidth() * rhs.getHeight();
        }
    }

    /*private void cameraCapture(){
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
    }*/

}
