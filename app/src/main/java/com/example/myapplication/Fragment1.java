package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends Fragment {
    private static final String PREFERENCIAS_IDIOMA = "config_idioma";
    private static final String PREFERENCIAS_TEMA = "config_tema";
    private static final String IDIOMA_PREF_KEY = "idioma";
    private static final String TEMA_PREF_KEY = "tema";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment1, container, false);
        Button alert = view.findViewById(R.id.salir);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFERENCIAS_TEMA, MODE_PRIVATE);
        String temaGuardado = sharedPreferences.getString("tema", "DEFAULT");

        // Aplicar el tema correspondiente
        if (temaGuardado.equals("DEFAULT")) {
            requireActivity().setTheme(R.style.AppThemeLight);
            view.setBackgroundColor(Color.WHITE);
        } else {
            requireActivity().setTheme(R.style.AppThemeDark);
            view.setBackgroundColor(Color.BLACK);
        }

        sharedPreferences = requireActivity().getSharedPreferences("config_idioma", MODE_PRIVATE);
        String idiomaGuardado = sharedPreferences.getString("idioma", "values");
        Locale nuevoLocale = new Locale(idiomaGuardado);
        Locale.setDefault(nuevoLocale);
        Configuration configuration = requireActivity().getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevoLocale);
        requireActivity().getResources().updateConfiguration(configuration, requireActivity().getBaseContext().getResources().getDisplayMetrics());

        alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(requireActivity());
                alerta.setMessage("Deseas salir de la aplicación?")
                        .setCancelable(false)
                        .setPositiveButton(Html.fromHtml("<font color='#FFA500'>Aceptar</font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which){
                                requireActivity().finish();
                            }
                        }).setNegativeButton(Html.fromHtml("<font color='#FFA500'>Cancelar</font>"), new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                dialog.cancel();
                            }
                        });
                AlertDialog titulo = alerta.create();
                titulo.setTitle("Salida");
                titulo.show();
            }
        });

        Button botonRegistrar = view.findViewById(R.id.registrar);
        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea un Intent para abrir la nueva actividad
                Intent intent = new Intent(getActivity(), Registrar.class);
                // Inicia la nueva actividad
                startActivity(intent);
            }
        });

        Button botonEditar = view.findViewById(R.id.editar);
        botonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea un Intent para abrir la nueva actividad
                Intent intent = new Intent(getActivity(), ListaTareas.class);
                // Inicia la nueva actividad
                startActivity(intent);
            }
        });

        Button botonWeb = view.findViewById(R.id.web);
        botonWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="https://musclewiki.com/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button botonSacarFoto = view.findViewById(R.id.sacarFoto);
        botonSacarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AnadirFoto.class);
                startActivity(intent);
            }
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button botonMapa = view.findViewById(R.id.mapa);
        botonMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Mapa.class);
                startActivity(intent);
            }
        });

        return view;
    }
}