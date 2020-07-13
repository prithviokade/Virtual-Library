package com.example.virtuallibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.virtuallibrary.MainActivity;
import com.example.virtuallibrary.R;
import com.example.virtuallibrary.SignUpActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    EditText etUsername;
    EditText etPassword;
    Button btnLogin;
    TextView tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }
        getSupportActionBar().hide();

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!(etUsername.getText().toString().isEmpty()) && !(etPassword.getText().toString().isEmpty())) {
                    // set login button color and enable
                    Log.d(TAG, "detected");
                    btnLogin.setBackgroundColor(0xFF55BD7E);
                    btnLogin.setTextColor(0xffffffff);
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                    btnLogin.setBackgroundColor(0x4A038132);
                    btnLogin.setTextColor(0xFFEBF4FB);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!(etUsername.getText().toString().isEmpty()) && !(etPassword.getText().toString().isEmpty())) {
                    // set login button color and enable
                    Log.d(TAG, "detected");
                    btnLogin.setEnabled(true);
                    btnLogin.setBackgroundColor(0xFF55BD7E);
                    btnLogin.setTextColor(0xffffffff);
                } else {
                    btnLogin.setEnabled(false);
                    btnLogin.setBackgroundColor(0x4A038132);
                    btnLogin.setTextColor(0xFFEBF4FB);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });
    }

    // Navigate to MainActivity if user logged in correctly
    private void loginUser(String username, String password) {
        // logInInBackground runs on background thread
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while logging in", e);
                    return;
                }
                goMainActivity();
            }
        });
    }

    private void goMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}