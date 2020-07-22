package com.example.virtuallibrary.fragments;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.databinding.FragmentCreateTableBinding;
import com.example.virtuallibrary.models.Table;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class CreateTableFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public static final String TAG = "CreateTableFragment";

    FragmentCreateTableBinding binding;
    ImageView ivTable;
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    Button btn8;
    Button btn10;
    ImageButton btnLock;
    Button btnCreate;
    Button btnVisitorsTrue;
    Button btnVisitorsFalse;
    EditText etTopic;
    Spinner spinType;
    EditText etDescription;

    int size;
    boolean visitors;
    boolean locked;
    String type;

    public CreateTableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateTableBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivTable = binding.ivTable;
        btn1 = binding.btn1;
        btn2 = binding.btn2;
        btn3 = binding.btn3;
        btn4 = binding.btn4;
        btn5 = binding.btn5;
        btn6 = binding.btn6;
        btn8 = binding.btn8;
        btn10 = binding.btn10;
        btnVisitorsTrue = binding.btnVisitorsTrue;
        btnVisitorsFalse = binding.btnVisitorsFalse;
        btnLock = binding.btnLock;
        btnCreate = binding.btnCreate;
        etTopic = binding.etTopic;
        spinType = binding.spinType;
        etDescription = binding.etDescription;
        final Resources res = getResources();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.tabletypes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinType.setAdapter(adapter);
        spinType.setOnItemSelectedListener(this);

        locked = false;
        size = 0;
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivTable.setImageResource(R.drawable.onetable);
                btn1.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightGrey));
                size = 1;
            }
        });
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ivTable.setImageResource(R.drawable.twotable);
                btn2.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightGrey));
                size = 2;
            }
        });
        btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ivTable.setImageResource(R.drawable.threetable);
                btn3.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightGrey));
                size = 3;
            }
        });
        btn4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ivTable.setImageResource(R.drawable.fourtable);
                btn4.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightGrey));
                size = 4;
            }
        });
        btn5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ivTable.setImageResource(R.drawable.fivetable);
                btn5.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightGrey));
                size = 5;
            }
        });
        btn6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ivTable.setImageResource(R.drawable.sixtable);
                btn6.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightGrey));
                size = 6;
            }
        });
        btn8.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ivTable.setImageResource(R.drawable.eighttable);
                btn8.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightGrey));
                size = 8;
            }
        });
        btn10.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ivTable.setImageResource(R.drawable.tentable);
                btn10.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightGrey));
                size = 10;
            }
        });

        btnVisitorsTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnVisitorsTrue.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightGrey));
                btnVisitorsFalse.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.plainWhite));
                visitors = true;
            }
        });
        btnVisitorsFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnVisitorsFalse.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightGrey));
                btnVisitorsTrue.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.plainWhite));
                visitors = false;
            }
        });

        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locked = !locked;
                if (locked) {
                    btnLock.setImageResource(R.drawable.ic_baseline_lock_24);
                } else {
                    btnLock.setImageResource(R.drawable.ic_baseline_lock_open_24);
                }
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (size == 0 || etTopic.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in the above categories!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String topic = etTopic.getText().toString();
                String description = etDescription.getText().toString();
                saveTable(topic, description);
            }
        });
    }

    private void saveTable(String topic, String description) {
        Table table = new Table();
        table.setCreator(ParseUser.getCurrentUser());
        table.setMates(new ArrayList<ParseUser>());
        table.addMate(ParseUser.getCurrentUser());
        table.setStatus("working");
        table.setSize(size);
        table.setTopic(topic);
        table.setType(type);
        table.setVisiting(visitors);
        table.setDescription(description);
        table.setLocked(locked);
        table.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving new post", e);
                } else {
                    Log.i(TAG, "Success saving new post");
                    etDescription.setText("");
                    etTopic.setText("");
                }
            }
        });
        removeFromPreviousTable(ParseUser.getCurrentUser());
        ParseUser.getCurrentUser().put("current", table);
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
            saveTableExists(currentTable);
        }
    }

    private void saveTableExists(Table table) {
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

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Log.d(TAG, "selected: " + Integer.toString(pos));
        type = parent.getItemAtPosition(pos).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        type = parent.getItemAtPosition(0).toString();
    }
}