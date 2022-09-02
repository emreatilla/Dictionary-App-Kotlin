package com.example.dictionaryapp.view.db_history

import android.annotation.SuppressLint

class HistoriesDao {

    @SuppressLint("Recycle", "Range")
    fun getHistory(dbh:DatabaseHelper): ArrayList<Histories> {
        val historyList = ArrayList<Histories>()
        val db = dbh.writableDatabase
        val c = db.rawQuery("SELECT * FROM histories ORDER BY \"word_id\" DESC", null)


        while(c.moveToNext()) {
            val history = Histories(c.getInt(c.getColumnIndex("word_id")), c.getString(c.getColumnIndex("word")), c.getString(c.getColumnIndex("definition")), c.getString(c.getColumnIndex("speech")), c.getInt(c.getColumnIndex("isFlagged")))
            historyList.add(history)
        }
        return historyList
    }

    @SuppressLint("Range", "Recycle")
    fun getFavorites(dbh:DatabaseHelper): ArrayList<Histories> {
        val historyList = ArrayList<Histories>()
        val db = dbh.writableDatabase
        val c = db.rawQuery("SELECT * FROM histories WHERE isFlagged = 1 ORDER BY \"word_id\" DESC", null)


        while(c.moveToNext()) {
            val history = Histories(c.getInt(c.getColumnIndex("word_id")), c.getString(c.getColumnIndex("word")), c.getString(c.getColumnIndex("definition")), c.getString(c.getColumnIndex("speech")), c.getInt(c.getColumnIndex("isFlagged")))
            historyList.add(history)
        }
        return historyList
    }


    @SuppressLint("Range", "Recycle")
    fun isFavorite(dbh: DatabaseHelper, w: String): Int{
        val db = dbh.writableDatabase
        val c = db.rawQuery("SELECT isFlagged FROM histories WHERE word = \"$w\"", null)
        var isF = 0

        while (c.moveToNext()){
            isF = c.getInt(c.getColumnIndex("isFlagged"))
        }
        return isF
    }

    fun addToFavorites(dbh: DatabaseHelper, w: String) {
        val db = dbh.writableDatabase
        db.execSQL("UPDATE histories SET isFlagged = 1 WHERE word = \"$w\"")
    }

    fun removeFavorites(dbh: DatabaseHelper, w: String) {
        val db = dbh.writableDatabase
        db.execSQL("UPDATE histories SET isFlagged = 0 WHERE word = \"$w\"")
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