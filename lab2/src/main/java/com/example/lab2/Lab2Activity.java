package com.example.lab2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

public class Lab2Activity extends AppCompatActivity {

    private EditText progress_title;
    private EditText progress;
    ArrayList<String> name_progressList = new ArrayList<>();

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, Lab2Activity.class);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("views", name_progressList);
    }
    private Lab2ViewsContainer lab2ViewsContainer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab2_activity);
        setTitle(getString(R.string.lab2_title, getClass().getSimpleName()));

        Button b = findViewById(R.id.btn_add_view);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lab2ViewsContainer = findViewById(R.id.container);
                progress_title = findViewById(R.id.progress_name);
                progress = findViewById(R.id.progress);
                String name = progress_title.getText().length() == 0 ? "No name" : progress_title.getText().toString();
                String progressString = progress.getText().length() == 0 ? "0" : progress.getText().toString();
                name_progressList.add(name);//четные значения - имена
                name_progressList.add(progressString);//нечетные - значение рейтинга
                lab2ViewsContainer.incrementViews(name, progressString);
            }
        });
        if (savedInstanceState != null) {
            ArrayList<String> Views = savedInstanceState.getStringArrayList("views");
            lab2ViewsContainer = findViewById(R.id.container);
            for(int i = 0; i <= Views.size()-2; i+=2)
            {
                String name = Views.get(i);
                String progress = Views.get(i+1);
                lab2ViewsContainer.incrementViews(name, progress);
            }
        }
    }
}
