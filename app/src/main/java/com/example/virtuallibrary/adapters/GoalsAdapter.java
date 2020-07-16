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
import com.example.virtuallibrary.models.Goal;
import com.example.virtuallibrary.models.Post;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class GoalsAdapter  extends RecyclerView.Adapter<GoalsAdapter.ViewHolder> {

    Context context;
    List<Goal> goals;
    public static final String TAG = "GoalsAdapter";

    public GoalsAdapter(Context context, List<Goal> goals) {
        this.context = context;
        this.goals = goals;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_goal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Goal goal = goals.get(position);
        holder.bind(goal);
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

        public void bind(final Goal goal) {
            tvGoal.setText(goal.getGoal());

            final String status = goal.getStatus();

            if (status.equals("complete")) {
                btnDone.setImageResource(R.drawable.ic_baseline_check_box_24);
            } else if (status.equals("intermediate")) {
                btnDone.setImageResource(R.drawable.ic_baseline_indeterminate_check_box_24);
            } else {
                btnDone.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24);
            }

            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String status = goal.getStatus();
                    ParseUser user = ParseUser.getCurrentUser();
                    if (status.equals("complete")) {
                        goal.setStatus("incomplete");
                        btnDone.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24);
                    } else if (status.equals("intermediate")) {
                        goal.setStatus("complete");
                        btnDone.setImageResource(R.drawable.ic_baseline_check_box_24);
                    } else {
                        goal.setStatus("intermediate");
                        btnDone.setImageResource(R.drawable.ic_baseline_indeterminate_check_box_24);
                    }
                    saveGoal(goal);
                    saveUser();
                }
            });
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        goals.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Goal> listGoals) {
        goals.addAll(listGoals);
        notifyDataSetChanged();
    }

    private void saveGoal(Goal goal) {
        goal.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving goal info", e);
                } else {
                    Log.i(TAG, "Success saving goal info");
                }
            }
        });
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

