package com.example.dictionaryapp.view.db_favorites

import android.annotation.SuppressLint
import com.example.dictionaryapp.view.db_history.DatabaseHelper

class FavoritesDao {

    fun addFavorites(dbh: DatabaseHelper, w: String, def: String, speech: String) {
        val db = dbh.writableDatabase
        db.execSQL("INSERT INTO favorites (word, definition, speech) VALUES(${w}, ${def}, ${speech})")
    }

    fun deleteFavorites(dbh: DatabaseHelper, w: String) {
        val db = dbh.writableDatabase
        db.execSQL("DELETE FROM favorites WHERE word = \"$w\"")
    }

    @SuppressLint("Range", "Recycle")
    fun getTenFavorites(dbh:DatabaseHelper): ArrayList<Favorites> {
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
    fun getFavorites(dbh:DatabaseHelper): ArrayList<Favorites> {
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