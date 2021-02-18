package com.ahmetgezici.huaweileaguemanager.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.ahmetgezici.huaweileaguemanager.R;
import com.ahmetgezici.huaweileaguemanager.databinding.DialogSettingBinding;
import com.ahmetgezici.huaweileaguemanager.databinding.FragmentHomeBinding;
import com.ahmetgezici.huaweileaguemanager.utils.Tools;
import com.mahfa.dnswitch.DayNightSwitchListener;

import java.util.Locale;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        ////////////////////////////////////////////////////////////////////////////////////////////

        // For Setting State Change

        if (Locale.getDefault().getLanguage().equals("tr")) {
            setLocale(requireActivity(), "tr");
        } else {
            setLocale(requireActivity(), "en");
        }


        // Go Teams Fragment

        binding.showTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TeamsFragment teamsFragment = new TeamsFragment();

                getParentFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                        .replace(R.id.fragmentLayout, teamsFragment, teamsFragment.getTag())
                        .commit();

            }
        });


        // Go Create Fragment

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


        // Open Settings

        binding.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create Setting Dialog

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        Resources.getSystem().getDisplayMetrics().widthPixels - (Tools.dpToPx(25) + Tools.dpToPx(25)), Tools.dpToPx(300));

                Dialog dialog = new Dialog(getContext());
                DialogSettingBinding dialogBinding = DialogSettingBinding.bind(inflater.inflate(R.layout.dialog_setting, null));

                dialog.setContentView(dialogBinding.getRoot(), params);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ///////////////////////////////////

                // Theme Setting

                int nightModeFlags = requireContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

                if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    dialogBinding.dayNightSwitch.setIsNight(true);
                } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
                    dialogBinding.dayNightSwitch.setIsNight(false);
                } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_UNDEFINED) {
                    dialogBinding.dayNightSwitch.setIsNight(false);
                }

                dialogBinding.dayNightSwitch.setListener(new DayNightSwitchListener() {
                    @Override
                    public void onSwitch(boolean is_night) {

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (is_night) {
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                } else {
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                }

                            }
                        }, 600);
                    }
                });

                ///////////////////////////////////

                // Language Setting

                if (Locale.getDefault().getLanguage().equals("tr")) {
                    dialogBinding.languageSwitch.setChecked(false);
                } else {
                    dialogBinding.languageSwitch.setChecked(true);
                }

                dialogBinding.languageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (isChecked) {

                                    setLocale(requireActivity(), "en");
                                    refleshUI();
                                    dialog.dismiss();

                                } else {

                                    setLocale(requireActivity(), "tr");
                                    refleshUI();
                                    dialog.dismiss();
                                }
                            }
                        }, 600);
                    }
                });

                ///////////////////////////////////

                dialog.show();
            }
        });

        // View Animations

        binding.showTeams.animate().alphaBy(1).setDuration(400).setInterpolator(new AccelerateDecelerateInterpolator()).setStartDelay(200);

        binding.createFixture.animate().alphaBy(1).setDuration(400).setInterpolator(new AccelerateDecelerateInterpolator()).setStartDelay(400);

        binding.setting.animate().alphaBy(1).rotationBy(360).setDuration(800).setInterpolator(new AccelerateDecelerateInterpolator()).setStartDelay(450);


        return binding.getRoot();
    }

    // For Language Setting

    public void refleshUI() {
        HomeFragment homeFragment = new HomeFragment();

        getParentFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                .replace(R.id.fragmentLayout, homeFragment, homeFragment.getTag())
                .commit();
    }


    // Set Language

    public void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}