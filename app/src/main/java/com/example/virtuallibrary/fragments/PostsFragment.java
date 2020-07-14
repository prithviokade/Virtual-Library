package com.example.virtuallibrary.fragments;

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

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.adapters.PostsAdapter;
import com.example.virtuallibrary.models.Post;
import com.example.virtuallibrary.models.Table;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends Fragment {

    public static final String TAG = "PostsFragment";
    RecyclerView rvPosts;
    List<Post> posts;
    PostsAdapter adapter;

    public PostsFragment() {
        // Required empty public constructor
    }

    public static PostsFragment newInstance(String param1, String param2) {
        PostsFragment fragment = new PostsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);
        posts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), posts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(linearLayoutManager);
        queryPosts();
    }

    protected void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> retreivedPosts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while getting posts" + e);
                    return;
                }
                for (Post post : retreivedPosts) {
                    Log.i(TAG, post.getUser().getString("name"));
                }
                adapter.clear();
                adapter.addAll(retreivedPosts);
            }
        });
    }
}