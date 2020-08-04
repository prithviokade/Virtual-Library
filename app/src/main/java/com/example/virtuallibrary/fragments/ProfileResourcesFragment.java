package com.example.virtuallibrary.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.UserUtils;
import com.example.virtuallibrary.adapters.GoalsAdapter;
import com.example.virtuallibrary.adapters.ProfileResourceAdapter;
import com.example.virtuallibrary.databinding.FragmentProfileGoalsBinding;
import com.example.virtuallibrary.databinding.FragmentProfileResourcesBinding;
import com.example.virtuallibrary.models.Goal;
import com.example.virtuallibrary.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileResourcesFragment extends Fragment {

    public static final String TAG = "ProfileResourcesFragment";

    RecyclerView rvResources;
    FragmentProfileResourcesBinding binding;
    List<Post> resources;
    ProfileResourceAdapter adapter;
    ParseUser user;

    public ProfileResourcesFragment() {
        // Required empty public constructor
    }

    public static ProfileResourcesFragment newInstance(ParseUser user) {
        ProfileResourcesFragment fragment = new ProfileResourcesFragment();
        Bundle args = new Bundle();
        args.putParcelable(UserUtils.TAG, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileResourcesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvResources = binding.rvResources;
        user = getArguments().getParcelable(UserUtils.TAG);

        resources = new ArrayList<>();
        adapter = new ProfileResourceAdapter(getContext(), resources);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvResources.setAdapter(adapter);
        rvResources.setLayoutManager(staggeredGridLayoutManager);
        queryResources();
    }

    protected void queryResources() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, user);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> retreivedPosts, ParseException e) {
                if (e != null) {
                    return;
                }
                adapter.clear();
                adapter.addAll(retreivedPosts);
            }
        });
    }
}