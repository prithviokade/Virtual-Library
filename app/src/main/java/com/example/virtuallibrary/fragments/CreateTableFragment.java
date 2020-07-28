package com.example.virtuallibrary.fragments;

import android.app.AlertDialog;
import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.example.virtuallibrary.activities.InviteActivity;
import com.example.virtuallibrary.activities.TableDetailsActivity;
import com.example.virtuallibrary.databinding.FragmentCreateTableBinding;
import com.example.virtuallibrary.models.Invite;
import com.example.virtuallibrary.models.Table;

import org.parceler.Parcels;

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
    AlertDialog alertDialog;
    Table table;

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

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.alert_dialog_text);
        builder.setCancelable(true);
        builder.setPositiveButton(
                R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getContext(), TableDetailsActivity.class);
                        intent.putExtra(TableUtils.TAG, Parcels.wrap(table));
                        startActivity(intent);
                        Intent intent2 = new Intent(getContext(), InviteActivity.class);
                        intent2.putExtra(TableUtils.TAG, Parcels.wrap(table));
                        intent2.putExtra(TableUtils.TYPE_TAG, Invite.TYPE_PERMANENT);
                        startActivity(intent2);
                    }
                });

        builder.setNegativeButton(
                R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(getContext(), TableDetailsActivity.class);
                        intent.putExtra(TableUtils.TAG, Parcels.wrap(table));
                        startActivity(intent);
                    }
                });
        alertDialog = builder.create();

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
                Table createdTable = TableUtils.saveNewTable(topic, description, size, type, locked, visitors);
                table = createdTable;
                etDescription.setText("");
                etTopic.setText("");
                spinSize.setSelection(0);
                spinType.setSelection(0);
                switchVisitors.setChecked(false);

                if (locked) {
                    alertDialog.show();
                } else {
                    Intent intent = new Intent(getContext(), TableDetailsActivity.class);
                    intent.putExtra(TableUtils.TAG, Parcels.wrap(createdTable));
                    startActivity(intent);
                }
            }
        });
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
            size = 1;
        }
    }
}