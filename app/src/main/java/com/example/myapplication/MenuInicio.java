package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;
import java.util.Locale;

public class MenuInicio extends AppCompatActivity {

    private Fragment fragment1, fragment2;
    private FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
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

            // Configurar la alarma para las 5:00 PM
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 17); // Hora en formato de 24 horas
            calendar.set(Calendar.MINUTE, 10);
            calendar.set(Calendar.SECOND, 0);

            int flags = PendingIntent.FLAG_UPDATE_CURRENT;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                flags |= PendingIntent.FLAG_IMMUTABLE;
            }

            Intent intent = new Intent(this, RecordatorioEjercicio.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, flags);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

            Toast.makeText(this, "Alarma configurada para las 5:00 PM todos los días", Toast.LENGTH_SHORT).show();
            //FirebaseApp.initializeApp(this);
            getFCMToken();
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

    private void getFCMToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    String token = task.getResult();
                    // Pasar el token al servicio
                    MyFirebaseMessagingService.sendTokenToServer(MenuInicio.this, token);
                } else {
                    Log.w("MyActivity", "Fetching FCM registration token failed", task.getException());
                }
            }
        });
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