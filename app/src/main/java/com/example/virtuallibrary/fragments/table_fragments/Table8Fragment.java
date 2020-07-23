package com.example.virtuallibrary.fragments.table_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.virtuallibrary.R;
import com.example.virtuallibrary.activities.ProfileActivity;
import com.example.virtuallibrary.databinding.FragmentTable6Binding;
import com.example.virtuallibrary.databinding.FragmentTable8Binding;
import com.example.virtuallibrary.models.Table;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class Table8Fragment extends Fragment {

    public Table8Fragment() {
        // Required empty public constructor
    }

    FragmentTable8Binding binding;
    ImageView ivTable;
    ImageView ivPerson1;
    ImageView ivPerson2;
    ImageView ivPerson3;
    ImageView ivPerson4;
    ImageView ivPerson5;
    ImageView ivPerson6;
    ImageView ivPerson7;
    ImageView ivPerson8;
    Table table;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTable8Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            table = bundle.getParcelable("TABLE");
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivTable = binding.ivTable;
        ivPerson1 = binding.ivPerson1;
        ivPerson2 = binding.ivPerson2;
        ivPerson3 = binding.ivPerson3;
        ivPerson4 = binding.ivPerson4;
        ivPerson5 = binding.ivPerson5;
        ivPerson6 = binding.ivPerson6;
        ivPerson7 = binding.ivPerson7;
        ivPerson8 = binding.ivPerson8;

        final List<ParseUser> mates = table.getMates();
        int matesCount = mates.size();
        for (int i = 0; i < matesCount; i++) {
            ImageView profileImage = null;
            ParseFile profile = mates.get(i).getParseFile("picture");
            if (i == 0) { profileImage = ivPerson1; }
            if (i == 1) { profileImage = ivPerson2; }
            if (i == 2) { profileImage = ivPerson3; }
            if (i == 3) { profileImage = ivPerson4; }
            if (i == 4) { profileImage = ivPerson5; }
            if (i == 5) { profileImage = ivPerson6; }
            if (i == 6) { profileImage = ivPerson7; }
            if (i == 7) { profileImage = ivPerson8; }
            if (profile != null) {
                Glide.with(getContext()).load(profile.getUrl()).transform(new CircleCrop()).into(profileImage);
            } else {
                Glide.with(getContext()).load(R.drawable.ic_baseline_people_alt_24).transform(new CircleCrop()).into(profileImage);
            }
            final int finalI = i;
            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), ProfileActivity.class);
                    intent.putExtra("USER", Parcels.wrap(mates.get(finalI)));
                    startActivity(intent);
                }
            });
        }
    }
}