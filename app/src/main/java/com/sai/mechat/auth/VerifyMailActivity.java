package com.sai.mechat.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sai.mechat.activities.MainActivity;
import com.sai.mechat.databinding.ActivityVerifyMailBinding;

public class VerifyMailActivity extends AppCompatActivity {
    private ActivityVerifyMailBinding view;
    FirebaseAuth fAuth;
    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = ActivityVerifyMailBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());

        fAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();



        view.resendBtn.setOnClickListener(v -> fUser.sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Toast.makeText(this, "verified successfully", Toast.LENGTH_SHORT).show();
                getUserDetails();
            }
        }).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show()));

    }

    private void getUserDetails() {
//        check the database for data if exists go to home else goto user details activity
        Intent userIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(userIntent);
        finishAffinity();
    }
    private void startEmailVerificationCheck() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            // Reload the user to get the updated email verification status
            fUser.reload().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (fUser.isEmailVerified()) {
                        // If email is verified, navigate to MainActivity
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finishAffinity();
                    } else {
                        // If email is not verified, check again after a delay
                        startEmailVerificationCheck();
                    }
                } else {
                    Toast.makeText(VerifyMailActivity.this, "Failed to reload user", Toast.LENGTH_SHORT).show();
                }
            });
        }, 5000); // Poll every 5 seconds
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(fUser.isEmailVerified()){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finishAffinity();
        }else {
            fUser.sendEmailVerification();
            startEmailVerificationCheck();
        }
    }
}