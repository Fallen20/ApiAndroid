package com.example.apiAndroid;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerAnime extends RecyclerView.Adapter<RecyclerAnime.MyViewHolder> {
    private Context context;
    private ArrayList<AnimeJson> animeList=new ArrayList<AnimeJson>();
    private UserJson usuario=new UserJson();


    public RecyclerAnime(Context context, ArrayList<AnimeJson> animeList, UserJson usuario) {
        this.context = context;
        this.animeList = animeList;
        this.usuario = usuario;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.anime_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.titulo.setText(animeList.get(position).getName());
        holder.desc.setText(animeList.get(position).getDescripcion());
        holder.year.setText(animeList.get(position).getYear());
        holder.airedOn.setText(animeList.get(position).getType());
        Picasso.get().load(Direcciones.IP+animeList.get(position).imagenUrl).fit().centerCrop().into(holder.caratula);


        if(context instanceof FavsActivity){holder.favIcon.setImageResource(R.drawable.full_heart_icon_rojo);}//favs
        else if(animeList.get(position).getFavs()==null){holder.favIcon.setImageResource(R.drawable.full_heart_icon_rojo);}//favs
        else{//si esta lleno
            if(animeList.get(position).getFavs().equals(usuario.id)){holder.favIcon.setImageResource(R.drawable.full_heart_icon_rojo);}
            else if(animeList.get(position).getFavs().equals("null")){holder.favIcon.setImageResource(R.drawable.heart_icon);}
            else{holder.favIcon.setImageResource(R.drawable.heart_icon);}
        }


        Drawable.ConstantState constantState;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            constantState=context.getResources().getDrawable(R.drawable.full_heart_icon_rojo, context.getTheme()).getConstantState();
        }
        else{constantState=context.getResources().getDrawable(R.drawable.full_heart_icon_rojo).getConstantState();}


        //System.out.println(holder.favIcon.getDrawable().getConstantState()==constantState);//mira si es la misma imagen

        holder.favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.favIcon.getDrawable().getConstantState()==constantState){
                    deleteFavs(holder.getAdapterPosition());
                    //Toast.makeText(context.getApplicationContext(), "del", Toast.LENGTH_SHORT).show();

                    holder.favIcon.setImageResource(R.drawable.heart_icon);

                    //System.out.println(holder.favIcon.getDrawable().getConstantState()==constantState);
                }
                else{
                    addFavs(holder.getAdapterPosition());
                    //Toast.makeText(context.getApplicationContext(), "add", Toast.LENGTH_SHORT).show();
                    holder.favIcon.setImageResource(R.drawable.full_heart_icon_rojo);
                }
            }
        });
    }

    private void deleteFavs(int adapterPosition) {

        RequestQueue queue = Volley.newRequestQueue(context);
        String deleteURL=Direcciones.IP+Direcciones.EDT+Direcciones.DELETEFAV;


        StringRequest reques = new StringRequest(
                Request.Method.POST,
                deleteURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error borrando de favoritos", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("anime", animeList.get(adapterPosition).getName());
                params.put("email", usuario.getEmail());
                return params;
            }
        };

        queue.add(reques);
    }

    private void addFavs(int adapterPosition) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String insertURL=Direcciones.IP+Direcciones.EDT+Direcciones.INSERTFAV;


        StringRequest reques = new StringRequest(
                Request.Method.POST,
                insertURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error añadiendo a favs", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("anime", animeList.get(adapterPosition).getName());
                params.put("email", usuario.getEmail());
                return params;
            }
        };

        queue.add(reques);
    }


    @Override
    public int getItemCount() {
        return animeList.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView caratula;
        private TextView titulo;
        private TextView desc;
        private TextView year;
        private TextView airedOn;
        private ImageView favIcon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            caratula=itemView.findViewById(R.id.caratulaAnime);
            titulo=itemView.findViewById(R.id.nameAnime);
            desc=itemView.findViewById(R.id.descAnime);
            year=itemView.findViewById(R.id.yearAnime);
            airedOn=itemView.findViewById(R.id.platformAnime);
            favIcon=itemView.findViewById(R.id.favIconAnime);
        }
    }
}




