package com.b07group47.taamcollectionmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class TableCard extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LOT_NUM = "lotNum";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String CATEGORY = "category";
    private static final String PERIOD = "period";
    private static final String IMAGE = "image";


    // TODO: Rename and change types of parameters
    private int lot, imageID;
    private String name, description, category, period;

    public TableCard() {
        // Required empty public constructor
    }

    public static TableCard newInstance(int lot, String name, String desc, String cat, String period, int imageID) {
        TableCard fragment = new TableCard();
        Bundle args = new Bundle();
        args.putInt(LOT_NUM, lot);
        args.putString(NAME, name);
        args.putString(DESCRIPTION, desc);
        args.putString(CATEGORY, cat);
        args.putString(PERIOD, period);
        args.putInt(IMAGE, imageID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lot = getArguments().getInt(LOT_NUM);
            name = getArguments().getString(NAME);
            description = getArguments().getString(DESCRIPTION);
            category = getArguments().getString(CATEGORY);
            period = getArguments().getString(PERIOD);
            imageID = getArguments().getInt(IMAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.table_card, container, false);

        // Inflate the layout for this fragment
        TextView lotNum = view.findViewById(R.id.lotNum);
        TextView cardHeader = view.findViewById(R.id.cardHeader);
        TextView cardDescription = view.findViewById(R.id.cardDescription);
        TextView cardCategory = view.findViewById(R.id.cardCategory);
        TextView cardPeriod = view.findViewById(R.id.cardPeriod);
        ImageView image = view.findViewById(R.id.cardImage);

        lotNum.setText(Integer.toString(lot));
        cardHeader.setText(name);
        cardDescription.setText(description);
        cardCategory.setText(category);
        cardPeriod.setText(period);
        image.setImageResource(imageID);

        return view;
    }
}