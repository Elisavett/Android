
package com.example.lab5.adapter;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5.R;

public class RepoHolder extends RecyclerView.ViewHolder {

    public final TextView repo;
    public final LinearLayout mainLayout;
    public final TextView description;

    public RepoHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.lab5_item_repo, parent, false));
        repo = itemView.findViewById(R.id.item_repo);
        mainLayout = itemView.findViewById(R.id.HolderLayout);
        description = itemView.findViewById(R.id.description);
        repo.setTextSize(20);
        description.setTextSize(15);
        description.setMaxLines(5);
        description.setTextColor(Color.GRAY);
        mainLayout.setPadding(0,5,0,5);
        //repo.setGravity(Gravity.CENTER);
    }
}

