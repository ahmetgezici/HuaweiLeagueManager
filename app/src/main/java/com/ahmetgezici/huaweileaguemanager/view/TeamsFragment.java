package com.ahmetgezici.huaweileaguemanager.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ahmetgezici.huaweileaguemanager.R;
import com.ahmetgezici.huaweileaguemanager.adapter.TeamsAdapter;
import com.ahmetgezici.huaweileaguemanager.databinding.FragmentTeamsBinding;
import com.ahmetgezici.huaweileaguemanager.model.Teams;
import com.ahmetgezici.huaweileaguemanager.utils.datautils.Resource;
import com.ahmetgezici.huaweileaguemanager.utils.datautils.Status;
import com.ahmetgezici.huaweileaguemanager.viewmodel.TeamsViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class TeamsFragment extends Fragment {

    FragmentTeamsBinding binding;

    TeamsViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTeamsBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(TeamsViewModel.class);

        ////////////////////////////////////////////////////////////////////////////////////////////

        // Loading State & Fadaout Animation

        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fadeout);

        viewModel.loadingLiveData.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean) {
                    binding.progress.setVisibility(View.VISIBLE);
                    binding.layout.setVisibility(View.GONE);
                } else {

                    binding.progress.startAnimation(fadeOut);
                    fadeOut.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            binding.progress.setVisibility(View.GONE);
                            binding.layout.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });

                }
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////

        // Get Local Teams Data

        viewModel.teamsDBLiveData.observe(getViewLifecycleOwner(), new Observer<List<Teams>>() {
            @Override
            public void onChanged(List<Teams> teamList) {
                if (teamList.size() > 0) {

                    // If Local Data Exist

                    viewModel.teamListLiveData.setValue(teamList);

                } else {

                    // If Local Data Not Exist
                    // Get Remote Data

                    viewModel.getTeams().observe(getViewLifecycleOwner(), new Observer<Resource<List<Teams>>>() {
                        @Override
                        public void onChanged(Resource<List<Teams>> teamsResource) {

                            if (teamsResource.status == Status.LOADING) {

                                // Loading Data
                                viewModel.loadingLiveData.setValue(true);

                            } else if (teamsResource.status == Status.SUCCESS) {

                                // Success
                                List<Teams> teamList = teamsResource.data;

                                viewModel.insertTeams(teamList);

                                viewModel.teamListLiveData.setValue(teamList);

                            } else if (teamsResource.status == Status.ERROR) {

                                // Error
                                new MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_Rounded)
                                        .setTitle(getString(R.string.error_title))
                                        .setMessage(getString(R.string.error_message))
                                        .setPositiveButton(getString(R.string.error_ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                closeFragment();
                                            }
                                        })
                                        .show();
                            }
                        }
                    });
                }
            }
        });


        //Local or Remote Team Data Observer

        viewModel.teamListLiveData.observe(getViewLifecycleOwner(), new Observer<List<Teams>>() {
            @Override
            public void onChanged(List<Teams> teamList) {

                // Adapter Send Data

                TeamsAdapter teamsAdapter = new TeamsAdapter(teamList);
                binding.recyclerTeams.setLayoutManager(new LinearLayoutManager(requireContext()));
                binding.recyclerTeams.setAdapter(teamsAdapter);
                teamsAdapter.notifyDataSetChanged();
                binding.recyclerTeams.scheduleLayoutAnimation();

                viewModel.loadingLiveData.setValue(false);
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////

        // Go CreateFixture Fragment

        binding.createFixture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateFixtureFragment createFixtureFragment = new CreateFixtureFragment();

                getParentFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                        .replace(R.id.fragmentLayout, createFixtureFragment, createFixtureFragment.getTag())
                        .commit();
            }
        });


        // Go Back Button

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
            }
        });


        // Go Back Pressed

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                closeFragment();
            }
        });

        return binding.getRoot();
    }


    // Close Fragment

    void closeFragment() {

        HomeFragment homeFragment = new HomeFragment();

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                .replace(R.id.fragmentLayout, homeFragment, homeFragment.getTag())
                .commit();
    }
}