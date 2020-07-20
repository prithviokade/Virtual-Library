package com.example.virtuallibrary.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    Table table;
    ImageView ivTable;
    TextView tvSize;
    TextView tvMembers;
    TextView tvVisitors;
    TextView tvDescription;
    RecyclerView rvMessages;
    EditText etCompose;
    ImageButton btnSend;

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

        tvSize.setText(Integer.toString(table.getSize()));

        List<ParseUser> mates = table.getMates();
        String members = "";
        for (ParseUser user : mates) {
            members += "@";
            members += user.getUsername();
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
    }
}