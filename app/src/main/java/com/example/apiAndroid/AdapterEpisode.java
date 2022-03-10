package com.example.apiAndroid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterEpisode extends RecyclerView.Adapter<AdapterEpisode.MyViewHolder>{
    private Context context;
    private List<EpisodeJson> episodios=new ArrayList<>();

    public AdapterEpisode(Context context, List<EpisodeJson> episodios) {
        this.context = context;
        this.episodios = episodios;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.detail_episode_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.episode.setText("Episodio "+episodios.get(position).getEpisode());

        holder.episode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context.getApplicationContext(),VideoActivity.class);
                intent.putExtra("urlEpisodio", episodios.get(holder.getAdapterPosition()).getUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return episodios.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private Button episode;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            episode=itemView.findViewById(R.id.buttonEpisodeRow);

        }
    }
}
