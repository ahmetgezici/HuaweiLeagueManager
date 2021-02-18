package com.ahmetgezici.huaweileaguemanager.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.ahmetgezici.huaweileaguemanager.R;
import com.ahmetgezici.huaweileaguemanager.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    FragmentManager manager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ////////////////////////////////////////////////////////////////////////////////////////////

        // Go Home Fragment

        HomeFragment homeFragment = new HomeFragment();

        manager.beginTransaction()
                .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                .replace(binding.fragmentLayout.getId(), homeFragment, homeFragment.getTag())
                .commit();

    }
}