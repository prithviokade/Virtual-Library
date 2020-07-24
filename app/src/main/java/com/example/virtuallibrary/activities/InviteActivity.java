package com.example.virtuallibrary.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.TableUtils;
import com.example.virtuallibrary.UserUtils;
import com.example.virtuallibrary.models.Table;
import com.parse.ParseUser;

import org.parceler.Parcels;

public class InviteActivity extends AppCompatActivity {

    Table table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        table = (Table) Parcels.unwrap(getIntent().getParcelableExtra(TableUtils.TAG));
        
    }
}