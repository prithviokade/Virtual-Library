package com.example.virtuallibrary.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.databinding.ActivityDetailsBinding;
import com.example.virtuallibrary.models.Table;
import com.facebook.messenger.MessengerThreadParams;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    public static final String TAG = "DetailsActivity";

    Table table;
    ImageView ivTable;
    TextView tvSize;
    TextView tvMembers;
    TextView tvVisitors;
    TextView tvDescription;
    RecyclerView rvMessages;
    EditText etCompose;
    ImageButton btnSend;
    Button btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailsBinding binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        table = (Table) Parcels.unwrap(getIntent().getParcelableExtra("TABLE"));

        ivTable = binding.ivTable;
        tvSize = binding.tvSize;
        tvMembers = binding.tvMembers;
        tvVisitors = binding.tvVisitors;
        tvDescription = binding.tvDescription;
        rvMessages = binding.rvMessages;
        etCompose = binding.etCompose;
        btnSend = binding.btnSend;
        btnJoin = binding.btnJoin;

        int size = table.getSize();
        tvSize.setText(Integer.toString(size));
        if (size == 1) { ivTable.setImageResource(R.drawable.onetable); }
        if (size == 2) { ivTable.setImageResource(R.drawable.twotable); }
        if (size == 3) { ivTable.setImageResource(R.drawable.threetable); }
        if (size == 4) { ivTable.setImageResource(R.drawable.fourtable); }
        if (size == 5) { ivTable.setImageResource(R.drawable.fivetable); }
        if (size == 6) { ivTable.setImageResource(R.drawable.sixtable); }
        if (size == 8) { ivTable.setImageResource(R.drawable.eighttable); }
        if (size == 10) { ivTable.setImageResource(R.drawable.tentable); }

        List<ParseUser> mates = table.getMates();
        String members = "";
        for (ParseUser user : mates) {
            members += "@";
            try {
                members += user.fetchIfNeeded().getUsername();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            members += ", ";
        }
        tvMembers.setText(members.substring(0, members.length() - 2));

        if (table.getVisiting()) {
            tvVisitors.setText("allowed");
        } else {
            tvVisitors.setText("not allowed");
        }

        String topic = table.getTopic();
        String type = table.getType();
        String description = table.getDescription();
        String fullDescription = "This is a " + type + " table, focusing on " + topic + ".\n" + description;
        tvDescription.setText(fullDescription);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = etCompose.getText().toString();
            }
        });

        if (containsUser(table, ParseUser.getCurrentUser())) {
            btnJoin.setText("Leave");
        }

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("Join".equals(btnJoin.getText().toString())) {
                    table.addMate(ParseUser.getCurrentUser());
                    ParseUser.getCurrentUser().remove("current");
                    btnJoin.setText("Leave");
                    saveTable(table);
                    saveUser();
                } else {
                    removeFromPreviousTable(ParseUser.getCurrentUser());
                    ParseUser.getCurrentUser().put("current", table);
                    saveUser();
                    btnJoin.setText("Join");
                }
            }
        });
    }

    private void removeFromPreviousTable(ParseUser user) {
        Table currentTable = (Table) user.get("current");
        if (currentTable != null) {
            List<ParseUser> newMates = new ArrayList<>();
            for (ParseUser mate : currentTable.getMates()) {
                try {
                    Log.d(TAG, mate.fetch().getUsername() + "  " + user.fetch().getUsername());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (!(mate.getUsername().equals(user.getUsername()))) {
                    try {
                        Log.d(TAG, mate.fetch().getUsername() + "  " + user.fetch().getUsername());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    newMates.add(mate);
                }
            }
            currentTable.setMates(newMates);
            saveTable(currentTable);
        }
    }

    private void saveTable(Table table) {
        table.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving new post", e);
                } else {
                    Log.i(TAG, "Success saving new post");
                }
            }
        });
    }

    private boolean containsUser(Table table, ParseUser user) {
        for (ParseUser mate : table.getMates()) {
            if (mate.getUsername().equals(user.getUsername())) {
                return true;
            }
        }
        return false;
    }

    private void saveUser() {
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving user info", e);
                } else {
                    Log.i(TAG, "Success saving user info");
                }
            }
        });
    }
}