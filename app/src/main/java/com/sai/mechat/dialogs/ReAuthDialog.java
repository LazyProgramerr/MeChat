package com.sai.mechat.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sai.mechat.R; // Ensure the correct package path

public class ReAuthDialog {

    private Dialog dialog;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button reAuthButton;
    private Button cancelButton;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private ReAuthCallback callback; // Callback interface

    // Define the callback interface
    public interface ReAuthCallback {
        void onReAuthSuccess();
        void onReAuthFailure(String error);
    }

    public ReAuthDialog(Context context, String mail, ReAuthCallback callback) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_verify_password); // Ensure the correct layout file

        emailEditText = dialog.findViewById(R.id.reAuth_mail);
        passwordEditText = dialog.findViewById(R.id.reAuth_password);
        reAuthButton = dialog.findViewById(R.id.reAuth_btn);
        cancelButton = dialog.findViewById(R.id.cancel_btn);

        this.callback = callback; // Set the callback

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

        // Set the email field to display only the first 5 letters and hide the rest
        emailEditText.setText(hideEmailContent(mail));
        emailEditText.setEnabled(false); // Disable editing of the email field

        setupListeners();
    }

    private String hideEmailContent(String email) {
        if (email.length() <= 5) {
            return email; // Return the full email if it's shorter than or equal to 5 characters
        }
        return email.substring(0, 5) + "*****"; // Show first 5 letters and hide the rest
    }

    private void setupListeners() {
        // Set up cancel button listener
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        // Set up re-authentication button listener
        reAuthButton.setOnClickListener(v -> reAuthenticateUser());

        // Add text watcher to enable/disable button based on input
        passwordEditText.addTextChangedListener(createTextWatcher());
    }

    private TextWatcher createTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                reAuthButton.setEnabled(passwordEditText.getText().toString().trim().length() >= 8 &&
                        !passwordEditText.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
    }

    private void reAuthenticateUser() {
        if (fUser == null) {
            if (callback != null) {
                callback.onReAuthFailure("User not logged in");
            }
            return;
        }

        String email = emailEditText.getText().toString().trim().replace("*****", ""); // Get original email
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            if (callback != null) {
                callback.onReAuthFailure("Please fill in all fields");
            }
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        // Re-authenticate the user
        fUser.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                dialog.dismiss();
                if (callback != null) {
                    callback.onReAuthSuccess(); // Notify success
                }
            } else {
                String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                if (callback != null) {
                    callback.onReAuthFailure(errorMessage); // Notify failure
                }
            }
        });
    }

    public void show() {
        dialog.show();
    }
}
