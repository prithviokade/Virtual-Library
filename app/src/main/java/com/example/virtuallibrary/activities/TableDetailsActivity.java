package com.example.virtuallibrary.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.TableUtils;
import com.example.virtuallibrary.UserUtils;
import com.example.virtuallibrary.adapters.MessageAdapter;
import com.example.virtuallibrary.databinding.ActivityTableDetailsBinding;
import com.example.virtuallibrary.fragments.TableFragment;
import com.example.virtuallibrary.models.Message;
import com.example.virtuallibrary.models.Table;
import com.parse.ParseUser;

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

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_notification);

        table = (Table) Parcels.unwrap(getIntent().getParcelableExtra(TableUtils.TAG));

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
        Fragment tableFragment = new TableFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TableUtils.TAG, table);
        tableFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.flContainer, tableFragment).commit();

        List<ParseUser> mates = table.getMates();
        String members = "";
        for (ParseUser user : mates) {
            members += "@";
            members += UserUtils.getUsername(user);
            members += ", ";
        }
        if (!members.isEmpty()) { tvMembers.setText(members.substring(0, members.length() - 2)); }
        else { tvMembers.setText(R.string.none); }

        if (table.getVisiting()) {
            tvVisitors.setText(R.string.allowed);
        } else {
            tvVisitors.setText(R.string.not_allowed);
        }

        String topic = table.getTopic();
        String type = table.getType();
        String description = table.getDescription();
        String fullDescription = getString(R.string.description_pt1) + " " + type + " " + getString(R.string.description_pt2) + " " + topic + ".\n" + description;
        tvDescription.setText(fullDescription);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = etCompose.getText().toString();
                Message newMessage = new Message();
                newMessage.setText(text);
                newMessage.setSender(ParseUser.getCurrentUser());
                newMessage.saveInBackground();
                table.addChat(newMessage);
                table.saveInBackground();
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
            btnJoin.setText(R.string.leave);
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
                    btnJoin.setText(R.string.leave);
                    table.saveInBackground();
                    ParseUser.getCurrentUser().saveInBackground();
                } else { // left table
                    rvMessages.setVisibility(View.INVISIBLE);
                    etCompose.setVisibility(View.INVISIBLE);
                    btnSend.setVisibility(View.INVISIBLE);
                    String members = tvMembers.getText().toString();
                    members = members.replaceAll("\\, \\@" + ParseUser.getCurrentUser().getUsername(), "");
                    members = members.replaceAll("\\@" + ParseUser.getCurrentUser().getUsername(), "");
                    tvMembers.setText(members);
                    spnStatus.setEnabled(false);
                    btnInvite.setVisibility(View.INVISIBLE);

                    TableUtils.removeFromPreviousTable(ParseUser.getCurrentUser());
                    UserUtils.removeCurrentTable(ParseUser.getCurrentUser());
                    ParseUser.getCurrentUser().saveInBackground();
                    btnJoin.setText(R.string.join);
                }
                isMember = !isMember;
            }
        });

        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TableDetailsActivity.this, InviteActivity.class);
                intent.putExtra(TableUtils.TAG, Parcels.wrap(table));
                startActivity(intent);
            }
        });
    }

    private void getMessages() {
        List<Message> foundMessage = (List<Message>) table.getChat();
        adapter.clear();
        adapter.addAll(foundMessage);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        table.setStatus(parent.getItemAtPosition(pos).toString());
        table.saveInBackground();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
}