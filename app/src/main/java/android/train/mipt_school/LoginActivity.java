package android.train.mipt_school;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.train.mipt_school.DataHolders.User;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private Button logInButton;
    private Button registerButton;
    private EditText loginField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logInButton = findViewById(R.id.log_in_button);
        registerButton = findViewById(R.id.register_button);
        loginField = findViewById(R.id.login_field);
        passwordField = findViewById(R.id.password_field);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}
