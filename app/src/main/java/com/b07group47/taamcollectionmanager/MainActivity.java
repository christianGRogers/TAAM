    package com.b07group47.taamcollectionmanager;

    import static android.content.ContentValues.TAG;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;

    import com.google.firebase.firestore.DocumentSnapshot;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.firestore.Query;


    import android.view.View;
    import android.widget.Button;
    import android.widget.ImageView;

    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import java.util.ArrayList;
    import java.util.List;

    public class MainActivity extends BaseActivity {
        private final List<Item> itemList = new ArrayList<>();
        private ItemAdapter itemAdapter;
        private Button buttonReport;
        private ImageView buttonAdd;

        private QueryFactory queryFactory;

        /**
         * Equivalent to a constructor of the activity
         * @param savedInstanceState the saved parameters of the activity
         */
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            queryFactory = new QueryFactory();
            initButtons();
            createTable();
            handleIntent();
        }

            FirebaseFirestore db = FirebaseFirestore.getInstance();

        private void initButtons() {
            buttonReport = findViewById(R.id.buttonReport);
            buttonAdd = findViewById(R.id.buttonAddItem);
            buttonAdd.setOnClickListener(v -> switchToActivity(new Intent(this, AddItemActivity.class)));
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
            insertData(queryFactory.getAll());
        }

        /**
         * Populates the list of the items displayed by the table in activity_main.xml
         */
    //      TODO: refactor to work with Firebase
        private void insertData(Query query) {
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    for(DocumentSnapshot d: task.getResult().getDocuments()) {
                        Item i = new Item(d.getData());
                        itemList.add(i);
                        itemAdapter.notifyItemInserted(itemList.size());
                    }
                } else {
                    Log.d(TAG, "Error getting items");
                }
            }).addOnFailureListener(e -> {
                Log.d(TAG, "Error querying item");
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
                //buttonDelete.setVisibility(View.VISIBLE);
            } else {
                Log.d(TAG, "AdminActivity not detected, buttons remain hidden.");
            }
            Bundle b = intent.getExtras();
            if ( b == null ) return;

            Integer lot = b.getInt("lot", -1);
            String name = b.getString("name", null);
            String category = b.getString("category", null);
            String period = b.getString("period", null);

            Query searchQuery = queryFactory.getFilteredQuery(lot, name, category, period);
            if (searchQuery == null)
                searchQuery = db.collection("artifactData");
            insertData(searchQuery);

        }
    }
