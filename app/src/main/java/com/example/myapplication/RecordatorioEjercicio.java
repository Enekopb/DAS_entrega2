package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RecordatorioEjercicio extends BroadcastReceiver {
    private static boolean alternar = false; // Variable de estado para alternar entre las frases

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("eneko", "entrauOnReceiveAlarm");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.primer_widget);

        // Obtener las frases
        String frase1 = "¡Mantente activo! Tu cuerpo te lo agradecerá.";
        String frase2 = "Haz ejercicio, tu salud te lo agradecerá.";

        // Alternar entre las frases
        String fraseMostrar = alternar ? frase2 : frase1;
        alternar = !alternar; // Cambiar el estado para la próxima llamada

        // Actualizar el widget con la frase actual
        remoteViews.setTextViewText(R.id.appwidget_text, fraseMostrar);

        // Actualizar el widget
        ComponentName tipowidget = new ComponentName(context, PrimerWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(tipowidget, remoteViews);
    }
}

