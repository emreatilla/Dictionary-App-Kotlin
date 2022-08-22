package com.example.dictionaryapp.view.db_history

import android.annotation.SuppressLint
import android.util.Log

class HistoriesDao {
    @SuppressLint("Recycle", "Range")
    fun getHistory(dbh:DatabaseHelper): ArrayList<Histories> {
        val historyList = ArrayList<Histories>()
        val db = dbh.writableDatabase
        val c = db.rawQuery("SELECT * FROM histories", null)
        Log.e("HIS", "Henüz while a girmedi")


        while(c.moveToNext()) {
            val history = Histories(c.getInt(c.getColumnIndex("word_id")), c.getString(c.getColumnIndex("word")), c.getString(c.getColumnIndex("definition")), c.getInt(c.getColumnIndex("isFlagged")))
            historyList.add(history)
            Log.e("HIS", history.word_id.toString() + history.word + history.definition + history.isFlagged.toString())
        }
        return historyList
    }

    fun addWord(dbh: DatabaseHelper, w: String, def: String, flag: Int) {
        val db = dbh.writableDatabase
        db.execSQL("INSERT INTO histories (word, definition, isFlagged) VALUES(${w}, ${def}, ${flag})")
    }
}