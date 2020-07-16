package com.example.virtuallibrary.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.models.Goal;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CreateGoalFragment extends Fragment {

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
        return inflater.inflate(R.layout.fragment_create_goal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etGoal = view.findViewById(R.id.etGoal);
        btnIncomp = view.findViewById(R.id.btnIncomp);
        btnInprog = view.findViewById(R.id.btnInprog);
        btnComp = view.findViewById(R.id.btnComp);
        btnSave = view.findViewById(R.id.btnSave);

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
                ParseUser.getCurrentUser().add("goals", addGoal);
                saveGoal(addGoal);
                saveUser();
                etGoal.setText("");
                btnComp.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24);
                btnInprog.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24);
                btnIncomp.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24);
            }
        });
    }

    private void saveGoal(Goal goal) {
        goal.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving goal info", e);
                } else {
                    Log.i(TAG, "Success saving goal info");
                }
            }
        });
    }

    private void saveUser() {
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving post info", e);
                } else {
                    Log.i(TAG, "Success saving post info");
                }
            }
        });
    }
}