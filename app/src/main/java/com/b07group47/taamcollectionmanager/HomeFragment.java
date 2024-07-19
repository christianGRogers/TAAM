package com.b07group47.taamcollectionmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);
//        Button buttonRecyclerView = view.findViewById(R.id.buttonRecyclerView);
//        Button buttonScroller = view.findViewById(R.id.buttonScroller);
//        Button buttonSpinner = view.findViewById(R.id.buttonSpinner);
        Button buttonManageItems = view.findViewById(R.id.buttonView);
        Button buttonSearch = view.findViewById(R.id.buttonSearch);
        Button buttonAdmin = view.findViewById(R.id.buttonAdmin);
        Button buttonReport = view.findViewById(R.id.buttonReport);
        Button dataTable = view.findViewById(R.id.data_table);

        dataTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new MainTableFragment());}
        });

//        buttonScroller.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadFragment(new ScrollerFragment());
//            }
//        });

//        buttonSpinner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadFragment(new SpinnerFragment());
//            }
//        });

        buttonManageItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { loadFragment(new ViewFragment());}
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { loadFragment(new SearchFragment());}
        });
        buttonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { loadFragment(new ReportFragment());}
        });

        buttonAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { loadFragment(new AdminFragment());}
        });
        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
