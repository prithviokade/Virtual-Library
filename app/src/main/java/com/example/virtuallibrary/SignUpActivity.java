package com.example.virtuallibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.virtuallibrary.MainActivity;
import com.example.virtuallibrary.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.w3c.dom.Text;

public class SignUpActivity extends AppCompatActivity {

    EditText etPhone;
    EditText etEmail;
    EditText etUsername;
    EditText etPassword;
    Button btnSignUp;
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvLogin = findViewById(R.id.tvLogin);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the ParseUser
                ParseUser user = new ParseUser();
                // Set properties
                user.setUsername(etUsername.getText().toString());
                user.setPassword(etPassword.getText().toString());
                String email = etEmail.getText().toString();
                if (email != null) {
                    user.setEmail(email);
                }
                String phone = etPhone.getText().toString();
                if (phone != null) {
                    user.put("phone", phone);
                }
                // Invoke signUpInBackground
                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            goMainActivity();
                        } else {
                            Log.e("SignUpActivity", "Error while signing up", e);
                            return;
                        }
                    }
                });
            }
        });
    }

    private void goMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}