package com.sai.mechat.ViewModels;

import static com.sai.mechat.constants.IntentConstants.UPLOADING;
import static com.sai.mechat.constants.IntentConstants.UPLOAD_SUCCESS;

import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageKt;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sai.mechat.activities.UserDetailsActivity;

public class ImageUploadViewModel extends ViewModel {

    private FirebaseStorage storage;
    private StorageReference storageRef;

    private final MutableLiveData<String> uploadStatus = new MutableLiveData<>();
    private final MutableLiveData<String> downloadUrl = new MutableLiveData<>();
    private final MutableLiveData<Integer> uploadPercentage = new MutableLiveData<>();

    public ImageUploadViewModel(){
        storage = FirebaseStorage.getInstance();
    }

    public LiveData<String> getUploadStatus(){
        return uploadStatus;
    }
    public LiveData<String> getDownloadUrl(){
        return downloadUrl;
    }
    public LiveData<Integer> getUploadPercent(){
        return uploadPercentage;
    }
    public void uploadImage(Uri imageUri,String path,String name){
        uploadStatus.setValue(UPLOADING);
        storageRef = storage.getReference(path);
        storageRef.child(name).putFile(imageUri).addOnProgressListener(snapshot -> {
            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
            uploadPercentage.setValue((int) progress);
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                storageRef.child(name).getDownloadUrl().addOnCompleteListener(task1 -> {
                    downloadUrl.setValue(task1.getResult().toString());
                    uploadStatus.setValue(UPLOAD_SUCCESS);
                });
            }else {
                uploadStatus.setValue("Upload Failed : "+task.getException().getMessage());
            }
        });
    }



}
