package com.b07group47.taamcollectionmanager;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class FilteredSearcher {

    private final String TAG = "FilteredSearcher";

    FirebaseFirestore db;

    public FilteredSearcher() {
         db = FirebaseFirestore.getInstance();
    }

    ArrayList<Item> search(Integer lotNumber, String name, String category, String period) {
        ArrayList<Item> itemList = new ArrayList<>();
         db.collection("artifacts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();

                                if ( lotNumber != null && lotNumber.equals(data.get("lot"))
                                        && (name != null && name.equals(data.get("category")))
                                        && (category != null && category.equals(data.get("category")))
                                        && (period != null && category.equals(data.get("category")))
                                ) {
                                    itemList.add(new Item(data));
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return itemList;
    }

}
