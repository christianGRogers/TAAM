package com.b07group47.taamcollectionmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class SearchItemFragment extends Fragment {

    private EditText editText, editTextAuthor, editTextGenre, editTextDescription;
    private Spinner spinnerCategory;
    private Button buttonSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_items, container, false);

        Button buttonAddItem = view.findViewById(R.id.buttonAddItem);
        Button buttonDeleteItem = view.findViewById(R.id.buttonDeleteItem);
        Button buttonBack = view.findViewById(R.id.buttonBack);

        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new AddItemFragment());
            }
        });

        buttonDeleteItem.setOnClickListener(v -> loadFragment(new DeleteItemFragment()));

        buttonBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

