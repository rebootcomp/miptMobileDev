package android.train.mipt_school;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.train.mipt_school.DataHolders.User;
import android.util.Log;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences mSP;

    private String userName = null;
    private String password = null;
    private String deviceToken = "";

    public ResponseCallback responseCallback;
    public ResponseCallback initCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        mSP = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String logInCondition = mSP.getString("signed", "");


        if(logInCondition.equals("true")){

            userName = mSP.getString("login","");
            password = mSP.getString("pass","");
            deviceToken = mSP.getString("token", "");
            initCallback = new ResponseCallback() {

                @Override
                public void onResponse(String data) {
                    if (User.getInstance().init(data)) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } else{
                        Toast.makeText(SplashActivity.this,
                                "Что-то пошло не так", Toast.LENGTH_LONG).show();
                        toLoginActivity();
                    }
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(SplashActivity.this, message, Toast.LENGTH_LONG).show();
                    toLoginActivity();
                }
            };


            responseCallback = new ResponseCallback() {
                @Override
                public void onResponse(String data) {
                    if (User.getInstance().updateToken(data)) {
                        User.getInstance().scheduleRequest(initCallback);
                    } else {
                        Toast.makeText(SplashActivity.this,
                                "Что-то пошло не так", Toast.LENGTH_LONG).show();
                        toLoginActivity();
                    }

                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(SplashActivity.this, message, Toast.LENGTH_LONG).show();
                    toLoginActivity();
                }
            };
            Log.d("DATA_DEBUG", userName + " " + password + " " + deviceToken);
            User.getInstance().logIn(userName, password, deviceToken, responseCallback);
        }

        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }, 2500);
        }

    }

    private void toLoginActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, 1000);
    }
}
