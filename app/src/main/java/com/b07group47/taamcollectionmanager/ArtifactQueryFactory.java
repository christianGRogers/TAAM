package com.b07group47.taamcollectionmanager;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ArtifactQueryFactory {

    private final String TAG = "QueryManager";

    public static CollectionReference getAll() { return FirebaseFirestore.getInstance().collection("artifactData"); }

    public static Query getFilteredQuery(Long lot, String name, String category, String period) {

        Query query = getAll();

        if (lot != null) {
            query = query.whereEqualTo("lot", lot);
        }
        if (name != null) {
            query = query.whereEqualTo("name", name);
        }
        if (category != null) {
            query = query.whereEqualTo("category", category);
        }
        if (period != null) {
            query = query.whereEqualTo("period", period);
        }

        return query;
    }

}
