package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.myapplication.ListaTareas;
import com.example.myapplication.R;
import com.example.myapplication.Registrar;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends Fragment {

    private Button alert;

    public Fragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment1, container, false);

        // Obtener el tema guardado de las preferencias compartidas
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("config_tema", MODE_PRIVATE);
        String temaGuardado = sharedPreferences.getString("tema", "DEFAULT");

        // Aplicar el tema correspondiente
        if (temaGuardado.equals("DEFAULT")) {
            // Aplicar tema claro
            requireActivity().setTheme(R.style.AppThemeLight);
            view.setBackgroundColor(Color.WHITE);
        } else {
            // Aplicar tema oscuro
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

        alert = view.findViewById(R.id.salir);
        alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(requireActivity());
                alerta.setMessage("Deseas salir de la aplicación?")
                        .setCancelable(false)
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which){
                                requireActivity().finish();
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener(){
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

        return view;
    }
}
