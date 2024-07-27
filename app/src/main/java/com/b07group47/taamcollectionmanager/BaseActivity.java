package com.b07group47.taamcollectionmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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

    protected void switchToActivity(Context context, Class<? extends AppCompatActivity> activity) {
        startActivity(new Intent(context, activity));
    }

    // Method used by activities which display information relevant to a particular item
    // Examples: DeleteItemActivity, ViewActivity, ReportActivity
    protected Item getPassedAttributes() {
        Item item = new Item();
        item.setLotNumber(getIntent().getIntExtra("LOT", 1));
        item.setTitle(getIntent().getStringExtra("TITLE"));
        item.setDescription(getIntent().getStringExtra("DESCRIPTION"));
        item.setCategory(getIntent().getStringExtra("CATEGORY"));
        item.setPeriod(getIntent().getStringExtra("PERIOD"));
        item.setImgID(getIntent().getIntExtra("IMAGE", R.drawable.mew_vase));
        return item;
    }
}