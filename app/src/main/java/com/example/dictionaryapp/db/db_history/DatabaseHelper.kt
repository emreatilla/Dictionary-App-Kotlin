package com.example.dictionaryapp.db.db_history

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "history.sqlite", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS histories (\"word_id\" INTEGER PRIMARY KEY AUTOINCREMENT, \"word\" TEXT, \"definition\" TEXT, \"speech\" TEXT, \"isFlagged\" INTEGER, UNIQUE(\"word\"))")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS histories")
        onCreate(db)
    }

}