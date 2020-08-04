package com.example.virtuallibrary.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.bumptech.glide.Glide;
import com.example.virtuallibrary.R;
import com.example.virtuallibrary.TableUtils;
import com.example.virtuallibrary.UserUtils;
import com.example.virtuallibrary.activities.ProfileActivity;
import com.example.virtuallibrary.databinding.FragmentTableBinding;
import com.example.virtuallibrary.models.Table;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class TableFragment extends Fragment {

    public TableFragment() {
        // Required empty public constructor
    }

    FragmentTableBinding binding;
    ImageView ivTable;
    ImageView ivPerson1;
    ImageView ivPerson2;
    ImageView ivPerson3;
    ImageView ivPerson4;
    ImageView ivPerson5;
    ImageView ivPerson6;
    ImageView ivPerson7;
    ImageView ivPerson8;
    ImageView ivPerson9;
    ImageView ivPerson10;
    Table table;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTableBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            table = bundle.getParcelable(TableUtils.TAG);
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
        ivPerson9 = binding.ivPerson9;
        ivPerson10 = binding.ivPerson10;

        int tableSize = table.getSize();
        ivTable.setImageResource(TableUtils.getTableImage(tableSize));
        List<Integer> margins = TableUtils.getTableMargins(tableSize);

        final List<ParseUser> mates = table.getMates();
        int matesCount = mates.size();

        for (int i = 0; i < matesCount; i++) {
            ImageView profileImage = null;
            if (i == 0) { profileImage = ivPerson1; }
            if (i == 1) { profileImage = ivPerson2; }
            if (i == 2) { profileImage = ivPerson3; }
            if (i == 3) { profileImage = ivPerson4; }
            if (i == 4) { profileImage = ivPerson5; }
            if (i == 5) { profileImage = ivPerson6; }
            if (i == 6) { profileImage = ivPerson7; }
            if (i == 7) { profileImage = ivPerson8; }
            if (i == 8) { profileImage = ivPerson9; }
            if (i == 9) { profileImage = ivPerson10; }

            // set margins, dimensions of image
            float d = getContext().getResources().getDisplayMetrics().density; // convert pixels to dp
            LayoutParams lp = new LayoutParams((int) (35*d), (int) (35*d));
            lp.setMargins((int) (margins.get(2*i) * d), (int)(margins.get(2*i + 1) * d), 0, 0);
            profileImage.setLayoutParams(lp);

            // set profile image
            ParseFile profile = UserUtils.getProfilePicture(mates.get(i));
            if (profile != null) {
                Glide.with(getContext()).load(profile.getUrl()).into(profileImage);
            } else {
                Glide.with(getContext()).load(R.drawable.ic_baseline_person_24).into(profileImage);
            }

            final int finalI = i;
            ImageView finalProfileImage = profileImage;
            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), ProfileActivity.class);
                    intent.putExtra(UserUtils.TAG, Parcels.wrap(mates.get(finalI)));
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), (View) finalProfileImage, "profilepicture");
                    startActivity(intent, options.toBundle());
                }
            });
        }
    }
}