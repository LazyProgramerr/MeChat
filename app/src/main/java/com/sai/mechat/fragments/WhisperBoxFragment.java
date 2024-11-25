package com.sai.mechat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.sai.mechat.databinding.FragmentWhisperBoxBinding;

public class WhisperBoxFragment extends Fragment {
    private FragmentWhisperBoxBinding views;

    public WhisperBoxFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        views = FragmentWhisperBoxBinding.inflate(inflater, container, false);
        return views.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        views.btn.setOnClickListener(v -> {
            // Create and enqueue the work request
//            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class).build();
//            WorkManager.getInstance(requireContext()).enqueue(workRequest);
//
//            // Observe the work status
//            WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(workRequest.getId()).observe(getViewLifecycleOwner(), workInfo -> {
//                if (workInfo != null && workInfo.getState().isFinished()) {
//                    // Check if the work failed
//                    if (workInfo.getState() == WorkInfo.State.FAILED) {
//                        String error = workInfo.getOutputData().getString("error_message");
//                        Toast.makeText(requireContext(), "Failed: " + error, Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(requireContext(), "Worker finished its process", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
        });
    }
}
