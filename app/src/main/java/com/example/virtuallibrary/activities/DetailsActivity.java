package com.example.virtuallibrary.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.virtuallibrary.R;
import com.facebook.messenger.MessengerThreadParams;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;

public class DetailsActivity extends AppCompatActivity {

    // This is the request code that the SDK uses for startActivityForResult. See the code below
    // that references it. Messenger currently doesn't return any data back to the calling
    // application.
    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 1;

    private Toolbar mToolbar;
    private View mMessengerButton;
    private MessengerThreadParams mThreadParams;
    private boolean mPicking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mMessengerButton = findViewById(R.id.messenger_send_button);

        mToolbar.setTitle(R.string.app_name);

        // If we received Intent.ACTION_PICK from Messenger, we were launched from a composer shortcut
        // or the reply flow.
        Intent intent = getIntent();
        if (Intent.ACTION_PICK.equals(intent.getAction())) {
            mThreadParams = MessengerUtils.getMessengerThreadParamsForIntent(intent);
            mPicking = true;

            // Note, if mThreadParams is non-null, it means the activity was launched from Messenger.
            // It will contain the metadata associated with the original content, if there was content.
        }

        mMessengerButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onMessengerButtonClicked();
                    }
                });
    }

    private void onMessengerButtonClicked() {
        // The URI can reference a file://, content://, or android.resource. Here we use
        // android.resource for sample purposes.
        Uri uri =
                Uri.parse("android.resource://com.example.virtuallibrary/" + R.drawable.tree);

        // Create the parameters for what we want to send to Messenger.
        ShareToMessengerParams shareToMessengerParams =
                ShareToMessengerParams.newBuilder(uri, "image/jpeg")
                        .setMetaData("{ \"image\" : \"tree\" }")
                        .build();

        if (mPicking) {
            // If we were launched from Messenger, we call MessengerUtils.finishShareToMessenger to return
            // the content to Messenger.
            MessengerUtils.finishShareToMessenger(this, shareToMessengerParams);
        } else {
            // Otherwise, we were launched directly (for example, user clicked the launcher icon). We
            // initiate the broadcast flow in Messenger. If Messenger is not installed or Messenger needs
            // to be upgraded, this will direct the user to the play store.
            MessengerUtils.shareToMessenger(
                    this, REQUEST_CODE_SHARE_TO_MESSENGER, shareToMessengerParams);
        }
    }
}