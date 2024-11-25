package com.sai.mechat.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GithubAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sai.mechat.R;
import com.sai.mechat.activities.MainActivity;
import com.sai.mechat.databinding.ActivityLoginBinding;
import com.sai.mechat.databinding.LayoutPasswordBinding;

import java.util.Objects;

import com.sai.mechat.dialogs.ReAuthDialog;
import com.sai.mechat.manager.SharedPreferenceManager;
import com.sai.mechat.notification.Token;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding view;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private DatabaseReference db;
    private GoogleSignInClient client;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        fAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance().getReference("users");


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(this,googleSignInOptions);




        LayoutPasswordBinding passwordBinding = view.loginPassword;
        passwordBinding.passwordToggleBtn.setOnClickListener(v -> {
            isPasswordVisible = !isPasswordVisible;

            if (isPasswordVisible) {
                // Show password and play the animation in reverse
                passwordBinding.inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                passwordBinding.passwordToggleBtn.setSpeed(-1f);  // Reverse the animation
            } else {
                // Hide password and play the animation normally
                passwordBinding.inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                passwordBinding.passwordToggleBtn.setSpeed(1f);   // Play the animation forward
            }

            // Refresh the cursor position
            passwordBinding.inputPassword.setSelection(passwordBinding.inputPassword.getText().length());

            // Play the animation
            passwordBinding.passwordToggleBtn.playAnimation();
        });

//      executed when login(click to continue) button is clicked
        view.loginBtn.setOnClickListener(v->{
            String email = view.loginMail.getText().toString().trim();
            String password = view.loginPassword.inputPassword.getText().toString().trim();

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.isEmpty()){
                // set error
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                    view.loginMail.setError("enter valid email");
                if (password.isEmpty() || password.length() < 8)
                    view.loginPassword.inputPassword.setError("enter valid password and its length must be >= 8");
            }else{
//                continue with login process
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                        // get device token and store
                        fUser = FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(s -> {
                            db.child(fUser.getUid()).child("Token").setValue(s);
                            startActivity(new Intent(getApplicationContext(), VerifyMailActivity.class));
                            finishAffinity();
                        });

                    }else{
                        Toast.makeText(this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

//        executed when continue with google button is clicked
        view.continueWithGoogle.setOnClickListener(v->{
            googleActivityResultantLauncher.launch(new Intent(client.getSignInIntent()));
        });

        view.signupPage.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
        });


    }

    ActivityResultLauncher<Intent> googleActivityResultantLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),result ->{
                if (result.getData() != null && result.getResultCode() == Activity.RESULT_OK){
                    Intent resultData = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(resultData);
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        SigningWithGoogle(account.getIdToken());
                    } catch (ApiException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private void SigningWithGoogle(String idToken){
        AuthCredential credential = GithubAuthProvider.getCredential(idToken);
        fAuth.signInWithCredential(credential).addOnCompleteListener(task->{
            if (task.isSuccessful()){
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                // get device token and store
                FirebaseMessaging.getInstance().getToken().addOnSuccessListener(s -> {
                    db.child(fUser.getUid()).child("Token").setValue(s);
                    startActivity(new Intent(getApplicationContext(), VerifyMailActivity.class));
                    finishAffinity();
                });

            }else{
                Toast.makeText(this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ReAuth(){
        if (fUser.getProviderId().equals(GoogleAuthProvider.PROVIDER_ID))
            GoogleReAuth();
        else if (fUser.getProviderId().equals(EmailAuthProvider.PROVIDER_ID))
            showReAuthDialog(fUser.getEmail());   // email reAuth
    }

    private void GoogleReAuth(){
        AuthCredential credential = GoogleAuthProvider.getCredential(GoogleSignInAccount.createDefault().getIdToken(), null);
        fUser.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Re-authentication successful
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                SharedPreferenceManager.saveLastLoginTime(this,System.currentTimeMillis());
                finishAffinity();
            }
        });
    }

    private void showReAuthDialog(String email) {
        ReAuthDialog reAuthDialog = new ReAuthDialog(this, email, new ReAuthDialog.ReAuthCallback() {
            @Override
            public void onReAuthSuccess() {
                SharedPreferenceManager.saveLastLoginTime(LoginActivity.this,System.currentTimeMillis());
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finishAffinity();
            }

            @Override
            public void onReAuthFailure(String error) {
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
        reAuthDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null  && !user.isEmailVerified()){
            Intent i = new Intent(getApplicationContext(), VerifyMailActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
            finishAffinity();
//            dateAndTime lastLoginTime = SharedPreferenceManager.getLastLoginTime(this);
//            if (TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - lastLoginTime.getPreviousLoginTime()) > 2)
//                ReAuth();
//            else {
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                finishAffinity();
//            }

        } else if (user != null  && user.isEmailVerified()) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
            finishAffinity();
        }

    }
}