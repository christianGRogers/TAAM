    package com.b07group47.taamcollectionmanager;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;

    import com.google.firebase.firestore.DocumentSnapshot;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.firestore.Query;
    import com.google.firebase.firestore.QuerySnapshot;


    import android.view.View;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.TextView;

    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Map;
    import java.util.concurrent.atomic.AtomicBoolean;

    public class MainActivity extends BaseActivity {
        private final List<Item> itemList = new ArrayList<>();

        private final String TAG = "MainActivity";

        private ItemAdapter itemAdapter;
        private Button buttonReport;
        private ImageView buttonAdd;
        private TextView emptyText;

        private ArtifactQueryFactory queryFactory;

        /**
         * Equivalent to a constructor of the activity
         * @param savedInstanceState the saved parameters of the activity
         */
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            queryFactory = new ArtifactQueryFactory();
            initButtons();
            createTable();
            handleIntent();
            Log.d(TAG, "mainactivity created.");
        }

            FirebaseFirestore db = FirebaseFirestore.getInstance();

        private void initButtons() {
            buttonReport = findViewById(R.id.buttonReport);
            buttonAdd = findViewById(R.id.buttonAddItem);
            buttonAdd.setOnClickListener(v -> switchToActivity(new Intent(this, AddItemActivity.class)));
            emptyText = findViewById(R.id.emptyText);
            emptyText.setVisibility(View.VISIBLE);
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
    //      TODO: refactor to work with Firebase
        private void insertData(Query query) {
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot qs = task.getResult();
                    if ( qs.isEmpty()) {
                        Log.d(TAG, "Empty query results.");
                    }
                    for (DocumentSnapshot d : qs.getDocuments()) {
                        // Make sure that `d.getData()` is cast or processed correctly based on your Item class constructor
                        Map<String, Object> data = d.getData();
                        if (data != null) {
                            Item i = new Item(data);
                            int position = itemList.size(); // Get the current size, which is where the item will be inserted
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
                return;
            }).addOnFailureListener(e -> {
                Log.d(TAG, "Error querying items", e);
            });
        }

        private void handleIntent() {
            Intent intent = getIntent();
            boolean fromAdmin = intent.getBooleanExtra("fromAdmin", false);
            Log.d(TAG, "fromAdmin: " + fromAdmin); // Log the value of fromAdmin
            if (fromAdmin) {
                // Show the buttons if coming from AdminActivity
                Log.d(TAG, "AdminActivity detected, showing buttons.");
                buttonReport.setVisibility(View.VISIBLE);
                buttonAdd.setVisibility(View.VISIBLE);
            } else {
                Log.d(TAG, "AdminActivity not detected, buttons remain hidden.");
            }
            Bundle b = intent.getExtras();
            if ( b == null ) return;

            Integer lot = b.getInt("lot", -1);
            if (lot == -1) lot = null;
            String name = b.getString("name", null);
            String category = b.getString("category", null);
            String period = b.getString("period", null);

            Log.d(TAG, "lot: "+lot);
            Log.d(TAG, "name: "+name);
            Log.d(TAG, "category: "+category);
            Log.d(TAG, "period: "+period);

            Query searchQuery = queryFactory.getFilteredQuery(lot, name, category, period);
            if (searchQuery == null) {
                Log.w(TAG, "searchQuery was null");
                searchQuery = db.collection("artifactData");
            }
            insertData(searchQuery);

        }
    }
