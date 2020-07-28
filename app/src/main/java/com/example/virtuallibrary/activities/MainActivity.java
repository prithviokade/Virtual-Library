package com.example.virtuallibrary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.databinding.ActivityMainBinding;
import com.example.virtuallibrary.fragments.CreateGoalFragment;
import com.example.virtuallibrary.fragments.CreateResourceFragment;
import com.example.virtuallibrary.fragments.CreateTableFragment;
import com.example.virtuallibrary.fragments.GoalsFragment;
import com.example.virtuallibrary.fragments.HomeFragment;
import com.example.virtuallibrary.fragments.ResourcesFragment;
import com.example.virtuallibrary.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private String selectedFragmentTag = HomeFragment.TAG;

    BottomNavigationView bottomNavigation;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    ImageButton btnAdd;
    Button btnCancel;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        btnAdd = binding.btnAdd;
        btnCancel = binding.btnCancel;
        bottomNavigation = binding.bottomNavigation;
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_notification);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.actionProgress:
                        getSupportActionBar().setCustomView(R.layout.actionbar_default);
                        btnAdd.setVisibility(View.VISIBLE);
                        selectedFragmentTag = GoalsFragment.TAG;
                        fragment = new GoalsFragment();
                        break;
                    case R.id.actionPosts:
                        getSupportActionBar().setCustomView(R.layout.actionbar_default);
                        btnAdd.setVisibility(View.VISIBLE);
                        selectedFragmentTag = ResourcesFragment.TAG;
                        fragment = new ResourcesFragment();
                        break;
                    case R.id.actionSearch:
                        getSupportActionBar().setCustomView(R.layout.actionbar_default);
                        selectedFragmentTag = SearchFragment.TAG;
                        fragment = new SearchFragment();
                        btnAdd.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.actionHome:
                    default:
                        getSupportActionBar().setCustomView(R.layout.actionbar_notification);
                        btnAdd.setVisibility(View.VISIBLE);
                        selectedFragmentTag = HomeFragment.TAG;
                        fragment =  new HomeFragment();
                        break;
                }
                btnCancel.setVisibility(View.INVISIBLE);
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigation.setSelectedItemId(R.id.actionHome);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setCustomView(R.layout.actionbar_default);
                btnCancel.setVisibility(View.VISIBLE);
                Fragment fragment;
                // case on each fragment and navigate to the corresponding add table/goal/post fragment
                switch (selectedFragmentTag) {
                    case GoalsFragment.TAG:
                        fragment = new CreateGoalFragment();
                        break;
                    case ResourcesFragment.TAG:
                        fragment = new CreateResourceFragment();
                        break;
                    case HomeFragment.TAG:
                    default:
                        fragment = new CreateTableFragment();
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
                switch (selectedFragmentTag) {
                    case HomeFragment.TAG:
                    default:
                        fragment = new HomeFragment();
                        break;
                    case GoalsFragment.TAG:
                        fragment = new GoalsFragment();
                        break;
                    case ResourcesFragment.TAG:
                        fragment = new ResourcesFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                btnCancel.setVisibility(View.INVISIBLE);
            }
        });
    }
}