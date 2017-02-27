package com.jm.gon.triphelper;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends Activity {

    private CallbackManager callbackManager;
    private LoginButton LoginActivity_LoginBtn;
    private Button justlogin;


    //로그인 화면입니다. 페이스북로그인과 로그인없이 그냥접속이 있습니다.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this);

        setContentView(R.layout.login);
        final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        justlogin = (Button) findViewById(R.id.LoginActivity_LoginBtnCommon);

        if (Profile.getCurrentProfile() != null) {
            startActivity(intent);
        }

        justlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });


        callbackManager = CallbackManager.Factory.create();
        LoginActivity_LoginBtn = (LoginButton) findViewById(R.id.LoginActivity_LoginBtn);
        LoginActivity_LoginBtn.setReadPermissions(Arrays.asList("public_profile", "email"));
        LoginActivity_LoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        //여기서 정보 보내주면 될듯. 저 밑에 parameters.putstring으로 인자 넣어주었더니 저기서 다 얻어오네. 굿굿
                        Log.i("TAG", "result = " + object.toString());
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
