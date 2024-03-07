package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Editar extends AppCompatActivity {

    public static final String EXTRA_TAG = "EJERCICIO";
    public static final String EXTRA_TAG2 = "FECHA";
    private EditText fechaInput;
    private EditText tituloInput;
    private EditText descripcionInput;
    private String intentStringTitulo;
    private String intentStringFecha;
    private Button guardar;
    private DBController dbController;

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
        intentStringTitulo = intent.getStringExtra(EXTRA_TAG);
        Log.d("Eneko", intentStringTitulo + " intentStringTitulo");
        intentStringFecha = intent.getStringExtra(EXTRA_TAG2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        fechaInput = (EditText) findViewById(R.id.fecha_input);
        tituloInput = (EditText) findViewById(R.id.titulo_input);
        descripcionInput = (EditText) findViewById(R.id.desc_input);

        guardar = (Button) findViewById(R.id.save_button);

        dbController = new DBController(this.getApplicationContext());
    }

    public void saveUpdates(View view){
        try {
            // Todos los campos son obligatorios de introducir
            Log.d("Eneko", fechaInput.getText().toString());
            if (!(fechaInput.getText().toString().trim().isEmpty() ||
                    tituloInput.getText().toString().trim().isEmpty() ||
                    descripcionInput.getText().toString().trim().isEmpty())) {

                // Mirar si esta libre el nombre (si no esta libre dialog de que ya existe)
                if (!dbController.existe(fechaInput.getText().toString(), tituloInput.getText().toString())) {
                    dbController.editar(intentStringFecha, intentStringTitulo,
                            fechaInput.getText().toString(),
                            tituloInput.getText().toString(),
                            descripcionInput.getText().toString());
                    finish();
                } else {
                    Toast.makeText(this.getApplicationContext(), "Ya hay información asociada a ese nombre", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this.getApplicationContext(), "No estan llenos todos los campos", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            Toast.makeText(this.getApplicationContext(), "No se ha podido guardar", Toast.LENGTH_SHORT).show();
        }
    }

    //Boton de eliminar para que haga el delete de esa linea y vuelva pa atras. Si es la ultima no habra nada en el listview
    public void eliminar(View view){
        try {
            Log.d("Eneko", intentStringTitulo + " intentStringTitulo2");
            dbController.eliminar(intentStringFecha, intentStringTitulo);
            finish();
        } catch (Exception e){
            Toast.makeText(this.getApplicationContext(), "No se ha podido guardar", Toast.LENGTH_SHORT).show();
        }
    }
    public void volver(View view) {
        finish();
    }
}
