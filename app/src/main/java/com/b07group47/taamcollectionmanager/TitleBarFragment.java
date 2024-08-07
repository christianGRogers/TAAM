package com.b07group47.taamcollectionmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class TitleBarFragment extends Fragment {
    private static final String ARG_SCREEN = "currentScreen";
    private String currentScreen;

    public TitleBarFragment() {
        // Required empty public constructor
    }

    public static TitleBarFragment newInstance(String currentScreen) {
        TitleBarFragment fragment = new TitleBarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SCREEN, currentScreen);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentScreen = getArguments().getString(ARG_SCREEN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_title_bar, container, false);
        ImageView backButton = view.findViewById(R.id.backButton);
        ImageView searchButton = view.findViewById(R.id.searchIcon);
        ImageView adminButton = view.findViewById(R.id.adminIcon);
        adminButton.setOnClickListener(v -> startActivity(new Intent(getContext(), AdminActivity.class)));
        searchButton.setOnClickListener(v -> startActivity(new Intent(getContext(), SearchActivity.class)));

        backButton.setOnClickListener(v -> {
            if (getActivity() == null) {
                return;
            }

            getActivity().onBackPressed();
        });

        if (currentScreen == null)
            return view;

        if (currentScreen.contentEquals("MAIN_SCREEN")) {
            backButton.setVisibility(View.INVISIBLE);
        } else if (currentScreen.contentEquals("ADMIN_SCREEN")) {
            searchButton.setVisibility(View.INVISIBLE);
            adminButton.setVisibility(View.INVISIBLE);
        } else if (currentScreen.contentEquals("SEARCH_SCREEN")) {
            searchButton.setVisibility(View.INVISIBLE);
        }

        return view;
    }
}