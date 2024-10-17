package com.sai.mechat.activities;

import static com.sai.mechat.constants.IntentConstants.UPLOADING;
import static com.sai.mechat.constants.IntentConstants.UPLOAD_SUCCESS;
import static com.sai.mechat.constants.IntentConstants.USER_DETAILS_CODE;
import static com.sai.mechat.constants.IntentConstants.USER_IMAGE_PICK_CODE;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sai.mechat.R;
import com.sai.mechat.ViewModels.ImageUploadViewModel;
import com.sai.mechat.ViewModels.UserViewModel;
import com.sai.mechat.databinding.ActivityUserDetailsBinding;
import com.sai.mechat.models.User;

import java.util.Objects;

public class UserDetailsActivity extends AppCompatActivity {
    private ActivityUserDetailsBinding views;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ImageUploadViewModel uploadImageVM;
    private UserViewModel userVM;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        views = ActivityUserDetailsBinding.inflate(getLayoutInflater());
        setContentView(views.getRoot());

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uploadImageVM = new ViewModelProvider(this).get(ImageUploadViewModel.class);
        Intent i = getIntent();
        if (i != null){
            User u;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                u = i.getSerializableExtra("user_data", User.class);
                if (u != null){
                    Glide.with(this).load(u.getUserPhoto()).placeholder(R.drawable.person).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(views.userProfileImage);
                    views.userNameInput.setText(u.getUserName());
                    views.userBioInput.setText(u.getUserBio());
                }
            }


        }

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), activityResult -> {
                    if (activityResult.getResultCode() == RESULT_OK && activityResult.getData() != null){
                        Uri selectedImage = activityResult.getData().getData();
                        Glide.with(this).load(selectedImage).into(views.userProfileImage);
                        uploadImageVM.uploadImage(selectedImage,"profiles",firebaseUser.getUid()+".jpeg");
                        uploadImageVM.getUploadStatus().observe(this, s -> {
                            if (Objects.equals(s, UPLOADING)){
                                uploadImageVM.getUploadPercent().observe(this,uploadStatus ->{
                                    //set progress bar and show progress
                                    Toast.makeText(this, uploadStatus.toString(), Toast.LENGTH_SHORT).show();
                                });
                            } else if (Objects.equals(s, UPLOAD_SUCCESS)) {
                                url = String.valueOf(uploadImageVM.getDownloadUrl().getValue());
                                Toast.makeText(this, UPLOAD_SUCCESS, Toast.LENGTH_SHORT).show();
                                Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
                            }else if (s.contains("Upload Failed")){
                                Toast.makeText(this,s, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
        );

        views.continueBtn.setOnClickListener(v->{
            if (views.userNameInput.getText().toString().isEmpty())
                views.userNameInput.setError("it can't be empty");
            else{
                views.userPhotoNameBio.setVisibility(View.INVISIBLE);
                views.userPhone.setVisibility(View.VISIBLE);
            }
        });



        views.userProfileImage.setOnClickListener(v->{
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        views.verify.setOnClickListener(v->{
            // verify phone and save the details

        });

        views.skip.setOnClickListener(v->{
            User u = new User();
            u.setUserBio(views.userBioInput.getText().toString().trim());
            u.setUserName(views.userNameInput.getText().toString().trim());
            if (url != null && !url.isEmpty()){
                u.setUserPhoto(url);
            }
            returnBack(u);

        });



    }



    private void returnBack(User user){
        Intent i = getIntent();
        i.putExtra("data",user);
        setResult(USER_DETAILS_CODE,i);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}