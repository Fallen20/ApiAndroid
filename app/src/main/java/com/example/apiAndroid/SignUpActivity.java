package com.example.apiAndroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText pass;
    private EditText phone;
    private Button signUp;
    private Button login;

    private TextInputLayout nameInput;
    private TextInputLayout emailInput;
    private TextInputLayout passInput;
    private TextInputLayout phoneInput;

    private ImageView imageView;
    private ImageView logo;

    private static String direccionBase = Direcciones.IP + Direcciones.EDT + Direcciones.CREATELOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.nameEditSignin);
        email = findViewById(R.id.emailEditSignin);
        pass = findViewById(R.id.passEditSignin);
        phone = findViewById(R.id.phoneEditSignin);

        nameInput = findViewById(R.id.nameInputSignin);
        emailInput = findViewById(R.id.emailInputSignin);
        passInput = findViewById(R.id.passInputSignin);
        phoneInput = findViewById(R.id.phoneInputSignin);

        signUp = findViewById(R.id.buton1Signin);
        login = findViewById(R.id.buton2Signin);

        imageView = findViewById(R.id.imagenIzqSigin);
        logo = findViewById(R.id.logo);

        imageView.setImageResource(R.drawable.img);
        logo.setImageResource(R.drawable.logo_anime_api);

        //comprobaciones de que no este vacio las cosas

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().trim().equals(null) || name.getText().toString().trim() != null || !name.getText().toString().trim().equals("")) {
                    if (!email.getText().toString().trim().equals(null) || email.getText().toString().trim() != null || !email.getText().toString().trim().equals("")) {
                        if (email.getText().toString().trim().indexOf("@") != -1) {
                            if (!pass.getText().toString().trim().equals(null) || pass.getText().toString().trim() != null || !pass.getText().toString().trim().equals("")) {
                                if (!phone.getText().toString().trim().equals(null) || phone.getText().toString().trim() != null || !phone.getText().toString().trim().equals("")) {

                                    crearUsuario(name.getText().toString().trim(), email.getText().toString().trim(),pass.getText().toString().trim(),phone.getText().toString().trim());

                                } else {Toast.makeText(getApplicationContext(), "El telefono no puede estar vacio", Toast.LENGTH_SHORT).show();}
                            } else {Toast.makeText(getApplicationContext(), "El passoword no puede estar vacio", Toast.LENGTH_SHORT).show();}
                        } else {Toast.makeText(getApplicationContext(), "El campo Email debe tener un @ para ser valido", Toast.LENGTH_SHORT).show();}
                    } else {Toast.makeText(getApplicationContext(), "El email no puede estar vacio", Toast.LENGTH_SHORT).show();}
                } else {Toast.makeText(getApplicationContext(), "El nombre no puede estar vacio", Toast.LENGTH_SHORT).show();}
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void crearUsuario(String name, String email, String pass, String phone) {

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest sr = new StringRequest(
                Request.Method.POST,
                direccionBase, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Usuario creado", Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(SignUpActivity.this, ListActivity.class);

                intent.putExtra("usuarioNameSignup",name);
                intent.putExtra("usuarioEmailSignup",email );
                intent.putExtra("usuarioPassSignup", pass);
                intent.putExtra("usuarioPhoneSignup", phone);

                startActivity(intent);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "mal", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", pass);
                params.put("phone", phone);

                return params;
            }
        };
        queue.add(sr);


    }


}