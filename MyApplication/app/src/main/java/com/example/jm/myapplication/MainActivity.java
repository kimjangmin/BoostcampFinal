package com.example.jm.myapplication;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import static android.R.attr.data;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private List<String> permissionNeeds = Arrays.asList("email");
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);
        if(Profile.getCurrentProfile()!=null){
            Log.d("TAG","이름 = "+Profile.getCurrentProfile().getFirstName()+Profile.getCurrentProfile().getLastName());
            Log.i("TAG","id = "+Profile.getCurrentProfile().getId());
            Log.i("TAG","something = "+Profile.getCurrentProfile().getName());
        }

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Tag", "로그인이 성공해부렀네?");
            }

            @Override
            public void onCancel() {
                Log.d("Tag", "로그인 하려다 맘");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Tag", "좆망 : " + error.getLocalizedMessage());
            }
        });
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, permissionNeeds);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

