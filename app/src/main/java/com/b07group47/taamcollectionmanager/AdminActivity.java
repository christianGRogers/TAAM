package com.b07group47.taamcollectionmanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminActivity extends BaseActivity {
    EditText Email, Password;
    Button buttonLogin;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin); // Make sure to set the content view
        mAuth = FirebaseAuth.getInstance();
        buttonLogin = findViewById(R.id.login);
        Email = findViewById(R.id.username);
        Password = findViewById(R.id.password);

        buttonLogin.setEnabled(false); // Initially disable the button

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Enable the button only if both fields are filled
                buttonLogin.setEnabled(!Email.getText().toString().trim().isEmpty() &&
                        !Password.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed here
            }
        };

        Email.addTextChangedListener(textWatcher);
        Password.addTextChangedListener(textWatcher);

        buttonLogin.setOnClickListener(v -> {
            saveValue();
            Email.setText(""); // Clear the email field
            Password.setText(""); // Clear the password field
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_admin;
    }

    protected void saveValue() {
        String user_email = Email.getText().toString().trim();
        String user_password = Password.getText().toString().trim();

        if (user_email.isEmpty() || user_password.isEmpty()) {
            Toast.makeText(this, "PLEASE FILL OUT ALL FIELDS", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sign in with Firebase Authentication
        mAuth.signInWithEmailAndPassword(user_email, user_password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    protected void updateUI(FirebaseUser user) {
        if (user != null) {
            // User is signed in, navigate to the main activity or whatever is appropriate
            Toast.makeText(this, "Authentication successful.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("fromAdmin", true); // Pass the extra
            switchToActivity(this, intent);
        } else {
            // User is signed out, handle appropriately
        }
    }
}
