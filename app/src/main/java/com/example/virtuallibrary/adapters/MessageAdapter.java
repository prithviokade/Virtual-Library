package com.example.virtuallibrary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.virtuallibrary.R;
import com.example.virtuallibrary.models.Goal;
import com.example.virtuallibrary.models.Message;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    Context context;
    List<Message> messages;
    public static final String TAG = "MessageAdapter";

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvRecieved;
        ImageView ivRecieved;
        TextView tvSent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecieved = itemView.findViewById(R.id.tvRecieved);
            ivRecieved = itemView.findViewById(R.id.ivRecieved);
            tvSent = itemView.findViewById(R.id.tvSent);
        }

        public void bind(Message message) {
            String senderUsername = "";
            try {
                senderUsername = message.getSender().fetchIfNeeded().getUsername();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (ParseUser.getCurrentUser().getUsername().equals(senderUsername)) { // user is sender
                tvRecieved.setVisibility(View.INVISIBLE);
                ivRecieved.setVisibility(View.INVISIBLE);

                tvSent.setText(message.getText());

            } else { // user is reciever
                tvSent.setVisibility(View.INVISIBLE);

                ParseFile profile = message.getSender().getParseFile("picture");
                if (profile != null) {
                    Glide.with(context).load(profile.getUrl()).transform(new CircleCrop()).into(ivRecieved);
                } else {
                    Glide.with(context).load(R.drawable.ic_baseline_people_alt_24).transform(new CircleCrop()).into(ivRecieved);
                }
                tvRecieved.setText(message.getText());
            }

        }
    }

    // Clean all elements of the recycler
    public void clear() {
        messages.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Message> addMessages) {
        messages.addAll(addMessages);
        notifyDataSetChanged();
    }

    public void add(Message newMessage) {
        messages.add(newMessage);
        notifyDataSetChanged();
    }

}
