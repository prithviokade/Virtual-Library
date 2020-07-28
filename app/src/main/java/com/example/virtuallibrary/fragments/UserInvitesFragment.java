package com.example.virtuallibrary.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.adapters.InviteAdapter;
import com.example.virtuallibrary.databinding.FragmentUserInvitesBinding;
import com.example.virtuallibrary.models.Invite;

import java.util.ArrayList;
import java.util.List;

public class UserInvitesFragment extends DialogFragment {

    public UserInvitesFragment() {
        // Required empty public constructor
    }

    public static UserInvitesFragment newInstance(List<Invite> invites) {
        UserInvitesFragment fragment = new UserInvitesFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(Invite.TAG, (ArrayList<Invite>) invites);
        fragment.setArguments(args);
        return fragment;
    }

    FragmentUserInvitesBinding binding;
    List<Invite> invites;
    public TextView tvNotification;
    RecyclerView rvInvites;
    InviteAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment// Inflate the layout for this fragment
        binding = FragmentUserInvitesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvNotification = binding.tvNotification;
        rvInvites = binding.rvInvites;

        invites = new ArrayList<>();
        adapter = new InviteAdapter(this, invites);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvInvites.setAdapter(adapter);
        rvInvites.setLayoutManager(linearLayoutManager);
        getInvites();

        tvNotification.setText(getString(R.string.you_have) + " " + Integer.toString(invites.size()) + " " + getString(R.string.notification_plural));
        if (invites.size() == 1) {
            tvNotification.setText(getString(R.string.you_have) + " " + Integer.toString(invites.size()) + " " + getString(R.string.notification_singular));
        }
    }

    private void getInvites() {
        adapter.clear();
        List<Invite> argumentInvites = getArguments().getParcelableArrayList(Invite.TAG);
        adapter.addAll(argumentInvites);
    }
}

