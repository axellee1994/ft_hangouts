package com.student.ft_hangouts

import android.content.ContentValues
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

    // Adding a contact
    // ID is place automatically from the database
    fun addContact(contact: Contact): Long {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(COL_FIRST_NAME, contact.firstName)
        values.put(COL_LAST_NAME, contact.lastName )
        values.put(COL_PHONE, contact.phoneNumber )
        values.put(COL_EMAIL, contact.email)
        values.put(COL_INFO, contact.info)

        return db.insert(TABLE_NAME, null, values)
    }

    // Fetch all contacts from the database
    fun getAllContacts(): List<Contact> {
        val contactList = mutableListOf<Contact>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID))
                val firstName = cursor.getString(cursor.getColumnIndexOrThrow(COL_FIRST_NAME))
                val lastName = cursor.getString(cursor.getColumnIndexOrThrow(COL_LAST_NAME))
                val phone = cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE))
                val email = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL))
                val info = cursor.getString(cursor.getColumnIndexOrThrow(COL_INFO))

                val contact = Contact(id, firstName, lastName, phone, email, info)
                contactList.add(contact)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return contactList
    }

    // Get a single contact by ID
    fun getContact(id: Long): Contact? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME, null, "$COL_ID=?", arrayOf(id.toString()),
            null, null, null
        )

        var contact: Contact? = null
        if (cursor.moveToFirst()) {
            val firstName = cursor.getString(cursor.getColumnIndexOrThrow(COL_FIRST_NAME))
            val lastName = cursor.getString(cursor.getColumnIndexOrThrow(COL_LAST_NAME))
            val phone = cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL))
            val info = cursor.getString(cursor.getColumnIndexOrThrow(COL_INFO))
            contact = Contact(id, firstName, lastName, phone, email, info)
        }
        cursor.close()
        return contact
    }

    // Update an existing contact
    fun updateContact(contact: Contact): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_FIRST_NAME, contact.firstName)
            put(COL_LAST_NAME, contact.lastName)
            put(COL_PHONE, contact.phoneNumber)
            put(COL_EMAIL, contact.email)
            put(COL_INFO, contact.info)
        }
        return db.update(TABLE_NAME, values, "$COL_ID=?", arrayOf(contact.id.toString()))
    }

    // Delete a contact
    fun deleteContact(id: Long): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "$COL_ID=?", arrayOf(id.toString()))
    }
}