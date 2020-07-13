package com.example.virtuallibrary.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.models.Table;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";
    List<Table> tables;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        queryTables();
    }

    protected void queryTables() {
        // Specify which class to query
        ParseQuery<Table> query = ParseQuery.getQuery(Table.class);
        query.include(Table.KEY_CREATOR);
        query.include(Table.KEY_MATES);
        query.setLimit(20);
        query.addDescendingOrder(Table.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Table>() {
            @Override
            public void done(List<Table> retreivedTables, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while getting table" + e);
                    return;
                }
                for (Table table : retreivedTables) {
                    Log.i(TAG, table.getCreator().getUsername());
                    // Log.i(TAG, table.getMates().get(0).getUsername());
                }
                // tables.clear();
                // tables.addAll(retreivedTables);
                // adapter.notifyDataSetChange();
            }
        });
    }
}