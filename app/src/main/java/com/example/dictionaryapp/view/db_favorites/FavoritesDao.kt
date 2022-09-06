package com.example.dictionaryapp.view.db_favorites

import android.annotation.SuppressLint
import android.util.Log
import com.example.dictionaryapp.view.db_history.DatabaseHelper

class FavoritesDao {

    @SuppressLint("Recycle", "Range")
    fun addFavorites(dbhf: DatabaseHelperFavorites, dbh: DatabaseHelper, w: String) {
        val db = dbhf.writableDatabase
        val dbHistory = dbh.writableDatabase
        var defString = ""
        var speString = ""
        val def = dbHistory.rawQuery("SELECT definition FROM histories WHERE word = \"$w\"", null)
        while (def.moveToNext()) {
            defString = def.getString(def.getColumnIndex("definition"))
        }
        val speech = dbHistory.rawQuery("SELECT speech FROM histories WHERE word = \"$w\"", null)
        while (speech.moveToNext()) {
            speString = speech.getString(speech.getColumnIndex("speech"))
        }

        db.execSQL("INSERT INTO favorites (word, definition, speech) VALUES(\"${w}\", \"${defString}\", \"${speString}\")")
    }

    fun addDailyWordFavorites(dbhf: DatabaseHelperFavorites, w: String, def:String, speech:String) {
        val db = dbhf.writableDatabase
        db.execSQL("INSERT INTO favorites (word, definition, speech) VALUES(\"${w}\", \"${def}\", \"${speech}\")")
    }

    fun deleteFavorites(dbh: DatabaseHelperFavorites, w: String) {
        val db = dbh.writableDatabase
        db.execSQL("DELETE FROM favorites WHERE word = \"$w\"")
    }

    @SuppressLint("Range", "Recycle")
    fun isInFavorite(dbh: DatabaseHelperFavorites, w: String): Int {
        val db = dbh.writableDatabase
        val isFavorite = db.rawQuery("SELECT * FROM favorites WHERE word = \"$w\"", null)
        val favoritesList = ArrayList<String>()
        var wordForIsFavorite: String
        while (isFavorite.moveToNext()) {
            wordForIsFavorite = isFavorite.getString(isFavorite.getColumnIndex("word"))
            favoritesList.add(wordForIsFavorite)
        }
        Log.e("result", favoritesList.toString())
        if (favoritesList.isEmpty()) {
            return 0
        }
        else { return 1 }
    }

    @SuppressLint("Range", "Recycle")
    fun getTenFavorites(dbh:DatabaseHelperFavorites): ArrayList<Favorites> {
        val favoritesList = ArrayList<Favorites>()
        val db = dbh.writableDatabase
        val c = db.rawQuery("SELECT * FROM favorites ORDER BY \"word_id\" DESC LIMIT 10", null)


        while(c.moveToNext()) {
            val favorite = Favorites(c.getInt(c.getColumnIndex("word_id")), c.getString(c.getColumnIndex("word")), c.getString(c.getColumnIndex("definition")), c.getString(c.getColumnIndex("speech")))
            favoritesList.add(favorite)
        }
        return favoritesList
    }

    @SuppressLint("Range", "Recycle")
    fun getFavorites(dbh:DatabaseHelperFavorites): ArrayList<Favorites> {
        val favoritesList = ArrayList<Favorites>()
        val db = dbh.writableDatabase
        val c = db.rawQuery("SELECT * FROM favorites ORDER BY \"word_id\" DESC", null)


        while(c.moveToNext()) {
            val favorite = Favorites(c.getInt(c.getColumnIndex("word_id")), c.getString(c.getColumnIndex("word")), c.getString(c.getColumnIndex("definition")), c.getString(c.getColumnIndex("speech")))
            favoritesList.add(favorite)
        }
        return favoritesList
    }
}