package com.example.lab5;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lab5.adapter.RepoAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Lab5Activity extends AppCompatActivity {

    private static final String TAG = Lab5Activity.class.getSimpleName();
    private SearchTask task;
    public ProgressDialog dialog;
    private SwipeRefreshLayout swipeContainer;


    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, Lab5Activity.class);
    }

    // private TextView mTextViewResult;
    private ProgressBar pgBar;
    private RepoAdapter repoAdapter;
    private Button RepeatButton;
    private MyWorkerThread mWorkerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab5_activity);
        setTitle(getString(R.string.lab5_title, getClass().getSimpleName()));
        list = findViewById(R.id.ReposList);

        pgBar = findViewById(R.id.progressBar);
        layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        RepeatButton = findViewById(R.id.button);
        mWorkerThread = new MyWorkerThread("myWorkerThread");
        mWorkerThread.start();
        mWorkerThread.prepareHandler();
        // mTextViewResult = findViewById(R.id.textView);
        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading) {
                    if (dy > 0) {
                        if ((layoutManager.getChildCount() + layoutManager.findFirstVisibleItemPosition()) >= layoutManager.getItemCount()) {
                            Log.d(TAG, "scroled");
                            insertionIndx = layoutManager.findFirstVisibleItemPosition();
                            pageCount++;
                            pgBar.setVisibility(View.VISIBLE);
                            loadRepos(currFindingString, pageCount);
                            isLoading = false;

                        }
                    }
                }
            }
        });
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                pageCount = 1;
                rc.clear();


                loadRepos(currFindingString, pageCount);
                swipeContainer.setRefreshing(false);
                insertionIndx = 0;
            }
        });
    }

    int insertionIndx = 0;

    @Override

    protected void onDestroy() {

        super.onDestroy();
        mWorkerThread.quit();
        task.unregisterObserver();

    }

    //Thread requestThread = new Thread();
    String currFindingString = "";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_repos);
        dialog = new ProgressDialog(this);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                //if(requestThread.isAlive())
                // requestThread.interrupt();
                if (newText.length() > 2) {
                    rc.clear();
                    currFindingString = newText;
                    pageCount = 1;
                    Log.d(TAG, "textChanged");
                    pgBar.setVisibility(View.VISIBLE);
                    loadRepos(newText, pageCount);

                    insertionIndx = 0;

                }

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    ReposCache rc = ReposCache.getInstance();

    public void loadRepos(final String repoSubstring, final int page) {
        Log.d(TAG, "loadRepo");
        //Handler h = new Handler();
        // requestThread.destroy();
        /*h.postDelayed(new Runnable() {
            @Override
            public void run() {*/
        task = new SearchTask(searchObserver, repoSubstring, page);
        //requestThread = new Thread(task);
        //requestThread.start();
        mWorkerThread.postTask(task);
        // }
        //}, 500);

    }

    private RecyclerView list;
    int pageCount = 1;
    boolean isLoading = false;
    LinearLayoutManager layoutManager;
    private Observer<List<Repo>> searchObserver = new Observer<List<Repo>>() {

        @Override
        public void onLoading(Task<List<Repo>> task) {


            Log.d(TAG, "onLoad");
        }

        @Override
        public void onSuccess(Task<List<Repo>> task, List<Repo> data) {

            // requestThread.interrupt();
            RepeatButton.setVisibility(View.INVISIBLE);
            Log.d(TAG, "onSuccess");

            list.setAdapter(repoAdapter = new RepoAdapter(data));


            pgBar.setVisibility(View.INVISIBLE);
            if (insertionIndx > 0) {
                int itemCount = repoAdapter.getItemCount();
                Log.d(TAG, "itemCount= " + itemCount);
                Log.d(TAG, "scrolposition= " + insertionIndx);
                repoAdapter.notifyItemRangeInserted(itemCount, itemCount + 20);
                list.scrollToPosition(insertionIndx + 1);
            }

            isLoading = true;
        }

        @Override
        public void onError(Task<List<Repo>> task, Exception e) {
            pgBar.setVisibility(View.INVISIBLE);
            RepeatButton.setVisibility(View.VISIBLE);
            RepeatButton.setOnClickListener(v->loadRepos(currFindingString, pageCount));
           // RepeatButton.setOnClickListener();
            Log.d(TAG, "onError");
        }
    };

    public class MyWorkerThread extends HandlerThread {

        private Handler mWorkerHandler;

        public MyWorkerThread(String name) {
            super(name);
        }

        public void postTask(Runnable task) {
            mWorkerHandler.post(task);
        }

        public void prepareHandler() {
            mWorkerHandler = new Handler(getLooper());
        }
    }



}
