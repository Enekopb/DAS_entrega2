package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Mostrar extends AppCompatActivity {

    public static final String EXTRA_TAG = "EJERCICIO";
    private String intentString;
    private String fileName;
    private RecyclerView recyclerView;
    private DBController dbController;
    private ArrayList<String> listaEjercicios = new ArrayList<>();
    private ArrayList<String> listaDesc = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("config_tema", MODE_PRIVATE);
        String temaGuardado = sharedPreferences.getString("tema", "DEFAULT");

        // Aplicar el tema correspondiente
        if (temaGuardado.equals("DEFAULT")) {
            setTheme(R.style.AppThemeLight);
        } else {
            setTheme(R.style.AppThemeDark);
        }

        Intent intent = getIntent();
        intentString = intent.getStringExtra(EXTRA_TAG);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar);
        dbController = new DBController(this.getApplicationContext());

        recyclerView = findViewById(R.id.edit_list2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initializeRecyclerView();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(Mostrar.this, Editar.class);
                        String valor = listaEjercicios.get(position);
                        Log.d("Eneko", valor);
                        intent.putExtra(Mostrar.EXTRA_TAG, valor);
                        intent.putExtra(Editar.EXTRA_TAG2, intentString);
                        finish();
                        // Se acaba porque al editarla en la siguiente pantalla se volvera a crear la actividad, para que aparezcan actualizada
                        startActivity(intent);
                    }
                }));
    }
    private void initializeRecyclerView() {
        try {
            listaEjercicios = dbController.getEjerciciosList(intentString);
            //Meter un segundo parametro en el listview
            listaDesc = dbController.getDescList(intentString);
            MyAdapter adapter = new MyAdapter(listaEjercicios, listaDesc);
            recyclerView.setAdapter(adapter);
        } catch (NullPointerException npe) {
            recyclerView.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void descargar(View view) {
        // Guardar en un fichero de texto informacion, el fichero tendra de nombre la fecha.
        // Para ver el fichero en el simulador: View -> tool windows -> Device explorer -> /storage/emulated/0/Documents/fitFlow.txt
        try {
            fileName = intentString.replaceAll("/", "_");;
            listaEjercicios = dbController.getEjerciciosList(intentString);
            listaDesc = dbController.getDescList(intentString);
            String content;
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), fileName);
            FileWriter writer = new FileWriter(file);
            writer.append("Dia: "+intentString + "\n" + "\n");
            for (int i = 0; i < listaEjercicios.size(); i++) {
                String ejercicio = listaEjercicios.get(i);
                String descripcion = listaDesc.get(i);

                // Construir la cadena de contenido con el formato deseado
                content = ejercicio + " : " + descripcion + "\n";
                Log.d("enekop", content);

                // Escribir la cadena en el archivo
                writer.append(content);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Boton de volver hacia atras que haga finish
    public void volver(View view) {
        finish();
    }
}
