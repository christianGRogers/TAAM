package com.b07group47.taamcollectionmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.fragment.app.FragmentTransaction;

public class ViewFragment  extends Fragment{
    private TextView itemInfo;
    private Button back_btn;
    private ImageView itemImage;

    private FirebaseDatabase db;
    private DatabaseReference itemsRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_view, container, false);

        itemInfo = view.findViewById(R.id.itemInfo);
        back_btn = view.findViewById(R.id.back_btn);
        itemImage = view.findViewById(R.id.itemImage);

        itemInfo.setText("Sample text\nitem information goes here");

        //todo
        //
        back_btn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                //todo
                //go back
            }
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
