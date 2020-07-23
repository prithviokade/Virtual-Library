package com.example.virtuallibrary.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.TableUtils;
import com.example.virtuallibrary.UserUtils;
import com.example.virtuallibrary.adapters.MessageAdapter;
import com.example.virtuallibrary.databinding.ActivityTableDetailsBinding;
import com.example.virtuallibrary.models.Message;
import com.example.virtuallibrary.models.Table;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class TableDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TAG = "TableDetailsActivity";

    Table table;
    TextView tvSize;
    TextView tvMembers;
    TextView tvVisitors;
    TextView tvDescription;
    RecyclerView rvMessages;
    EditText etCompose;
    ImageButton btnSend;
    Button btnJoin;
    Spinner spnStatus;
    ImageButton btnInvite;

    List<Message> messages;
    MessageAdapter adapter;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    boolean isMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTableDetailsBinding binding = ActivityTableDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        table = (Table) Parcels.unwrap(getIntent().getParcelableExtra("TABLE"));

        tvSize = binding.tvSize;
        tvMembers = binding.tvMembers;
        tvVisitors = binding.tvVisitors;
        tvDescription = binding.tvDescription;
        rvMessages = binding.rvMessages;
        etCompose = binding.etCompose;
        btnSend = binding.btnSend;
        btnJoin = binding.btnJoin;
        spnStatus = binding.spnStatus;
        btnInvite = binding.btnInvite;

        messages = new ArrayList<>();
        adapter = new MessageAdapter(this, messages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvMessages.setAdapter(adapter);
        rvMessages.setLayoutManager(linearLayoutManager);
        getMessages();

        ArrayAdapter<CharSequence> spnAdapter = ArrayAdapter.createFromResource(this, R.array.statustypes_array, R.layout.spinneritem);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnStatus.setAdapter(spnAdapter);
        spnStatus.setOnItemSelectedListener(this);

        int size = table.getSize();
        tvSize.setText(Integer.toString(size));
        Fragment tableFragment = TableUtils.getTableFragment(size);
        Bundle bundle = new Bundle();
        bundle.putParcelable("TABLE", table);
        tableFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.flContainer, tableFragment).commit();

        List<ParseUser> mates = table.getMates();
        String members = "";
        for (ParseUser user : mates) {
            members += "@";
            members += UserUtils.getUsername(user);
            members += ", ";
        }
        if (!members.isEmpty()) { members = members.substring(0, members.length() - 2); }
        else { members = "None"; }
        tvMembers.setText(members);

        // SpannableString ssMembers = new SpannableString(members);


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
                Message newMessage = new Message();
                newMessage.setText(text);
                newMessage.setSender(ParseUser.getCurrentUser());
                saveMessage(newMessage);
                table.addChat(newMessage);
                TableUtils.saveTable(table);
                etCompose.setText("");
                adapter.add(newMessage);
            }
        });

        rvMessages.setVisibility(View.INVISIBLE);
        etCompose.setVisibility(View.INVISIBLE);
        btnSend.setVisibility(View.INVISIBLE);
        spnStatus.setEnabled(false);
        btnInvite.setVisibility(View.INVISIBLE);

        isMember = table.containsUser(ParseUser.getCurrentUser());
        if (isMember) { // current table
            btnJoin.setText("Leave");
            rvMessages.setVisibility(View.VISIBLE);
            etCompose.setVisibility(View.VISIBLE);
            btnSend.setVisibility(View.VISIBLE);
            spnStatus.setEnabled(true);
            btnInvite.setVisibility(View.VISIBLE);
        }

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isMember) { // joined table
                    rvMessages.setVisibility(View.VISIBLE);
                    etCompose.setVisibility(View.VISIBLE);
                    btnSend.setVisibility(View.VISIBLE);
                    String members = tvMembers.getText().toString();
                    members += ", @" + ParseUser.getCurrentUser().getUsername();
                    members = members.replaceAll("None\\, ", "");
                    tvMembers.setText(members);
                    spnStatus.setEnabled(true);
                    btnInvite.setVisibility(View.VISIBLE);

                    TableUtils.removeFromPreviousTable(ParseUser.getCurrentUser());
                    table.addMate(ParseUser.getCurrentUser());
                    UserUtils.setCurrentTable(ParseUser.getCurrentUser(), table);
                    btnJoin.setText("Leave");
                    TableUtils.saveTable(table);
                    UserUtils.saveUser(ParseUser.getCurrentUser());
                } else { // left table
                    rvMessages.setVisibility(View.INVISIBLE);
                    etCompose.setVisibility(View.INVISIBLE);
                    btnSend.setVisibility(View.INVISIBLE);
                    String members = tvMembers.getText().toString();
                    Log.d(TAG, members);
                    members = members.replaceAll("\\, \\@" + ParseUser.getCurrentUser().getUsername(), "");
                    members = members.replaceAll("\\@" + ParseUser.getCurrentUser().getUsername(), "");
                    Log.d(TAG, ", @" + ParseUser.getCurrentUser().getUsername());
                    tvMembers.setText(members);
                    spnStatus.setEnabled(false);
                    btnInvite.setVisibility(View.INVISIBLE);

                    TableUtils.removeFromPreviousTable(ParseUser.getCurrentUser());
                    UserUtils.removeCurrentTable(ParseUser.getCurrentUser());
                    UserUtils.saveUser(ParseUser.getCurrentUser());
                    btnJoin.setText("Join");
                }
                isMember = !isMember;
            }
        });

        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void getMessages() {
        List<Message> foundMessage = (List<Message>) table.getChat();
        adapter.clear();
        adapter.addAll(foundMessage);
    }

    private void saveMessage(Message message) {
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving new message", e);
                } else {
                    Log.i(TAG, "Success saving new message");
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        table.setStatus(parent.getItemAtPosition(pos).toString());
        TableUtils.saveTable(table);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
}