package com.example.lab3;

import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GroupCache {
    private static GroupCache instance;

    public static GroupCache getInstance() {
        if (instance == null) {
            //
            synchronized (GroupCache.class) {
                if (instance == null) {
                    instance = new GroupCache();
                    instance.groups.add(new Group("8i6a"));
                    instance.groups.add(new Group("8i6b"));
                }
            }
        }

        return instance;
    }

    private Set<Group> groups = new LinkedHashSet<>();

    private GroupCache() {
    }

    public Group getGroupByName(String name) {
        for (Group g : groups) {
            if (name.equals(g.groupName))
                return g;
        }
        return null;
    }

    @NonNull
    public List<Group> getGroups() {

        return new ArrayList<>(groups);

    }


    public void addGroup(@NonNull Group group) {
        groups.add(group);
    }

    public boolean contains(@NonNull Group group) {
        return groups.contains(group);
    }
}
