package com.example.virtuallibrary.fragments;

import android.content.Intent;
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
import android.widget.ImageView;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.activities.DetailsActivity;
import com.example.virtuallibrary.adapters.GoalsAdapter;
import com.example.virtuallibrary.adapters.TableAdapter;
import com.example.virtuallibrary.databinding.FragmentGoalsBinding;
import com.example.virtuallibrary.databinding.FragmentHomeBinding;
import com.example.virtuallibrary.models.Table;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;

    public static final String TAG = "HomeFragment";
    List<Table> tables;
    TableAdapter adapter;
    RecyclerView rvTables;
    ImageView ivCurrTable;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvTables = binding.rvTables;
        ivCurrTable = binding.ivCurrTable;

        Table currTable = null;
        try {
            currTable = (Table) ParseUser.getCurrentUser().fetch().get("current");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (currTable == null) {
            ivCurrTable.setVisibility(View.GONE);
        } else {
            int size = currTable.getSize();
            if (size == 1) { ivCurrTable.setImageResource(R.drawable.onetable); }
            if (size == 2) { ivCurrTable.setImageResource(R.drawable.twotable); }
            if (size == 3) { ivCurrTable.setImageResource(R.drawable.threetable); }
            if (size == 4) { ivCurrTable.setImageResource(R.drawable.fourtable); }
            if (size == 5) { ivCurrTable.setImageResource(R.drawable.fivetable); }
            if (size == 6) { ivCurrTable.setImageResource(R.drawable.sixtable); }
            if (size == 8) { ivCurrTable.setImageResource(R.drawable.eighttable); }
            if (size == 10) { ivCurrTable.setImageResource(R.drawable.tentable); }
        }

        final Table finalCurrTable = currTable;
        ivCurrTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra("TABLE", Parcels.wrap(finalCurrTable));
                startActivity(intent);
            }
        });

        tables = new ArrayList<>();
        adapter = new TableAdapter(getContext(), tables);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        rvTables.setAdapter(adapter);
        rvTables.setLayoutManager(linearLayoutManager);
        queryTables();
    }

    protected void queryTables() {
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
                    Log.i(TAG, table.getCreator().getString("name"));

                }
                adapter.clear();
                adapter.addAll(retreivedTables);
            }
        });
    }
}