package android.train.mipt_school;

import android.content.Intent;
import android.os.Handler;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    String ans = "-1";
    String er = "-1";
    String firstname = "-1";
    String lastname = "-1";
    Integer id = -1;
    ResponseCallback rc;

    public interface ResponseCallback {
        void call(String s);
    }

    public void userRequest(String username, String password) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .getmdluser(username, password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                if (response.code() == 200) {
                    try {
                        s = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else
                    s = "error1";
                rc.call(s);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String s = "error2";
                rc.call(s);
            }
        });
    }


    private Button logInButton;
    private Button registerButton;
    private EditText loginField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rc = new ResponseCallback() {
            @Override
            public void call(String s) {
                ans = s;
            }
        };

        logInButton = findViewById(R.id.log_in_button);
        registerButton = findViewById(R.id.register_button);
        loginField = findViewById(R.id.login_field);
        passwordField = findViewById(R.id.password_field);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = loginField.getText().toString();
                String password = passwordField.getText().toString();
                ans = er = firstname = lastname = "-1";
                id = -1;
                userRequest(username, password);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        //Toast.makeText(LoginActivity.this, ans, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(ans);
                            if (jsonObject.length() == 1)
                                er = jsonObject.getString("bad_request");
                            else {
                                firstname = jsonObject.getString("firstname");
                                lastname = jsonObject.getString("lastname");
                                id = jsonObject.getInt("id");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (id != -1)
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        else
                            Toast.makeText(LoginActivity.this, er, Toast.LENGTH_LONG).show();
                    }
                }, 1000);
//                Toast.makeText(LoginActivity.this, id.toString(), Toast.LENGTH_LONG).show();

            }
        });
    }
}
