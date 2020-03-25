
package com.example.lab5.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5.R;

public class RepoHolder extends RecyclerView.ViewHolder {

    public final TextView repo;

    public RepoHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.lab5_item_repo, parent, false));
        repo = itemView.findViewById(R.id.item_repo);
        repo.setTextSize(25);
        repo.setPadding(0,5,0,5);
        repo.setGravity(Gravity.CENTER);
    }
}

