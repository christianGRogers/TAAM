package com.b07group47.taamcollectionmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    private int layoutID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutID = getLayoutResourceId();
        setContentView(layoutID);
        createTitleBar();
    }

    protected abstract int getLayoutResourceId();

    private void createTitleBar() {
        boolean isMainScreen = layoutID == R.layout.activity_main;
        TitleBarFragment titleBarFragment = TitleBarFragment.newInstance(isMainScreen);

        FrameLayout fragmentContainer = new FrameLayout(this);
        fragmentContainer.setId(View.generateViewId());

        ViewGroup rootLayout = findViewById(R.id.root_layout);
        rootLayout.addView(fragmentContainer, 0, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        getSupportFragmentManager().beginTransaction().replace(fragmentContainer.getId(), titleBarFragment).commit();
    }

    protected void switchToActivity(Context context, Class<? extends AppCompatActivity> activity) {
        startActivity(new Intent(context, activity));
    }
}