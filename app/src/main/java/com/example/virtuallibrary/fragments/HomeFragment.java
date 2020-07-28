package com.example.virtuallibrary.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.TableUtils;
import com.example.virtuallibrary.UserUtils;
import com.example.virtuallibrary.activities.TableDetailsActivity;
import com.example.virtuallibrary.adapters.TableAdapter;
import com.example.virtuallibrary.databinding.FragmentHomeBinding;
import com.example.virtuallibrary.models.Invite;
import com.example.virtuallibrary.models.Message;
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
    boolean first_open = true;
    List<Table> tables;
    TableAdapter adapter;
    RecyclerView rvTables;
    ImageView ivCurrTable;
    TextView tvStatus;
    TextView tvSize;
    TextView tvMemberCount;
    TextView tvVisitors;
    TextView tvDescription;
    TextView tvCurrentTableText;
    ProgressDialog progressDialog;
    List<Invite> invites;

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
        tvStatus = binding.tvStatus;
        tvSize = binding.tvSize;
        tvMemberCount = binding.tvMemberCount;
        tvVisitors = binding.tvVisitors;
        tvDescription = binding.tvDescription;
        tvCurrentTableText = binding.tvCurrentTableText;

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle(getString(R.string.loading));
        progressDialog.setMessage(getString(R.string.wait));
        progressDialog.setCancelable(false);

        Table currTable = UserUtils.getCurrentTable(ParseUser.getCurrentUser());
        if (currTable == null) {
            ivCurrTable.setVisibility(View.GONE);
            tvCurrentTableText.setVisibility(View.INVISIBLE);
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

            tvCurrentTableText.setVisibility(View.VISIBLE);
            tvStatus.setText(currTable.getStatus());
            tvSize.setText(Integer.toString(currTable.getSize()));
            tvMemberCount.setText(Integer.toString(currTable.getMates().size()));
            if (currTable.getVisiting()) {
                tvVisitors.setText(R.string.allowed);
            } else {
                tvVisitors.setText(R.string.not_allowed);
            }

            String topic = currTable.getTopic();
            String type = currTable.getType();
            String description = currTable.getDescription();
            String fullDescription = getString(R.string.description_open_pt1) + " " + type + " " + getString(R.string.description_pt2) + " " + topic + ".\n" + description;
            if (currTable.getLocked()) {
                fullDescription = getString(R.string.description_closed_pt1) + " " + type + " " + getString(R.string.description_pt2) + " " + topic + ".\n" + description;
            }
            tvDescription.setText(fullDescription);
        }

        final Table finalCurrTable = currTable;
        ivCurrTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TableDetailsActivity.class);
                intent.putExtra(TableUtils.TAG, Parcels.wrap(finalCurrTable));
                startActivity(intent);
            }
        });

        tables = new ArrayList<>();
        adapter = new TableAdapter(getContext(), tables);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvTables.setAdapter(adapter);
        rvTables.setLayoutManager(linearLayoutManager);
        showLoading();
        invites = new ArrayList<>();
        queryTables();
    }

    protected void showLoading() {
        progressDialog.show();
    }

    protected void dismissLoading() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void showInviteDialog() {
        FragmentManager fm = getFragmentManager();
        UserInvitesFragment userInvitesFragment = UserInvitesFragment.newInstance(invites);
        userInvitesFragment.show(fm, "fragment_user_invites");
    }

    protected void queryTables() {
        ParseQuery<Table> query = ParseQuery.getQuery(Table.class);
        query.include(Table.KEY_CREATOR);
        query.include(Table.KEY_MATES);
        query.include(Table.KEY_CHAT);
        query.include(Table.KEY_CHAT + "." + Message.KEY_SENDER);
        query.include(Table.KEY_INVITES);
        query.include(Table.KEY_INVITES + "." + Invite.KEY_TO);
        query.include(Table.KEY_INVITES + "." + Invite.KEY_FROM);
        query.include(Table.KEY_INVITES + "." + Invite.KEY_TABLE);
        query.setLimit(20);
        query.addDescendingOrder(Table.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Table>() {
            @Override
            public void done(List<Table> retreivedTables, ParseException e) {
                invites.clear();
                invites.addAll(UserUtils.queryInvites(retreivedTables, ParseUser.getCurrentUser()));
                if (e != null) {
                    return;
                }
                int i = 0;
                List<Table> filteredTables = new ArrayList<>();
                for (Table table : retreivedTables) {
                    if (table.equals( UserUtils.getCurrentTable(ParseUser.getCurrentUser()) )) {
                        continue;
                    }
                    if (table.getLocked()) {
                        continue;
                    }
                    filteredTables.add(table);
                }
                adapter.clear();
                adapter.addAll(filteredTables);
                dismissLoading();
                if (invites.size() > 0) {
                    showInviteDialog();
                }
            }
        });
    }
}