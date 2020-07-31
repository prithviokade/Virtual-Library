package com.example.virtuallibrary.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowMetrics;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.RtcTokenGenerator;
import com.example.virtuallibrary.TableUtils;
import com.example.virtuallibrary.databinding.ActivityCallBinding;
import com.example.virtuallibrary.models.Table;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

public class CallActivity extends AppCompatActivity {

    private static final int PERMISSION_REQ_ID = 22;
    public static final String TAG = "CallActivity";
    private RtcEngine mRtcEngine;
    private FrameLayout mLocalContainer;
    private SurfaceView mLocalView;
    private FrameLayout mRemoteContainer;
    Table table;

    int usersPresent = 0;
    List<SurfaceView> remoteUserViews = new ArrayList<>();

    // Ask for Android device permissions at runtime.
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCallBinding binding = ActivityCallBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mLocalContainer = binding.mLocalContainer;
        mRemoteContainer = binding.mRemoteContainer;
        table = (Table) Parcels.unwrap(getIntent().getParcelableExtra(TableUtils.TAG));

        // If all the permissions are granted, initialize the RtcEngine object and join a channel.
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
            initializeEngine();
            setupLocalVideo();

            joinChannel(0);
        }

    }

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        // This callback occurs when the local user successfully joins the channel.
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("agora", "Join channel success, uid: " + (uid & 0xFFFFFFFFL));
                }
            });
        }

        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("agora","First remote video decoded, uid: " + (uid & 0xFFFFFFFFL));
                    setupRemoteVideo(uid);
                }
            });
        }


    };

    // Initialize the RtcEngine object.
    private void initializeEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    public final void joinChannel(int uid) {
        String channel = table.getChannel();
        String accessToken = RtcTokenGenerator.getToken(channel);
        // String accessToken = getApplicationContext().getString(R.string.agora_access_token);
        if (TextUtils.equals(accessToken, "")) {
            accessToken = null; // no token
        }

        mRtcEngine.joinChannel(accessToken, channel, "OpenVCall", uid);
    }

    private void setupLocalVideo() {
        // Enable the video module.
        mRtcEngine.enableVideo();

        mLocalView = RtcEngine.CreateRendererView(getBaseContext());
        mLocalView.setZOrderMediaOverlay(true);
        mLocalContainer.addView(mLocalView);

        // Set the local video view.
        VideoCanvas localVideoCanvas = new VideoCanvas(mLocalView, VideoCanvas.RENDER_MODE_HIDDEN, 0);
        mRtcEngine.setupLocalVideo(localVideoCanvas);
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private void setupRemoteVideo(int uid) {
        usersPresent += 1;

        SurfaceView newRemoteView = RtcEngine.CreateRendererView(getBaseContext());
        mRemoteContainer.addView(newRemoteView);

        // Set the remote video view.
        mRtcEngine.setupRemoteVideo(new VideoCanvas(newRemoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        remoteUserViews.add(newRemoteView);
        resizeRemoteVideos();
    }

    private void resizeRemoteVideos() {
        int screen_height = getScreenHeight();
        int screen_width = getScreenWidth();

        // convert to for loop after testing is done
        if (usersPresent == 1) {
            FrameLayout.LayoutParams parms0 = (FrameLayout.LayoutParams) mLocalView.getLayoutParams();
            parms0.height = 400;
            parms0.width = 300;
            parms0.leftMargin = screen_width - 300 - 20;
            parms0.topMargin = 20;
            mLocalView.setLayoutParams(parms0);

            FrameLayout.LayoutParams parms = (FrameLayout.LayoutParams) remoteUserViews.get(0).getLayoutParams();
            parms.height = screen_height;
            parms.width = screen_width;
            remoteUserViews.get(0).setLayoutParams(parms);
        }

        if (usersPresent == 2) {
            FrameLayout.LayoutParams parms0 = (FrameLayout.LayoutParams) remoteUserViews.get(0).getLayoutParams();
            parms0.height = screen_height / 2 - 150;
            parms0.width = screen_width;
            remoteUserViews.get(0).setLayoutParams(parms0);

            FrameLayout.LayoutParams parms1 = (FrameLayout.LayoutParams) remoteUserViews.get(1).getLayoutParams();
            parms1.height = screen_height / 2 - 150;
            parms1.width = screen_width;
            parms1.topMargin = screen_height / 2 - 150;
            remoteUserViews.get(1).setLayoutParams(parms1);
        }
    }



}