package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ListaTareas extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DBController dbController;
    private ArrayList<String> listaTareas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("config_tema", MODE_PRIVATE);
        String temaGuardado = sharedPreferences.getString("tema", "DEFAULT");

        // Aplicar el tema correspondiente
        if (temaGuardado.equals("DEFAULT")) {
            setTheme(R.style.AppThemeLight);
        } else {
            setTheme(R.style.AppThemeDark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listatareas);

        dbController = new DBController(this.getApplicationContext());

        recyclerView = findViewById(R.id.edit_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initializeRecyclerView();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(ListaTareas.this, Mostrar.class);
                        String valor = listaTareas.get(position);
                        intent.putExtra(Mostrar.EXTRA_TAG, valor);
                        finish();
                        startActivity(intent);
                    }
                }));

    }

    // Obtenemos todos los títulos de las tareas
    private void initializeRecyclerView() {
        try {
            listaTareas = dbController.getFechaList();
            MyAdapter adapter = new MyAdapter(listaTareas, null);
            recyclerView.setAdapter(adapter);
        } catch (NullPointerException npe) {
            recyclerView.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void volver(View view) {
        finish();
    }
}
