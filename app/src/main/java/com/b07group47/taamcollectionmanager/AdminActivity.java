package com.b07group47.taamcollectionmanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminActivity extends BaseActivity {
    private EditText Email, Password;

    //WHAT I WANT TO ADD
    /// the button that is not clickable until text is filled out
    // once you click it text erase
    //saving it throughout the app so that you can stay login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button buttonLogin = findViewById(R.id.login);
        Email = findViewById(R.id.username);
        Password = findViewById(R.id.password);

        buttonLogin.setOnClickListener(v -> saveValue());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_admin;
    }

    private void saveValue() {
        String user_password = Password.getText().toString().trim();
        String user_email = Email.getText().toString().trim();

        if (user_email.isEmpty() || user_password.isEmpty()) {
            Toast.makeText(this, "PLEASE FILL OUT ALL FIELDS", Toast.LENGTH_SHORT).show();
        }
    }
}