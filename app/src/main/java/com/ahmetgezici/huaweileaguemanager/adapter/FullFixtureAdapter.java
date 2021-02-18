package com.ahmetgezici.huaweileaguemanager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmetgezici.huaweileaguemanager.R;
import com.ahmetgezici.huaweileaguemanager.databinding.ItemFixtureRoundBinding;
import com.ahmetgezici.huaweileaguemanager.model.Versus;

import java.util.ArrayList;

public class FullFixtureAdapter extends RecyclerView.Adapter<FullFixtureAdapter.ViewHolder> {

    int half;
    ArrayList<ArrayList<Versus>> roundList;
    Context context;

    public FullFixtureAdapter(int half, ArrayList<ArrayList<Versus>> roundList,Context context) {
        this.half = half;
        this.roundList = roundList;
        this.context = context;
    }

    @NonNull
    @Override
    public FullFixtureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemFixtureRoundBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FullFixtureAdapter.ViewHolder holder, int position) {

        // Set Round

        if (half == 1) {
            holder.binding.round.setText(position + 1 + ". "+ context.getString(R.string.fixture_round));
        } else if (half == 2) {
            holder.binding.round.setText(roundList.size() + 1 + position + ". "+context.getString(R.string.fixture_round));
        }

        // Adapter Send Data

        FixtureAdapter fixtureAdapter = new FixtureAdapter(roundList.get(position));
        holder.binding.roundRecycler.setAdapter(fixtureAdapter);
    }

    @Override
    public int getItemCount() {
        return roundList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ItemFixtureRoundBinding binding;

        private ViewHolder(@NonNull ItemFixtureRoundBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }
}
