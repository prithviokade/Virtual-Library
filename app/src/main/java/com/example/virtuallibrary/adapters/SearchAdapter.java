package com.example.virtuallibrary.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.example.virtuallibrary.UserUtils;
import com.example.virtuallibrary.activities.ProfileActivity;
import com.example.virtuallibrary.activities.ResourceDetailsActivity;
import com.example.virtuallibrary.models.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final int VIEW_TYPE_USER = 0;
    final int VIEW_TYPE_RESOURCE = 1;

    Context context;
    List<ParseUser> users;
    List<Post> resources;
    Fragment fragment;

    public SearchAdapter(Fragment fragment, List<ParseUser> users, List<Post> resources) {
        this.fragment = fragment;
        this.context = fragment.getContext();
        this.users = users;
        this.resources = resources;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_USER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_search_user, parent, false);
            return new UserViewHolder(view);
        }

        if (viewType == VIEW_TYPE_RESOURCE) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_profile_resource, parent, false);
            return new ResourceViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  UserViewHolder) {
            ((UserViewHolder) holder).bind(users.get(position));
        }
        if (holder instanceof  ResourceViewHolder) {
            ((ResourceViewHolder) holder).bind(resources.get(position - users.size()));
        }
    }

    @Override
    public int getItemCount() {
        return users.size() + resources.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < users.size()) {
            return VIEW_TYPE_USER;
        }
        if (position - users.size() < resources.size()) {
            return VIEW_TYPE_RESOURCE;
        }
        return -1;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfilePic;
        TextView tvUsername;
        TextView tvName;
        RelativeLayout container;
        ImageButton btnAddFriend;
        boolean areFriends;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            // find views by id, is there a way to use view binding with adapters?
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvName = itemView.findViewById(R.id.tvName);
            container = itemView.findViewById(R.id.container);
            btnAddFriend = itemView.findViewById(R.id.btnAddFriend);

        }

        public void bind(final ParseUser user) {

            areFriends = false;

            if (UserUtils.userContained(UserUtils.getFriends(ParseUser.getCurrentUser()), user)) {
                btnAddFriend.setImageResource(R.drawable.ic_baseline_person_24);
                areFriends = true;
            }
            btnAddFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (areFriends) {
                        UserUtils.removeFriend(ParseUser.getCurrentUser(), user);
                        ParseUser.getCurrentUser().saveInBackground();
                        btnAddFriend.setImageResource(R.drawable.ic_baseline_person_add_24);
                    }
                    else {
                        UserUtils.addFriend(ParseUser.getCurrentUser(), user);
                        ParseUser.getCurrentUser().saveInBackground();
                        btnAddFriend.setImageResource(R.drawable.ic_baseline_person_24);
                    }
                    areFriends = !areFriends;
                }
            });

            ParseFile profile = UserUtils.getProfilePicture(user);
            if (profile != null) {
                Glide.with(context).load(profile.getUrl()).transform(new CircleCrop()).into(ivProfilePic);
            } else {
                Glide.with(context).load(R.drawable.ic_baseline_person_24_black).transform(new CircleCrop()).into(ivProfilePic);
            }

            tvUsername.setText("@"+UserUtils.getUsername(user));
            tvName.setText(UserUtils.getName(user));

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra(UserUtils.TAG, Parcels.wrap(user));
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(fragment.getActivity(), (View) ivProfilePic, "profilepicture");
                    context.startActivity(intent, options.toBundle());
                }
            });

        }
    }

    public class ResourceViewHolder extends RecyclerView.ViewHolder {

        TextView tvCaption;
        TextView tvSubject;
        ImageView ivPost;
        TextView tvLink;
        TextView tvFile;
        ImageView ivBookmark;
        RelativeLayout container;

        public ResourceViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCaption = itemView.findViewById(R.id.tvCaption);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            ivPost = itemView.findViewById(R.id.ivPost);
            tvLink = itemView.findViewById(R.id.tvLink);
            tvFile = itemView.findViewById(R.id.tvFile);
            container = itemView.findViewById(R.id.container);
            ivBookmark = itemView.findViewById(R.id.ivBookmark);
        }

        public void bind(Post resource) {
            ivPost.setVisibility(View.GONE);
            tvLink.setVisibility(View.GONE);
            tvFile.setVisibility(View.GONE);
            ivBookmark.setVisibility(View.VISIBLE);

            tvCaption.setText(resource.getCaption());
            tvSubject.setText(resource.getSubject());

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ResourceDetailsActivity.class);
                    intent.putExtra(Post.TAG, Parcels.wrap(resource));
                    context.startActivity(intent);
                }
            });
        }
    }

    // Clean all user elements of the recycler
    public void clearUsers() {
        users.clear();
        notifyDataSetChanged();
    }

    // Add a list of user items
    public void addAllUsers(List<ParseUser> list) {
        users.addAll(list);
        notifyDataSetChanged();
    }

    // Clean all resource elements of the recycler
    public void clearResources() {
        resources.clear();
        notifyDataSetChanged();
    }

    // Add a list of resource items
    public void addAllResources(List<Post> list) {
        resources.addAll(list);
        notifyDataSetChanged();
    }
}


