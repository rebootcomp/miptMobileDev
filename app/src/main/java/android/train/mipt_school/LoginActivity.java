package android.train.mipt_school;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.train.mipt_school.DataHolders.User;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    SharedPreferences mSP;
    private Button logInButton;
    private Button registerButton;
    private EditText loginField;
    private EditText passwordField;
    private String userName = null;
    private String password = null;

    public ResponseCallback responseCallback;
    public ResponseCallback initCallback;
    public ResponseCallback allusersCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSP = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String logInCondition = mSP.getString("signed", "");
        if(logInCondition.equals("true"))startActivity(new Intent(getApplicationContext(), MainActivity.class));

        logInButton = findViewById(R.id.log_in_button);
        registerButton = findViewById(R.id.register_button);
        loginField = findViewById(R.id.login_field);
        passwordField = findViewById(R.id.password_field);

        loginField.setText("alexmuratidi");
        passwordField.setText("Alex17112001#");

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = loginField.getText().toString();
                password = passwordField.getText().toString();
                initCallback = new ResponseCallback() {

                    @Override
                    public void onResponse(String data) {
                        if (User.getInstance().init(data))
                            User.getInstance().allUsersRequest(allusersCallback);
                        else
                            Toast.makeText(LoginActivity.this,
                                    "Что-то пошло не так", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                };
                allusersCallback = new ResponseCallback() {
                    @Override
                    public void onResponse(String data) {
                        if (User.getInstance().updateAllUsers(data)) {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Что-то пошло не так", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                };
                responseCallback = new ResponseCallback() {
                    @Override
                    public void onResponse(String data) {
                        if (User.getInstance().updateToken(data))
                            User.getInstance().scheduleRequest(initCallback);
                        else
                            Toast.makeText(LoginActivity.this,
                                    "Что-то пошло не так", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                };
                User.getInstance().logIn(userName, password, responseCallback);
//                Toast.makeText(getApplicationContext(), userName + " " + password, Toast.LENGTH_LONG).show();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTabsIntent intent = new CustomTabsIntent.Builder().build();

                intent.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                final String url = "https://it-edu.com/el/login/signup.php";
                intent.launchUrl(getApplicationContext(), Uri.parse(url));
            }
        });
    }
}

