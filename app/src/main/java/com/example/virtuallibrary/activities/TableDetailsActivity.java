package com.example.virtuallibrary.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.Toast;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.TableUtils;
import com.example.virtuallibrary.UserUtils;
import com.example.virtuallibrary.adapters.MessageAdapter;
import com.example.virtuallibrary.databinding.ActivityTableDetailsBinding;
import com.example.virtuallibrary.fragments.MusicFragment;
import com.example.virtuallibrary.fragments.TableFragment;
import com.example.virtuallibrary.fragments.UserInvitesFragment;
import com.example.virtuallibrary.models.Invite;
import com.example.virtuallibrary.models.Message;
import com.example.virtuallibrary.models.Table;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import org.parceler.Parcels;

import java.io.IOException;
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
    ImageButton btnVideoCall;
    public ImageButton btnPlayMusic;
    Button btnVisit;
    List<String> songs;
    String currentSong;
    public MediaPlayer mediaPlayer;

    List<Message> messages;
    MessageAdapter adapter;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    boolean isMember;
    boolean isVisiting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTableDetailsBinding binding = ActivityTableDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_notification);

        table = (Table) Parcels.unwrap(getIntent().getParcelableExtra(TableUtils.TAG));
        songs = table.getSongs();

        mediaPlayer = new MediaPlayer();

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
        btnVideoCall = binding.btnVideoCall;
        btnPlayMusic = binding.btnPlayMusic;
        btnVisit = binding.btnVisit;

        btnPlayMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentSong = table.getCurrentSong();
                if (currentSong == null || currentSong.isEmpty()) {
                    if (songs != null && songs.size() != 0) {
                        currentSong = songs.get(0);
                        table.setCurrentSong(currentSong);
                        table.saveInBackground();
                        prepareMediaPlayer();
                    } else {
                        Toast.makeText(TableDetailsActivity.this, "Please add songs to your table's playlist", Toast.LENGTH_SHORT).show();
                        showMusicDialog();
                        return;
                    }
                } else {
                    prepareMediaPlayer();
                }
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                        btnPlayMusic.setImageResource(R.drawable.ic_baseline_pause_24);
                        btnPlayMusic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (mediaPlayer.isPlaying()) {
                                    mediaPlayer.pause();
                                    btnPlayMusic.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                                } else {
                                    mediaPlayer.start();
                                    btnPlayMusic.setImageResource(R.drawable.ic_baseline_pause_24);
                                }
                            }
                        });
                    }
                });
            }
        });

        btnPlayMusic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showMusicDialog();
                return false;
            }
        });

        messages = new ArrayList<>();
        adapter = new MessageAdapter(this, messages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvMessages.setAdapter(adapter);
        rvMessages.setLayoutManager(linearLayoutManager);
        getMessages();
        if (messages.size() > 0) { rvMessages.scrollToPosition(messages.size() - 1); }
        ArrayAdapter<CharSequence> spnAdapter = ArrayAdapter.createFromResource(this, R.array.statustypes_array, R.layout.spinneritem);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnStatus.setAdapter(spnAdapter);
        spnStatus.setOnItemSelectedListener(this);

        liveQuery();

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
                messages.add(newMessage);
                adapter.notifyItemInserted(messages.size() - 1);
                rvMessages.smoothScrollToPosition(messages.size() - 1);
            }
        });

        rvMessages.setVisibility(View.INVISIBLE);
        etCompose.setVisibility(View.INVISIBLE);
        btnSend.setVisibility(View.INVISIBLE);
        spnStatus.setEnabled(false);
        btnInvite.setVisibility(View.GONE);
        btnVideoCall.setVisibility(View.INVISIBLE);
        btnPlayMusic.setVisibility(View.INVISIBLE);
        if (table.getVisiting()) {
            btnVisit.setVisibility(View.VISIBLE);
        } else {
            btnVisit.setVisibility(View.INVISIBLE);
        }

        isMember = table.containsUser(ParseUser.getCurrentUser());
        isVisiting = UserUtils.isVisiting(ParseUser.getCurrentUser(), table);
        if (isMember) { // current table
            btnJoin.setText(R.string.leave);
            rvMessages.setVisibility(View.VISIBLE);
            etCompose.setVisibility(View.VISIBLE);
            btnSend.setVisibility(View.VISIBLE);
            spnStatus.setEnabled(true);
            btnInvite.setVisibility(View.VISIBLE);
            btnVideoCall.setVisibility(View.VISIBLE);
            btnPlayMusic.setVisibility(View.VISIBLE);
            btnVisit.setVisibility(View.INVISIBLE);
        } else if (isVisiting) {
            btnVisit.setText(getString(R.string.end_visit));
            rvMessages.setVisibility(View.VISIBLE);
            etCompose.setVisibility(View.VISIBLE);
            btnSend.setVisibility(View.VISIBLE);
            btnVideoCall.setVisibility(View.VISIBLE);
            btnPlayMusic.setVisibility(View.VISIBLE);
        }

        btnVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVisiting) { // leaving table
                    UserUtils.removeVisitingTable(ParseUser.getCurrentUser(), table);
                    btnVisit.setText(getString(R.string.visit));

                    rvMessages.setVisibility(View.INVISIBLE);
                    etCompose.setVisibility(View.INVISIBLE);
                    btnSend.setVisibility(View.INVISIBLE);
                    btnVideoCall.setVisibility(View.INVISIBLE);
                    btnPlayMusic.setVisibility(View.INVISIBLE);
                } else {
                    UserUtils.setVisiting(ParseUser.getCurrentUser(), table);
                    btnVisit.setText(getString(R.string.end_visit));

                    rvMessages.setVisibility(View.VISIBLE);
                    etCompose.setVisibility(View.VISIBLE);
                    btnSend.setVisibility(View.VISIBLE);
                    btnVideoCall.setVisibility(View.VISIBLE);
                    btnPlayMusic.setVisibility(View.VISIBLE);
                }
                isVisiting = !isVisiting;
            }
        });

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
                    btnVideoCall.setVisibility(View.VISIBLE);
                    btnPlayMusic.setVisibility(View.VISIBLE);
                    btnVisit.setVisibility(View.INVISIBLE);
                    UserUtils.removeVisitingTable(ParseUser.getCurrentUser(), table);

                    TableUtils.removeFromPreviousTable(ParseUser.getCurrentUser());
                    table.addMate(ParseUser.getCurrentUser());
                    UserUtils.setCurrentTable(ParseUser.getCurrentUser(), table);
                    UserUtils.addJoinedSize(ParseUser.getCurrentUser(), table.getSize());
                    UserUtils.addJoinedType(ParseUser.getCurrentUser(), table.getType());
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
                    btnVideoCall.setVisibility(View.INVISIBLE);
                    btnPlayMusic.setVisibility(View.INVISIBLE);
                    if (table.getVisiting()) {
                        btnVisit.setVisibility(View.VISIBLE);
                        btnVisit.setText("Visit");
                    }

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

        btnVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TableDetailsActivity.this, CallActivity.class);
                intent.putExtra(TableUtils.TAG, Parcels.wrap(table));
                startActivity(intent);
            }
        });
    }

    private void showMusicDialog() {
        FragmentManager fm = getSupportFragmentManager();
        MusicFragment musicFragment = MusicFragment.newInstance(table);
        musicFragment.show(fm, "fragment_music");
    }

    private void liveQuery() {
        // Make sure the Parse server is setup to configured for live queries
        ParseLiveQueryClient parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();

        ParseQuery<Message> parseQuery = ParseQuery.getQuery(Message.class);

        // Connect to Parse server
        parseLiveQueryClient.subscribe(parseQuery).handleEvents(new SubscriptionHandling.HandleEventsCallback<Message>() {
            @Override
            public void onEvents(ParseQuery<Message> query, final SubscriptionHandling.Event event, final Message message) {
                Handler refresh = new Handler(Looper.getMainLooper());
                refresh.post(new Runnable() {
                    public void run() {
                        // handle message created
                        if (event == SubscriptionHandling.Event.CREATE) {
                            Log.d(TAG, message.getText());
                            if (!UserUtils.equals(ParseUser.getCurrentUser(), message.getSender()) && UserUtils.getCurrentTable(message.getSender()).equals(table)) {
                                messages.add(message);
                                adapter.notifyItemInserted(messages.size() - 1);
                                rvMessages.smoothScrollToPosition(messages.size() - 1);
                            }
                        }
                    }
                });
            }
        });
    }

    private void prepareMediaPlayer() {
        try {
            currentSong = table.getCurrentSong();
            mediaPlayer.setDataSource(currentSong);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    int currentSongIndex = findSong(songs, currentSong);
                    Log.d(TAG, " " + currentSongIndex);
                    songs = table.getSongs();

                    if (currentSongIndex + 1 < songs.size()) {
                        currentSong = songs.get(currentSongIndex + 1);
                        table.setCurrentSong(currentSong);
                        table.saveInBackground();
                        try {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(currentSong);
                            mediaPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d(TAG, "here");
                        currentSong = songs.get(0);
                        Log.d(TAG, currentSong);
                        table.setCurrentSong(currentSong);
                        table.saveInBackground();
                        try {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(currentSong);
                            mediaPlayer.prepare();
                        } catch (IOException e) {
                            Log.d(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private int findSong(List<String> songs, String currentSong) {
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).equals(currentSong)) {
                return i;
            }
        }
        return -1;
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