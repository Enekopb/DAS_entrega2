package com.example.myapplication;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

public class WidgetEjercicio extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Configurar el mensaje del widget para animar al usuario a abrir la app
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_text);
        views.setTextViewText(R.id.widget_text_view, "¡Vamos! ¡Es hora de hacer ejercicio!");

        // Actualizar el widget
        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }
}
