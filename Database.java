package com.example.movies;

import java.util.Map;
import android.os.Build;
import java.util.HashMap;
import android.content.Context;
import android.database.Cursor;
import android.content.ContentValues;
import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "highScores.db";
    private static final int DATABASE_VERSION = 1;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String CREATE_TABLE = "CREATE TABLE highScoreTable (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)";
        database.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS highScoreTable");
        onCreate(database);
    }

    public void add(String playerName) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", playerName);

        database.insert("highScoreTable", null, values);
        database.close();
    }

    @SuppressLint("Range")
    public Map<String, Integer> get() {
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM highScoreTable", null);
        Map<String, Integer> playerNames = new HashMap<>();

        while (cursor.moveToNext()) {
            String playerName = cursor.getString(cursor.getColumnIndex("name"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                playerNames.compute(playerName, (k, v) -> v == null ? 1 : v + 1);
            }
        }

        cursor.close();
        database.close();

        return playerNames;
    }
}