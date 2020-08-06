package com.example.virtuallibrary.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.TableUtils;
import com.example.virtuallibrary.activities.TableDetailsActivity;
import com.example.virtuallibrary.databinding.FragmentMusicBinding;
import com.example.virtuallibrary.databinding.FragmentUserInvitesBinding;
import com.example.virtuallibrary.models.Invite;
import com.example.virtuallibrary.models.Table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicFragment extends DialogFragment {

    public MusicFragment() {
        // Required empty public constructor
    }

    public static MusicFragment newInstance(Table table) {
        MusicFragment fragment = new MusicFragment();
        Bundle args = new Bundle();
        args.putParcelable(TableUtils.TAG, table);
        fragment.setArguments(args);
        return fragment;
    }

    FragmentMusicBinding binding;
    Table table;
    ImageView ivPlay;
    SeekBar seekBar;
    TextView tvCurrentTime;
    TextView tvTotalTime;
    ImageView ivAddSong;
    EditText etSong;
    ImageView ivDone;
    MediaPlayer player;
    Handler handler = new Handler();
    List<String> songs;
    String currentSong;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMusicBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        player = ((TableDetailsActivity) getActivity()).mediaPlayer;
        table = getArguments().getParcelable(TableUtils.TAG);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivPlay = binding.ivPlay;
        seekBar = binding.seekBar;
        tvCurrentTime = binding.tvCurrentTime;
        tvTotalTime = binding.tvTotalTime;
        ivAddSong = binding.ivAddSong;
        etSong = binding.etSong;
        ivDone = binding.ivDone;

        currentSong = table.getCurrentSong();
        songs = table.getSongs();

        seekBar.setMax(100);

        if (player.isPlaying()) {
            updateSeekBar();
            ivPlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
        } else {
            ivPlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
        }

        tvTotalTime.setText(millisecondsToTimer(player.getDuration()));

        ivDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String song = etSong.getText().toString();
                table.addSong(song);
                table.saveInBackground();
            }
        });

        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player.isPlaying()) {
                    handler.removeCallbacks(updater);
                    player.pause();
                    ivPlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                } else {
                    player.start();
                    ivPlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                    updateSeekBar();
                }
            }
        });

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                int currentSongIndex = findSong(songs, currentSong);
                songs = table.getSongs();

                if (currentSongIndex + 1 < songs.size()) {
                    currentSong = songs.get(currentSongIndex + 1);
                    table.setCurrentSong(currentSong);
                    table.saveInBackground();
                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(currentSong);
                        mediaPlayer.prepare();
                        tvTotalTime.setText(millisecondsToTimer(player.getDuration()));
                        updateSeekBar();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    currentSong = songs.get(0);
                    table.setCurrentSong(currentSong);
                    table.saveInBackground();
                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(currentSong);
                        mediaPlayer.prepare();
                        tvTotalTime.setText(millisecondsToTimer(player.getDuration()));
                        updateSeekBar();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
            long currentDuration = player.getCurrentPosition();
            tvCurrentTime.setText(millisecondsToTimer(currentDuration));
        }
    };

    private void updateSeekBar() {
        if (player.isPlaying()) {
            seekBar.setProgress((int) (((float) player.getCurrentPosition() / player.getDuration()) * 100));
            handler.postDelayed(updater, 1000);
        }
    }

    private String millisecondsToTimer(long milliseconds) {
        String timerString = "";
        String secondString;

        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {
            timerString = hours + ":";
        }
        if (seconds < 10) {
            secondString = "0" + seconds;
        } else {
            secondString = "" + seconds;
        }

        timerString = timerString + minutes + ":" + secondString;
        return timerString;
    }

    private int findSong(List<String> songs, String currentSong) {
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).equals(currentSong)) {
                return i;
            }
        }
        return -1;
    }
}