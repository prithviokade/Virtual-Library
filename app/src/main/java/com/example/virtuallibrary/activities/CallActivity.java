package com.example.virtuallibrary.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.telecom.Call;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.token.RtcTokenGenerator;
import com.example.virtuallibrary.TableUtils;
import com.example.virtuallibrary.databinding.ActivityCallBinding;
import com.example.virtuallibrary.models.Table;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.BeautyOptions;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

public class CallActivity extends AppCompatActivity {

    private static final int PERMISSION_REQ_ID = 22;
    public static final int DEFAULT_CONTRAST = 1;
    public static final float DEFAULT_LIGHTNESS = .7f;
    public static final float DEFAULT_SMOOTHNESS = .5f;
    public static final float DEFAULT_REDNESS = .1f;
    public static final String TAG = "CallActivity";
    private RtcEngine mRtcEngine;
    private FrameLayout mLocalContainer;
    private SurfaceView mLocalView;
    private FrameLayout mRemoteContainer;
    Table table;
    ImageButton btnOffSound;
    ImageButton btnOffVideo;
    ImageButton btnEndCall;
    ImageButton btnAddFilter;
    ImageView blankProfile;
    ImageView remoteBlankProfile;
    boolean audioMuted = false;
    boolean videoMuted = true;
    boolean filterEnabled = false;

    int usersPresent = 0;
    List<SurfaceView> remoteUserViews = new ArrayList<>();
    List<Integer> remoteUserUid = new ArrayList<>();
    List<ImageView> remoteUserBlankViews = new ArrayList<>();
    List<Integer> remoteUserBlankUid = new ArrayList<>();

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
        btnOffSound = binding.btnOffSound;
        btnOffVideo = binding.btnOffVideo;
        btnEndCall = binding.btnEndCall;
        btnAddFilter = binding.btnAddFilter;

        table = (Table) Parcels.unwrap(getIntent().getParcelableExtra(TableUtils.TAG));

        // If all the permissions are granted, initialize the RtcEngine object and join a channel.
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
            initializeEngine();
            setupVideoConfig();
            setupLocalVideo();

            joinChannel(0);
        }

        btnOffSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioMuted = !audioMuted;
                mRtcEngine.muteLocalAudioStream(audioMuted);
                if (audioMuted) {
                    btnOffSound.setImageResource(R.drawable.ic_baseline_mic_off_24);
                } else {
                    btnOffSound.setImageResource(R.drawable.ic_baseline_mic_24);
                }
            }
        });

        btnOffVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int muteVideoResource;
                videoMuted = !videoMuted;
                mRtcEngine.enableLocalVideo(videoMuted);
                if (!videoMuted) {
                    muteVideoResource = R.drawable.ic_baseline_videocam_off_24;
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mLocalView.getLayoutParams();
                    blankProfile = new ImageView(CallActivity.this);
                    blankProfile.setImageResource(R.drawable.ic_baseline_person_24_black);
                    blankProfile.setBackgroundColor(getColor(R.color.lightGrey));
                    blankProfile.setLayoutParams(params);
                    blankProfile.setVisibility(View.VISIBLE);
                    mLocalContainer.addView(blankProfile);
                } else {
                    muteVideoResource = R.drawable.ic_baseline_videocam_24;
                    blankProfile.setVisibility(View.INVISIBLE);
                }
                btnOffVideo.setImageResource(muteVideoResource);

            }
        });

        btnEndCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaveChannel();
                finish();
            }
        });

        mLocalContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRtcEngine.switchCamera();
            }
        });

        btnAddFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterEnabled = !filterEnabled;
                if (filterEnabled) {
                    mRtcEngine.setBeautyEffectOptions(true, new BeautyOptions(DEFAULT_CONTRAST, DEFAULT_LIGHTNESS, DEFAULT_SMOOTHNESS, DEFAULT_REDNESS));
                    btnAddFilter.setImageResource(R.drawable.btn_filter);
                } else {
                    mRtcEngine.setBeautyEffectOptions(false, new BeautyOptions(DEFAULT_CONTRAST, DEFAULT_LIGHTNESS, DEFAULT_SMOOTHNESS, DEFAULT_REDNESS));
                    btnAddFilter.setImageResource(R.drawable.btn_filter_off);
                }
            }
        });
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
                }
            });
        }

        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "triggered");
                    setupRemoteVideo(uid);
                }
            });
        }

        @Override
        public void onUserOffline(final int uid, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    removeUserLeft(uid);
                }
            });
        }
    };

    public int findUserIndex(int uid, List<Integer> userUid) {
        int userIndex = 0;
        for (int i = 0; i < userUid.size(); i++) {
            if (userUid.get(i) == uid) {
                userIndex = i;
                break;
            }
        }
        return userIndex;
    }

    public void removeUserLeft(int uid) {
        int userIndex = findUserIndex(uid, remoteUserUid);
        SurfaceView userLeftView = remoteUserViews.get(userIndex);
        userLeftView.setVisibility(View.GONE);
        remoteUserUid.remove(userIndex);
        remoteUserViews.remove(userIndex);
        usersPresent--;
        resizeRemoteVideos();

    }

    // Initialize the RtcEngine object.
    private void initializeEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception e) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        leaveChannel();
        RtcEngine.destroy();
    }

    private void leaveChannel() {
        // Leave the current channel.
        mRtcEngine.leaveChannel();
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

    private void setupVideoConfig() {
        mRtcEngine.enableVideo();

        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private void setupRemoteVideo(int uid) {
        if (remoteUserUid.contains(uid)) {
            return;
        }

        usersPresent += 1;

        SurfaceView newRemoteView = RtcEngine.CreateRendererView(getBaseContext());
        mRemoteContainer.addView(newRemoteView);

        // Set the remote video view.
        mRtcEngine.setupRemoteVideo(new VideoCanvas(newRemoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));

        remoteUserUid.add(uid);
        remoteUserViews.add(newRemoteView);
        resizeRemoteVideos();
    }

    private void resizeRemoteVideos() {
        float density = Resources.getSystem().getDisplayMetrics().density;
        int bottomNavBarHeight = (int) (48 * density);
        int topActionBarHeight = getSupportActionBar().getHeight();
        int screen_height = getScreenHeight() - bottomNavBarHeight - topActionBarHeight;
        int screen_width = getScreenWidth();

        if (usersPresent == 0) {
            FrameLayout.LayoutParams parms0 = (FrameLayout.LayoutParams) mLocalView.getLayoutParams();
            parms0.height = screen_height;
            parms0.width = screen_width;
            parms0.setMargins(0, 0, 0, 0);
            mLocalView.setLayoutParams(parms0);
        }

        if (usersPresent == 1) {
            FrameLayout.LayoutParams parms0 = (FrameLayout.LayoutParams) mLocalView.getLayoutParams();
            parms0.height = 400;
            parms0.width = 300;
            int padding = 20;
            parms0.leftMargin = screen_width - parms0.width - padding;
            parms0.topMargin = 20;
            mLocalView.setLayoutParams(parms0);

            FrameLayout.LayoutParams parms1 = (FrameLayout.LayoutParams) remoteUserViews.get(0).getLayoutParams();
            parms1.height = screen_height;
            parms1.width = screen_width;
            parms1.setMargins(0, 0, 0, 0);
            remoteUserViews.get(0).setLayoutParams(parms1);
        }

        if (usersPresent == 2) {
            FrameLayout.LayoutParams parms1 = (FrameLayout.LayoutParams) remoteUserViews.get(0).getLayoutParams();
            parms1.height = screen_height / 2;
            parms1.width = screen_width;
            remoteUserViews.get(0).setLayoutParams(parms1);

            FrameLayout.LayoutParams parms2 = (FrameLayout.LayoutParams) remoteUserViews.get(1).getLayoutParams();
            parms2.height = screen_height / 2;
            parms2.width = screen_width;
            parms2.topMargin = parms1.height;
            remoteUserViews.get(1).setLayoutParams(parms2);
        }

        if (usersPresent == 3 || usersPresent == 4) {
            if (usersPresent == 3) {
                FrameLayout.LayoutParams parms1 = (FrameLayout.LayoutParams) remoteUserViews.get(0).getLayoutParams();
                parms1.height = screen_height / 2;
                parms1.width = screen_width;
                remoteUserViews.get(0).setLayoutParams(parms1);
            }
            if (usersPresent == 4) {
                FrameLayout.LayoutParams parms1 = (FrameLayout.LayoutParams) remoteUserViews.get(0).getLayoutParams();
                parms1.height = screen_height / 2;
                parms1.width = screen_width / 2;
                remoteUserViews.get(0).setLayoutParams(parms1);

                FrameLayout.LayoutParams parms4 = (FrameLayout.LayoutParams) remoteUserViews.get(3).getLayoutParams();
                parms4.height = screen_height / 2;
                parms4.width = screen_width / 2;
                parms4.leftMargin = parms4.width;
                remoteUserViews.get(3).setLayoutParams(parms4);
            }
            FrameLayout.LayoutParams parms2 = (FrameLayout.LayoutParams) remoteUserViews.get(1).getLayoutParams();
            parms2.height = screen_height / 2;
            parms2.width = screen_width / 2;
            parms2.topMargin = parms2.height;
            remoteUserViews.get(1).setLayoutParams(parms2);

            FrameLayout.LayoutParams parms3 = (FrameLayout.LayoutParams) remoteUserViews.get(2).getLayoutParams();
            parms3.height = screen_height / 2;
            parms3.width = screen_width / 2;
            parms3.leftMargin = parms3.width;
            parms3.topMargin = parms3.height;
            remoteUserViews.get(2).setLayoutParams(parms3);
        }

        if (usersPresent == 5 || usersPresent == 6 || usersPresent == 7) {
            FrameLayout.LayoutParams parms1 = (FrameLayout.LayoutParams) remoteUserViews.get(0).getLayoutParams();
            parms1.height = screen_height / 3;
            parms1.width = screen_width;
            remoteUserViews.get(0).setLayoutParams(parms1);
            if (usersPresent != 7) {
                FrameLayout.LayoutParams parms2 = (FrameLayout.LayoutParams) remoteUserViews.get(1).getLayoutParams();
                parms2.height = screen_height / 3;
                parms2.width = screen_width / 2;
                parms2.topMargin = parms2.height;
                remoteUserViews.get(1).setLayoutParams(parms2);

                FrameLayout.LayoutParams parms3 = (FrameLayout.LayoutParams) remoteUserViews.get(2).getLayoutParams();
                parms3.height = screen_height / 2;
                parms3.width = screen_width / 2;
                parms3.topMargin = parms3.height;
                parms3.leftMargin = parms3.width;
                remoteUserViews.get(2).setLayoutParams(parms3);
            } else {
                FrameLayout.LayoutParams parms2 = (FrameLayout.LayoutParams) remoteUserViews.get(1).getLayoutParams();
                parms2.height = screen_height / 3;
                parms2.width = screen_width / 3;
                parms2.topMargin = parms2.height;
                remoteUserViews.get(1).setLayoutParams(parms2);

                FrameLayout.LayoutParams parms3 = (FrameLayout.LayoutParams) remoteUserViews.get(2).getLayoutParams();
                parms3.height = screen_height / 2;
                parms3.width = screen_width / 3;
                parms3.topMargin = parms3.height;
                parms3.leftMargin = parms3.width;
                remoteUserViews.get(2).setLayoutParams(parms3);

                FrameLayout.LayoutParams parms7 = (FrameLayout.LayoutParams) remoteUserViews.get(6).getLayoutParams();
                parms7.height = screen_height / 2;
                parms7.width = screen_width / 3;
                parms7.topMargin = parms7.height;
                parms7.leftMargin = parms7.width * 2;
                remoteUserViews.get(6).setLayoutParams(parms7);
            }
            if (usersPresent == 6) {
                FrameLayout.LayoutParams parms4 = (FrameLayout.LayoutParams) remoteUserViews.get(3).getLayoutParams();
                parms4.height = screen_height / 3;
                parms4.width = screen_width / 2;
                parms4.topMargin = parms4.height * 2;
                remoteUserViews.get(3).setLayoutParams(parms4);

                FrameLayout.LayoutParams parms5 = (FrameLayout.LayoutParams) remoteUserViews.get(4).getLayoutParams();
                parms5.height = screen_height / 2;
                parms5.width = screen_width / 2;
                parms5.topMargin = parms5.height * 2;
                parms5.leftMargin = parms5.width;
                remoteUserViews.get(4).setLayoutParams(parms5);
            } else {
                FrameLayout.LayoutParams parms4 = (FrameLayout.LayoutParams) remoteUserViews.get(3).getLayoutParams();
                parms4.height = screen_height / 3;
                parms4.width = screen_width / 3;
                parms4.topMargin = parms4.height * 2;
                remoteUserViews.get(3).setLayoutParams(parms4);

                FrameLayout.LayoutParams parms5 = (FrameLayout.LayoutParams) remoteUserViews.get(4).getLayoutParams();
                parms5.height = screen_height / 2;
                parms5.width = screen_width / 3;
                parms5.topMargin = parms5.height * 2;
                parms5.leftMargin = parms5.width;
                remoteUserViews.get(4).setLayoutParams(parms5);

                FrameLayout.LayoutParams parms6 = (FrameLayout.LayoutParams) remoteUserViews.get(5).getLayoutParams();
                parms6.height = screen_height / 2;
                parms6.width = screen_width / 3;
                parms6.topMargin = parms6.height * 2;
                parms6.leftMargin = parms6.width * 2;
                remoteUserViews.get(5).setLayoutParams(parms6);
            }
        }

        if (usersPresent == 8 || usersPresent == 9) {
            FrameLayout.LayoutParams parms1 = (FrameLayout.LayoutParams) remoteUserViews.get(0).getLayoutParams();
            parms1.height = screen_height / 3;
            parms1.width = screen_width / 3;
            remoteUserViews.get(0).setLayoutParams(parms1);

            FrameLayout.LayoutParams parms2 = (FrameLayout.LayoutParams) remoteUserViews.get(1).getLayoutParams();
            parms2.height = screen_height / 3;
            parms2.width = screen_width / 3;
            parms2.leftMargin = parms2.width;
            remoteUserViews.get(1).setLayoutParams(parms2);

            FrameLayout.LayoutParams parms3 = (FrameLayout.LayoutParams) remoteUserViews.get(2).getLayoutParams();
            parms3.height = screen_height / 3;
            parms3.width = screen_width / 3;
            parms3.topMargin = parms3.height;
            remoteUserViews.get(2).setLayoutParams(parms3);

            FrameLayout.LayoutParams parms4 = (FrameLayout.LayoutParams) remoteUserViews.get(3).getLayoutParams();
            parms4.height = screen_height / 3;
            parms4.width = screen_width / 3;
            parms4.topMargin = parms4.height;
            parms4.leftMargin = parms4.width;
            remoteUserViews.get(3).setLayoutParams(parms4);

            FrameLayout.LayoutParams parms5 = (FrameLayout.LayoutParams) remoteUserViews.get(4).getLayoutParams();
            parms5.height = screen_height / 3;
            parms5.width = screen_width / 3;
            parms5.topMargin = parms5.height;
            parms5.leftMargin = parms5.width * 2;
            remoteUserViews.get(4).setLayoutParams(parms5);

            FrameLayout.LayoutParams parms6 = (FrameLayout.LayoutParams) remoteUserViews.get(5).getLayoutParams();
            parms6.height = screen_height / 3;
            parms6.width = screen_width / 3;
            parms6.topMargin = parms6.height * 2;
            remoteUserViews.get(5).setLayoutParams(parms6);

            FrameLayout.LayoutParams parms7 = (FrameLayout.LayoutParams) remoteUserViews.get(6).getLayoutParams();
            parms7.height = screen_height / 3;
            parms7.width = screen_width / 3;
            parms7.topMargin = parms7.height * 2;
            parms7.leftMargin = parms7.width;
            remoteUserViews.get(6).setLayoutParams(parms7);

            FrameLayout.LayoutParams parms8 = (FrameLayout.LayoutParams) remoteUserViews.get(7).getLayoutParams();
            parms8.height = screen_height / 3;
            parms8.width = screen_width / 3;
            parms8.topMargin = parms8.height * 2;
            parms8.leftMargin = parms8.width * 2;
            remoteUserViews.get(7).setLayoutParams(parms8);

            if (usersPresent == 9) {
                FrameLayout.LayoutParams parms9 = (FrameLayout.LayoutParams) remoteUserViews.get(8).getLayoutParams();
                parms9.height = screen_height / 3;
                parms9.width = screen_width / 3;
                parms9.leftMargin = parms9.width * 2;
                remoteUserViews.get(8).setLayoutParams(parms9);
            }
        }
    }
}