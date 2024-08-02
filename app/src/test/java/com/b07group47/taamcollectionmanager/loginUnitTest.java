package com.b07group47.taamcollectionmanager;
import android.content.Intent;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class loginUnitTest {

    private AdminActivity adminActivity;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Before
    public void setUp() {
        adminActivity = Robolectric.buildActivity(AdminActivity.class).create().get();
        emailEditText = adminActivity.findViewById(R.id.username);
        passwordEditText = adminActivity.findViewById(R.id.password);
        loginButton = adminActivity.findViewById(R.id.login);
    }

    @Test
    public void testButtonLoginInitialState() {
        // Initial state of loginButton should be disabled
        assertFalse(loginButton.isEnabled());
    }

    @Test
    public void testButtonEnabledOnTextChange() {
        // Simulate entering text
        emailEditText.setText("test@example.com");
        passwordEditText.setText("password");

        // Check if button is enabled
        assertTrue(loginButton.isEnabled());
    }

    @Test
    public void testButtonDisabledWhenEmailEmpty() {
        // Simulate entering text
        emailEditText.setText("");
        passwordEditText.setText("password");

        // Check if button is disabled
        assertFalse(loginButton.isEnabled());
    }

    @Test
    public void testButtonDisabledWhenPasswordEmpty() {
        // Simulate entering text
        emailEditText.setText("test@example.com");
        passwordEditText.setText("");

        // Check if button is disabled
        assertFalse(loginButton.isEnabled());
    }

    @Test
    public void testSaveValueFieldsNotEmpty() {
        // Simulate entering text
        emailEditText.setText("test@example.com");
        passwordEditText.setText("password");

        // Call saveValue method
        adminActivity.runOnUiThread(() -> adminActivity.saveValue());

        // Check if email and password fields are cleared
        assertEquals("", emailEditText.getText().toString());
        assertEquals("", passwordEditText.getText().toString());
    }

    @Test
    public void testSaveValueFieldsEmpty() {
        // Simulate entering text
        emailEditText.setText("");
        passwordEditText.setText("");

        // Call saveValue method
        adminActivity.runOnUiThread(() -> adminActivity.saveValue());

        // Check if email and password fields are still empty
        assertEquals("", emailEditText.getText().toString());
        assertEquals("", passwordEditText.getText().toString());
    }
}
