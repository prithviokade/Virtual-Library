package com.example.virtuallibrary.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.TableUtils;
import com.example.virtuallibrary.UserUtils;
import com.example.virtuallibrary.activities.ProfileActivity;
import com.example.virtuallibrary.activities.TableDetailsActivity;
import com.example.virtuallibrary.fragments.UserInvitesFragment;
import com.example.virtuallibrary.models.Invite;
import com.example.virtuallibrary.models.Table;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.ViewHolder> {

    Context context;
    List<Invite> invites;
    UserInvitesFragment fragment;

    public InviteAdapter(UserInvitesFragment fragment, List<Invite> invites) {
        this.context = fragment.getContext();
        this.invites = invites;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_invite, parent, false);
        return new InviteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Invite invite = invites.get(position);
        holder.bind(invite);
    }

    @Override
    public int getItemCount() {
        return invites.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDescription;
        TextView tvFrom;
        ImageButton btnAccept;
        ImageButton btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvFrom = itemView.findViewById(R.id.tvFrom);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }

        public void bind(final Invite invite) {

            tvFrom.setText(context.getString(R.string.from) + " @" + invite.getFrom().getUsername());
            tvFrom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra(UserUtils.TAG, Parcels.wrap(invite.getFrom()));
                    context.startActivity(intent);
                }
            });

            Table table = invite.getTable();
            String type = table.getType();
            String topic = table.getTopic();
            int size = table.getSize();
            tvDescription.setText(context.getString(R.string.join_size) + " " + Integer.toString(size) + ", " + type + " " + context.getString(R.string.table_working_on) + " " + topic );

            tvDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, TableDetailsActivity.class);
                    intent.putExtra(TableUtils.TAG, Parcels.wrap(invite.getTable()));
                    context.startActivity(intent);
                }
            });

            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TableUtils.removeFromPreviousTable(ParseUser.getCurrentUser());
                    invite.getTable().addMate(ParseUser.getCurrentUser());
                    UserUtils.setCurrentTable(ParseUser.getCurrentUser(), invite.getTable());
                    if (!invite.getType().equals(Invite.TYPE_PERMANENT)) {
                        int position = getAdapterPosition();
                        invites.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, invites.size());
                        invite.getTable().removeInvite(invite);
                    }
                    fragment.dismiss();
                    ParseUser.getCurrentUser().saveInBackground();
                    Intent intent = new Intent(context, TableDetailsActivity.class);
                    intent.putExtra(TableUtils.TAG, Parcels.wrap(invite.getTable()));
                    context.startActivity(intent);
                }
            });

            btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    invites.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, invites.size());
                    if (!invite.getType().equals(Invite.TYPE_PERMANENT)) {
                        invite.getTable().removeInvite(invite);
                    }
                    if (invites.size() == 0) {
                        fragment.dismiss();
                    }
                    fragment.tvNotification.setText(context.getString(R.string.you_have) + " " + Integer.toString(invites.size()) + " " + context.getString(R.string.notification_plural));
                    if (invites.size() == 1) {
                        fragment.tvNotification.setText(context.getString(R.string.you_have) + " " + Integer.toString(invites.size()) + " " + context.getString(R.string.notification_singular));
                    }
                }
            });
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        invites.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Invite> list) {
        invites.addAll(list);
        notifyDataSetChanged();
    }

}
