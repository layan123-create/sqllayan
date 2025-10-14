package com.example.sqllayan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASENAME12 = "result.db";
    private static final int DATABASEVERSION12 = 2;

    private static final String TABLERECORD12 = "tblresult";
    private static final String TABLEUSERS12 = "users";
    private static final String TABLECLIENTS12 = "clients";

    private static final String COLUMNID12 = "_id";
    private static final String COLUMNNAME12 = "name";
    private static final String COLUMNAMOUNT12 = "amount";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASENAME12, null, DATABASEVERSION12);
    }

    @Override
    public void onCreate(SQLiteDatabase db1) {
        db1.execSQL("CREATE TABLE IF NOT EXISTS " + TABLERECORD12 + " (" +
                COLUMNID12 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMNNAME12 + " TEXT, " +
                COLUMNAMOUNT12 + " INTEGER)");

        db1.execSQL("CREATE TABLE IF NOT EXISTS " + TABLEUSERS12 + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE, " +
                "password TEXT)");

        db1.execSQL("CREATE TABLE IF NOT EXISTS " + TABLECLIENTS12 + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "amount INTEGER, " +
                "username TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db1, int oldVer, int newVer) {
        db1.execSQL("DROP TABLE IF EXISTS " + TABLERECORD12);
        db1.execSQL("DROP TABLE IF EXISTS " + TABLEUSERS12);
        db1.execSQL("DROP TABLE IF EXISTS " + TABLECLIENTS12);
        onCreate(db1);
    }

    public boolean insertUser(String userna, String pass) {
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues cv1 = new ContentValues();
        cv1.put("username", userna);
        cv1.put("password", pass);
        try {
            long result = db1.insertOrThrow(TABLEUSERS12, null, cv1);
            return result != -1;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkUser(String userna, String pass) {
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor = db1.rawQuery(
                "SELECT * FROM users WHERE username=? AND password=?",
                new String[]{userna, pass});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public boolean userExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE username=?",
                new String[]{username});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public boolean insertClient(String name1, int amount1, String userna) {
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues cv1 = new ContentValues();
        cv1.put("name", name1);
        cv1.put("amount", amount1);
        cv1.put("username", userna);
        long result = db1.insert(TABLECLIENTS12, null, cv1);
        return result != -1;
    }

    public boolean updateClient(String oldNa, String newName, int newAmount) {
        if (oldNa == null || newName == null) return false;

        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues cv1 = new ContentValues();
        cv1.put("name", newName);
        cv1.put("amount", newAmount);
        int rows = db1.update(TABLECLIENTS12, cv1, "name=?", new String[]{oldNa});
        db1.close();
        return rows > 0;
    }

    public ModelClient getClientByName(String name) {
        if (name == null || name.trim().isEmpty()) return null;

        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor = db1.rawQuery("SELECT * FROM clients WHERE name = ?", new String[]{name});
        ModelClient client = null;

        if (cursor != null && cursor.moveToFirst()) {
            client = new ModelClient();
            client.setId1(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            client.setName1(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            client.setamount1(cursor.getInt(cursor.getColumnIndexOrThrow("amount")));
            cursor.close();
        }
        return client;
    }

    public List<ModelClient> getClientsByUser(String username) {
        List<ModelClient> clients = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM clients WHERE username=?", new String[]{username});
        if (cursor.moveToFirst()) {
            do {
                clients.add(new ModelClient(
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("amount")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return clients;
    }

    public void deleteClientById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLECLIENTS12, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }
}
