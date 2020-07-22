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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.TableUtils;
import com.example.virtuallibrary.databinding.FragmentCreateTableBinding;
import com.example.virtuallibrary.models.Table;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class CreateTableFragment extends Fragment {

    public static final String TAG = "CreateTableFragment";

    FragmentCreateTableBinding binding;
    ImageView ivTable;
    ImageButton btnLock;
    Button btnCreate;
    EditText etTopic;
    Spinner spinType;
    EditText etDescription;
    Spinner spinSize;
    Switch switchVisitors;

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
        btnLock = binding.btnLock;
        btnCreate = binding.btnCreate;
        etTopic = binding.etTopic;
        spinType = binding.spinType;
        etDescription = binding.etDescription;
        spinSize = binding.spinSize;
        switchVisitors = binding.switchVisitors;

        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(getContext(), R.array.tabletypes_array, android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinType.setAdapter(adapterType);
        spinType.setOnItemSelectedListener(new TypesSpinnerClass());

        ArrayAdapter<CharSequence> adapterSize = ArrayAdapter.createFromResource(getContext(), R.array.tablesizes_array, android.R.layout.simple_spinner_item);
        adapterSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinSize.setAdapter(adapterSize);
        spinSize.setOnItemSelectedListener(new SizesSpinnerClass());

        visitors = false;
        switchVisitors.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                visitors = isChecked;
            }
        });

        locked = false;
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
        TableUtils.removeFromPreviousTable(ParseUser.getCurrentUser());
        ParseUser.getCurrentUser().put("current", table);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class TypesSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
            type = parent.getItemAtPosition(pos).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            type = parent.getItemAtPosition(0).toString();
        }
    }

    class SizesSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
            size = Integer.valueOf(parent.getItemAtPosition(pos).toString());
            ivTable.setImageResource(TableUtils.getTableImage(size));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            size = 0;
        }
    }
}