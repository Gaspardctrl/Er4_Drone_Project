package com.example.admin.pilotage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

class ViewTest extends RelativeLayout {

    public ViewTest(Context context) {
        super(context);
        this.initComponent(context);
    }

    public ViewTest(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initComponent(context);
//        TextView Text = findViewById(R.id.textVideo);
//        Text.setText("Worked");
    }


    private void initComponent(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.activity_main_pilotage, null, false);
        this.addView(v);
    }
}