package com.example.apiAndroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;

    private String idUser;
    private String nameUser;
    private String emailUser;
    private String passUser;
    private String phoneUser;

    private static String direccionBase = Direcciones.IP + Direcciones.EDT + Direcciones.ANIMESFAV;

    private RecyclerAnime adapter;
    private ArrayList<AnimeJson> animeList = new ArrayList<AnimeJson>();
    private UserJson usuario = new UserJson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favs);

        recyclerView = findViewById(R.id.recyclerFavList);
        //hacer otro for json pero de favs

        Intent intent = getIntent();

        idUser = intent.getStringExtra("usuarioId2");
        nameUser = intent.getStringExtra("usuarioName2");
        emailUser = intent.getStringExtra("usuarioEmail2");
        passUser = intent.getStringExtra("usuarioPass2");
        phoneUser = intent.getStringExtra("usuarioPhone2");

        usuario.setName(nameUser);
        usuario.setEmail(emailUser);
        usuario.setPassword(passUser);
        usuario.setPhone(phoneUser);


        toolbar = findViewById(R.id.toolbarFavs);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initList();
    }

    private void initList() {

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest reques = new JsonObjectRequest(
                Request.Method.GET,
                direccionBase + "?email=" + emailUser,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray arrayAnimes = response.getJSONArray("animesfavorites");

                            for (int contador = 0; contador < arrayAnimes.length(); contador++) {
                                JSONObject obj = arrayAnimes.getJSONObject(contador);


                                AnimeJson anime = new AnimeJson();


                                anime.setName(obj.getString("name"));
                                anime.setDescripcion(obj.getString("description"));
                                anime.setType(obj.getString("type"));
                                anime.setYear(obj.getString("year"));
                                anime.setImagenUrl(obj.getString("image"));

                                animeList.add(anime);
                            }
                            System.out.println("dentro");
                            System.out.println("size " + animeList.size());
                            if (animeList.size() != 0) {//sino esta vacia
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                adapter = new RecyclerAnime(FavsActivity.this, animeList, usuario);
                                recyclerView.setAdapter(adapter);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error leyendo favoritos o no hay ningun favorito", Toast.LENGTH_SHORT).show();
                        System.out.println(error);
                    }
                }
        );

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
                Toast.makeText(getApplicationContext(), "Ya estás en esta actividad", Toast.LENGTH_SHORT).show();
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
        intent.putExtra("usuarioId2", idUser);
        intent.putExtra("usuarioName2", nameUser);
        intent.putExtra("usuarioEmail2", emailUser);
        intent.putExtra("usuarioPass2", passUser);
        intent.putExtra("usuarioPhone2", phoneUser);

        finish();
        startActivity(intent);
    }
}