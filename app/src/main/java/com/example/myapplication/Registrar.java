package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;


public class Registrar extends AppCompatActivity {

    private EditText fechaInput;
    private EditText tituloInput;
    private EditText descripcionInput;
    private DBController dbController;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 123;

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

        fechaInput = findViewById(R.id.fecha_input);
        tituloInput = findViewById(R.id.titulo_input);
        descripcionInput = findViewById(R.id.desc_input);
        Button guardar = (Button) findViewById(R.id.save_button);
        dbController = new DBController(this.getApplicationContext());
        SQLiteDatabase db = dbController.getWritableDatabase();
    }

    public void guardar(View view) {
        try {
            if (!isFinishing()) {
                // Que ningun campo este vacio
                if (!(tituloInput.getText().toString().trim().isEmpty() || descripcionInput.getText().toString().trim().isEmpty() || fechaInput.getText().toString().trim().isEmpty())) {
                    boolean result = dbController.addData(fechaInput.getText().toString(), tituloInput.getText().toString(), descripcionInput.getText().toString());
                    if (result) {

                        //Crear canal de notificaciones
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                            requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
                            int importancia = NotificationManager.IMPORTANCE_HIGH;
                            NotificationChannel channel = new NotificationChannel("1", "channel1", importancia);

                            NotificationManager notificationManager = getSystemService(NotificationManager.class);
                            notificationManager.createNotificationChannel(channel);
                        }

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                                .setSmallIcon(R.drawable.baseline_adb_24)
                                .setContentTitle("Actividad Registrada")
                                .setContentText("Tu actividad ha sido registrada correctamente.")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManagerCompat notificacionman = NotificationManagerCompat.from(this);
                        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
                        }
                        notificacionman.notify(Integer.parseInt("1"), builder.build());

                        finish();
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

    public void empezarEntrenamiento(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            // Si el permiso no está concedido, solicitarlo al usuario
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_CALENDAR},
                    MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
        } else {
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setData(CalendarContract.Events.CONTENT_URI);
            intent.putExtra(CalendarContract.Events.TITLE, "Entrenamiento");
            intent.putExtra(CalendarContract.Events.DESCRIPTION, "Hora de inicio: "+ String.valueOf(System.currentTimeMillis()));
            intent.putExtra(CalendarContract.Events.ALL_DAY, "true");
            startActivity(intent);
        }
        finish();
    }
}
