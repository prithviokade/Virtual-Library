package com.example.virtuallibrary.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.virtuallibrary.R;
import com.example.virtuallibrary.UserUtils;
import com.example.virtuallibrary.activities.ProfileActivity;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder> {

    Context context;
    List<ParseUser> users;

    public SearchUserAdapter(Context context, List<ParseUser> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public SearchUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_user, parent, false);
        return new SearchUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParseUser user = users.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfilePic;
        TextView tvUsername;
        TextView tvName;
        RelativeLayout container;
        ImageButton btnAddFriend;
        boolean areFriends;

        public ViewHolder(@NonNull View itemView) {
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
                        Log.d("HIIIISIR", UserUtils.getUsername(user));
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
                Glide.with(context).load(R.drawable.ic_baseline_people_alt_24).transform(new CircleCrop()).into(ivProfilePic);
            }

            tvUsername.setText("@"+UserUtils.getUsername(user));
            tvName.setText(UserUtils.getName(user));

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra(UserUtils.TAG, Parcels.wrap(user));
                    context.startActivity(intent);
                }
            });

        }
    }

    // Clean all elements of the recycler
    public void clear() {
        users.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<ParseUser> list) {
        users.addAll(list);
        notifyDataSetChanged();
    }
}


