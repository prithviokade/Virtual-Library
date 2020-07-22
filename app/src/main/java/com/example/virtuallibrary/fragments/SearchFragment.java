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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.activities.MainActivity;
import com.example.virtuallibrary.adapters.TableAdapter;
import com.example.virtuallibrary.adapters.UserAdapter;
import com.example.virtuallibrary.databinding.FragmentCreateTableBinding;
import com.example.virtuallibrary.databinding.FragmentSearchBinding;
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
    UserAdapter adapter;

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
        adapter = new UserAdapter(getContext(), users);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvUsers.setAdapter(adapter);
        rvUsers.setLayoutManager(linearLayoutManager);
        queryUsers();

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

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searched = etSearch.getText().toString();
                filterBy(searched);
            }
        });

    }

    private void filterBy(String searched) {
        List<ParseUser> searchedUsers = new ArrayList<>();

        for (ParseUser user : users) {
            if (user.getUsername().toLowerCase().contains(searched.toLowerCase())) {
                searchedUsers.add(user);
            }
        }
        adapter.clear();
        adapter.addAll(searchedUsers);
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
                    Log.e(TAG, "Error while getting users");
                    return;
                }
                for (int i = 0; i < retreivedUsers.size(); i++) {
                    if (retreivedUsers.get(i).getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                        retreivedUsers.remove(i);
                        break;
                    }
                }
                adapter.clear();
                adapter.addAll(retreivedUsers);
            }
        });
    }
}