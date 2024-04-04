package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AnadirFoto extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 10;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private Button btnCamara;
    private Button btnSubirFoto;
    private Button btnCargarFoto;
    private String nombreFoto;
    private Bitmap imgBitmap;
    private final String URL = "http://34.118.255.6:81/foto.php";
    private ImageView imgView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);

        btnCamara = findViewById(R.id.btnCamara);
        btnSubirFoto = findViewById(R.id.btnSubirFoto);
        btnCargarFoto = findViewById(R.id.btnCargarFoto);
        imgView = findViewById(R.id.imageView);

        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCamara();
            }
        });

        btnSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subirFoto();
            }
        });
    }

    private void abrirCamara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.d("eneko", "entra");//De aqui no pasa
        Log.d("eneko", String.valueOf(intent.resolveActivity(getPackageManager())));
        if(intent.resolveActivity(getPackageManager()) != null){
            Log.d("eneko", "entra2");
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("eneko", "entra3");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Log.d("eneko", "entra4");
            if (extras != null) {
                imgBitmap = (Bitmap) extras.get("data");
                if (imgBitmap != null) {
                    Log.d("eneko", "entra5");
                    imgView.setImageBitmap(imgBitmap);
                }
            }
        }
    }

    private void subirFoto(){
        //Subir la foto al servidor y coger el nombre del text y si existe poner una alerta.
        if(imgBitmap != null && nombreFoto != null){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("success")) {
                        Log.d("respuesta","Guardado correctamente.");
                    } else if (response.equals("failure")) {
                        Log.d("respuesta","No se ha guardado!");                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("nombre", nombreFoto);
                    data.put("foto", imgBitmap.toString());
                    data.put("accion", "subir");
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

    private void cargarFoto(){
        //Cargar la foto en el imgView y coger el nombre del text y si no existe poner una alerta.
        if(nombreFoto != null){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("failure")) {
                        Log.d("respuesta","No se ha podido cargar la imagen!.");
                    } else {
                        //Cargar de alguna manera la imagen de un string a bitmap o de una foto a bitmap
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("nombre", nombreFoto);
                    data.put("foto", imgBitmap.toString());
                    data.put("accion", "subir");
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

    public void volver(View view) {
        finish();
    }
}
