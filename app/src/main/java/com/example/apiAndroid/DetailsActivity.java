package com.example.apiAndroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView imagen;
    private TextView name;
    private TextView desc;
    private TextView year;
    private TextView platform;
    private ImageView fav;
    private Toolbar toolbar;

    private String idAnime="";
    private String idUser="";
    private String nombreAnime;
    private String emailUser;

    private List<EpisodeJson> capitulos=new ArrayList<>();
    private AdapterEpisode adapterEpisode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        recyclerView=findViewById(R.id.recyclerDetail);
        imagen=findViewById(R.id.caratulaAnimeDetail);
        name=findViewById(R.id.nameAnimeDetail);
        desc=findViewById(R.id.descAnimeDetail);
        year=findViewById(R.id.yearAnimeDetail);
        platform=findViewById(R.id.platformAnimeDetail);
        fav=findViewById(R.id.favIconAnimeDetail2);


        toolbar=findViewById(R.id.toolbarDetail);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();

        idAnime=intent.getStringExtra("idAnimeADetails");
        idUser=intent.getStringExtra("idUserADetails");
        nombreAnime=intent.getStringExtra("nombreADetails");
        emailUser=intent.getStringExtra("emailADetails");


        name.setText(intent.getStringExtra("nombreADetails"));
        desc.setText(intent.getStringExtra("descADetails"));
        year.setText(intent.getStringExtra("yearADetails"));
        platform.setText(intent.getStringExtra("platfADetails"));
        Picasso.get().load(intent.getStringExtra("UrlADetails")).fit().centerCrop().into(imagen);
        String favs=intent.getStringExtra("inFavADetails");


        if(favs==null){fav.setImageResource(R.drawable.full_heart_icon_rojo);}//viene de favs
        else if(favs.equals(idUser) && !idUser.equals("null")){fav.setImageResource(R.drawable.full_heart_icon_rojo);}//es el user quien tiene fav
        else{fav.setImageResource(R.drawable.heart_icon);}//no tiene nadie añadido o no eres tu


        Drawable.ConstantState constantState2;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            constantState2=getApplicationContext().getResources().getDrawable(R.drawable.heart_icon, getApplicationContext().getTheme()).getConstantState();
        }
        else{constantState2=getApplicationContext().getResources().getDrawable(R.drawable.heart_icon).getConstantState();}

//        System.out.println("lleno? pre");
//        System.out.println("img"+fav.getDrawable().getConstantState());
//        System.out.println("constant"+constantState2);
//        System.out.println(fav.getDrawable().getConstantState()==constantState2);


        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                System.out.println("lleno? click");
//                System.out.println("img"+fav.getDrawable().getConstantState());
//                System.out.println("constant"+constantState2);
//                System.out.println(fav.getDrawable().getConstantState()==constantState2);//mira si es la misma imagen


                if(fav.getDrawable().getConstantState()!=constantState2){
                    System.out.println("del");
                    deleteFavs();
                                    }
                else{
                    System.out.println("addd");
                    addFavs();
                }
            }
        });



        //recuperar episodios
        recoverEpisodes();
    }

    private void recoverEpisodes() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String direccionBase=Direcciones.IP+Direcciones.EDT+Direcciones.VIDEOS+idAnime;


        JsonObjectRequest reques=new JsonObjectRequest(
                Request.Method.GET,
                direccionBase,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            JSONArray arrayAnimes=response.getJSONArray("animevideos");

                            for(int contador=0;contador<arrayAnimes.length();contador++){
                                JSONObject obj=arrayAnimes.getJSONObject(contador);

                                EpisodeJson episodeJson=new EpisodeJson();
                                episodeJson.setEpisode(obj.getString("episode"));
                                episodeJson.setUrl(obj.getString("url"));

                                capitulos.add(episodeJson);
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            adapterEpisode = new AdapterEpisode(DetailsActivity.this, capitulos);
                            recyclerView.setAdapter(adapterEpisode);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();

                    }
                }
        );

        queue.add(reques);
    }


    private void deleteFavs() {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String deleteURL=Direcciones.IP+Direcciones.EDT+Direcciones.DELETEFAV;


        StringRequest reques = new StringRequest(
                Request.Method.POST,
                deleteURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        fav.setImageResource(R.drawable.heart_icon);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error borrando de favoritos", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("anime", nombreAnime);
                params.put("email", emailUser);
                return params;
            }
        };

        queue.add(reques);
    }

    private void addFavs() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String insertURL=Direcciones.IP+Direcciones.EDT+Direcciones.INSERTFAV;


        StringRequest reques = new StringRequest(
                Request.Method.POST,
                insertURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        fav.setImageResource(R.drawable.full_heart_icon_rojo);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error añadiendo a favs", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("anime", nombreAnime);
                params.put("email", emailUser);
                return params;
            }
        };

        queue.add(reques);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//esto es para que salga
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);//primero el xml, luego el menu que  nos da el metodo

        return true;//true es que se ha creado
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //para cuando le apretas al icono
        switch (item.getItemId()) {

            case R.id.fav_menu://los id de los item del menu
                intents(FavsActivity.class);
                return true;
            case R.id.list_menu://los id de los item del menu
                intents(ListActivity.class);
                return true;
            case R.id.user_mmenu://los id de los item del menu
                intents(UserActivity.class);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void intents(Class clase) {
        Intent intent = new Intent(getApplicationContext(), clase);
        finish();
        startActivity(intent);
    }
}