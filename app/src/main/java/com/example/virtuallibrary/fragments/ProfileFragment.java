package com.example.virtuallibrary.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.virtuallibrary.R;
import com.example.virtuallibrary.UserUtils;
import com.example.virtuallibrary.activities.LoginActivity;
import com.example.virtuallibrary.activities.MainActivity;
import com.example.virtuallibrary.activities.ProfileActivity;
import com.example.virtuallibrary.adapters.GoalsAdapter;
import com.example.virtuallibrary.databinding.FragmentProfileBinding;
import com.example.virtuallibrary.models.Goal;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;

    public static final String TAG = "GoalsFragment";
    ImageView ivProfPic;
    TextView tvUsername;
    TextView tvName;
    TextView tvBio;
    List<Goal> goals;
    BottomNavigationView bottomNavigation;
    Fragment fragment;
    FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        fragmentManager = getChildFragmentManager();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivProfPic = binding.ivProfile;
        tvUsername = binding.tvScreenName;
        tvName = binding.tvName;
        tvBio = binding.tvBio;
        bottomNavigation = binding.bottomNavigation;
        goals = new ArrayList<>();

        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar_profile);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        Button btnLogOut = actionBar.getCustomView().findViewById(R.id.btnLogout);
        TextView tvUsername = actionBar.getCustomView().findViewById(R.id.tvUsername);
        tvUsername.setText("@" + UserUtils.getUsername(ParseUser.getCurrentUser()));


        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_goals:
                        fragment = ProfileGoalsFragment.newInstance(ParseUser.getCurrentUser());
                        break;
                    case R.id.action_resources:
                    default:
                        fragment = ProfileResourcesFragment.newInstance(ParseUser.getCurrentUser());
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigation.setSelectedItemId(R.id.action_resources);


        ParseFile profile = UserUtils.getProfilePicture(ParseUser.getCurrentUser());
        if (profile != null) {
            Glide.with(getContext()).load(profile.getUrl()).transform(new CircleCrop()).into(ivProfPic);
        } else {
            Glide.with(getContext()).load(R.drawable.ic_baseline_person_24_black).transform(new CircleCrop()).into(ivProfPic);
        }

        String name = UserUtils.getName(ParseUser.getCurrentUser());
        if (name != null) {
            tvName.setText(name);
        } else {
            tvName.setText("");
        }

        String bio = UserUtils.getBio(ParseUser.getCurrentUser());
        if (bio != null) {
            tvBio.setText(bio);
        } else {
            tvBio.setText("");
        }

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                ParseUser.logOut();
                // go to login activity
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

}