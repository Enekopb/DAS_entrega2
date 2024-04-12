package com.example.myapplication;

import static androidx.core.app.ActivityCompat.requestPermissions;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public MyFirebaseMessagingService() {
    }

    private static final String URL = "http://34.118.255.6:81/token.php";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d("FCM Token", token);
        sendTokenToServer(MyFirebaseMessagingService.this, token);
    }

    public static void sendTokenToServer(Context context, String token) {
        String nombre = obtenerNombreUsuarioDePreferencias(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {
                    Log.d("respuesta", "Guardado correctamente.");
                } else if (response.equals("failure")) {
                    Log.d("respuesta", "No se ha guardado!");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context.getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("token", token);
                data.put("nombre", nombre);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        if (message.getData().size() > 0) {
            Log.d("fcm", "Entra");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importancia = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel("4", "channel1", importancia);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "4")
                    .setSmallIcon(R.drawable.baseline_adb_24)
                    .setContentTitle(message.getData().get("title"))
                    .setContentText(message.getData().get("body"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            if (notificationManagerCompat.areNotificationsEnabled()) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                notificationManagerCompat.notify(4, builder.build());
            } else {
                // Manejar el caso en que no tenemos permiso para mostrar notificaciones
                Log.d("fcm", "No tienes permiso para mostrar notificaciones");
            }
        }
        Log.d("fcm", "NoEntra");
    }

    private static String obtenerNombreUsuarioDePreferencias(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("nombreUsuario", "");
    }
}
