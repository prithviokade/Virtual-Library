package com.example.virtuallibrary.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.virtuallibrary.R;
import com.example.virtuallibrary.activities.DetailsActivity;
import com.example.virtuallibrary.models.Post;
import com.example.virtuallibrary.models.Table;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {

    Context context;
    List<Table> tables;

    public TableAdapter(Context context, List<Table> tables) {
        this.context = context;
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // find views by id
            ivTable = itemView.findViewById(R.id.ivTable);
        }

        public void bind(final Table table) {
            ivTable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("TABLE", Parcels.wrap(table));
                    context.startActivity(intent);
                }
            });
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

