package com.example.virtuallibrary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.databinding.ActivityMainBinding;
import com.example.virtuallibrary.fragments.CreateGoalFragment;
import com.example.virtuallibrary.fragments.CreateResourceFragment;
import com.example.virtuallibrary.fragments.CreateTableFragment;
import com.example.virtuallibrary.fragments.ProfileFragment;
import com.example.virtuallibrary.fragments.HomeFragment;
import com.example.virtuallibrary.fragments.ProfileGoalsFragment;
import com.example.virtuallibrary.fragments.ProfileResourcesFragment;
import com.example.virtuallibrary.fragments.ResourcesFragment;
import com.example.virtuallibrary.fragments.SearchFragment;
import com.example.virtuallibrary.models.Invite;
import com.example.virtuallibrary.models.Table;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private String selectedFragmentTag = "";
    public String selectedSubFragmentTag = "";

    BottomNavigationView bottomNavigation;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    ImageButton btnAdd;
    Button btnCancel;
    Fragment fragment;
    public List<Table> tables;
    public static List<Invite> invites;

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
        getSupportActionBar().setCustomView(R.layout.actionbar_notification_sort);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.actionProgress:
                        if (selectedFragmentTag.equals(ProfileFragment.TAG)) { return true; }
                        getSupportActionBar().setCustomView(R.layout.actionbar_default);
                        btnAdd.setVisibility(View.VISIBLE);
                        selectedFragmentTag = ProfileFragment.TAG;
                        fragment = new ProfileFragment();
                        break;
                    case R.id.actionPosts:
                        if (selectedFragmentTag.equals(ResourcesFragment.TAG)) { return true; }
                        getSupportActionBar().setCustomView(R.layout.actionbar_default);
                        btnAdd.setVisibility(View.VISIBLE);
                        selectedFragmentTag = ResourcesFragment.TAG;
                        fragment = new ResourcesFragment();
                        break;
                    case R.id.actionSearch:
                        if (selectedFragmentTag.equals(SearchFragment.TAG)) { return true; }
                        getSupportActionBar().setCustomView(R.layout.actionbar_default);
                        selectedFragmentTag = SearchFragment.TAG;
                        fragment = new SearchFragment();
                        btnAdd.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.actionHome:
                    default:
                        if (selectedFragmentTag.equals(HomeFragment.TAG)) { return true; }
                        getSupportActionBar().setCustomView(R.layout.actionbar_notification_sort);
                        btnAdd.setVisibility(View.VISIBLE);
                        selectedFragmentTag = HomeFragment.TAG;
                        fragment =  new HomeFragment();
                        break;
                }
                btnCancel.setVisibility(View.INVISIBLE);
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment, selectedFragmentTag).commit();
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
                // case on each fragment and navigate to the corresponding add table/goal/post fragment
                Log.d(TAG, selectedFragmentTag + selectedSubFragmentTag);
                switch (selectedFragmentTag) {
                    case ProfileFragment.TAG:
                        Log.d(TAG, "hi");
                        switch (selectedSubFragmentTag) {
                            case ProfileGoalsFragment.TAG:
                                fragment = new CreateGoalFragment();
                                selectedFragmentTag = CreateGoalFragment.TAG;
                                break;
                            case ProfileResourcesFragment.TAG:
                            default:
                                Log.d(TAG, "hiii");
                                fragment = new CreateResourceFragment();
                                selectedFragmentTag = CreateGoalFragment.TAG;
                                Log.d(TAG, "hiii" + selectedFragmentTag);
                                break;
                        }
                        break;
                    case ResourcesFragment.TAG:
                        fragment = new CreateResourceFragment();
                        selectedFragmentTag = CreateResourceFragment.TAG;
                        break;
                    case HomeFragment.TAG:
                    default:
                        fragment = new CreateTableFragment();
                        selectedFragmentTag = CreateTableFragment.TAG;
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment, selectedFragmentTag).commit();
                btnAdd.setVisibility(View.INVISIBLE);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAdd.setVisibility(View.VISIBLE);
                // case on each fragment and navigate to the corresponding add table/goal/post fragment
                Log.d(TAG, selectedFragmentTag);
                switch (selectedFragmentTag) {
                    case CreateGoalFragment.TAG:
                        fragment = new ProfileFragment();
                        selectedFragmentTag = ProfileFragment.TAG;
                        break;
                    case CreateResourceFragment.TAG:
                        fragment = new ResourcesFragment();
                        selectedFragmentTag = ResourcesFragment.TAG;
                        break;
                    case CreateTableFragment.TAG:
                    default:
                        fragment = new HomeFragment();
                        selectedFragmentTag = HomeFragment.TAG;
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                btnCancel.setVisibility(View.INVISIBLE);
            }
        });
    }
}