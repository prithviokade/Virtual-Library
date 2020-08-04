package com.example.virtuallibrary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.virtuallibrary.R;
import com.example.virtuallibrary.models.Post;

import java.util.List;

public class ProfileResourceAdapter extends RecyclerView.Adapter<ProfileResourceAdapter.ViewHolder> {
    Context context;
    List<Post> posts;
    public static final String TAG = "ProfileResourceAdapter";

    public ProfileResourceAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_profile_resource, parent, false);
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

        TextView tvCaption;
        TextView tvSubject;
        TextView tvLink;
        TextView tvFile;
        ImageView ivPost;
        RelativeLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            ivPost = itemView.findViewById(R.id.ivPost);
            container = itemView.findViewById(R.id.container);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvLink = itemView.findViewById(R.id.tvLink);
            tvFile = itemView.findViewById(R.id.tvFile);
        }

        public void bind(final Post post) {
            initializeView();

            tvCaption.setText(post.getCaption());
            tvSubject.setText(post.getSubject());

            if (post.getImage() != null) {
                ivPost.setVisibility(View.VISIBLE);
                Glide.with(context).load(post.getImage().getUrl()).into(ivPost);
            }
            if (post.getLink() != null && !post.getLink().isEmpty()) {
                tvLink.setVisibility(View.VISIBLE);
                tvLink.setText(post.getLink());
            }
            if (post.getFile() != null) {
                tvFile.setVisibility(View.VISIBLE);
                tvFile.setText(post.getFileName());
            }

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
    public void addAll(List<Post> listPosts) {
        posts.addAll(listPosts);
        notifyDataSetChanged();
    }

}


