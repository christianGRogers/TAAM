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
        getSupportFragmentManager().beginTransaction().replace(R.id.titleBarContainer, titleBarFragment).commit();
    }

    protected void switchToActivity(Context context, Intent intent) {
        startActivity(intent);
    }
}