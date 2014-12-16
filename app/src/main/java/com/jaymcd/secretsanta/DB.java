package com.jaymcd.secretsanta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DB {

    public static final String DATABASE_NAME = "NAMEDB";

    public static final int DB_VERS = 1;

    private Helper dbHelp;
    private final Context context;
    private SQLiteDatabase database;

    public static class Helper extends SQLiteOpenHelper {

        public Helper(Context context) {
            super(context, DATABASE_NAME, null, DB_VERS);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE NAMETBL(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT NOT NULL, SANTAID INTEGER);");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS NAMETBL");
            onCreate(db);
        }

    }

    public DB(Context context) { // the constructor for this class
        this.context = context;
    }

    public DB open() throws SQLException {
        dbHelp = new Helper(context);
        database = dbHelp.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelp.close();
    }

    public long addName(String nm) {
        ContentValues val = new ContentValues();
        val.put("NAME", nm);
        val.put("SANTAID", -1);
        return database.insert("NAMETBL", null, val);
    }

    public ArrayList<String> getNames() {

        String[] cols = {"ID", "NAME"};

        Cursor c = database.query("NAMETBL", cols, null, null, null, null,
                null);

        int iName = c.getColumnIndex("NAME");
        ArrayList<String> result = new ArrayList<String>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result.add(c.getString(iName));
        }

        c.close();
        return result;
    }

    public ArrayList<Integer> getIDs() {
        ArrayList<Integer> result = new ArrayList<Integer>();

        Cursor c = database.query("NAMETBL",new String[]{"ID"},null,null,null,null,null);

        int x = c.getColumnIndex("ID");

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result.add(c.getInt(x));
        }

        c.close();

        return result;
    }


    public String getName(int uid) {

        Cursor c = database.query("NAMETBL",new String[]{"NAME"}, "ID =" + String.valueOf(uid),null,null,null,null);
        int id = c.getColumnIndex("NAME");
        String result = null;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result = c.getString(id);
        }
        c.close();

        return result;

    }

    public void setSanta(int uid, int santa){
        ContentValues values = new ContentValues();
        values.put("SANTAID", String.valueOf(santa));
        database.update("NAMETBL", values, "ID = ?",new String[]{String.valueOf(uid)});


    }



    public int dbSize(){
        Cursor c = database.query("NAMETBL",new String[]{"ID"},null,null,null,null,null);
        int x = c.getColumnIndex("ID");
        int result = 0;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result++;
        }
        c.close();

        return result;

    }

    public int getSanta(int id){

        Cursor c = database.query("NAMETBL",new String[]{"SANTAID"},"ID =" + String.valueOf(id),null,null,null,null);
        int x = c.getColumnIndex("SANTAID");
        int result = 0;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result = c.getInt(x);
        }
        c.close();

        return result;
    }

    public int getID(String nm) {

        Cursor c = database.rawQuery("SELECT ID FROM NAMETBL WHERE NAME = ?", new String[]{nm});

        int id = c.getColumnIndex("ID");
        int result = -1;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result = c.getInt(id);
        }

        c.close();
        return result;
    }


    public void delRecord(String x) { //delete a santa
        database.delete("NAMETBL","NAME = ?", new String[]{x});
    }

    public void delAllRecords() { //drop the table

        database.execSQL("DROP TABLE IF EXISTS NAMETBL");
        database.execSQL("CREATE TABLE NAMETBL(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT NOT NULL, SANTAID INTEGER);");

    }
}
