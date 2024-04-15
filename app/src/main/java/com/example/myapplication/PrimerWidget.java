package com.example.myapplication;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class PrimerWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Obtener el texto del recurso de cadena
        CharSequence widgetText = context.getString(R.string.appwidget_text);

        // Construir las vistas remotas
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.primer_widget);

        Log.d("widget1",context.getString(R.string.appwidget_text));
        Log.d("widget2",widgetText.toString());

        // Comparar el texto del widget con el recurso de cadena
        if (context.getString(R.string.appwidget_text) == widgetText.toString()) {
            // Establecer el texto del widget según el recurso de cadena correspondiente
            views.setTextViewText(R.id.appwidget_text, context.getString(R.string.appwidget_text2));
            Log.d("PrimerWidget", "Texto1 del widget actualizado a: " + context.getString(R.string.appwidget_text2));
        } else {
            views.setTextViewText(R.id.appwidget_text, context.getString(R.string.appwidget_text));
            Log.d("PrimerWidget", "Texto del widget actualizado a: " + context.getString(R.string.appwidget_text));
        }
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
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}