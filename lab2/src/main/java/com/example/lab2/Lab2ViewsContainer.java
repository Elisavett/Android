package com.example.lab2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.LinearLayout;
import java.util.ArrayList;


public class Lab2ViewsContainer extends LinearLayout {


    public ArrayList<LinearLayout> layoutArrayList = new ArrayList<LinearLayout>();
    /**
     * Этот конструктор используется при создании View в коде.
     */
    public Lab2ViewsContainer(Context context) {
        this(context, null);
    }

    /**
     * Этот конструктор выдывается при создании View из XML.
     */
    public Lab2ViewsContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        getStartViewValues();
    }

    public void getStartViewValues() {

        int viewsCount = 4;
        String[] namesMas = {"Category1", "Category2", "Category3", "Category4"};
        String[] progressesMas = {"2", "4", "2", "3"};

        for (int i = 0; i < viewsCount; i++) {
            incrementViews(namesMas[i], progressesMas[i]);
        }
    }


    int currId = 0;
    double maxProgress = 0;
    int maxProgressId = 0;
    int progressColor = Color.BLACK;

    public void incrementViews(String name, String progressString) {
        double progress = Double.parseDouble(progressString);


        TextView textView1 = new TextView(this.getContext());
        textView1.setTextSize(16);
        textView1.setText(name);
        textView1.setGravity(Gravity.CENTER);
        LayoutParams layoutParams1 = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
        textView1.setLayoutParams(layoutParams1);

        TextView textView2 = new TextView(this.getContext());
        textView2.setTextSize(16);
        textView2.setGravity(Gravity.CENTER);
        textView2.setText(progressString);
        LayoutParams layoutParams2 = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
        textView2.setLayoutParams(layoutParams2);

        ProgressBar progressBar = new ProgressBar(this.getContext(), null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setId(currId++);

        if(progress > maxProgress)
        {
            if(currId!=1)
            {
                ProgressBar prevMaxProgressBar = findViewById(maxProgressId);
                prevMaxProgressBar.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
            }
            maxProgress = progress;
            progressColor = Color.RED;
            maxProgressId = progressBar.getId();
        }
        else
        {
            progressColor = Color.BLACK;
        }

        progressBar.setLayoutParams(layoutParams1);
        progressBar.setProgress((int)Math.round(progress));
        progressBar.getProgressDrawable().setColorFilter(progressColor, PorterDuff.Mode.SRC_IN);
        progressBar.setMax(10);

        LinearLayout mainLayout = new LinearLayout(this.getContext());
        mainLayout.addView(textView1);
        mainLayout.addView((progressBar));
        mainLayout.addView(textView2);
        layoutArrayList.add(mainLayout);
        addView(mainLayout);

    }

}
