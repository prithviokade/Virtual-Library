package com.example.virtuallibrary.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.virtuallibrary.R;
import com.example.virtuallibrary.TableUtils;
import com.example.virtuallibrary.UserUtils;
import com.example.virtuallibrary.databinding.ActivityResourceDetailsBinding;
import com.example.virtuallibrary.databinding.ActivityTableDetailsBinding;
import com.example.virtuallibrary.models.Post;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResourceDetailsActivity extends AppCompatActivity {

    public static final String TAG = "ResourceDetailsActivity";
    Post resource;

    TextView tvScreenName;
    TextView tvCaption;
    TextView tvSubject;
    TextView tvLink;
    TextView tvFile;
    ImageView ivPost;
    ImageView ivProfile;
    TextView tvCreated;
    ImageButton btnThumbsUp;
    ImageButton btnThumbsDown;
    ImageButton btnAddComment;
    ImageButton btnSendResource;
    ImageButton btnBookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityResourceDetailsBinding binding = ActivityResourceDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_default);

        tvScreenName = binding.tvScreenName;
        tvCaption = binding.tvCaption;
        tvSubject = binding.tvSubject;
        tvLink = binding.tvLink;
        tvFile = binding.tvFile;
        ivPost = binding.ivPost;
        ivProfile = binding.ivProfPic;
        tvCreated = binding.tvCreatedAt;
        btnThumbsUp = binding.btnThumbsUp;
        btnThumbsDown = binding.btnThumbsDown;
        btnAddComment = binding.btnAddComment;
        btnSendResource = binding.btnSendResource;
        btnBookmark = binding.btnBookmark;

        resource = Parcels.unwrap(getIntent().getParcelableExtra(Post.TAG));
        initializeView();

        tvScreenName.setText(UserUtils.getUsername(resource.getUser()));
        tvCaption.setText(resource.getCaption());
        tvSubject.setText(resource.getSubject());
        Date time = resource.getCreatedAt();
        if (time != null) {
            DateFormat dateFormat = new SimpleDateFormat("h:mm aa EEE MMM dd yyyy");
            String dateString = dateFormat.format(time);
            tvCreated.setText(dateString);
        }
        ParseFile profile = UserUtils.getProfilePicture(resource.getUser());
        if (profile != null) {
            Glide.with(this).load(profile.getUrl()).transform(new CircleCrop()).into(ivProfile);
        } else {
            Glide.with(this).load(R.drawable.ic_baseline_person_24_black).transform(new CircleCrop()).into(ivProfile);
        }
        if (resource.getImage() != null) {
            ivPost.setVisibility(View.VISIBLE);
            Glide.with(this).load(resource.getImage().getUrl()).into(ivPost);
        }
        if (resource.getLink() != null && !resource.getLink().isEmpty()) {
            tvLink.setVisibility(View.VISIBLE);
            tvLink.setText(Html.fromHtml("<font color=#000000>" + getString(R.string.linked) + " " + "</font>" + resource.getLink()));
        }
        if (resource.getFileName() != null) {
            tvFile.setVisibility(View.VISIBLE);
            tvFile.setText(Html.fromHtml("<font color=#000000>" + getString(R.string.attached) + " " + "</font>" + resource.getFileName()));
        }

        tvScreenName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResourceDetailsActivity.this, ProfileActivity.class);
                intent.putExtra(UserUtils.TAG, Parcels.wrap(resource.getUser()));
                startActivity(intent);
            }
        });

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResourceDetailsActivity.this, ProfileActivity.class);
                intent.putExtra(UserUtils.TAG, Parcels.wrap(resource.getUser()));
                startActivity(intent);
            }
        });

        tvFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                pdfIntent.setDataAndType(Uri.parse(resource.getFileName()), "application/pdf");
                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(pdfIntent);
            }
        });
    }

    private void initializeView() {
        ivPost.setVisibility(View.GONE);
        tvLink.setVisibility(View.GONE);
        tvFile.setVisibility(View.GONE);
    }
}