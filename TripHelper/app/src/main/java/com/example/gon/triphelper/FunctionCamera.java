package com.example.gon.triphelper;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by jm on 2017-02-18.
 */

public class FunctionCamera extends AppCompatActivity implements View.OnTouchListener {

    private static final int PICK_FROM_CAMERA = 0;
    private static final int MY_PERMISSION_REQUEST_STORAGE = 10;
    private static final int MY_PERMISSION_REQUEST_CAMERA = 11;

    private boolean CAMERA_PERMISSION = false;
    private boolean STORAGE_PERMISSION = false;

    private Uri mImageCaptureUri;
    private ImageView mPhotoImageView;
    private Button mButton;
    TextView tv;
    EditText et;
    float oldXvalue;
    float oldYvalue;

    Typeface typeface;
    Button btn_ddalgi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cameramain);
        mPhotoImageView = (ImageView) findViewById(R.id.IV_image);
        doTakePhotoAction();
        tv = (TextView) findViewById(R.id.fl_tv);
        tv.setOnTouchListener(this);
        et = (EditText) findViewById(R.id.fl_et);
        et.isFocused();
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                et.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv.setText(et.getText().toString());

            }
        });
        btn_ddalgi = (Button) findViewById(R.id.ddalgi);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/Ddalgi.ttf");
        btn_ddalgi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setTypeface(typeface);
            }
        });

    }
    private void doTakePhotoAction()
    {
        Log.i("TAG","doTakePhotoAction");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 임시로 사용할 파일의 경로를 생성
        String url = "tmp_" + String.valueOf(System.currentTimeMillis())+".jpg";
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
        Log.i("TAG","ImageCaptureUriString = "+mImageCaptureUri.getPath());
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        Log.i("TAG","doTakePhotoAction startActivityForResult");
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode != RESULT_OK)
        {
            return;
        }

        switch(requestCode)
        {
            case PICK_FROM_CAMERA:
            {
                Log.i("TAG","PICK_FROM_CAMERA");
                rotationImage(setReduceImageSize());
                break;
            }
        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int width = ((ViewGroup) v.getParent()).getWidth() - v.getWidth();
        int height = ((ViewGroup) v.getParent()).getHeight() - v.getHeight();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            oldXvalue = event.getX();
            oldYvalue = event.getY();
            Log.i("Tag1", "Action Down rX " + event.getRawX() + "," + event.getRawY());
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            v.setX(event.getRawX() - oldXvalue);
            v.setY(event.getRawY() - (oldYvalue + v.getHeight()));
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (v.getX() > width && v.getY() > height) {
                v.setX(width);
                v.setY(height);
            } else if (v.getX() < 0 && v.getY() > height) {
                v.setX(0);
                v.setY(height);
            } else if (v.getX() > width && v.getY() < 0) {
                v.setX(width);
                v.setY(0);
            } else if (v.getX() < 0 && v.getY() < 0) {
                v.setX(0);
                v.setY(0);
            } else if (v.getX() < 0 || v.getX() > width) {
                if (v.getX() < 0) {
                    v.setX(0);
                    v.setY(event.getRawY() - oldYvalue - v.getHeight());
                } else {
                    v.setX(width);
                    v.setY(event.getRawY() - oldYvalue - v.getHeight());
                }
            } else if (v.getY() < 0 || v.getY() > height) {
                if (v.getY() < 0) {
                    v.setX(event.getRawX() - oldXvalue);
                    v.setY(0);
                } else {
                    v.setX(event.getRawX() - oldXvalue);
                    v.setY(height);
                }
            }
        }
        return true;
    }
    private void rotationImage(Bitmap bitmap){
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(mImageCaptureUri.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch ( orientation){
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
        }
        Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        mPhotoImageView.setImageBitmap(rotateBitmap);

    }

    private Bitmap setReduceImageSize(){
        int targetImageViewWidth = mPhotoImageView.getWidth();
        Log.i("TAG","targetImageViewWidth = "+targetImageViewWidth);
        int targetImageViewHeight = mPhotoImageView.getHeight();
        Log.i("TAG","targetImageViewHeight = "+targetImageViewHeight);

        BitmapFactory.Options bitOptions = new BitmapFactory.Options();
        bitOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mImageCaptureUri.getPath(), bitOptions);
        bitOptions.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(mImageCaptureUri.getPath(), bitOptions);
    }
}
