package com.ahmetgezici.huaweileaguemanager.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.ahmetgezici.huaweileaguemanager.R;
import com.ahmetgezici.huaweileaguemanager.adapter.FullFixtureAdapter;
import com.ahmetgezici.huaweileaguemanager.databinding.FragmentFixtureBinding;
import com.ahmetgezici.huaweileaguemanager.model.Versus;
import com.ahmetgezici.huaweileaguemanager.utils.PagerAnimation;
import com.ahmetgezici.huaweileaguemanager.viewmodel.FixtureViewModel;
import com.ahmetgezici.huaweileaguemanager.viewmodel.TeamsViewModel;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class FixtureFragment extends Fragment {

    FragmentFixtureBinding binding;

    FixtureViewModel viewModel;
    TeamsViewModel teamsViewModel;

    int uid;
    int half;
    ArrayList<ArrayList<Versus>> fixtureList;

    Fragment fragment;

    public FixtureFragment(int half, ArrayList<ArrayList<Versus>> fixtureList, int uid) {
        this.half = half;
        this.fixtureList = fixtureList;
        this.uid = uid;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentFixtureBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(FixtureViewModel.class);
        teamsViewModel = new ViewModelProvider(this).get(TeamsViewModel.class);

        ////////////////////////////////////////////////////////////////////////////////////////////

        // For Close Fragment

        fragment = this;


        // Fixture Half Title

        binding.fixtureHalfTitle.setText(getString(R.string.fixture).toUpperCase(Locale.getDefault()) + " " + uid + " / " + half + ". "+getString(R.string.fixture_half));


        // Adapter Send Data

        FullFixtureAdapter fullFixtureAdapter = new FullFixtureAdapter(half, fixtureList, requireContext());
        binding.fixturePager.setPageTransformer(new PagerAnimation());
        binding.fixturePager.setAdapter(fullFixtureAdapter);


        // ViewPager Listener for GoTop Fab and GoEnd Fab

        int pagerCount = Objects.requireNonNull(binding.fixturePager.getAdapter()).getItemCount();

        binding.fixturePager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == 0) {
                    binding.goTop.hide();
                    binding.goEnd.show();
                } else if (position == pagerCount - 1) {
                    binding.goTop.show();
                    binding.goEnd.hide();
                } else {
                    binding.goTop.show();
                    binding.goEnd.show();
                }

            }
        });

        // Go Top

        binding.goTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.fixturePager.setCurrentItem(0);
            }
        });


        //Go End

        binding.goEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.fixturePager.setCurrentItem(pagerCount);
            }
        });


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

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                .remove(fragment)
                .commit();
    }

}