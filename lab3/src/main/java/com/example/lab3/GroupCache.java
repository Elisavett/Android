package com.example.lab3;

import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GroupCache {
    private static GroupCache instance;

    /**
     * Классическая реализация паттерна Singleton. Нам необходимо, чтобы в приложении был только
     * один кэш студентов.
     */
    public static GroupCache getInstance() {
        if (instance == null) {
            //
            synchronized (GroupCache.class) {
                if (instance == null) {
                    instance = new GroupCache();
                }
            }
        }
        return instance;
    }

    private Set<Group> groups = new LinkedHashSet<>();

    private GroupCache() {
    }
    public Group getGroupByName(String name)
    {
        for(Group g:groups)
        {
            if(name.equals(g.groupName))
                return g;
        }
        return null;
    }
    @NonNull
    public List<Group> getGroups() {
        groups.add(new Group("8i6a"));
        groups.add(new Group("8i6b"));
        return new ArrayList<>(groups);

    }


    public void addGroup(@NonNull Group group) {
        groups.add(group);
    }

    public boolean contains(@NonNull Group group) {
        return groups.contains(group);
    }
}
