package com.example.uilizacionia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class HobbyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "hobbies.db";
    private static final int DATABASE_VERSION = 1;

    // Tabla hobbies
    private static final String TABLE_HOBBIES = "hobbies";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_DIFICULTAD = "dificultad";

    // SQL para crear la tabla
    private static final String CREATE_TABLE_HOBBIES =
        "CREATE TABLE " + TABLE_HOBBIES + "(" +
        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        COLUMN_NOMBRE + " TEXT NOT NULL," +
        COLUMN_DIFICULTAD + " TEXT NOT NULL" +
        ")";

    public HobbyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_HOBBIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOBBIES);
        onCreate(db);
    }

    // Método para agregar un hobby
    public long addHobby(Hobby hobby) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, hobby.getNombre());
        values.put(COLUMN_DIFICULTAD, hobby.getDificultad());

        long id = db.insert(TABLE_HOBBIES, null, values);
        db.close();
        return id;
    }

    // Método para obtener todos los hobbies
    public List<Hobby> getAllHobbies() {
        List<Hobby> hobbies = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_HOBBIES + " ORDER BY " + COLUMN_NOMBRE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Hobby hobby = new Hobby();
                hobby.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                hobby.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)));
                hobby.setDificultad(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIFICULTAD)));
                hobbies.add(hobby);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return hobbies;
    }

    // Método para obtener un hobby por ID
    public Hobby getHobby(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HOBBIES,
            new String[]{COLUMN_ID, COLUMN_NOMBRE, COLUMN_DIFICULTAD},
            COLUMN_ID + "=?",
            new String[]{String.valueOf(id)},
            null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Hobby hobby = new Hobby(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIFICULTAD))
            );
            cursor.close();
            db.close();
            return hobby;
        }

        db.close();
        return null;
    }

    // Método para actualizar un hobby
    public int updateHobby(Hobby hobby) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, hobby.getNombre());
        values.put(COLUMN_DIFICULTAD, hobby.getDificultad());

        int result = db.update(TABLE_HOBBIES, values, COLUMN_ID + " = ?",
            new String[]{String.valueOf(hobby.getId())});
        db.close();
        return result;
    }

    // Método para eliminar un hobby
    public void deleteHobby(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HOBBIES, COLUMN_ID + " = ?",
            new String[]{String.valueOf(id)});
        db.close();
    }

    // Método para obtener el número total de hobbies
    public int getHobbiesCount() {
        String countQuery = "SELECT * FROM " + TABLE_HOBBIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
}
