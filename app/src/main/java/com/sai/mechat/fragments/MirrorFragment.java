package com.sai.mechat.fragments;

import static com.sai.mechat.constants.NotificationConstants.TYPE_FRIEND_REQUEST;
import static com.sai.mechat.constants.NotificationConstants.TYPE_FRIEND_REQUEST_RESPONSE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sai.mechat.R;
import com.sai.mechat.ViewModels.UserViewModel;
import com.sai.mechat.databinding.FragmentMirrorBinding;
import com.sai.mechat.functions.FirebaseFunctions;
import com.sai.mechat.networkServices.NetworkClient;
import com.sai.mechat.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class MirrorFragment extends Fragment {
    private FragmentMirrorBinding views;
    private FirebaseUser fUser;
    private UserViewModel userVM;
    private String userId = null;
    private User userData = new User();
    private boolean isFriend = false;
    private boolean isRequested = false;
    private boolean sentByYou = false;

    public MirrorFragment() {
        // Required empty public constructor
    }

    public static MirrorFragment newInstance(String data) {

        Bundle args = new Bundle();
        args.putString("userId",data);
        MirrorFragment fragment = new MirrorFragment();
        if (data != null)
            fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        views = FragmentMirrorBinding.inflate(inflater,container,false);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (getArguments() != null){
            userId = getArguments().getString("userId");
        }else{
            userId = fUser.getUid();
        }


        userVM = new ViewModelProvider(this).get(UserViewModel.class);
        if (Objects.equals(userId, fUser.getUid()))
            views.friendRequest.setVisibility(View.GONE);

        userVM.getUser(userId).observe((LifecycleOwner) requireContext(), user -> {
            userData = user;
            Glide.with(requireContext()).load(user.getUserPhoto()).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.person).into(views.profilePic);
            views.userName.setText(user.getUserName());
            views.bio.setText(user.getUserBio());
            views.friendsCount.setText(user.getUserFriends());
            setFriendButton();
        });

        return views.getRoot();
    }

    private void setFriendButton() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("users").child(fUser.getUid());
        db.keepSynced(true);
        db.child("friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userData.getUid()) && Boolean.TRUE.equals(snapshot.child(userData.getUid()).getValue(Boolean.class))){
                    views.friendRequest.setText("Friends");
                    isFriend = true;
                }else {
                    checkRequestReceived(db);
                    isFriend = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkRequestReceived(DatabaseReference db) {
        db.child(TYPE_FRIEND_REQUEST).child("received").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userData.getUid())){
                    views.friendRequest.setText("accept");
                    isRequested = true;
                    sentByYou = false;
                }else{
                    checkRequestSent(db);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void checkRequestSent(DatabaseReference db){
        db.child(TYPE_FRIEND_REQUEST).child("sent").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userData.getUid())){
                    views.friendRequest.setText("Requested");
                    isRequested = true;
                    sentByYou = true;
                }else{
                    views.friendRequest.setText("Make Friend");
                    isRequested = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NetworkClient client = new NetworkClient();
        views.friendRequest.setOnClickListener(v->{
            if (isFriend){
                removeFriend();
                Toast.makeText(getContext(), "unfriend", Toast.LENGTH_SHORT).show();
            } else if (isRequested && !sentByYou) {
                acceptRequest();
                Toast.makeText(getContext(), "accept", Toast.LENGTH_SHORT).show();
            }else if(isRequested){
                cancelRequest("received");
                Toast.makeText(getContext(), "Requested", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(),"make friend",Toast.LENGTH_SHORT).show();
                JSONObject data = new JSONObject();
                try {
                    data.put("senderId",fUser.getUid());
                    data.put("message","you had received an friend request");
                    data.put("title","test ");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("users").child(userData.getUid()).child("Token");
                db.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        String token = Objects.requireNonNull(task.getResult().getValue(String.class));
                        Toast.makeText(getContext(), "request sent to "+ token, Toast.LENGTH_SHORT).show();

                        client.sendFriendRequest(token, fUser.getUid(),"you had received an friend request",TYPE_FRIEND_REQUEST,userData.getUid());

                    }
                });

            }
        });
    }

    private void removeFriend() {

    }

    private void acceptRequest() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("users");
        db.child(fUser.getUid()).child("friends").child(userData.getUid()).setValue(Boolean.TRUE);
        db.child(fUser.getUid()).child("FriendRequest").child("received").child(userData.getUid()).removeValue();
        String token = new FirebaseFunctions(requireContext()).GetFirebaseToken(userData.getUid());
        sendResponse(token);


    }

    private void sendResponse(String token) {
        NetworkClient client = new NetworkClient();
        client.sendFriendRequestResponse(token, fUser.getUid(), "you had received the response of your request",true,TYPE_FRIEND_REQUEST_RESPONSE);
    }

    private void cancelRequest(String path) {
//        remove request and notify the other person

    }

}