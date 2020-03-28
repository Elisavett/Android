package com.example.lab5;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ReposCache {
    private static ReposCache instance;


    public static ReposCache getInstance() {
        if (instance == null) {
            //
            synchronized (ReposCache.class) {
                if (instance == null) {
                    instance = new ReposCache();
                }
            }
        }
        return instance;
    }

    private List<Repo> repos = new ArrayList<>();
    private int pageCount;
    private String searchWord;

    private ReposCache() {
    }

    @NonNull
    public List<Repo> getRepos() {
        return new ArrayList<>(repos);
    }

    @NonNull
    public String getSearchWord() {
        return searchWord;
    }

    @NonNull
    public void setSearchWord(String pc) {
        searchWord = pc;
    }

    @NonNull
    public int getPageCount() {
        return pageCount;
    }

    @NonNull
    public void setPageCount(int pc) {
        pageCount = pc;
    }

    public void clear() {
        repos.clear();
    }

    public void addRepo(@NonNull Repo repo) {
        repos.add(repo);
    }

}
