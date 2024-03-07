package com.example.myapplication;

import static android.provider.BaseColumns._ID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBController extends SQLiteOpenHelper {

    private SQLiteDatabase database = getWritableDatabase();
    private static final String DATABASE = "ejercicio.db";
    private static final String TABLA = "EJERCICIO";
    private static final String FECHA = "FECHA";
    private static final String TITULO = "TITULO";
    private static final String DESCRIPCION = "DESCRIPCION";

    public DBController(Context context) {
        super(context, DATABASE, null, 2);
    }
    // Los datos estan en View -> tool windows -> device explorer -> com.example.myapplication -> databases

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLA +" (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FECHA + " DATE NOT NULL," +
                TITULO + " VARCHAR NOT NULL," +
                DESCRIPCION + " VARCHAR NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA);
        onCreate(db);
    }

    // Registrar una tarea
    public boolean addData(String fecha, String titulo, String descripcion) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FECHA, fecha);
        contentValues.put(TITULO, titulo);
        contentValues.put(DESCRIPCION, descripcion);
        long result = database.insert(TABLA,null, contentValues);
        return result != -1;
    }

    public ArrayList<String> getFechaList() throws NullPointerException {
        database = this.getReadableDatabase();
        ArrayList<String> returnList = new ArrayList<>();
        Cursor data = database.rawQuery("SELECT DISTINCT " + FECHA + " FROM " + TABLA , null);
        data.moveToNext();
        do {
            returnList.add(data.getString(0));
        } while (data.moveToNext());
        if (returnList.size() > 0) {
            return returnList;
        } else {
            throw new NullPointerException();
        }
    }

    public ArrayList<String> getEjerciciosList(String fecha) throws NullPointerException {
        database = this.getReadableDatabase();
        ArrayList<String> returnList = new ArrayList<>();
        Cursor data = database.rawQuery("SELECT " + TITULO + " FROM " + TABLA + " WHERE " + FECHA + "='" + fecha + "'", null);
        data.moveToNext();
        do {
            returnList.add(data.getString(0));
        } while (data.moveToNext());
        if (returnList.size() > 0) {
            return returnList;
        } else {
            throw new NullPointerException();
        }
    }

    public ArrayList<String> getDescList(String fecha) throws NullPointerException {
        database = this.getReadableDatabase();
        ArrayList<String> returnList = new ArrayList<>();
        Cursor data = database.rawQuery("SELECT " + DESCRIPCION + " FROM " + TABLA + " WHERE " + FECHA + "='" + fecha + "'", null);
        data.moveToNext();
        do {
            returnList.add(data.getString(0));
        } while (data.moveToNext());
        if (returnList.size() > 0) {
            return returnList;
        } else {
            throw new NullPointerException();
        }
    }

    public boolean editar(String fechaAnt, String tituloAnt, String fechaNueva, String tituloNuevo, String descNuevo) {
        try {
            Log.d("Eneko", "entra en editar titulo: "+tituloAnt + " fecha: " + fechaAnt);
            database = this.getWritableDatabase();
            database.execSQL("UPDATE " + TABLA +
                    " SET " + FECHA + "= '" + fechaNueva + "', " +
                    TITULO + " = '" + tituloNuevo + "', " +
                    DESCRIPCION + " = '" + descNuevo +
                    "' WHERE " + TITULO + " = '" + tituloAnt +
                    "' AND " + FECHA + " = '" + fechaAnt + "';");
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public boolean eliminar(String fechaAnt, String tituloAnt) {
        try {
            Log.d("Eneko", "entra en eliminar titulo: "+tituloAnt + " fecha: " + fechaAnt);
            database = this.getWritableDatabase();
            database.execSQL("DELETE FROM " + TABLA +
                    " WHERE " + TITULO + " = '" + tituloAnt +
                    "' AND " + FECHA + " = '" + fechaAnt + "';");
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public boolean existe(String fecha, String titulo) {
        database = this.getReadableDatabase();
        Cursor titles = database.rawQuery("SELECT * FROM " + TABLA + " WHERE " + FECHA + "='" + fecha + "' AND " + TITULO + "='" + titulo + "'", null);
        if (titles.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
