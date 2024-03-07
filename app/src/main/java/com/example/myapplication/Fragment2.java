package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import java.util.Locale;

public class Fragment2 extends Fragment {

    private static final String PREFERENCIAS_IDIOMA = "config_idioma";
    private static final String PREFERENCIAS_TEMA = "config_tema";
    private static final String IDIOMA_PREF_KEY = "idioma";
    private static final String TEMA_PREF_KEY = "tema";
    private Switch cambio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment2, container, false);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFERENCIAS_TEMA, MODE_PRIVATE);
        String temaGuardado = sharedPreferences.getString("tema", "DEFAULT");

        // Aplicar el tema correspondiente
        if (temaGuardado.equals("DEFAULT")) {
            requireActivity().setTheme(R.style.AppThemeLight);
        } else {
            requireActivity().setTheme(R.style.AppThemeDark);
        }

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button button1 = view.findViewById(R.id.english);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button button2 = view.findViewById(R.id.castellano);

        // Listener para el botón en inglés
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

        cambio = view.findViewById(R.id.switch1);

        cambio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            // Verificar tema actual
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("Eneko", "Cambio boton");
                if (cambio.isChecked()) {
                    updateTema("DARK", container);
                } else {
                    updateTema("DEFAULT", container);
                }
            }
        });

        return view;
    }

    private void cambiarIdioma(String languageCode) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFERENCIAS_IDIOMA, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(IDIOMA_PREF_KEY, languageCode);
        editor.apply();

        Locale nuevoLocale = new Locale(languageCode);
        Locale.setDefault(nuevoLocale);
        Configuration configuration = requireActivity().getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevoLocale);
        requireActivity().getResources().updateConfiguration(configuration, requireActivity().getBaseContext().getResources().getDisplayMetrics());
        requireActivity().recreate();
    }

    public void updateTema(String key, View view) {
        Log.d("Eneko", key);
        SharedPreferences sp = requireActivity().getSharedPreferences(PREFERENCIAS_TEMA, MODE_PRIVATE);
        SharedPreferences.Editor objEditor = sp.edit();
        objEditor.putString(TEMA_PREF_KEY, key);
        objEditor.apply();
        Log.d("Tema", sp.getString("tema", "DEFAULT"));
        if (key.equals("DEFAULT")) {
            // Se guarda al hacer el siguiente onCreate de la nueva actividad, no se aplica seguido.
            view.setBackgroundColor(Color.WHITE);
        } else {
            view.setBackgroundColor(Color.BLACK);
        }
    }
}
