package com.example.virtuallibrary.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.virtuallibrary.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoalsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalsFragment extends Fragment {

    ImageView ivProfPic;
    TextView tvUsername;
    TextView tvName;
    RecyclerView rvChecklist;

    public static GoalsFragment newInstance(String param1, String param2) {
        GoalsFragment fragment = new GoalsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

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
        rvChecklist = view.findViewById(R.id.rvChecklist);

        ParseFile profile = ParseUser.getCurrentUser().getParseFile("picture");
        if (profile != null) {
            Glide.with(getContext()).load(profile.getUrl()).transform(new CircleCrop()).into(ivProfPic);
        } else {
            Glide.with(getContext()).load(R.drawable.ic_baseline_people_alt_24).transform(new CircleCrop()).into(ivProfPic);
        }

        tvUsername.setText(ParseUser.getCurrentUser().getUsername());

        String name = ParseUser.getCurrentUser().getString("name");
        if (name != null) {
            tvName.setText(name);
        } else {
            Log.d("name", "hi");
            tvName.setText("");
        }

    }
}