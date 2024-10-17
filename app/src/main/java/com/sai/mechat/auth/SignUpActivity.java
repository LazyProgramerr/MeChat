package com.sai.mechat.auth;

import static com.sai.mechat.constants.IntentConstants.MAIL;
import static com.sai.mechat.constants.IntentConstants.PASSWORD;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sai.mechat.R;
import com.sai.mechat.ViewModels.UserViewModel;
import com.sai.mechat.activities.MainActivity;
import com.sai.mechat.databinding.ActivitySignUpBinding;
import com.sai.mechat.databinding.LayoutPasswordBinding;
import com.sai.mechat.models.User;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding view;
    private FirebaseAuth firebaseAuth;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        
        firebaseAuth = FirebaseAuth.getInstance();
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        view.signupBtn.setOnClickListener(v->{
            String mail = view.signupMail.getText().toString().trim();
            String pass = view.signupPassword.inputPassword.getText().toString().trim();

            if (mail.isEmpty() || pass.isEmpty()){
                
            }else{
                firebaseAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        User userData = new User();
                        userData.setUid(user.getUid());
                        userData.setUserMail(user.getEmail());
                        userData.setUserPassword(pass);
                        userData.setUserFriends("0");
                        userData.setUserName(".");
                        userData.setUserPhone(".");
                        userData.setUserPhoto(".");
                        userData.setUserBio(".");

                        UserViewModel userVM = new ViewModelProvider(this).get(UserViewModel.class);
                        userVM.setUserData(userData);

                        verifyTheUserMail();

                    }
                }).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
            }

        });
        LayoutPasswordBinding passwordBinding = view.signupPassword;
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


    }

    private void verifyTheUserMail() {
        Intent mailVerifyIntent = new Intent(getApplicationContext(),VerifyMailActivity.class);
        startActivity(mailVerifyIntent);
    }

}