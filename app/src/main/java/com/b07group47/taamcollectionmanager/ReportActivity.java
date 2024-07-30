package com.b07group47.taamcollectionmanager;

import android.os.Bundle;
import android.widget.EditText;

public class ReportActivity extends BaseActivity {
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = getPassedAttributes();
        setLayoutValues();
    }

    /**
     * Auto-fills the appropriate values in the layout based on the attributes passed in the Intent
     * which invoked the activity and which are stores in the 'item' object
     */
    private void setLayoutValues() {
        EditText editLot = findViewById(R.id.editTextLotNumber);
        EditText editTitle = findViewById(R.id.editTextTitle);
        EditText editCategory = findViewById(R.id.editTextCategory);
        EditText editCategoryWithDescAndImg = findViewById(R.id.editTextCategoryWithDescAndImg);
        EditText editPeriod = findViewById(R.id.editTextPeriod);
        EditText editPeriodWithDescAndImg = findViewById(R.id.editTextPeriodWithDescAndImg);

        editLot.setText("" + item.getLotNumber());
        editTitle.setText(item.getTitle());
        editCategory.setText(item.getCategory());
        editCategoryWithDescAndImg.setText(item.getCategory());
        editPeriod.setText(item.getPeriod());
        editPeriodWithDescAndImg.setText(item.getPeriod());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_report;
    }
}