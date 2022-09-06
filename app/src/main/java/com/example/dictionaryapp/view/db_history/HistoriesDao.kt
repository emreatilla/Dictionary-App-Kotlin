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
        val removedDoubleQuotesDefinition = def.substring(1, def.length - 1)
        val removedDoubleQuotesDefinition2 = removedDoubleQuotesDefinition.replace("\"", "'")
        db.execSQL("INSERT INTO histories (word, definition, speech, isFlagged) VALUES(${w}, \"${removedDoubleQuotesDefinition2}\", ${speech}, ${flag})")
    }

    fun deleteWord(dbh: DatabaseHelper, w: String) {
        val db = dbh.writableDatabase
        db.execSQL("DELETE FROM histories WHERE word = \"$w\"")
    }
}