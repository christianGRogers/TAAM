package com.b07group47.taamcollectionmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Class which all activities should extend as it minimizes boilerplate code
 * and instantiates the title bar on top of the screen
 */
public abstract class BaseActivity extends AppCompatActivity {
    private int layoutID;

    /**
     * Called whenever the activity is created
     * Sets the layout of the activity and creates the navigation bar
     *
     * @param savedInstanceState the saved parameters of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutID = getLayoutResourceId();
        setContentView(layoutID);
        createTitleBar();
    }

    protected abstract int getLayoutResourceId();

    /**
     * Creates the title bar fragment by replacing the "titleBarContainer" FragmentContainer
     * in the xml of the activity
     */
    private void createTitleBar() {
        boolean isMainScreen = layoutID == R.layout.activity_main;
        TitleBarFragment titleBarFragment = TitleBarFragment.newInstance(isMainScreen);
        getSupportFragmentManager().beginTransaction().replace(R.id.titleBarContainer, titleBarFragment).commit();
    }

    /**
     * Switches from the current activity with 'context' context to the 'activity' activity
     *
     * @param intent to use when switching to an activity
     */
    protected void switchToActivity(Intent intent) {
        startActivity(intent);
    }

    // Method used by activities which display information relevant to a particular item
    // Examples: DeleteItemActivity, ViewActivity, ReportActivity

    /**
     * When an activity is called with an Intent, this method must be called to capture the
     * attributes that were passed within the Intent
     *
     * @return Item object which contains all the details that the activity received from the Intent which invoked it
     */
    protected Item getPassedAttributes() {
        Item item = new Item();

        if (getIntent() != null) {
            item.setLotNumber(getIntent().getLongExtra("LOT", 1));
            item.setTitle(getIntent().getStringExtra("TITLE"));
            item.setDescription(getIntent().getStringExtra("DESCRIPTION"));
            item.setCategory(getIntent().getStringExtra("CATEGORY"));
            item.setPeriod(getIntent().getStringExtra("PERIOD"));
            item.setImgID(getIntent().getStringExtra("IMAGE"));
        }

        return item;
    }
}