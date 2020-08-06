package com.example.virtuallibrary.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.UserUtils;
import com.example.virtuallibrary.activities.MainActivity;
import com.example.virtuallibrary.adapters.SearchAdapter;
import com.example.virtuallibrary.databinding.FragmentSearchBinding;
import com.example.virtuallibrary.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    public static final String TAG = "SearchFragment";
    FragmentSearchBinding binding;

    public SearchFragment() {
        // Required empty public constructor
    }

    EditText etSearch;
    TextView tvCancel;
    RecyclerView rvUsers;
    List<ParseUser> users;
    List<Post> resources;
    SearchAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etSearch = binding.etSearch;
        tvCancel = binding.tvCancel;
        rvUsers = binding.rvUsers;

        users = new ArrayList<>();
        resources = new ArrayList<>();
        adapter = new SearchAdapter(this, users, resources);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvUsers.setAdapter(adapter);
        rvUsers.setLayoutManager(linearLayoutManager);
        queryUsers();
        queryPosts();

        tvCancel = view.findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "detected BEFORE");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "detected ON" + charSequence + i + i1 + i2);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searched = etSearch.getText().toString();
                filterUserBy(searched);
                filterResourceBy(searched);
            }
        });

    }

    private void filterUserBy(String searched) {
        if (searched.isEmpty()) {
            queryUsers();
            return;
        }
        List<ParseUser> searchedUsers = new ArrayList<>();

        for (ParseUser user : users) {
            if (UserUtils.getUsername(user).toLowerCase().contains(searched.toLowerCase())) {
                searchedUsers.add(user);
            }
        }
        adapter.clearUsers();
        adapter.addAllUsers(searchedUsers);
    }

    private void filterResourceBy(String searched) {
        if (searched.isEmpty()) {
            queryPosts();
            return;
        }
        List<Post> searchedResources = new ArrayList<>();

        for (Post resource : resources) {
            if (resource.getCaption().toLowerCase().contains(searched.toLowerCase())) {
                searchedResources.add(resource);
            } else if (resource.getSubject().toLowerCase().contains(searched.toLowerCase())) {
                searchedResources.add(resource);
            }
        }
        adapter.clearResources();
        adapter.addAllResources(searchedResources);
    }

    private void queryUsers() {
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.include("goals");
        query.include("current");
        query.include("name");
        query.include("username");
        query.include("picture");
        query.addDescendingOrder("username");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> retreivedUsers, ParseException e) {
                if (e != null) {
                    return;
                }
                for (int i = 0; i < retreivedUsers.size(); i++) {
                    if (UserUtils.equals(retreivedUsers.get(i), ParseUser.getCurrentUser())) {
                        retreivedUsers.remove(i);
                        break;
                    }
                }
                adapter.clearUsers();
                adapter.addAllUsers(retreivedUsers);
            }
        });
    }

    protected void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> retreivedPosts, ParseException e) {
                if (e != null) {
                    return;
                }
                adapter.clearResources();
                adapter.addAllResources(retreivedPosts);
            }
        });
    }
}