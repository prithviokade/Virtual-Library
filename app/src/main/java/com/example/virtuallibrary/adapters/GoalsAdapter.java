package com.example.virtuallibrary.adapters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.virtuallibrary.R;
import com.example.virtuallibrary.models.Post;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class GoalsAdapter  extends RecyclerView.Adapter<GoalsAdapter.ViewHolder> {

    Context context;
    List<String> goals;
    List<String> done;
    public static final String TAG = "GoalsAdapter";

    public GoalsAdapter(Context context, List<String> goals, List<String> done) {
        this.context = context;
        this.goals = goals;
        this.done = done;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_goal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String goal = goals.get(position);
        String status = done.get(position);
        holder.bind(goal, done, position);
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvGoal;
        ImageButton btnDone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGoal = itemView.findViewById(R.id.tvGoal);
            btnDone = itemView.findViewById(R.id.btnDone);
        }

        public void bind(final String goal, final List<String> statuses, final int position) {
            tvGoal.setText(goal);

            final String[] status = {statuses.get(position)};

            Log.d("GOAL", goal + " STATUS " + status[0] + "text");

            if (status[0].equals("complete")) {
                btnDone.setImageResource(R.drawable.ic_baseline_check_box_24);
            } else if (status[0].equals("intermediate")) {
                btnDone.setImageResource(R.drawable.ic_baseline_indeterminate_check_box_24);
            } else {
                btnDone.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24);
            }

            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParseUser user = ParseUser.getCurrentUser();
                    if (status[0].equals("complete")) {
                        status[0] = "incomplete";
                        done.set(position, "incomplete");
                        btnDone.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24);
                    } else if (status[0].equals("intermediate")) {
                        status[0] = "complete";
                        done.set(position, "complete");
                        btnDone.setImageResource(R.drawable.ic_baseline_check_box_24);
                    } else {
                        status[0] = "intermediate";
                        statuses.set(position,"intermediate");
                        done.set(position,"intermediate");
                        btnDone.setImageResource(R.drawable.ic_baseline_indeterminate_check_box_24);
                    }
                    user.put("done", done);
                    saveUser();
                }
            });
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        goals.clear();
        done.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<String> listGoals, List<String> listDone) {
        goals.addAll(listGoals);
        done.addAll(listDone);
        notifyDataSetChanged();
    }

    private void saveUser() {
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving post info", e);
                } else {
                    Log.i(TAG, "Success saving post info");
                }
            }
        });
    }

}

