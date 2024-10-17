package com.sai.mechat.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sai.mechat.R;
import com.sai.mechat.ViewModels.UserViewModel;
import com.sai.mechat.activities.NotificationsActivity;
import com.sai.mechat.databinding.FragmentMirrorBinding;
import com.sai.mechat.models.User;

import java.util.Objects;

public class MirrorFragment extends Fragment {
    private FragmentMirrorBinding views;
    private FirebaseUser fUser;
    private UserViewModel userVM;
    private String userId = null;

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
        return views.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
            Glide.with(requireActivity()).load(user.getUserPhoto()).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.person).into(views.profilePic);
            views.userName.setText(user.getUserName());
            views.bio.setText(user.getUserBio());
            views.friendsCount.setText(user.getUserFriends());
        });
    }
}