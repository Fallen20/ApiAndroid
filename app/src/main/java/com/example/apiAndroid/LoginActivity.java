package com.example.apiAndroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText pass;
    private Button signUp;
    private Button login;

    private ImageView imageView;
    private ImageView logo;


    UserJson userLogJson=new UserJson();
    private static String direccionBase= Direcciones.IP+Direcciones.EDT+Direcciones.LOGIN;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.emailEditLogin);
        pass=findViewById(R.id.passEditLogin);
        signUp=findViewById(R.id.buton2SignLogin);
        login=findViewById(R.id.buton1SignLogin);

        imageView=findViewById(R.id.imagenIzqLogin);
        logo=findViewById(R.id.logo);


        imageView.setImageResource(R.drawable.img);
        logo.setImageResource(R.drawable.logo_anime_api);
        //comprobaciones de que no este vacio las cosas

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!email.getText().toString().trim().equals(null) || email.getText().toString().trim()!=null || !email.getText().toString().trim().equals("")){
                    if(email.getText().toString().trim().indexOf("@")!=-1){
                        if(!pass.getText().toString().trim().equals(null) || pass.getText().toString().trim()!=null || !pass.getText().toString().trim().equals("")){
                            logUser(email.getText().toString().trim(), pass.getText().toString().trim());
                        }else{Toast.makeText(getApplicationContext(), "El campo password no puede estar vacio", Toast.LENGTH_SHORT).show();}
                    }else{Toast.makeText(getApplicationContext(), "El campo Email debe tener un @ para ser valido", Toast.LENGTH_SHORT).show();}
                }else {Toast.makeText(getApplicationContext(), "El campo email no puede estar vacio", Toast.LENGTH_SHORT).show();}

            }
        });
    }

    private void logUser(String email, String pass){

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest reques=new JsonObjectRequest(
                Request.Method.GET,
                direccionBase+"?email="+email+"&password="+pass,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if(response.getString("email")!=null){
                                Intent intent=new Intent(getApplicationContext(), ListActivity.class);
                                //enviar el usuario actual porque lo necesitamos para el apartado user

                                intent.putExtra("usuarioIdLogin", response.getString("id"));
                                intent.putExtra("usuarioNameLogin", response.getString("name"));
                                intent.putExtra("usuarioEmailLogin",response.getString("email") );
                                intent.putExtra("usuarioPassLogin", response.getString("password"));
                                intent.putExtra("usuarioPhoneLogin", response.getString("phone"));
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Este usuario no esta en la base de datos", Toast.LENGTH_SHORT).show();

                    }
                }
        );

        queue.add(reques);

    }
}