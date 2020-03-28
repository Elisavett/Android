package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.lab1.Lab1Activity;
import com.example.lab2.Lab2Activity;
import com.example.lab3.Lab3Activity;
import com.example.lab4.Lab4Activity;
import com.example.lab5.Lab5Activity;
import com.example.lab6.Lab6Activity;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("StringFormatInvalid")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.app_name, getClass().getSimpleName()));
        findViewById(R.id.lab1).setOnClickListener((v) -> startActivity(Lab1Activity.newIntent(this)));
        findViewById(R.id.lab2).setOnClickListener((v) -> startActivity(Lab2Activity.newIntent(this)));
        findViewById(R.id.lab3).setOnClickListener((v) -> startActivity(Lab3Activity.newIntent(this)));
        findViewById(R.id.lab4).setOnClickListener((v) -> startActivity(Lab4Activity.newIntent(this)));
        findViewById(R.id.lab6).setOnClickListener((v) -> startActivity(Lab6Activity.newIntent(this)));
        findViewById(R.id.lab5).setOnClickListener((v) -> startActivity(Lab5Activity.newIntent(this)));
    }
}
