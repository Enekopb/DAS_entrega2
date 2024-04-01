package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText etUsuario, etContrasena;
    private String usuario, contrasena;
    private String URL = "http://34.118.255.6:81/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usuario = contrasena = "";
        etUsuario = findViewById(R.id.username_edit_text);
        etContrasena = findViewById(R.id.password_edit_text);
    }

    public void login(View view) {
        usuario = etUsuario.getText().toString().trim();
        contrasena = etContrasena.getText().toString().trim();
        if(!usuario.equals("") && !contrasena.equals("")){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("res", response);
                    if (response.equals("success")) {
                        Intent intent = new Intent(MainActivity.this, MenuInicio.class);
                        startActivity(intent);
                        finish();
                    } else if (response.equals("failure")) {
                        Toast.makeText(MainActivity.this, "Usuario o contraseña incorrecta!", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("usuario", usuario);
                    data.put("contrasena", contrasena);
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }else{
            Toast.makeText(this, "Los campos no pueden estar vacios!", Toast.LENGTH_SHORT).show();
        }
    }

    public void registrarUsuario(View view) {
        Intent intent = new Intent(this, RegistrarUsuario.class);
        startActivity(intent);
        finish();
    }
}