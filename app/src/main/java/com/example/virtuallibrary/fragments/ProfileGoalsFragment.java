package com.example.virtuallibrary.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.UserUtils;
import com.example.virtuallibrary.adapters.GoalsAdapter;
import com.example.virtuallibrary.databinding.FragmentGoalsBinding;
import com.example.virtuallibrary.databinding.FragmentProfileGoalsBinding;
import com.example.virtuallibrary.models.Goal;
import com.example.virtuallibrary.models.Invite;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ProfileGoalsFragment extends Fragment {

    public static final String TAG = "ProfileGoalsFragment";

    RecyclerView rvChecklist;
    FragmentProfileGoalsBinding binding;
    List<Goal> goals;
    GoalsAdapter adapter;
    ParseUser user;

    public ProfileGoalsFragment() {
        // Required empty public constructor
    }

    public static ProfileGoalsFragment newInstance(ParseUser user) {
        ProfileGoalsFragment fragment = new ProfileGoalsFragment();
        Bundle args = new Bundle();
        args.putParcelable(UserUtils.TAG, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileGoalsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvChecklist = binding.rvChecklist;
        user = getArguments().getParcelable(UserUtils.TAG);

        goals = new ArrayList<>();
        adapter = new GoalsAdapter(getContext(), goals);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvChecklist.setAdapter(adapter);
        rvChecklist.setLayoutManager(linearLayoutManager);
        queryGoals();
    }

    private void queryGoals() {
        List<Goal> foundGoals = UserUtils.getGoals(user);
        adapter.clear();
        adapter.addAll(foundGoals);
    }


}