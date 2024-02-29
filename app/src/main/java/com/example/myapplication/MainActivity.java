package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Obtener el tema guardado de las preferencias compartidas
        SharedPreferences sharedPreferences = getSharedPreferences("config_tema", MODE_PRIVATE);
        String temaGuardado = sharedPreferences.getString("tema", "DEFAULT");

        // Aplicar el tema correspondiente
        if (temaGuardado.equals("DEFAULT")) {
            setTheme(R.style.AppThemeLight);
        } else {
            setTheme(R.style.AppThemeDark);
        }

        sharedPreferences = getSharedPreferences("config_idioma", MODE_PRIVATE);
        String idiomaGuardado = sharedPreferences.getString("idioma", "values");
        Locale nuevoLocale = new Locale(idiomaGuardado);
        Locale.setDefault(nuevoLocale);
        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevoLocale);
        getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alert = (Button) findViewById(R.id.salir);
        alert.setOnClickListener(new View.OnClickListener() {

            //Boton salir app con dialogs
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                alerta.setMessage("Deseas salir de la aplicación?")
                        .setCancelable(false)
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() { //Salir de la app
                            public void onClick(DialogInterface dialog, int which){
                                finish();
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener(){ //Quedarse en la app
                            public void onClick(DialogInterface dialog, int which){
                                dialog.cancel();
                            }
                        });
                AlertDialog titulo = alerta.create();
                titulo.setTitle("Salida");
                titulo.show();
            }
        });
    }

    // Empezar la actividad de registro
    public void registrar(View view){
        Intent registrarIntent = new Intent(MainActivity.this, Registrar.class);
        startActivity(registrarIntent);
    }

    // Empezar la actividad para editar
    public void editar(View view) {
        Intent editIntent = new Intent(MainActivity.this, ListaTareas.class);
        startActivity(editIntent);
    }

    // Empezar la actividad de configuracion
    public void configurar(View view) {
        Intent editIntent = new Intent(MainActivity.this, Configuracion.class);
        finish();
        startActivity(editIntent);
    }

    // Intent explicito para abrir el navegador al hacer click en un boton
    public void web(View view) {
        String url="https://musclewiki.com/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}