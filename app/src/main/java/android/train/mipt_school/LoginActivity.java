package android.train.mipt_school;

import android.content.Intent;
import android.os.Handler;
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

    private Button logInButton;
    private Button registerButton;
    private EditText loginField;
    private EditText passwordField;
    private String returnedString = null;
    private ResponseCallback rc = null;
    private String error = null;
    private String firstName = null;
    private String lastName = null;
    private String thirdName = null;
    private String userName = null;
    private String email = null;
    private long userId;
    private long groupId;

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
                    s = "{\"bad_request\":\"Ошибка сервера\"}";
                rc.call(s);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String s = "{\"bad_request\":\"Проверьте соединение с интернетом\"}";
                rc.call(s);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logInButton = findViewById(R.id.log_in_button);
        registerButton = findViewById(R.id.register_button);
        loginField = findViewById(R.id.login_field);
        passwordField = findViewById(R.id.password_field);

        rc = new ResponseCallback() {
            @Override
            public void call(String s) {
                returnedString = s;
            }
        };

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = loginField.getText().toString();
                String password = passwordField.getText().toString();
                if (userName.equals("admin") && password.equals("admin")) { // временный вход
                    User.getInstance().logIn("firstName", "lastName", "thirdName",
                            "emial@gmail.com", "userName", 0, 0);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    error = firstName = lastName = thirdName = email = null;
                    userRequest(userName, password);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            try {
                                if (returnedString == null) {
                                    error = "Проверьте соединение с интернетом";
                                } else {
                                    JSONObject jsonObject = new JSONObject(returnedString);
                                    if (jsonObject.has("bad_request"))
                                        error = jsonObject.getString("bad_request");
                                    else {
                                        JSONObject data = jsonObject.getJSONObject("data");
                                        firstName = data.getString("firstname");
                                        lastName = data.getString("lastname");
                                        thirdName = data.getString("thirdname");
                                        email = data.getString("email");
                                        userId = data.getLong("id");
                                        //groupId = data.getLong("groudid"); не добавлено еще
                                        groupId = 0;
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (error != null) {
                                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                            } else {
                                User.getInstance().logIn(firstName, lastName,
                                        thirdName, email, userName, userId, groupId);
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        }
                    }, 1000);
                }
            }
        });
    }
}
