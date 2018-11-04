package com.example.pablom.agenda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AgendaDB.db";
    private static final String CONTACTOS_TABLE = "contactos";
    private static final String CONTACTOS_CREATE_TABLE = "CREATE TABLE contactos " +
            "(_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, nombre VARCHAR(30) NOT NULL, " +
            "direccion VARCHAR(50), movil VARCHAR(9), email VARCHAR(40))";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CONTACTOS_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTOS_TABLE );
        onCreate(db);
    }

    public void insertContacto(String nombre, String direccion, String movil, String email) {
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("direccion", direccion);
            values.put("movil", movil);
            values.put("email", email);
            db.insert("contactos", null, values);
        }

        db.close();
    }

    public void updateContacto(int id, String nombre, String direccion, String movil, String email) {
        SQLiteDatabase db = getWritableDatabase();

        if (db != null) {
            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("direccion", direccion);
            values.put("movil", movil);
            values.put("email", email);
            db.update("contactos",  values, "_id=" + id, null);
        }

        db.close();
    }

    public void deleteContacto(int id) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            db.delete("contactos", "_id" + id, null);
        }
        db.close();
    }

    public Contacto queryrContacto(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"_id", "nombre", "direccion", "movil", "email"};
        Cursor c = db.query("contactos", valores_recuperar, "_id=" + id, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        Contacto contacto = new Contacto(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4));
        db.close();
        c.close();
        return contacto;
    }

    public Cursor queryContactosCursor() {
        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"_id","nombre", "direccion", "movil", "email"};

        Cursor c = db.query("contactos", valores_recuperar, null, null, null, null, null, null);
        return c;
    }

    public int numberOfRows(){
        int dato= (int) DatabaseUtils.queryNumEntries(this.getWritableDatabase(), "contactos");
        return dato;
    }

    public int [] queryIds(){
        int [] datosId;
        int i;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id FROM contactos", null);
        if (cursor.getCount()>0){
            datosId= new int[cursor.getCount()];
            i=0;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                datosId[i] = cursor.getInt(0);
                i++;
                cursor.moveToNext();
            }
        }
        else datosId= new int [0];
        cursor.close();
        return datosId;
    }
}
