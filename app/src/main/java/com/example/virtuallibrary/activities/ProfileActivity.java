package com.example.virtuallibrary.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
import com.example.virtuallibrary.databinding.FragmentGoalsBinding;
import com.example.virtuallibrary.models.Goal;
import com.example.virtuallibrary.models.Table;
import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    public static final String TAG = "ProfileActivity";
    ImageView ivProfPic;
    TextView tvUsername;
    TextView tvName;
    TextView tvBio;
    RecyclerView rvChecklist;
    ImageButton btnAddFriend;
    List<Goal> goals;
    GoalsAdapter adapter;
    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProfileBinding binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        user = (ParseUser) Parcels.unwrap(getIntent().getParcelableExtra(UserUtils.TAG));
        ivProfPic = binding.ivProfile;
        tvUsername = binding.tvScreenName;
        tvName = binding.tvName;
        tvBio = binding.tvBio;
        rvChecklist = binding.rvChecklist;
        btnAddFriend = binding.btnAddFriend;
        goals = new ArrayList<>();

        adapter = new GoalsAdapter(this, goals);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvChecklist.setAdapter(adapter);
        rvChecklist.setLayoutManager(linearLayoutManager);
        queryGoals();

        ParseFile profile = UserUtils.getProfilePicture(user);
        if (profile != null) {
            Glide.with(this).load(profile.getUrl()).transform(new CircleCrop()).into(ivProfPic);
        } else {
            Glide.with(this).load(R.drawable.ic_baseline_people_alt_24).transform(new CircleCrop()).into(ivProfPic);
        }

        tvUsername.setText("@" + UserUtils.getUsername(user));

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
    }

    private void queryGoals() {
        List<Goal> foundGoals = UserUtils.getGoals(user);
        adapter.clear();
        adapter.addAll(foundGoals);
    }
}