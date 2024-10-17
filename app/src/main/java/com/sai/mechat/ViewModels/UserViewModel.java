package com.sai.mechat.ViewModels;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sai.mechat.models.User;

import java.util.HashMap;
import java.util.Map;

public class UserViewModel  extends ViewModel {
    private MutableLiveData<User> userLiveData;
    private MutableLiveData<Boolean> uploadStatus;
    private FirebaseDatabase db;
    private DatabaseReference userRef;

    public UserViewModel(){
        db = FirebaseDatabase.getInstance();
        userRef = db.getReference("users");
    }
    // Function to retrieve user data from Firebase
    public LiveData<User> getUser(String userId) {
        if (userLiveData == null) {
            userLiveData = new MutableLiveData<>();
            loadUserData(userId);
        }
        return userLiveData;
    }
    public LiveData<Boolean> updateUserData(User user){
        uploadStatus = new MutableLiveData<>();
        updateUserD(user);
        return uploadStatus;
    }

    // Loading data from Firebase with offline support
    private void loadUserData(String userId) {
        // Attach a listener for both online and offline data
        userRef.child(userId).keepSynced(true); // Keep this user's data in sync

        userRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userLiveData.postValue(user);  // Update LiveData
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }
    private void updateUserD(User updatedUser) {
        Map<String, Object> updates = new HashMap<>();

        if (updatedUser.getUserName() != null && !updatedUser.getUserName().isEmpty()) {
            updates.put("userName", updatedUser.getUserName());
        }
        if (updatedUser.getUserMail() != null && !updatedUser.getUserMail().isEmpty()) {
            updates.put("userMail", updatedUser.getUserMail());
        }
        if (updatedUser.getUserPhone() != null && !updatedUser.getUserPhone().isEmpty()) {
            updates.put("userPhone", updatedUser.getUserPhone());
        }
        if (updatedUser.getUserPhoto() != null && !updatedUser.getUserPhoto().isEmpty()) {
            updates.put("userPhoto", updatedUser.getUserPhoto());
        }
        if (updatedUser.getUserBio() != null && !updatedUser.getUserBio().isEmpty()) {
            updates.put("userBio", updatedUser.getUserBio());
        }
        if (updatedUser.getUserFriends() != null && !updatedUser.getUserFriends().isEmpty()) {
            updates.put("userFriends", updatedUser.getUserFriends());
        }
        // You can add more fields if necessary

        // Update only non-empty fields
        if (!updates.isEmpty()) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            userRef.child(firebaseUser.getUid()).updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        uploadStatus.setValue(true);
                    }else{
                        uploadStatus.setValue(false);
                    }
                }
            });
        }else
            uploadStatus.setValue(false);
    }
    public void setUserData(User u){
        userRef.child(u.getUid()).setValue(u);
    }
}
