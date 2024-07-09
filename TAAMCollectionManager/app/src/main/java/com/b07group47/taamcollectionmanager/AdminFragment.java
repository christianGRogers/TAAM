package com.b07group47.taamcollectionmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AdminFragment extends Fragment{
    private EditText Email, Password;
    private String User_email, User_password;
    private Button buttonLogin;


    //WHAT I WANT TO ADD
    /// the button that is not clickable until text is filled out
    // once you click it text erase
    //saving it throughout the app so that you can stay login

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        buttonLogin = view.findViewById(R.id.login);
        Email = view.findViewById(R.id.username);
        Password = view.findViewById(R.id.password);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveValue();
            }
        });
        return view;
    }
    private void saveValue() {
        User_password = Password.getText().toString().trim();
        User_email = Email.getText().toString().trim();

        if (User_email.isEmpty() || User_password.isEmpty()){
            Toast.makeText(getContext(), "PLEASE FILL OUT ALL FIELDS", Toast.LENGTH_SHORT).show();
            return;
        }
        return;
    }
}
