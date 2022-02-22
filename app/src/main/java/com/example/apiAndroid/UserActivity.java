package com.example.apiAndroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private UserJson user;
    private EditText name;
    private TextView email;
    private EditText pass;
    private EditText phone;
    private Button update;
    private Button delete;


    private String idUser;
    private String nameUser;
    private String emailUser;
    private String passUser;
    private String phoneUser;
    private final String deleteURL = Direcciones.IP + Direcciones.EDT + Direcciones.DELETELOGIN;
    private final String updateURL = Direcciones.IP + Direcciones.EDT + Direcciones.UPDATELOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        name = findViewById(R.id.editName);
        email = findViewById(R.id.editEmail);
        pass = findViewById(R.id.editPass);
        phone = findViewById(R.id.editPhone);
        update = findViewById(R.id.updateButton);
        delete = findViewById(R.id.deleteButton);

        toolbar = findViewById(R.id.toolbarUser);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();

        idUser = intent.getStringExtra("usuarioId2");
        nameUser = intent.getStringExtra("usuarioName2");
        emailUser = intent.getStringExtra("usuarioEmail2");
        passUser = intent.getStringExtra("usuarioPass2");
        phoneUser = intent.getStringExtra("usuarioPhone2");

        name.setText(nameUser);
        email.setText(emailUser);
        pass.setText(passUser);
        phone.setText(phoneUser);


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarUsuario();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

    }

    private void updateUser() {
        RequestQueue queue = Volley.newRequestQueue(this);


        StringRequest reques = new StringRequest(
                Request.Method.POST,
                updateURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error cambiando datos", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name.getText().toString().trim());
                params.put("email", email.getText().toString().trim());
                params.put("password", pass.getText().toString().trim());
                params.put("phone", phone.getText().toString().trim());
                return params;
            }
        };

        queue.add(reques);
    }

    private void borrarUsuario() {
        RequestQueue queue = Volley.newRequestQueue(this);


        StringRequest sr = new StringRequest(
                Request.Method.POST,
                deleteURL,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(UserActivity.this, SignUpActivity.class);
                startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Algo ha salido mal", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email", emailUser);

                return params;
            }
        };
        queue.add(sr);
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
                Toast.makeText(getApplicationContext(), "Ya estás en esta actividad", Toast.LENGTH_SHORT).show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void intents(Class clase) {
        Intent intent = new Intent(getApplicationContext(), clase);

        intent.putExtra("usuarioId2", idUser);
        intent.putExtra("usuarioName2", name.getText().toString().trim());
        intent.putExtra("usuarioEmail2",email.getText().toString().trim());
        intent.putExtra("usuarioPass2", pass.getText().toString().trim());
        intent.putExtra("usuarioPhone2", phone.getText().toString().trim());
        finish();
        startActivity(intent);
    }
}