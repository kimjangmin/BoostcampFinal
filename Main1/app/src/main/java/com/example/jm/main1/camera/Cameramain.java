package com.example.jm.main1.camera;

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
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.example.jm.main1.R;

import java.io.File;
import java.io.IOException;

public class Cameramain extends AppCompatActivity implements View.OnTouchListener{

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int MY_PERMISSION_REQUEST_STORAGE=10;
    private static final int MY_PERMISSION_REQUEST_CAMERA=11;

    private boolean CAMERA_PERMISSION=false;
    private boolean STORAGE_PERMISSION=false;

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
        mPhotoImageView = (ImageView)findViewById(R.id.IV_image);
        doTakePhotoAction();
        tv = (TextView)findViewById(R.id.fl_tv);
        tv.setOnTouchListener(this);
        et = (EditText)findViewById(R.id.fl_et);
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
        btn_ddalgi = (Button)findViewById(R.id.ddalgi);
        typeface = Typeface.createFromAsset(getAssets(),"fonts/Ddalgi.ttf");
        btn_ddalgi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setTypeface(typeface);
            }
        });

    }
    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkPermissionCamera(){
        Log.i("TAG","checkPermissionCamera intro");
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            Log.i("TAG","checkPermissionCamera not granted");
            if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                Toast.makeText(this,"camera",Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},MY_PERMISSION_REQUEST_CAMERA);
        }else{
            Log.i("TAG","cehckPermissionCamera Already GRANTED");
            CAMERA_PERMISSION = true;
            return CAMERA_PERMISSION;
        }
        return CAMERA_PERMISSION;
    }
    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkPermissionStorage() {
        Log.i("TAG","checkPermissionStorage intro");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
            || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            Log.i("TAG","checkPermissionStorage not granted");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
            }

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSION_REQUEST_STORAGE);
        } else {
            Log.i("TAG","cehckPermissionStorage Already GRANTED");
            STORAGE_PERMISSION = true;
            return STORAGE_PERMISSION;
        }
        return STORAGE_PERMISSION;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Log.i("TAG", "Permission always deny");
                }
                break;
            case MY_PERMISSION_REQUEST_CAMERA:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    CAMERA_PERMISSION = true;
                }else{
                    CAMERA_PERMISSION = false;
                    Log.i("TAG","Camera Permission deny");
                }
                break;
        }
    }
    private void doTakePhotoAction()
    {
        Log.i("TAG","doTakePhotoAction");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(checkPermissionCamera() && checkPermissionStorage()){
            // 임시로 사용할 파일의 경로를 생성
            String url = "tmp_" + String.valueOf(System.currentTimeMillis())+".jpg";
            File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
           /* try {
                String path = File.createTempFile(url,".jpg", storageDirectory).getAbsolutePath();
                Log.i("TAG","File path = "+path);
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
            Log.i("TAG","ImageCaptureUriString = "+mImageCaptureUri.getPath());
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            // 특정기기에서 사진을 저장못하는 문제가 있어 다음을 주석처리 합니다.
            //intent.putExtra("return-data", true);
            Log.i("TAG","doTakePhotoAction startActivityForResult");
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }

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
            case CROP_FROM_CAMERA:
            {
                // 크롭이 된 이후의 이미지를 넘겨 받습니다.
                // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
                // 임시 파일을 삭제합니다.
                final Bundle extras = data.getExtras();

                if(extras != null)
                {
                    Bitmap photo = extras.getParcelable("data");
                    mPhotoImageView.setImageBitmap(photo);
                }

                // 임시 파일 삭제
                File f = new File(mImageCaptureUri.getPath());
                if(f.exists())
                {
                    f.delete();
                }
                break;
            }

            case PICK_FROM_ALBUM:
            {
                // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.
                // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.

                mImageCaptureUri = data.getData();
            }

            case PICK_FROM_CAMERA:
            {
                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.

                Log.i("TAG","PICK_FROM_CAMERA");
                /*Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                intent.putExtra("outputX", mPhotoImageView.getWidth());
                intent.putExtra("outputY", mPhotoImageView.getHeight());
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_CAMERA);*/
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
        int cameraImageWidth = bitOptions.outWidth;
        int cameraImageHeight = bitOptions.outHeight;

       /* int scaleFactor = Math.min(cameraImageWidth/targetImageViewWidth, cameraImageHeight/targetImageViewHeight);
        bitOptions.inSampleSize = scaleFactor;*/
        bitOptions.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(mImageCaptureUri.getPath(), bitOptions);
    }
}
