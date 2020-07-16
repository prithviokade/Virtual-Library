package com.example.virtuallibrary.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.virtuallibrary.R;
import com.example.virtuallibrary.activities.LoginActivity;
import com.example.virtuallibrary.adapters.GoalsAdapter;
import com.example.virtuallibrary.models.Goal;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class GoalsFragment extends Fragment {

    public static final String TAG = "GoalsFragment";
    ImageView ivProfPic;
    TextView tvUsername;
    TextView tvName;
    TextView tvBio;
    RecyclerView rvChecklist;
    Button btnLogOut;
    List<Goal> goals;
    GoalsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivProfPic = view.findViewById(R.id.ivProfile);
        tvUsername = view.findViewById(R.id.tvScreenName);
        tvName = view.findViewById(R.id.tvName);
        tvBio = view.findViewById(R.id.tvBio);
        rvChecklist = view.findViewById(R.id.rvChecklist);
        btnLogOut = view.findViewById(R.id.btnLogOut);
        goals = new ArrayList<>();

        adapter = new GoalsAdapter(getContext(), goals);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        rvChecklist.setAdapter(adapter);
        rvChecklist.setLayoutManager(linearLayoutManager);
        queryGoals();

        ParseFile profile = ParseUser.getCurrentUser().getParseFile("picture");
        if (profile != null) {
            Glide.with(getContext()).load(profile.getUrl()).transform(new CircleCrop()).into(ivProfPic);
        } else {
            Glide.with(getContext()).load(R.drawable.ic_baseline_people_alt_24).transform(new CircleCrop()).into(ivProfPic);
        }

        tvUsername.setText("@" + ParseUser.getCurrentUser().getUsername());

        String name = ParseUser.getCurrentUser().getString("name");
        if (name != null) {
            tvName.setText(name);
        } else {
            tvName.setText("");
        }

        String bio = ParseUser.getCurrentUser().getString("bio");
        if (bio != null) {
            tvBio.setText(bio);
        } else {
            tvBio.setText("");
        }

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void queryGoals() {
        List<Goal> foundGoals = (List<Goal>) ParseUser.getCurrentUser().get("goals");
        try {
            Log.d(TAG, foundGoals.get(0).fetchIfNeeded().getString("goal"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        adapter.clear();
        adapter.addAll(foundGoals);
    }
}