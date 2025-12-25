package com.student.ft_hangouts

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

// Object class with variables
class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "ft_hangouts.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "contacts"
        const val COL_ID = "id"
        const val COL_FIRST_NAME = "first_name"
        const val COL_LAST_NAME = "last_name"
        const val COL_PHONE = "phone_number"
        const val COL_EMAIL = "email"
        const val COL_INFO = "contextual_info"
    }

    // Create database
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE_QUERY = "CREATE TABLE $TABLE_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_FIRST_NAME TEXT NOT NULL, " +
                "$COL_LAST_NAME TEXT NOT NULL, " +
                "$COL_PHONE TEXT NOT NULL, " +
                "$COL_EMAIL TEXT, " +
                "$COL_INFO TEXT NOT NULL" +
                ")"
        db.execSQL(CREATE_TABLE_QUERY)
        Log.d("DatabaseHelper", "Database created with table $TABLE_NAME") // Added logging
    }

    // Update database version
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
        Log.d("DatabaseHelper", "Database upgraded from version $oldVersion to $newVersion")
    }
}