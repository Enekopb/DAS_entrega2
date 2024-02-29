package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

/** @noinspection ALL*/
public class Configuracion extends AppCompatActivity {

    private static final String PREFERENCIASIDIOMA = "config_idioma";
    private static final String PREFERENCIASTEMA = "config_tema";
    private static final String IDIOMA_PREF_KEY = "idioma";
    private static final String TEMA_PREF_KEY = "tema";
    private Switch cambio;

    protected void onCreate(Bundle savedInstanceState){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCIASTEMA, MODE_PRIVATE);
        String temaGuardado = sharedPreferences.getString("tema", "DEFAULT");

        // Aplicar el tema correspondiente
        if (temaGuardado.equals("DEFAULT")) {
            setTheme(R.style.AppThemeLight);
        } else {
            setTheme(R.style.AppThemeDark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button button1 = findViewById(R.id.english);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button button2 = findViewById(R.id.castellano);

        // Listener para el botón en ingles
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarIdioma("en");
            }
        });

        // Listener para el botón en castellano
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarIdioma("values");
            }
        });

        cambio = (Switch) findViewById(R.id.switch1);

        cambio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            // Verificar tema actual
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("Eneko", "Cambio boton");
                if(cambio.isChecked()){
                    updateTema("DARK");
                } else {
                    updateTema("DEFAULT");
                }
            }
        });
    }

    private void cambiarIdioma(String languageCode) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCIASIDIOMA, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(IDIOMA_PREF_KEY, languageCode);
        editor.apply();

        Locale nuevoLocale = new Locale(languageCode);
        Locale.setDefault(nuevoLocale);
        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevoLocale);
        getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        finish();
        startActivity(new Intent(this, Configuracion.class));
    }

    public void volver(View view) {
        Intent editIntent = new Intent(Configuracion.this, MainActivity.class);
        startActivity(editIntent);
    }

    public void configurar(View view) {
        //No hace na de na
    }

    public void updateTema(String key){
        Log.d("Eneko", key);
        SharedPreferences sp = getSharedPreferences(PREFERENCIASTEMA, MODE_PRIVATE);
        SharedPreferences.Editor objEditor = sp.edit();
        objEditor.putString(TEMA_PREF_KEY, key);
        objEditor.commit();
        Log.d("Tema", sp.getString("tema","DEFAULT"));
        if (key.equals("DEFAULT")){
            //Se guarda al hacer el siguiente onCreate de la nueva actividad, no se aplica seguido.
            setTheme(R.style.AppThemeLight);
        } else{
            setTheme(R.style.AppThemeDark);
        }
    }

}
