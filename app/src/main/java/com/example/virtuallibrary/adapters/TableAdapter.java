package com.example.virtuallibrary.adapters;

import android.content.Context;
import android.content.Intent;
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

import com.example.virtuallibrary.R;
import com.example.virtuallibrary.TableUtils;
import com.example.virtuallibrary.activities.TableDetailsActivity;
import com.example.virtuallibrary.models.Table;

import org.parceler.Parcels;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {

    Context context;
    List<Table> tables;
    Fragment fragment;

    public TableAdapter(Fragment fragment, List<Table> tables) {
        this.fragment = fragment;
        this.context = fragment.getContext();;
        this.tables = tables;
    }

    @NonNull
    @Override
    public TableAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_table, parent, false);
        return new TableAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Table table = tables.get(position);
        holder.bind(table);
    }

    @Override
    public int getItemCount() {
        return tables.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivTable;
        TextView tvStatus;
        TextView tvSize;
        TextView tvMemberCount;
        TextView tvVisitors;
        TextView tvDescription;
        RelativeLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // find views by id
            ivTable = itemView.findViewById(R.id.ivTable);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvSize = itemView.findViewById(R.id.tvSize);
            tvMemberCount = itemView.findViewById(R.id.tvMemberCount);
            tvVisitors = itemView.findViewById(R.id.tvVisitors);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            container = itemView.findViewById(R.id.container);
        }

        public void bind(final Table table) {
            int size = table.getSize();
            ivTable.setImageResource(TableUtils.getTableImage(size));


            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, TableDetailsActivity.class);
                    intent.putExtra(TableUtils.TAG, Parcels.wrap(table));
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(fragment.getActivity(), (View)ivTable, "table");
                    context.startActivity(intent, options.toBundle());
                }
            });

            tvStatus.setText(table.getStatus());
            tvSize.setText(Integer.toString(table.getSize()));
            tvMemberCount.setText(Integer.toString(table.getMates().size()));
            if (table.getVisiting()) {
                tvVisitors.setText(R.string.allowed);
            } else {
                tvVisitors.setText(R.string.not_allowed);
            }

            String topic = table.getTopic();
            String type = table.getType();
            String description = table.getDescription();
            String fullDescription = context.getString(R.string.description_pt1) + " " + type + " " + context.getString(R.string.description_pt2) + topic + ".\n" + description;
            tvDescription.setText(fullDescription);
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        tables.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Table> list) {
        tables.addAll(list);
        notifyDataSetChanged();
    }


}

