package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class Registrar extends AppCompatActivity {

    private EditText fechaInput;
    private EditText tituloInput;
    private EditText descripcionInput;
    private Button guardar;
    private DBController dbController;

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
        setContentView(R.layout.activity_registrar);

        fechaInput = (EditText) findViewById(R.id.fecha_input);
        tituloInput = (EditText) findViewById(R.id.titulo_input);
        descripcionInput = (EditText) findViewById(R.id.desc_input);
        guardar = (Button) findViewById(R.id.save_button);
        dbController = new DBController(this.getApplicationContext());
        SQLiteDatabase db = dbController.getWritableDatabase();
    }

    public void guardar(View view){
        try {
            if (!isFinishing()) {
                // Que ningun campo este vacio
                if (!(tituloInput.getText().toString().trim().isEmpty() || descripcionInput.getText().toString().trim().isEmpty() || fechaInput.getText().toString().trim().isEmpty())) {
                    boolean result = dbController.addData(fechaInput.getText().toString(), tituloInput.getText().toString(), descripcionInput.getText().toString());
                    if (result) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(this);
                        Log.e("DB-CONTROLLER:", "Se ha podido guardar");
                        alert.setTitle("Guardado correctamente");
                        alert.setMessage("La información está en la base de datos!");
                        alert.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 5000);
                    } else {
                        Log.e("DB-CONTROLLER:", "No se ha podido guardar");
                        Toast.makeText(this.getApplicationContext(), "No se ha podido guardar!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this.getApplicationContext(), "Existen campos que no tienen información", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e){
            Toast.makeText(this.getApplicationContext(), "No se ha podido guardar la información", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void volver(View view) {
        finish();
    }
}
