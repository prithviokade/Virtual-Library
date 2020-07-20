package com.example.virtuallibrary.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.databinding.ActivityDetailsBinding;
import com.example.virtuallibrary.models.Table;
import com.facebook.messenger.MessengerThreadParams;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {

    Table table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailsBinding binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        table = (Table) Parcels.unwrap(getIntent().getParcelableExtra("TABLE"));
    }
}