package com.example.virtuallibrary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.fragments.CreateGoalFragment;
import com.example.virtuallibrary.fragments.CreatePostFragment;
import com.example.virtuallibrary.fragments.CreateTableFragment;
import com.example.virtuallibrary.fragments.GoalsFragment;
import com.example.virtuallibrary.fragments.HomeFragment;
import com.example.virtuallibrary.fragments.PostsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public String fragTag = "HomeFragment";

    BottomNavigationView bottomNavigation;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    ImageButton btnAdd;
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setVisibility(View.INVISIBLE);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        // getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        // getSupportActionBar().setCustomView(R.layout.actionbar);
        // getSupportActionBar().setTitle("");

        // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.actionHome:
                    default:
                        fragTag = "HomeFragment";
                        fragment =  new HomeFragment();
                        break;
                    case R.id.actionProgress:
                        fragTag = "GoalsFragment";
                        fragment = new GoalsFragment();
                        break;
                    case R.id.actionPosts:
                        fragTag = "PostsFragment";
                        // getSupportActionBar().hide();
                        fragment = new PostsFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigation.setSelectedItemId(R.id.actionHome);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCancel.setVisibility(View.VISIBLE);
                Fragment fragment;
                // case on each fragment and navigate to the corresponding add table/goal/post fragment
                switch (fragTag) {
                    case "HomeFragment":
                    default:
                        fragment = new CreateTableFragment();
                        break;
                    case "GoalsFragment":
                        fragment = new CreateGoalFragment();
                        break;
                    case "PostsFragment":
                        fragment = new CreatePostFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                btnAdd.setVisibility(View.INVISIBLE);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAdd.setVisibility(View.VISIBLE);
                Fragment fragment;
                // case on each fragment and navigate to the corresponding add table/goal/post fragment
                switch (fragTag) {
                    case "HomeFragment":
                    default:
                        fragment = new HomeFragment();
                        break;
                    case "GoalsFragment":
                        fragment = new GoalsFragment();
                        break;
                    case "PostsFragment":
                        btnCancel.setText("Close");
                        fragment = new PostsFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                btnCancel.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void goLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}