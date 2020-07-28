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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.virtuallibrary.R;
import com.example.virtuallibrary.TableUtils;
import com.example.virtuallibrary.UserUtils;
import com.example.virtuallibrary.activities.ProfileActivity;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class InviteUserAdapter extends RecyclerView.Adapter<InviteUserAdapter.ViewHolder> {

    Context context;
    List<ParseUser> friends;

    public InviteUserAdapter(Context context, List<ParseUser> friends) {
        this.context = context;
        this.friends = friends;
    }

    @NonNull
    @Override
    public InviteUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_invite_user, parent, false);
        return new InviteUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InviteUserAdapter.ViewHolder holder, int position) {
        ParseUser friend = friends.get(position);
        holder.bind(friend);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfilePic;
        TextView tvUsername;
        TextView tvName;
        RelativeLayout container;
        ImageButton btnInvite;
        boolean invited;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvName = itemView.findViewById(R.id.tvName);
            container = itemView.findViewById(R.id.container);
            btnInvite = itemView.findViewById(R.id.btnInvite);
            invited = false;

        }

        public void bind(final ParseUser friend) {

            ParseFile profile = UserUtils.getProfilePicture(friend);
            if (profile != null) {
                Glide.with(context).load(profile.getUrl()).transform(new CircleCrop()).into(ivProfilePic);
            } else {
                Glide.with(context).load(R.drawable.ic_baseline_person_24_black).transform(new CircleCrop()).into(ivProfilePic);
            }

            tvUsername.setText("@"+UserUtils.getUsername(friend));
            tvName.setText(UserUtils.getName(friend));

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra(UserUtils.TAG, Parcels.wrap(friend));
                    context.startActivity(intent);
                }
            });

            btnInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (invited) {
                        btnInvite.setImageResource(R.drawable.circle);
                        TableUtils.removeInvite(friend, ParseUser.getCurrentUser(), UserUtils.getCurrentTable(ParseUser.getCurrentUser()));
                    } else {
                        btnInvite.setImageResource(R.drawable.ic_baseline_check_circle_24);
                        TableUtils.addInvite(friend, ParseUser.getCurrentUser(), UserUtils.getCurrentTable(ParseUser.getCurrentUser()));
                    }
                    invited = !invited;
                }
            });
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        friends.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<ParseUser> list) {
        friends.addAll(list);
        notifyDataSetChanged();
    }

}
