package com.example.virtuallibrary.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.UserUtils;
import com.example.virtuallibrary.databinding.FragmentCreateGoalBinding;
import com.example.virtuallibrary.models.Goal;
import com.parse.ParseUser;

public class CreateGoalFragment extends Fragment {

    FragmentCreateGoalBinding binding;

    EditText etGoal;
    ImageButton btnIncomp;
    ImageButton btnInprog;
    ImageButton btnComp;
    Button btnSave;

    public static final String TAG = "CreateGoalFragment";
    String status;

    public CreateGoalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateGoalBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etGoal = binding.etGoal;
        btnIncomp = binding.btnIncomp;
        btnInprog = binding.btnInprog;
        btnComp = binding.btnComp;
        btnSave = binding.btnSave;

        btnIncomp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnIncomp.setImageResource(R.drawable.ic_baseline_check_box_24);
                status = "incomplete";
            }
        });

        btnInprog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnInprog.setImageResource(R.drawable.ic_baseline_check_box_24);
                status = "intermediate";
            }
        });

        btnComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnComp.setImageResource(R.drawable.ic_baseline_check_box_24);
                status = "complete";
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String goal = etGoal.getText().toString();
                if (goal.isEmpty()) {
                    Toast.makeText(getContext(), "Goal cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (status == null) {
                    status = "incomplete";
                }
                Goal addGoal = new Goal();
                addGoal.setGoal(goal);
                addGoal.setStatus(status);
                addGoal.setUser(ParseUser.getCurrentUser());
                UserUtils.addGoal(ParseUser.getCurrentUser(), addGoal);
                addGoal.saveInBackground();
                ParseUser.getCurrentUser().saveInBackground();
                etGoal.setText("");
                btnComp.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24);
                btnInprog.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24);
                btnIncomp.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24);
            }
        });
    }

}