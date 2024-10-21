package com.sai.mechat.activities;

import static com.sai.mechat.constants.IntentConstants.USER_DETAILS_CODE;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sai.mechat.R;
import com.sai.mechat.functions.Animation;
import com.sai.mechat.functions.Time;
import com.sai.mechat.ViewModels.UserViewModel;
import com.sai.mechat.databinding.ActivityMainBinding;
import com.sai.mechat.fragments.FlashFeedFragment;
import com.sai.mechat.fragments.HavenFragment;
import com.sai.mechat.fragments.MirrorFragment;
import com.sai.mechat.fragments.WhisperBoxFragment;
import com.sai.mechat.functions.functions;
import com.sai.mechat.models.User;
import com.sai.mechat.notification.Token;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;

    private FirebaseUser fUser;
    private UserViewModel userVM;
    ActivityResultLauncher<Intent> userDetailsActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());




        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userVM = new ViewModelProvider(this).get(UserViewModel.class);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragments_holder, new HavenFragment()).commit();

        

        userDetailsActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),result->{
                    if (result.getResultCode() == USER_DETAILS_CODE && result.getData() != null){
                        User u = (User) result.getData().getSerializableExtra("data");
                        userVM.updateUserData(u).observe(MainActivity.this, aBoolean -> {
                            if (aBoolean)
                                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
        );


        userVM.getUser(fUser.getUid()).observe(this, user -> {

            if (user.getUserName() == null || user.getUserName().equals(".")){
                Intent intent = new Intent(getApplicationContext(), UserDetailsActivity.class);
                userDetailsActivityLauncher.launch(intent);
            }
        });

        view.headerSearch.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(), SearchActivity.class));
        });

        view.headerOptions.setOnClickListener(v->{
            PopupMenu popupMenu = new PopupMenu(this,view.headerOptions);
            MenuInflater inflater = popupMenu.getMenuInflater();

            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragments_holder);
            if (currentFragment instanceof MirrorFragment)
                inflater.inflate(R.menu.mirror_menu, popupMenu.getMenu());
            if (currentFragment instanceof HavenFragment)
                inflater.inflate(R.menu.haven_menu,popupMenu.getMenu());
            if (currentFragment instanceof WhisperBoxFragment)
                inflater.inflate(R.menu.whisper_box_menu,popupMenu.getMenu());
            inflater.inflate(R.menu.common_menu, popupMenu.getMenu());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                popupMenu.setForceShowIcon(true);
            }
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (String.valueOf(item.getTitle())){
                    case "settings" -> {
                        Toast.makeText(MainActivity.this, "clicked on screw", Toast.LENGTH_SHORT).show();
                    }
                    case "Edit Details" -> {
//                            Toast.makeText(MainActivity.this, "clicked on pen", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), UserDetailsActivity.class);
                        i.putExtra("user_data",userVM.getUser(fUser.getUid()).getValue());
                        userDetailsActivityLauncher.launch(i);
                    }
                    case "vibrate" -> {
                        functions.INSTANCE.vibrate(this,500);
                    }
                }
                return false;
            });
            popupMenu.show();
            Animation.INSTANCE.vibrate(this,view.headerOptions,500);
        });

        view.bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int lastIndex, @Nullable AnimatedBottomBar.Tab lastTab, int newIndex, @NonNull AnimatedBottomBar.Tab newTab) {
//                Toast.makeText(MainActivity.this, "i : "+lastTab.getTitle()+"\ni1 : "+newTab.getTitle(), Toast.LENGTH_SHORT).show();
                if(lastIndex != newIndex){
                    Fragment selectedFragment = null;
//                    Toast.makeText(MainActivity.this, "tab id : "+newTab.getId(), Toast.LENGTH_SHORT).show();

                    switch (newTab.getTitle()){
                        case "Haven" -> selectedFragment = new HavenFragment();
                        case "WhisperBox" -> selectedFragment = new WhisperBoxFragment();
                        case "FlashFeed" -> selectedFragment = new FlashFeedFragment();
                        case "Mirror" -> selectedFragment = MirrorFragment.newInstance(null);
                    }
                    view.headerTitle.setText(newTab.getTitle().equals("Heaven")? getString(R.string.app_name): newTab.getTitle());
                    view.headerLayout.setVisibility(newTab.getTitle().equals("FlashFeed") ? View.GONE : View.VISIBLE);
                    if (selectedFragment != null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragments_holder,selectedFragment).commit();
                    }
                }

            }

            @Override
            public void onTabReselected(int index, @NonNull AnimatedBottomBar.Tab tab) {
            }
        });



    }



    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser fU = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference userStateRef = db.getReference("users/"+fU.getUid());

        userStateRef.child("userOnline").setValue("online");
        userStateRef.child("Token").setValue(Token.INSTANCE.getToken());

    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference userStateRef = db.getReference("users/"+fUser.getUid());
        String time = Time.INSTANCE.readableTime(System.currentTimeMillis());
        userStateRef.child("userOnline").setValue(time);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference userStateRef = db.getReference("users/"+fUser.getUid());
        String time = Time.INSTANCE.readableTime(System.currentTimeMillis());
        userStateRef.child("userOnline").setValue(time);
    }
}