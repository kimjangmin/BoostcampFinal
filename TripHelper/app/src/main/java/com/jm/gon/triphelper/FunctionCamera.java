package com.jm.gon.triphelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by jm on 2017-02-18.
 */

public class FunctionCamera extends AppCompatActivity implements View.OnTouchListener {

    private static final String TAG ="FunctionCamera";
    private static final int PICK_FROM_CAMERA = 0;


    private Uri photoPath;
    private ImageView iv_FunctionCamera_catpureImage;
    private FrameLayout fl_FunctionCamera_captureLayout;
    private Toolbar t_FunctionCamera_toolbar;
    private TextView tv_FunctionCamera_captureText;
    private EditText et_FunctionCamera_captureEdit;

    private Button bt_FunctionCamera_fontbm;
    private Button bt_FunctionCamera_fontddalgi;
    private Button bt_FunctionCamera_fontmonsori;
    private Button bt_FunctionCamera_fonttvnbold;
    private Button bt_FunctionCamera_fonttvnlight;

    float oldXvalue;
    float oldYvalue;
    Typeface typeface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cameramain);
        iv_FunctionCamera_catpureImage = (ImageView) findViewById(R.id.iv_FunctionCamera_catpureImage);
        tv_FunctionCamera_captureText = (TextView) findViewById(R.id.tv_FunctionCamera_captureText);
        et_FunctionCamera_captureEdit = (EditText) findViewById(R.id.et_FunctionCamera_captureEdit);
        fl_FunctionCamera_captureLayout = (FrameLayout)findViewById(R.id.fl_FunctionCamera_captureLayout);
        t_FunctionCamera_toolbar = (Toolbar)findViewById(R.id.t_FunctionCamera_toolbar);
        bt_FunctionCamera_fontbm = (Button)findViewById(R.id.bt_FunctionCamera_fontbm);
        bt_FunctionCamera_fontddalgi = (Button)findViewById(R.id.bt_FunctionCamera_fontddalgi);
        bt_FunctionCamera_fontmonsori = (Button)findViewById(R.id.bt_FunctionCamera_fontmonsori);
        bt_FunctionCamera_fonttvnbold = (Button)findViewById(R.id.bt_FunctionCamera_fonttvnbold);
        bt_FunctionCamera_fonttvnlight = (Button)findViewById(R.id.bt_FunctionCamera_fonttvnlight);

        bt_FunctionCamera_fontbm.setOnClickListener(fontbmClickListener);
        bt_FunctionCamera_fontddalgi.setOnClickListener(fontddalgiClickListener);
        bt_FunctionCamera_fontmonsori.setOnClickListener(fontmonsoriClickListener);
        bt_FunctionCamera_fonttvnbold.setOnClickListener(fonttvnboldClickListener);
        bt_FunctionCamera_fonttvnlight.setOnClickListener(fonttvnlightClickListener);

        tv_FunctionCamera_captureText.setOnTouchListener(this);
        et_FunctionCamera_captureEdit.isFocused();
        et_FunctionCamera_captureEdit.addTextChangedListener(watchCatpureEdit);

        setSupportActionBar(t_FunctionCamera_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        doTakePhotoAction();

    }
    TextWatcher watchCatpureEdit = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            tv_FunctionCamera_captureText.setText(et_FunctionCamera_captureEdit.getText().toString());
        }
    };

    View.OnClickListener fontbmClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            typeface = Typeface.createFromAsset(getAssets(), "fonts/bm.ttf");
            tv_FunctionCamera_captureText.setTypeface(typeface);
        }
    };
    View.OnClickListener fontddalgiClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            typeface = Typeface.createFromAsset(getAssets(), "fonts/ddalgi.ttf");
            tv_FunctionCamera_captureText.setTypeface(typeface);
        }
    };
    View.OnClickListener fontmonsoriClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            typeface = Typeface.createFromAsset(getAssets(), "fonts/monsori.otf");
            tv_FunctionCamera_captureText.setTypeface(typeface);
        }
    };
    View.OnClickListener fonttvnboldClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            typeface = Typeface.createFromAsset(getAssets(), "fonts/tvn_Bold.ttf");
            tv_FunctionCamera_captureText.setTypeface(typeface);
        }
    };
    View.OnClickListener fonttvnlightClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            typeface = Typeface.createFromAsset(getAssets(), "fonts/tvn_Light.ttf");
            tv_FunctionCamera_captureText.setTypeface(typeface);
        }
    };


    private void doTakePhotoAction()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String url = "tmp_" + String.valueOf(System.currentTimeMillis())+".jpg";
        photoPath = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoPath);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == RESULT_OK && requestCode == PICK_FROM_CAMERA){
            rotationImage(setReduceImageSize());
        }
    }
    private void capture(){

        iv_FunctionCamera_catpureImage.bringToFront();
        tv_FunctionCamera_captureText.bringToFront();
        fl_FunctionCamera_captureLayout.setDrawingCacheEnabled(true);
        fl_FunctionCamera_captureLayout.buildDrawingCache(true);

        Bitmap capture = Bitmap.createBitmap(fl_FunctionCamera_captureLayout.getMeasuredWidth(), fl_FunctionCamera_captureLayout.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(capture);
        fl_FunctionCamera_captureLayout.draw(canvas);
        String savePath = Environment.getExternalStorageDirectory()+"/triphelper";
        File file = new File(savePath);
        boolean result =true;
        if(!file.exists()) {
            result = file.mkdir();
            if(file.getParent()!=null){
                Log.i("TAG","file path = "+file.getPath());
            }
        }
        if(!result){
            Log.i("TAG","fail");
            return;
        }

        savePath += "/"+ "tmp+"+String.valueOf(System.currentTimeMillis())+".jpg";

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(savePath);
            capture.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fl_FunctionCamera_captureLayout.setDrawingCacheEnabled(false);
        } catch (FileNotFoundException e) {
            Log.d("TAG",e.getMessage());
        }finally {
            try{
                if(fileOutputStream != null){
                    fileOutputStream.close();
                }
            }catch (IOException e){
                Log.d("TAG",e.getMessage());
            }
        }
    }
    private void rotationImage(Bitmap bitmap){
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(photoPath.getPath());
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
        iv_FunctionCamera_catpureImage.setImageBitmap(rotateBitmap);

    }

    private Bitmap setReduceImageSize(){
        int targetImageViewWidth = iv_FunctionCamera_catpureImage.getWidth();
        int targetImageViewHeight = iv_FunctionCamera_catpureImage.getHeight();

        BitmapFactory.Options bitOptions = new BitmapFactory.Options();
        bitOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath.getPath(), bitOptions);
        bitOptions.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(photoPath.getPath(), bitOptions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, FunctionCamera.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.functionCamera_save:
                capture();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
}
