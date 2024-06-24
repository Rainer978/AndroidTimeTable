package com.example.authentic.Notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.authentic.Notes.TaskNote;
import com.example.authentic.Task;

import java.util.ArrayList;

public class DataBaseNote extends SQLiteOpenHelper {

    public DataBaseNote(Context context) {
        super(context, "DatabaseNote", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase dbnote) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase dbnote, int i, int i1) {

    }

    public void checkTableNote(String date) {
        String create = "CREATE TABLE IF NOT EXISTS `" + date +
                "` (`ID` integer, `Name` text, `Description` text);";
        SQLiteDatabase dbnote = this.getWritableDatabase();
        dbnote.execSQL(create);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addTaskNote(TaskNote n, String date) {
        checkTableNote(date);
        SQLiteDatabase db = this.getWritableDatabase();
        String insert = "INSERT INTO `" + date + "` (`ID`, `Name`, `Description`) VALUES " +
                "('" + n.getID() + "', '" + n.getName() + "', '" + n.getDescription() + "');";
        db.execSQL(insert);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<TaskNote> getAllTasksNote(String date) {
        checkTableNote(date);
        SQLiteDatabase dbnote = this.getReadableDatabase();
        ArrayList<TaskNote> note = new ArrayList<>();
        String select = "SELECT * FROM `" + date + "`;";
        Cursor cursor = dbnote.rawQuery(select, null);
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                TaskNote n = new TaskNote();
                n.setID(cursor.getInt(0));
                n.setName(cursor.getString(1));
                n.setDiscription(cursor.getString(2));
                note.add(n);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return note;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getNextID(String date) {
        ArrayList<TaskNote> note = getAllTasksNote(date);
        int id = 0;
        int size = note.size();
        if (size != 0) {
            int lastIndex = note.size()-1;
            id = note.get(lastIndex).getID()+1;
        }
        return id;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateTaskNote(TaskNote n, String date) {
        String update = "UPDATE `" + date + "` SET `Name` = '" + n.getName() + "', `Description` = '" + n.getDescription() + "' WHERE `ID` = " + n.getID() + ";";
        SQLiteDatabase dbnote = this.getWritableDatabase();
        dbnote.execSQL(update);
    }

    public void deleteTaskNote(int id, String date) {
        String delete = "DELETE FROM `"+date+"` WHERE `ID` = '"+id+"';";
        SQLiteDatabase dbnote = this.getWritableDatabase();
        dbnote.execSQL(delete);
    }
}
