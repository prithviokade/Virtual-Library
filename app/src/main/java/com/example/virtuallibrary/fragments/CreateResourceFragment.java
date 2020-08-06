package com.example.virtuallibrary.fragments;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.TableUtils;
import com.example.virtuallibrary.activities.ResourceDetailsActivity;
import com.example.virtuallibrary.databinding.FragmentCreateResourceBinding;
import com.example.virtuallibrary.models.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class CreateResourceFragment extends Fragment {

    FragmentCreateResourceBinding binding;

    public static final String TAG = "CreatePostFragment";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 112;
    public static final int PICKFILE_RESULT_CODE = 113;
    public String photoFileName = "photo.jpg";
    File photoFile;
    EditText etCaption;
    EditText etSubject;
    Button btnCamera;
    Button btnPost;
    Button btnLink;
    Button btnFile;
    ImageButton btnAttach;
    ImageView ivPhoto;
    EditText etLink;
    TextView tvFilename;
    String filename;

    public CreateResourceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateResourceBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etCaption = binding.etCaption;
        btnCamera = binding.btnCamera;
        btnPost = binding.btnPost;
        ivPhoto = binding.ivPhoto;
        etLink = binding.etLink;
        etSubject = binding.etSubject;
        tvFilename = binding.tvFilename;
        btnLink = binding.btnLink;
        btnFile = binding.btnFile;
        btnAttach = binding.btnAttach;

        initializeView();

        btnAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCamera.setVisibility(View.VISIBLE);
                btnLink.setVisibility(View.VISIBLE);
                btnFile.setVisibility(View.VISIBLE);
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etLink.setVisibility(View.VISIBLE);
            }
        });

        btnFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, PICKFILE_RESULT_CODE);
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String caption = etCaption.getText().toString();
                if (caption.isEmpty()) {
                    Toast.makeText(getContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String subject = etSubject.getText().toString();
                if (subject.isEmpty()) {
                    Toast.makeText(getContext(), "Subject cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ivPhoto.getDrawable() == null) { photoFile = null; }
                String link = etLink.getText().toString();
                String filepath = tvFilename.getText().toString();
                ParseUser currentUser = ParseUser.getCurrentUser();
                Post createdPost = saveNewPost(caption, subject, currentUser, photoFile, link);
                initializeView();
                Intent intent = new Intent(getContext(), ResourceDetailsActivity.class);
                intent.putExtra(Post.TAG, Parcels.wrap(createdPost));
                startActivity(intent);
            }
        });

    }

    private void initializeView() {
        etCaption.setText("");
        etSubject.setText("");
        etLink.setText("");
        btnFile.setVisibility(View.INVISIBLE);
        btnLink.setVisibility(View.INVISIBLE);
        btnCamera.setVisibility(View.INVISIBLE);
        ivPhoto.setVisibility(View.GONE);
        etLink.setVisibility(View.GONE);
        tvFilename.setVisibility(View.GONE);
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){ }
        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // Load the taken image into a preview
                ivPhoto.setVisibility(View.VISIBLE);
                ivPhoto.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == PICKFILE_RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                Uri fileuri = data.getData();
                filename = fileuri.toString();
                tvFilename.setVisibility(View.VISIBLE);
                tvFilename.setText(Html.fromHtml("<font color=#000000>" + getString(R.string.attached) + " " + "</font>" + filename));
                tvFilename.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                        pdfIntent.setDataAndType(Uri.parse(filename), "application/pdf");
                        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(pdfIntent);
                    }
                });
            } else {
                Toast.makeText(getContext(), "File wasn't attatched!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private Post saveNewPost(String caption, String subject, ParseUser user, File photoFile, String link) {
        Post post = new Post();
        post.setCaption(caption);
        post.setSubject(subject);
        if (photoFile != null) { post.setImage(new ParseFile(photoFile)); }
        if (!link.isEmpty()) { post.setLink(link); }
        if (filename != null && !filename.isEmpty()) { post.setFileName(filename); }
        post.setUser(user);
        post.saveInBackground();
        return post;
    }

}