package com.example.apiAndroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;import androidx.appcompat.widget.Toolbar;


public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<AnimeJson> animeJson=new ArrayList<>();
    private RecyclerAnime adapter;
    private Toolbar toolbar;

    private String idUser;
    private String nameUser;
    private String emailUser;
    private String passUser;
    private String phoneUser;

    private String idUser2;
    private String nameUser2;
    private String emailUser2;
    private String passUser2;
    private String phoneUser2;

    private UserJson usuario=new UserJson();


    private static String direccionBase= Direcciones.IP+Direcciones.EDT+Direcciones.ANIMES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView=findViewById(R.id.recyclerAnimesList);

        toolbar=findViewById(R.id.toolbarList);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();

        if(intent.getStringExtra("usuarioId2")!=null){//esto es que viene de fav o user
            idUser = intent.getStringExtra("usuarioId2");
            nameUser= intent.getStringExtra("usuarioName2");
            emailUser= intent.getStringExtra("usuarioEmail2");
            passUser= intent.getStringExtra("usuarioPass2");
            phoneUser= intent.getStringExtra("usuarioPhone2");
        }
        else if(intent.getStringExtra("usuarioIdLogin")!=null){//esto es que viene de login
            idUser= intent.getStringExtra("usuarioIdLogin");
            nameUser= intent.getStringExtra("usuarioNameLogin");
            emailUser= intent.getStringExtra("usuarioEmailLogin");
            passUser= intent.getStringExtra("usuarioPassLogin");
            phoneUser= intent.getStringExtra("usuarioPhoneLogin");
        }
        else if(intent.getStringExtra("usuarioNameSignup")!=null){//esto es que viene de sign up
            idUser= intent.getStringExtra("usuarioIdSignup");
            nameUser= intent.getStringExtra("usuarioNameSignup");
            emailUser= intent.getStringExtra("usuarioEmailSignup");
            passUser= intent.getStringExtra("usuarioPassSignup");
            phoneUser= intent.getStringExtra("usuarioPhoneSignup");
        }

        usuario.setId(idUser);
        usuario.setName(nameUser);
        usuario.setEmail(emailUser);
        usuario.setPassword(passUser);
        usuario.setPhone(phoneUser);

        initList();
    }

    private void initList() {

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest reques=new JsonObjectRequest(
                Request.Method.GET,
                direccionBase+"?email="+emailUser,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            JSONArray arrayAnimes=response.getJSONArray("animes");

                            for(int contador=0;contador<arrayAnimes.length();contador++){
                                JSONObject obj=arrayAnimes.getJSONObject(contador);


                                AnimeJson anime=new AnimeJson();

                                anime.setId(obj.getString("id"));
                                anime.setName(obj.getString("name"));
                                anime.setDescripcion(obj.getString("description"));
                                anime.setType(obj.getString("type"));
                                anime.setYear(obj.getString("year"));
                                anime.setImagenUrl(obj.getString("image"));
                                anime.setFavs(obj.getString("favorite"));

                                animeJson.add(anime);
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            adapter = new RecyclerAnime(ListActivity.this, animeJson, usuario);
                            recyclerView.setAdapter(adapter);

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
                Toast.makeText(getApplicationContext(), "Ya estás en esta actividad", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.user_mmenu://los id de los item del menu
                intents(UserActivity.class);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void intents(Class clase){
        Intent intent=new Intent(getApplicationContext(),clase);

        intent.putExtra("usuarioName2", nameUser);
        intent.putExtra("usuarioId2", idUser);
        intent.putExtra("usuarioEmail2",emailUser);
        intent.putExtra("usuarioPass2", passUser);
        intent.putExtra("usuarioPhone2", phoneUser);


        finish();
        startActivity(intent);
    }
}