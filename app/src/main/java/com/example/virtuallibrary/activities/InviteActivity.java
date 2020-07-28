package com.example.virtuallibrary.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.TableUtils;
import com.example.virtuallibrary.UserUtils;
import com.example.virtuallibrary.adapters.InviteUserAdapter;
import com.example.virtuallibrary.databinding.ActivityInviteBinding;
import com.example.virtuallibrary.models.Invite;
import com.example.virtuallibrary.models.Table;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class InviteActivity extends AppCompatActivity {

    Table table;
    String type;
    List<ParseUser> friends;
    RecyclerView rvUsers;
    InviteUserAdapter adapter;
    TextView tvCancel;
    EditText etSearch;
    ImageView btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityInviteBinding binding = ActivityInviteBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_default);

        table = (Table) Parcels.unwrap(getIntent().getParcelableExtra(TableUtils.TAG));
        type = getIntent().getStringExtra(TableUtils.TYPE_TAG);
        if (type == null || !type.equals(Invite.TYPE_PERMANENT)) {
            type = "";
        }
        rvUsers = binding.rvUsers;
        etSearch = binding.etSearch;
        tvCancel = binding.tvCancel;
        btnSend = binding.btnSend;

        friends = new ArrayList<>();
        adapter = new InviteUserAdapter(this, friends, type);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvUsers.setAdapter(adapter);
        rvUsers.setLayoutManager(linearLayoutManager);
        findFriends();

        tvCancel = view.findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

        for (ParseUser user : friends) {
            if (UserUtils.getUsername(user).toLowerCase().contains(searched.toLowerCase())) {
                searchedUsers.add(user);
            }
        }
        adapter.clear();
        adapter.addAll(searchedUsers);
    }

    private void findFriends() {
        List<ParseUser> retrievedFriends = UserUtils.getFriends(ParseUser.getCurrentUser());
        List<ParseUser> currentMates = table.getMates();
        List<Invite> currentInvites = table.getInvites();
        List<ParseUser> addFriends = new ArrayList<>();
        for (ParseUser friend : retrievedFriends) {
            if (UserUtils.userContained(currentMates, friend) || UserUtils.userInviteContained(currentInvites, friend, ParseUser.getCurrentUser())) {
                continue;
            }
            addFriends.add(friend);
        }
        adapter.clear();
        adapter.addAll(addFriends);
    }
}