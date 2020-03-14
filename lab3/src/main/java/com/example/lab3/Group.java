package com.example.lab3;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.core.util.ObjectsCompat;

public class Group extends ListItem {
    @NonNull
    public String groupName;
    public Group(@NonNull String groupName) {
        this.groupName = groupName;
    }


    @NonNull
    @Override
    public String toString() {
        return groupName;
    }
    @Override
    public int getType() {
        return TYPE_GROUP;
    }
}