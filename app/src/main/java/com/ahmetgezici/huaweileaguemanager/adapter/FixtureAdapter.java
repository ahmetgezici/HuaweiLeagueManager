package com.ahmetgezici.huaweileaguemanager.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmetgezici.huaweileaguemanager.databinding.ItemVersusBinding;
import com.ahmetgezici.huaweileaguemanager.model.TeamData;
import com.ahmetgezici.huaweileaguemanager.model.Versus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class FixtureAdapter extends RecyclerView.Adapter<FixtureAdapter.ViewHolder> {

    ArrayList<Versus> versusMap;

    public FixtureAdapter(ArrayList<Versus> versusMap) {
        this.versusMap = versusMap;
    }

    @NonNull
    @Override
    public FixtureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemVersusBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FixtureAdapter.ViewHolder holder, int position) {

        TeamData homeTeam = versusMap.get(position).home;
        TeamData awayTeam = versusMap.get(position).away;

        String homeName = homeTeam.name;
        String awayName = awayTeam.name;

        String homeLogoUrl = homeTeam.logourl;
        String awayLogoUrl = awayTeam.logourl;

        // Set Teams Name

        holder.binding.home.setText(homeName);
        holder.binding.away.setText(awayName);

        // Get Teams Logo

        Glide.with(holder.itemView)
                .load(homeLogoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.homeLogo);

        Glide.with(holder.itemView)
                .load(awayLogoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.awayLogo);

    }

    @Override
    public int getItemCount() {
        return versusMap.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ItemVersusBinding binding;

        private ViewHolder(@NonNull ItemVersusBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }
}
