package com.example.todotasklist;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "TODO_LIST_SQLITEDB";
    public static final String TABLE_NAME = "TODO_LIST_TASK";
    public static final String KEY_ID = "ID";
    public static final String KEY_ITEM = "TASK";
    public static final String KEY_DATE = "DATE";

    private Context ctx;
    public DatabaseHandler(@Nullable Context context) {
        super(context, DB_NAME,null,DB_VERSION);
        this.ctx=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,TASK TEXT, DATE LONG)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void addTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // values.put(KEY_ID,task.getId());
        values.put(KEY_ITEM,task.getName());
        values.put(KEY_DATE, java.lang.System.currentTimeMillis());
        db.insert(TABLE_NAME, null, values);
        Toast.makeText(ctx, "saved to sqlite", Toast.LENGTH_SHORT).show();
        Log.d("Saved!", "Saved to sqlite" + values);

    }
    public int getTaskCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ITEM, task.getName());
        values.put(KEY_DATE, java.lang.System.currentTimeMillis());

        return db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{
                String.valueOf(task.getId())
        });
    }

    //delete task
    public void deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + "= ?", new String[]{String.valueOf(id)});
        db.close();
    }

    //    get all task
    @SuppressLint("Range")
    public List<Task> getAllTask() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor=null;
        List<Task> taskList = new ArrayList<>();

        try {
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    Task task = new Task();
                    task.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                    task.setName(cursor.getString(cursor.getColumnIndex(KEY_ITEM)));
                    java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                    String formatDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(KEY_DATE))));
                    task.setDateAdded(formatDate);
                    taskList.add(task);
                } while (cursor.moveToNext());
            }
        }
        finally {
           // db.endTransaction();
            cursor.close();
        }
        return taskList;
    }
}
