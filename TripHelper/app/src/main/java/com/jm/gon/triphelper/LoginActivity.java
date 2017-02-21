package com.jm.gon.triphelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton LoginActivity_LoginBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this);

        setContentView(R.layout.activity_login);
        final Intent intent = new Intent(getApplicationContext(), MainActivity.class);


        if(Profile.getCurrentProfile()!=null){
            Log.d("TAG","이름 = "+Profile.getCurrentProfile().getFirstName()+Profile.getCurrentProfile().getLastName());
            Log.i("TAG","id = "+Profile.getCurrentProfile().getId());
            Log.i("TAG","something = "+Profile.getCurrentProfile().getName());
            startActivity(intent);
        }


        callbackManager = CallbackManager.Factory.create();
        LoginActivity_LoginBtn = (LoginButton)findViewById(R.id.LoginActivity_LoginBtn);
        LoginActivity_LoginBtn.setReadPermissions(Arrays.asList("public_profile","email"));
        LoginActivity_LoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        //여기서 정보 보내주면 될듯. 저 밑에 parameters.putstring으로 인자 넣어주었더니 저기서 다 얻어오네. 굿굿
                        Log.i("TAG","result = "+object.toString());
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields","id,name,email,gender,birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
                startActivity(intent);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        /*
        커스텀으로 버튼을 만들어서 사용할 때
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
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
