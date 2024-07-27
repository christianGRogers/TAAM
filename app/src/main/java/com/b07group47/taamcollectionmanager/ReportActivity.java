package com.b07group47.taamcollectionmanager;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReportActivity extends BaseActivity {
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = getPassedAttributes();
        setLayoutValues();
    }

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