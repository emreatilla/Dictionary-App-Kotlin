package com.example.dictionaryapp.view.db_history

import android.annotation.SuppressLint
import android.util.Log

class HistoriesDao {
    @SuppressLint("Recycle", "Range")
    fun getHistory(dbh:DatabaseHelper): ArrayList<Histories> {
        val historyList = ArrayList<Histories>()
        val db = dbh.writableDatabase
        val c = db.rawQuery("SELECT * FROM histories ORDER BY \"word_id\" DESC LIMIT 10", null)
        Log.e("HIS", "Henüz while a girmedi")


        while(c.moveToNext()) {
            val history = Histories(c.getInt(c.getColumnIndex("word_id")), c.getString(c.getColumnIndex("word")), c.getString(c.getColumnIndex("definition")), c.getString(c.getColumnIndex("speech")), c.getInt(c.getColumnIndex("isFlagged")))
            historyList.add(history)
            Log.e("HIS", " word_id : " + history.word_id.toString() + " word : " + history.word + " definition : " + history.definition + " speech : " + history.speech +" isFlagged : " + history.isFlagged.toString())
        }
        return historyList
    }

    fun addWord(dbh: DatabaseHelper, w: String, def: String, speech: String, flag: Int) {
        val db = dbh.writableDatabase
        db.execSQL("INSERT INTO histories (word, definition, speech, isFlagged) VALUES(${w}, ${def}, ${speech}, ${flag})")
    }

    fun deleteWord(dbh: DatabaseHelper, w: String) {
        val db = dbh.writableDatabase
        db.execSQL("DELETE FROM histories WHERE word = \"$w\"")
    }
}