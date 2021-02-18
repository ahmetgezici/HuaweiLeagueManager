package com.ahmetgezici.huaweileaguemanager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmetgezici.huaweileaguemanager.R;
import com.ahmetgezici.huaweileaguemanager.databinding.ItemFixturesBinding;
import com.ahmetgezici.huaweileaguemanager.model.Versus;
import com.ahmetgezici.huaweileaguemanager.view.FixtureFragment;
import com.ahmetgezici.huaweileaguemanager.viewmodel.CreateFixtureViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class CreateFixtureAdapter extends RecyclerView.Adapter<CreateFixtureAdapter.ViewHolder> {

    FragmentManager manager;
    CreateFixtureViewModel viewModel;
    ArrayList<ArrayList<ArrayList<Versus>>> fixtureList;
    ArrayList<Integer> uidList;
    Context context;


    public CreateFixtureAdapter(FragmentManager manager, CreateFixtureViewModel viewModel, ArrayList<Integer> uidList,
                                ArrayList<ArrayList<ArrayList<Versus>>> fixtureList, Context context) {
        this.manager = manager;
        this.fixtureList = fixtureList;
        this.context = context;
        this.uidList = uidList;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public CreateFixtureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemFixturesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CreateFixtureAdapter.ViewHolder holder, int position) {

        int currentUID = uidList.get(position);

        // Fixture Card Animation

        holder.binding.fixtureCard.setAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_fixture));

        // Set Fixture Name

        holder.binding.fixtureName.setText(context.getString(R.string.fixture) + " " + currentUID);

        ArrayList<ArrayList<Versus>> fistHalfList = new ArrayList<>();
        ArrayList<ArrayList<Versus>> secondHalfList = new ArrayList<>();

        ArrayList<ArrayList<Versus>> fixture = fixtureList.get(position);

        // Splitting Fixtures

        for (int i = 0; i < fixture.size(); i++) {

            if (i < fixture.size() / 2) {
                fistHalfList.add(fixture.get(i));
            } else {
                secondHalfList.add(fixture.get(i));
            }
        }

        // Go Fixture - 1. Half

        holder.binding.firstHalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FixtureFragment fixtureFragment = new FixtureFragment(1, fistHalfList, currentUID);

                if (manager.getFragments().contains(manager.findFragmentByTag("fixtureFragment"))) {
                    manager.beginTransaction().remove(Objects.requireNonNull(manager.findFragmentByTag("fixtureFragment")));
                }

                manager.beginTransaction()
                        .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                        .add(R.id.rootLayout, fixtureFragment, "fixtureFragment")
                        .commit();
            }
        });

        // Go Fixture - 2. Half

        holder.binding.secondHalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FixtureFragment fixtureFragment = new FixtureFragment(2, secondHalfList, currentUID);

                if (manager.getFragments().contains(manager.findFragmentByTag("fixtureFragment"))) {
                    manager.beginTransaction().remove(Objects.requireNonNull(manager.findFragmentByTag("fixtureFragment")));
                }

                manager.beginTransaction()
                        .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
                        .add(R.id.rootLayout, fixtureFragment, "fixtureFragment")
                        .commit();

            }
        });

        // Delete Fixture

        holder.binding.deleteFixture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.deleteFixture(uidList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return fixtureList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ItemFixturesBinding binding;

        private ViewHolder(@NonNull ItemFixturesBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }
}
