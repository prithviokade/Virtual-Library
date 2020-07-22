package com.example.virtuallibrary.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.activities.MainActivity;
import com.example.virtuallibrary.databinding.ActivitySignUpBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

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
        ActivitySignUpBinding binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        getSupportActionBar().hide();
        etPhone = binding.etPhone;
        etEmail = binding.etEmail;
        etUsername = binding.etUsername;
        etPassword = binding.etPassword;
        btnSignUp = binding.btnSignUp;
        tvLogin = binding.tvLogin;

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                String phone = etPhone.getText().toString();
                String email = etEmail.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (!phone.isEmpty() && !email.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                    btnSignUp.setEnabled(true);
                    btnSignUp.setBackgroundColor(ContextCompat.getColor(SignUpActivity.this, R.color.vlGreen));
                    btnSignUp.setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.plainWhite));
                } else {
                    btnSignUp.setEnabled(false);
                    btnSignUp.setBackgroundColor(ContextCompat.getColor(SignUpActivity.this, R.color.lightGreen));
                    btnSignUp.setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.lightGrey));
                }
            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                String phone = etPhone.getText().toString();
                String email = etEmail.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (!phone.isEmpty() && !email.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                    btnSignUp.setEnabled(true);
                    btnSignUp.setBackgroundColor(ContextCompat.getColor(SignUpActivity.this, R.color.vlGreen));
                    btnSignUp.setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.plainWhite));
                } else {
                    btnSignUp.setEnabled(false);
                    btnSignUp.setBackgroundColor(ContextCompat.getColor(SignUpActivity.this, R.color.lightGreen));
                    btnSignUp.setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.lightGrey));
                }
            }
        });

        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                String phone = etPhone.getText().toString();
                String email = etEmail.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (!phone.isEmpty() && !email.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                    btnSignUp.setEnabled(true);
                    btnSignUp.setBackgroundColor(ContextCompat.getColor(SignUpActivity.this, R.color.vlGreen));
                    btnSignUp.setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.plainWhite));
                } else {
                    btnSignUp.setEnabled(false);
                    btnSignUp.setBackgroundColor(ContextCompat.getColor(SignUpActivity.this, R.color.lightGreen));
                    btnSignUp.setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.lightGrey));
                }
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                String phone = etPhone.getText().toString();
                String email = etEmail.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (!phone.isEmpty() && !email.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                    btnSignUp.setEnabled(true);
                    btnSignUp.setBackgroundColor(ContextCompat.getColor(SignUpActivity.this, R.color.vlGreen));
                    btnSignUp.setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.plainWhite));
                } else {
                    btnSignUp.setEnabled(false);
                    btnSignUp.setBackgroundColor(ContextCompat.getColor(SignUpActivity.this, R.color.lightGreen));
                    btnSignUp.setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.lightGrey));
                }
            }
        });

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