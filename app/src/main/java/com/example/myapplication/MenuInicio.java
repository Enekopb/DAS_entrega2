package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Locale;

public class MenuInicio extends AppCompatActivity {

    private Fragment fragment1, fragment2;
    private FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){
            SharedPreferences sp = getSharedPreferences("config_tema", MODE_PRIVATE);
            SharedPreferences.Editor e = sp.edit();
            e.putString("tema", "DEFAULT");
            e.apply();

            SharedPreferences sharedPreferences = getSharedPreferences("config_idioma", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("idioma", "values");
            editor.apply();

            Locale nuevoLocale = new Locale("values");
            Locale.setDefault(nuevoLocale);
            Configuration configuration = getBaseContext().getResources().getConfiguration();
            configuration.setLocale(nuevoLocale);
            getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
            recreate();
        }
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.container);
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();

        // Mostrar Fragment1 por defecto
        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment2).commit();
        }
    }

    public void configurar(View view) {
        // Cambiar al Fragment2 (Configuración) al hacer clic en la opción de configuración en el menú
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment2).commit();
    }

    public void menu(View view){
        // Cambiar al Fragment2 (Configuración) al hacer clic en la opción de configuración en el menú
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();
    }
}