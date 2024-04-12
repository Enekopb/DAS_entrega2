package com.example.myapplication;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class RecordatorioEjercicio extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // Crear canal de notificaciones
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importancia = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("2", "Canal de notificaciones de ejercicio", importancia);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        // Aquí se lanza la notificación recordando al usuario hacer ejercicio
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "exercise_notification_channel")
                .setSmallIcon(R.drawable.baseline_adb_24)
                .setContentTitle("¡Hora de hacer ejercicio!")
                .setContentText("¡Es hora de levantarse y moverse!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NOTIFICATION_POLICY) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((AppCompatActivity) context, new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY}, 1);
        }

        notificationManager.notify(2, builder.build());
    }
}
