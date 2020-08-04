package com.example.virtuallibrary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.virtuallibrary.R;
import com.example.virtuallibrary.UserUtils;
import com.example.virtuallibrary.adapters.GoalsAdapter;
import com.example.virtuallibrary.databinding.ActivityProfileBinding;
import com.example.virtuallibrary.fragments.GoalsFragment;
import com.example.virtuallibrary.fragments.ProfileGoalsFragment;
import com.example.virtuallibrary.fragments.ProfileResourcesFragment;
import com.example.virtuallibrary.models.Goal;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    public static final String TAG = "ProfileActivity";
    BottomNavigationView bottomNavigation;
    ImageView ivProfPic;
    TextView tvName;
    TextView tvBio;
    ImageButton btnAddFriend;
    ParseUser user;
    boolean areFriends;
    Button btnChangeProf;
    Fragment fragment;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProfileBinding binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        user = (ParseUser) Parcels.unwrap(getIntent().getParcelableExtra(UserUtils.TAG));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_profile);
        Button btnLogOut = actionBar.getCustomView().findViewById(R.id.btnLogout);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                ParseUser.logOut();
                // go to login activity
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        TextView tvUsername = actionBar.getCustomView().findViewById(R.id.tvUsername);
        tvUsername.setText("@" + user.getUsername());

        ivProfPic = binding.ivProfile;
        tvName = binding.tvName;
        tvBio = binding.tvBio;
        btnAddFriend = binding.btnAddFriend;
        btnChangeProf = binding.btnChangeProf;
        bottomNavigation = binding.bottomNavigation;
        btnChangeProf.setEnabled(false);
        btnChangeProf.setVisibility(View.GONE);
        btnLogOut.setEnabled(false);
        btnLogOut.setVisibility(View.GONE);
        if (UserUtils.equals(user, ParseUser.getCurrentUser())) {
            btnAddFriend.setEnabled(false);
            btnAddFriend.setVisibility(View.GONE);
            btnLogOut.setVisibility(View.VISIBLE);
            btnLogOut.setEnabled(true);
            btnChangeProf.setVisibility(View.VISIBLE);
            btnChangeProf.setEnabled(true);
        }
        areFriends = false;

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_goals:
                        fragment = ProfileGoalsFragment.newInstance(user);
                        break;
                    case R.id.action_resources:
                    default:
                        fragment = ProfileResourcesFragment.newInstance(user);
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigation.setSelectedItemId(R.id.action_resources);


        if (UserUtils.userContained(UserUtils.getFriends(ParseUser.getCurrentUser()), user)) {
            btnAddFriend.setImageResource(R.drawable.ic_baseline_person_24);
            areFriends = true;
        }

        ParseFile profile = UserUtils.getProfilePicture(user);
        if (profile != null) {
            Glide.with(this).load(profile.getUrl()).transform(new CircleCrop()).into(ivProfPic);
        } else {
            Glide.with(this).load(R.drawable.ic_baseline_person_24_black).transform(new CircleCrop()).into(ivProfPic);
        }

        String name = UserUtils.getName(user);
        if (name != null) {
            tvName.setText(name);
        } else {
            tvName.setText("");
        }

        String bio = UserUtils.getBio(user);
        if (bio != null) {
            tvBio.setText(bio);
        } else {
            tvBio.setText("");
        }

        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (areFriends) {
                    UserUtils.removeFriend(ParseUser.getCurrentUser(), user);
                    ParseUser.getCurrentUser().saveInBackground();
                    btnAddFriend.setImageResource(R.drawable.ic_baseline_person_add_24);
                }
                else {
                    UserUtils.addFriend(ParseUser.getCurrentUser(), user);
                    ParseUser.getCurrentUser().saveInBackground();
                    btnAddFriend.setImageResource(R.drawable.ic_baseline_person_24);
                }
                areFriends = !areFriends;
            }
        });
    }
}