package com.example.lab3;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.core.util.ObjectsCompat;

public class Group extends ListItem implements Parcelable {
    @NonNull
    public String groupName;
    public Group(@NonNull String groupName) {
        this.groupName = groupName;
    }
    protected Group(Parcel in) {
        groupName = in.readString();
    }

    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(groupName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        Group group = (Group) o;
        return groupName.equals(group.groupName);
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
    @Override
    public int hashCode() {
        return ObjectsCompat.hash(groupName);
    }
}
