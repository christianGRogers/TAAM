package com.b07group47.taamcollectionmanager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import android.text.Editable;
import android.text.TextWatcher;

import org.junit.Before;
import org.junit.Test;

class MockEditText {
    private String text = "";
    private TextWatcher textWatcher;

    public void setText(String text) {
        this.text = text;
        if (textWatcher != null) {
            textWatcher.afterTextChanged(null); // Simplified call
        }
    }

    public String getText() {
        return text;
    }

    public void addTextChangedListener(TextWatcher watcher) {
        this.textWatcher = watcher;
    }
}

class MockButton {
    private boolean enabled = false;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}

class AdminActivity {
    private MockEditText emailEditText;
    private MockEditText passwordEditText;
    private MockButton loginButton;

    public void setFields(MockEditText emailEditText, MockEditText passwordEditText, MockButton loginButton) {
        this.emailEditText = emailEditText;
        this.passwordEditText = passwordEditText;
        this.loginButton = loginButton;

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                loginButton.setEnabled(!emailEditText.getText().isEmpty() && !passwordEditText.getText().isEmpty());
            }
        };

        emailEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);
    }

    public void saveValue() {
        emailEditText.setText("");
        passwordEditText.setText("");
    }
}

public class loginUnitTest {

    private AdminActivity adminActivity;
    private MockEditText emailEditText;
    private MockEditText passwordEditText;
    private MockButton loginButton;

    @Before
    public void setUp() {
        adminActivity = new AdminActivity();
        emailEditText = new MockEditText();
        passwordEditText = new MockEditText();
        loginButton = new MockButton();
        adminActivity.setFields(emailEditText, passwordEditText, loginButton);
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
        adminActivity.saveValue();

        // Check if email and password fields are cleared
        assertEquals("", emailEditText.getText());
        assertEquals("", passwordEditText.getText());
    }

    @Test
    public void testSaveValueFieldsEmpty() {
        // Simulate entering text
        emailEditText.setText("");
        passwordEditText.setText("");

        // Call saveValue method
        adminActivity.saveValue();

        // Check if email and password fields are still empty
        assertEquals("", emailEditText.getText());
        assertEquals("", passwordEditText.getText());
    }
}
