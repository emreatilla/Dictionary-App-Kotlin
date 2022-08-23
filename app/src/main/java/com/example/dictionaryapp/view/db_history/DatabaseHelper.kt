package com.example.dictionaryapp.view.db_history

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "history.sqlite", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        Log.e("XX_Create", "create")
        db?.execSQL("CREATE TABLE IF NOT EXISTS histories (\"word_id\" INTEGER PRIMARY KEY AUTOINCREMENT, \"word\" TEXT, \"definition\" TEXT, \"speech\" TEXT, \"isFlagged\" INTEGER, UNIQUE(\"word\"))")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.e("XX_Upgrade", "upgrade")
        db?.execSQL("DROP TABLE IF EXISTS histories")
        onCreate(db)
    }

}