package com.example.virtuallibrary.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.TableUtils;
import com.example.virtuallibrary.UserUtils;
import com.example.virtuallibrary.activities.MainActivity;
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
import java.util.Arrays;
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
    ListView lvSort;
    SwipeRefreshLayout swipeContainer;
    View rootView;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        Log.d(TAG, "onsaveinstance");
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TableUtils.TAG, (ArrayList<Table>) tables);
        outState.putParcelableArrayList(Invite.TAG, (ArrayList<Invite>) invites);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            binding = FragmentHomeBinding.inflate(getLayoutInflater());
            rootView = binding.getRoot();
        }
        return rootView;
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
        // sort options list
        lvSort = binding.lvSort;
        lvSort.setVisibility(View.GONE);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) binding.swipeContainer;
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadTables();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        ArrayAdapter lvAdapter = new ArrayAdapter<>(getContext(), R.layout.simple_list_item_1, getResources().getStringArray(R.array.sort_options_array));
        lvSort.setAdapter(lvAdapter);

        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar_notification_sort);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        ImageView ivNotifications = actionBar.getCustomView().findViewById(R.id.ivNotification);
        ivNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInviteDialog();
            }
        });
        ImageView ivSort = actionBar.getCustomView().findViewById(R.id.ivSort);
        ivSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // open and close the sort options
                if (lvSort.getVisibility() == View.GONE) {
                    lvSort.setVisibility(View.VISIBLE);
                } else {
                    lvSort.setVisibility(View.GONE);
                }
            }
        });

        lvSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                String text = parent.getItemAtPosition(position).toString();
                List<String> options = Arrays.asList(getResources().getStringArray(R.array.sort_options_array));
                tables = TableUtils.sortTable(tables, text, options);
                adapter.notifyDataSetChanged();
                rvTables.smoothScrollToPosition(0);
                lvSort.setVisibility(View.GONE);
            }
        });

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle(getString(R.string.loading));
        progressDialog.setMessage(getString(R.string.wait));
        progressDialog.setCancelable(false);

        Table currTable = UserUtils.getCurrentTable(ParseUser.getCurrentUser());
        setUpCurrentTable(currTable);

        final Table finalCurrTable = currTable;
        ivCurrTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TableDetailsActivity.class);
                intent.putExtra(TableUtils.TAG, Parcels.wrap(finalCurrTable));
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), (View) ivCurrTable, "table");
                startActivity(intent, options.toBundle());
            }
        });

        if (savedInstanceState != null) {
            Log.d(TAG, "onactivitycreated -> savedInstance != null");
            tables = savedInstanceState.getParcelableArrayList(TableUtils.TAG);
            invites = savedInstanceState.getParcelableArrayList(Invite.TAG);
        } else if (((MainActivity) getActivity()).tables != null && ((MainActivity) getActivity()).invites != null) {
            tables = ((MainActivity) getActivity()).tables;
            invites = ((MainActivity) getActivity()).invites;
        } else if (tables == null || invites == null) {
            tables = new ArrayList<>();
            invites = new ArrayList<>();
            queryTables();
        }
        adapter = new TableAdapter(this, tables);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvTables.setAdapter(adapter);
        rvTables.setLayoutManager(linearLayoutManager);
    }

    private void setUpCurrentTable(Table currTable) {
        if (currTable == null) {
            ivCurrTable.setVisibility(View.GONE);
            tvCurrentTableText.setVisibility(View.INVISIBLE);
        } else {
            int size = currTable.getSize();
            ivCurrTable.setImageResource(TableUtils.getTableImage(size));

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
    }

    private void reloadTables() {
        Table currTable = UserUtils.getCurrentTable(ParseUser.getCurrentUser());
        setUpCurrentTable(currTable);
        queryTables();
        swipeContainer.setRefreshing(false);
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
        showLoading();
        ParseQuery<Table> query = ParseQuery.getQuery(Table.class);
        query.include(Table.KEY_CREATOR);
        query.include(Table.KEY_MATES);
        query.include(Table.KEY_CHAT);
        query.include(Table.KEY_CHAT + "." + Message.KEY_SENDER);
        query.include(Table.KEY_INVITES);
        query.include(Table.KEY_INVITES + "." + Invite.KEY_TO);
        query.include(Table.KEY_INVITES + "." + Invite.KEY_FROM);
        query.include(Table.KEY_INVITES + "." + Invite.KEY_TABLE);
        query.addDescendingOrder(Table.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Table>() {
            @Override
            public void done(List<Table> retreivedTables, ParseException e) {
                invites.clear();
                invites.addAll(UserUtils.queryInvites(retreivedTables, ParseUser.getCurrentUser()));
                ((MainActivity) getActivity()).invites = invites;
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
                ((MainActivity) getActivity()).tables = filteredTables;
                dismissLoading();
                if (invites.size() > 0 && first_open) {
                    showInviteDialog();
                    first_open = false;
                }
            }
        });
    }
}