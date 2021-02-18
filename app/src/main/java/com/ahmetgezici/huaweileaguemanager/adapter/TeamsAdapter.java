package com.ahmetgezici.huaweileaguemanager.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmetgezici.huaweileaguemanager.databinding.ItemTeamBinding;
import com.ahmetgezici.huaweileaguemanager.model.Teams;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.ViewHolder> {

    List<Teams> teamList;

    public TeamsAdapter(List<Teams> teamList) {
        this.teamList = teamList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemTeamBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Teams team = teamList.get(position);

        // Set Index Number

        holder.binding.number.setText(position + 1 + ".");

        // Set Team Name

        holder.binding.teamName.setText(team.name);

        // Get Team Logo

        Glide.with(holder.itemView)
                .load(team.logourl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.binding.logoProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.binding.logo);
    }

    @Override
    public int getItemCount() {
        return teamList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ItemTeamBinding binding;

        private ViewHolder(@NonNull ItemTeamBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }
}
