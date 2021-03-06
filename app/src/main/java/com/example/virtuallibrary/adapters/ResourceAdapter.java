package com.example.virtuallibrary.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.virtuallibrary.R;
import com.example.virtuallibrary.TableUtils;
import com.example.virtuallibrary.UserUtils;
import com.example.virtuallibrary.activities.ProfileActivity;
import com.example.virtuallibrary.activities.ResourceDetailsActivity;
import com.example.virtuallibrary.models.Post;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.ViewHolder> {

    Context context;
    List<Post> posts;
    Fragment fragment;

    public ResourceAdapter(Fragment fragment, List<Post> posts) {
        this.context = fragment.getContext();
        this.fragment = fragment;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvScreenName;
        TextView tvCaption;
        TextView tvSubject;
        TextView tvLink;
        TextView tvFile;
        ImageView ivPost;
        ImageView ivProfile;
        TextView tvCreated;
        RelativeLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            ivPost = itemView.findViewById(R.id.ivPost);
            ivProfile = itemView.findViewById(R.id.ivProfPic);
            tvCreated = itemView.findViewById(R.id.tvCreatedAt);
            container = itemView.findViewById(R.id.container);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvLink = itemView.findViewById(R.id.tvLink);
            tvFile = itemView.findViewById(R.id.tvFile);
        }

        public void bind(final Post post) {
            initializeView();

            tvScreenName.setText(UserUtils.getUsername(post.getUser()));
            tvCaption.setText(post.getCaption());
            tvSubject.setText(post.getSubject());
            Date time = post.getCreatedAt();
            DateFormat dateFormat = new SimpleDateFormat("h:mm aa EEE MMM dd yyyy");
            String dateString = dateFormat.format(time);
            tvCreated.setText(dateString);
            ParseFile profile = UserUtils.getProfilePicture(post.getUser());
            if (profile != null) {
                Glide.with(context).load(profile.getUrl()).transform(new CircleCrop()).into(ivProfile);
            } else {
                Glide.with(context).load(R.drawable.ic_baseline_person_24_black).transform(new CircleCrop()).into(ivProfile);
            }
            if (post.getImage() != null) {
                ivPost.setVisibility(View.VISIBLE);
                Glide.with(context).load(post.getImage().getUrl()).into(ivPost);
            }
            if (post.getLink() != null && !post.getLink().isEmpty()) {
                tvLink.setVisibility(View.VISIBLE);
                tvLink.setText(Html.fromHtml("<font color=#000000>" + context.getString(R.string.linked) + " " + "</font>" + post.getLink()));
            }
            if (post.getFileName() != null) {
                tvFile.setVisibility(View.VISIBLE);
                tvFile.setText(Html.fromHtml("<font color=#000000>" + context.getString(R.string.attached) + " " + "</font>" + post.getFileName()));
            }

            tvScreenName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra(UserUtils.TAG, Parcels.wrap(post.getUser()));
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(fragment.getActivity(), (View) ivProfile, "profilepicture");
                    context.startActivity(intent, options.toBundle());
                }
            });

            ivProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra(UserUtils.TAG, Parcels.wrap(post.getUser()));
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(fragment.getActivity(), (View) ivProfile, "profilepicture");
                    context.startActivity(intent, options.toBundle());
                }
            });

            tvFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                    pdfIntent.setDataAndType(Uri.parse(post.getFileName()), "application/pdf");
                    pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(pdfIntent);
                }
            });

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ResourceDetailsActivity.class);
                    intent.putExtra(Post.TAG, Parcels.wrap(post));
                    context.startActivity(intent);
                }
            });
        }

        private void initializeView() {
            ivPost.setVisibility(View.GONE);
            tvLink.setVisibility(View.GONE);
            tvFile.setVisibility(View.GONE);
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

}

