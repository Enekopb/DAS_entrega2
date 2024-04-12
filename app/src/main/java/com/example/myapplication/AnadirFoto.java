package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class AnadirFoto extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 10;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private Button btnCamara;
    private Button btnSubirFoto;
    private Bitmap imgBitmap;
    private final String URL = "http://34.118.255.6:81/foto.php";
    private ImageView imgView;

    private final ActivityResultLauncher<Intent> takePictureLauncher =
            registerForActivityResult(new
                    ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData()!= null) {
                    Bundle bundle = result.getData().getExtras();
                    imgBitmap = (Bitmap) bundle.get("data");
                    Log.d("bitmap", getStringImagen(imgBitmap));
                    imgView.setImageBitmap(imgBitmap);
                } else {
                    Log.d("TakenPicture", "No photo taken");
                }
            });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);

        btnCamara = findViewById(R.id.btnCamara);
        btnSubirFoto = findViewById(R.id.btnSubirFoto);
        imgView = findViewById(R.id.imageView);

        cargarImagen();

        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Verificar y solicitar permisos de la cámara
                if (ContextCompat.checkSelfPermission(AnadirFoto.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AnadirFoto.this,
                            new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                } else {
                    // Si los permisos ya están concedidos, abrir la cámara
                    abrirCamara();
                }
            }
        });

        btnSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subirFoto();
            }
        });
    }

    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureLauncher.launch(intent);
    }

    private void subirFoto() {
        //Subir la foto al servidor y coger el nombre del text y si existe poner una alerta.
        if (imgBitmap != null) {
            String nombreUsuario = obtenerNombreUsuarioDePreferencias();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("success")) {
                        Log.d("respuesta", "Guardado correctamente.");
                    } else if (response.equals("failure")) {
                        Log.d("respuesta", "No se ha guardado!");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("imagen", getStringImagen(imgBitmap));
                    data.put("nombre", nombreUsuario);
                    data.put("accion", "subir");
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

    private String obtenerNombreUsuarioDePreferencias() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("nombreUsuario", "");
    }

    private void cargarImagen() {
        String nombreUsuario = obtenerNombreUsuarioDePreferencias();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("failure")) {
                    // Decodificar la imagen base64 recibida del servidor
                    byte[] decodedString = Base64.decode(response, Base64.DEFAULT);
                    Bitmap loadedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    // Mostrar la imagen cargada en el ImageView
                    imgView.setImageBitmap(loadedBitmap);
                } else {
                    Log.d("respuesta", "No se ha cargado la imagen");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("nombre", nombreUsuario);
                data.put("accion", "cargar");
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void volver(View view) {
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            // Verificar si los permisos fueron concedidos
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Si los permisos de la cámara fueron concedidos, abrir la cámara
                abrirCamara();
            } else {
                // Si los permisos de la cámara fueron denegados, mostrar un mensaje al usuario
                Toast.makeText(this, "Permiso de la cámara denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
