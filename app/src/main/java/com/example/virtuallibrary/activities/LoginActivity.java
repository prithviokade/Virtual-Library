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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.virtuallibrary.GetUserCallback;
import com.example.virtuallibrary.R;
import com.example.virtuallibrary.UserRequest;
import com.example.virtuallibrary.databinding.ActivityLoginBinding;
import com.example.virtuallibrary.models.User;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.facebook.ParseFacebookUtils;

import java.util.Arrays;
import java.util.Collection;

public class LoginActivity extends AppCompatActivity implements GetUserCallback.IGetUserResponse {

    public static final String TAG = "LoginActivity";
    EditText etUsername;
    EditText etPassword;
    Button btnLogin;
    TextView tvSignUp;
    ImageView ivLogo;
    private static final String EMAIL = "email";
    private static final String USER_POSTS = "user_posts";
    private static final String AUTH_TYPE = "rerequest";
    public static String LOGIN_STATUS;

    private CallbackManager mCallbackManager;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    private void goLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserRequest.makeUserRequest(new GetUserCallback(LoginActivity.this).getCallback());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ivLogo = binding.ivLogo;
        ivLogo.setImageResource(R.drawable.logoname);

        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }
        getSupportActionBar().hide();

        mCallbackManager = CallbackManager.Factory.create();

        ImageButton mLoginButton = binding.loginButton;

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logInWithFacebook(null);
            }
        });

        // Set the initial permissions to request from the user while logging in
        // mLoginButton.setPermissions(Arrays.asList(EMAIL, USER_POSTS));

        // mLoginButton.setAuthType(AUTH_TYPE);

        // Register a callback to respond to the user
        /* mLoginButton.registerCallback(
                mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        setResult(RESULT_OK);
                        Log.d(TAG, "SUCCESS");
                    }

                    @Override
                    public void onCancel() {
                        setResult(RESULT_CANCELED);
                        finish();
                    }

                    @Override
                    public void onError(FacebookException e) { }
                });

         */


        etUsername = binding.etUsername;
        etPassword = binding.etPassword;
        btnLogin = binding.btnLogin;
        tvSignUp = binding.tvSignUp;

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
                    btnLogin.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.vlGreen));
                    btnLogin.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.plainWhite));
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                    btnLogin.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.lightGreen));
                    btnLogin.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.lightGrey));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            // @SuppressLint("ResourceAsColor")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!(etUsername.getText().toString().isEmpty()) && !(etPassword.getText().toString().isEmpty())) {
                    // set login button color and enable
                    btnLogin.setEnabled(true);
                    btnLogin.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.vlGreen));
                    btnLogin.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.plainWhite));
                } else {
                    btnLogin.setEnabled(false);
                    btnLogin.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.lightGreen));
                    btnLogin.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.lightGrey));
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
                    return;
                }
                LOGIN_STATUS = "Success";
                goMainActivity();
            }
        });
    }

    private void goMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCompleted(User user) {
        Log.d(TAG, "onCompletedCalled");
        String username;
        String email = null;
        if (user.getEmail() == null) {
            username = user.getName();
        } else {
            email = user.getEmail();
            username = email.split("@")[0];
        }

        loginUser(username, user.getId());
        if (LOGIN_STATUS == null) {
            Log.d(TAG, "signedUpUSER");
            // Create the ParseUser
            ParseUser newUser = new ParseUser();
            // Set properties
            newUser.setUsername(username);
            newUser.setPassword(user.getId());
            if (email != null) {
                newUser.setEmail(email);
            }
            // Invoke signUpInBackground
            newUser.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        goMainActivity();
                    } else {
                        return;
                    }
                }
            });
        }

    }

    public void logInWithFacebook(Collection<String> permissions){
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(final ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else {
                    goMainActivity();
                }
            }
        });
    }
}