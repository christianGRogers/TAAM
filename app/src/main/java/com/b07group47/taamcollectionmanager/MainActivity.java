package com.b07group47.taamcollectionmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {
    private final List<Item> itemList = new ArrayList<>();

    private final String TAG = "MainActivity";

    private ItemAdapter itemAdapter;
    private Button buttonReport;
    private ImageView buttonAdd;
    private TextView emptyText;
    private Button clearSearch;

    /**
     * Equivalent to a constructor of the activity
     *
     * @param savedInstanceState the saved parameters of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initButtons();
        createTable();
        handleIntent();
        Log.d(TAG, "mainactivity created.");
    }

    private void initButtons() {
        buttonReport = findViewById(R.id.buttonReport);
        buttonAdd = findViewById(R.id.buttonAddItem);
        buttonAdd.setOnClickListener(v -> switchToActivity(new Intent(this, AddItemActivity.class)));
        emptyText = findViewById(R.id.emptyText);
        emptyText.setVisibility(View.VISIBLE);
        clearSearch = findViewById(R.id.clearSearch);
        clearSearch.setOnClickListener((v -> switchToActivity(new Intent(this, MainActivity.class))));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    /**
     * Creates the table implemented as a RecyclerView in activity_main.xml
     * Uses the ItemAdapter class to facilitate the logic of the tables
     * Uses the TableSpacing class to facilitate the spacing between elements of the table
     */
    private void createTable() {
        RecyclerView recyclerView = findViewById(R.id.table_layout);
        itemAdapter = new ItemAdapter(itemList, this);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.recycler_view_item_spacing);
        recyclerView.addItemDecoration(new TableSpacing(spacingInPixels));
    }

    /**
     * Populates the list of the items displayed by the table in activity_main.xml
     */
    private void insertData(Query query) {
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot qs = task.getResult();
                if (qs.isEmpty()) {
                    Log.d(TAG, "Empty query results.");
                }
                for (DocumentSnapshot d : qs.getDocuments()) {
                    Map<String, Object> data = d.getData();
                    if (data != null) {
                        Item i = new Item(data);
                        int position = itemList.size();
                        itemList.add(i);
                        itemAdapter.notifyItemInserted(position);
                        Log.d(TAG, "Item inserted: " + i);
                        emptyText.setVisibility(View.INVISIBLE);
                    } else {
                        Log.d(TAG, "Document data is null");
                    }
                }
            } else {
                Log.d(TAG, "Error in query");
            }
        }).addOnFailureListener(e -> {
            Log.d(TAG, "Error querying items", e);
        });
    }

    private void handleIntent() {
        Intent intent = getIntent();

        Guideline bottomGuideline = findViewById(R.id.bottomGuideline);

        if (UserState.isAdmin()) {
            Log.d(TAG, "User is an admin");
        } else {
            Log.d(TAG, "User is not an admin, hiding the buttons");
//            Hides the button drawer, making the table take up the whole screen
            bottomGuideline.setGuidelinePercent(1.0f);
            buttonReport.setVisibility(View.GONE);
            buttonAdd.setVisibility(View.GONE);
        }

        // handle Search inputs. if none present, query factory handles that
        // and just searches all input
        Bundle b = intent.getExtras();

        // if not a search
        if (b == null || !intent.getBooleanExtra("fromSearch", false)) {
            insertData(ArtifactQueryFactory.getAll());
            return;
        }

        // if a search
        bottomGuideline.setGuidelinePercent(.8f);
        buttonAdd.setVisibility(View.GONE);
        clearSearch.setVisibility(View.VISIBLE);

        Long lot = b.getLong("lot", -1);
        if (lot == -1) lot = null;
        String name = b.getString("name", null);
        String category = b.getString("category", null);
        String period = b.getString("period", null);

        Log.d(TAG, "lot: " + lot);
        Log.d(TAG, "name: " + name);
        Log.d(TAG, "category: " + category);
        Log.d(TAG, "period: " + period);

        //only searches non-null fields
        Query searchQuery = ArtifactQueryFactory.getFilteredQuery(lot, name, category, period);
        if (searchQuery == null) {
            Log.w(TAG, "searchQuery was null");
            return;
        }
        insertData(searchQuery);
    }
}
