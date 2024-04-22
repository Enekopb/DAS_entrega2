package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class PrimerWidget extends AppWidgetProvider {
    private static boolean alternar = false; // Variable de estado para alternar entre las frases

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Log.d("eneko", "entraupdateWidget");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.primer_widget);

        // Obtener las frases
        String frase1 = "¡Mantente activo! Tu cuerpo te lo agradecerá.";
        String frase2 = "Haz ejercicio, tu salud te lo agradecerá.";

        // Alternar entre las frases
        String fraseMostrar = alternar ? frase2 : frase1;
        alternar = !alternar; // Cambiar el estado para la próxima llamada

        // Actualizar el widget con la frase actual
        views.setTextViewText(R.id.appwidget_text, fraseMostrar);
        // Actualizar el widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, RecordatorioEjercicio.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE);

        // Establecer la alarma para actualizar el widget cada minuto
        long intervalMillis = 60000; // 1 minuto
        long triggerAtMillis = System.currentTimeMillis() + intervalMillis;
        alarmManager.setRepeating(AlarmManager.RTC, triggerAtMillis, intervalMillis, pendingIntent);
        Log.d("alarma", "alarma activada");
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, RecordatorioEjercicio.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Cancelar la alarma
        alarmManager.cancel(pendingIntent);
    }
}