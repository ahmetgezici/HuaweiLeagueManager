package com.ahmetgezici.huaweileaguemanager.view;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ahmetgezici.huaweileaguemanager.R;
import com.ahmetgezici.huaweileaguemanager.adapter.CreateFixtureAdapter;
import com.ahmetgezici.huaweileaguemanager.databinding.FragmentCreateFixtureBinding;
import com.ahmetgezici.huaweileaguemanager.model.FixtureData;
import com.ahmetgezici.huaweileaguemanager.model.Teams;
import com.ahmetgezici.huaweileaguemanager.model.Versus;
import com.ahmetgezici.huaweileaguemanager.utils.Tools;
import com.ahmetgezici.huaweileaguemanager.utils.datautils.Resource;
import com.ahmetgezici.huaweileaguemanager.utils.datautils.Status;
import com.ahmetgezici.huaweileaguemanager.viewmodel.CreateFixtureViewModel;
import com.ahmetgezici.huaweileaguemanager.viewmodel.TeamsViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateFixtureFragment extends Fragment {

    FragmentCreateFixtureBinding binding;

    CreateFixtureViewModel viewModel;
    TeamsViewModel teamsViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCreateFixtureBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(CreateFixtureViewModel.class);
        teamsViewModel = new ViewModelProvider(this).get(TeamsViewModel.class);

        ////////////////////////////////////////////////////////////////////////////////////////////

        // Loading State & Fadaout Animation

        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fadeout);

        viewModel.loadingLiveDate.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    binding.progress.setVisibility(View.VISIBLE);
                } else {
                    binding.progress.startAnimation(fadeOut);
                    fadeOut.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                }
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////

        // Get Local Team Data

        teamsViewModel.teamsDBLiveData.observe(getViewLifecycleOwner(), new Observer<List<Teams>>() {
            @Override
            public void onChanged(List<Teams> teams) {

                if (teams.size() > 0) {

                    // If Local Data Exist

                    viewModel.teamListLiveData.setValue(teams);

                    viewModel.loadingLiveDate.setValue(false);

                } else {

                    // If Local Data Not Exist
                    // Get Remote Team Data

                    teamsViewModel.getTeams().observe(getViewLifecycleOwner(), new Observer<Resource<List<Teams>>>() {
                        @Override
                        public void onChanged(Resource<List<Teams>> teamsResource) {

                            if (teamsResource.status == Status.LOADING) {

                                // Loading Data
                                viewModel.loadingLiveDate.setValue(true);

                            } else if (teamsResource.status == Status.SUCCESS) {

                                // Success
                                List<Teams> teamList = teamsResource.data;

                                viewModel.teamListLiveData.setValue(teamList);

                                viewModel.loadingLiveDate.setValue(false);

                                teamsViewModel.insertTeams(teamList);

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

        ////////////////////////////////////////////////////////////////////////////////////////////

        // Get Local Fixture Data

        viewModel.fixtureDBLiveData.observe(getViewLifecycleOwner(), new Observer<List<FixtureData>>() {
            @Override
            public void onChanged(List<FixtureData> fixtureDataList) {

                // Fixture Size Control

                if (fixtureDataList.size() > 0) {
                    binding.fixturesRecycler.setVisibility(View.VISIBLE);
                    binding.noData.setVisibility(View.GONE);
                } else {
                    binding.fixturesRecycler.setVisibility(View.GONE);
                    binding.noData.setVisibility(View.VISIBLE);
                }

                // Local Json to Data List

                ArrayList<ArrayList<ArrayList<Versus>>> fixtureList = new ArrayList<>();
                ArrayList<Integer> uidList = new ArrayList<>();

                for (FixtureData fixtureData : fixtureDataList) {

                    TypeToken<ArrayList<ArrayList<Versus>>> list = new TypeToken<ArrayList<ArrayList<Versus>>>() {
                    };
                    ArrayList<ArrayList<Versus>> fixture = new Gson().fromJson(fixtureData.json, list.getType());

                    fixtureList.add(fixture);
                    uidList.add(fixtureData.uid);

                }

                // Adapter Send Data

                CreateFixtureAdapter createFixtureAdapter = new CreateFixtureAdapter(getParentFragmentManager(), viewModel, uidList, fixtureList, requireContext());
                binding.fixturesRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
                binding.fixturesRecycler.setAdapter(createFixtureAdapter);
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////

        // Local or Remote Team Data Observer

        viewModel.teamListLiveData.observe(getViewLifecycleOwner(), new Observer<List<Teams>>() {
            @Override
            public void onChanged(List<Teams> teamList) {

                // Create Fixture

                binding.addFixture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Shuffle Team List

                        Collections.shuffle(teamList);

                        // Fixture Calculate

                        ArrayList<ArrayList<Versus>> fixtureDataList = viewModel.calculateFixture(teamList);

                        // Data List to Json

                        FixtureData fixtureData = new FixtureData();
                        fixtureData.json = new Gson().toJson(fixtureDataList);

                        // Save Fixture to Database

                        viewModel.insertFixture(fixtureData);

                    }
                });

                //////////////////////////////////

                binding.bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == R.id.allDelete) {

                            // Delete All Fixture

                            new MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_Rounded)
                                    .setTitle(getString(R.string.fixture_delete_title))
                                    .setMessage(getString(R.string.fixture_delete_message))
                                    .setPositiveButton(getString(R.string.error_ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            viewModel.deleteAllFixture();
                                        }
                                    })
                                    .setNegativeButton(getString(R.string.error_cancel), null)
                                    .show();

                        } else if (item.getItemId() == R.id.showTeam) {

                            // Go Teams Fragment

                            TeamsFragment teamsFragment = new TeamsFragment();

                            getParentFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                                    .replace(R.id.fragmentLayout, teamsFragment, teamsFragment.getTag())
                                    .commit();
                        }

                        return false;
                    }
                });

            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////

        // BottomAppBar Animation

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(Tools.dpToPx(40f), 0f);
        int mDuration = 1300;
        valueAnimator.setDuration(mDuration);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                binding.bottomAppBar.setCradleVerticalOffset((float) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();


        // FAB Animation

        binding.addFixture.animate().rotationBy(360).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator()).setStartDelay(450);


        // Back Button

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
            }
        });


        // Back Pressed

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