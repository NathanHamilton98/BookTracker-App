package com.example.booktracker

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteOpenHelper

class DBHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "bookkeepingdb"
        private const val DB_VERSION = 2 // Incremented the version number
        private const val TABLE_BOOKS = "books"
        private const val TABLE_USERS = "users"
        const val ID_COL = "id"
        const val TITLE_COL = "title"
        const val AUTHOR_COL = "author"
        const val DATE_COL = "date"
        const val SERIES_COL = "series"
        const val USERNAME_COL = "username"
        const val PASSWORD_COL = "password"
        private const val DATABASE_PASSWORD = "CIS480" // Replace with a strong password
    }

    init {
        SQLiteDatabase.loadLibs(context)
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createBooksTable = ("CREATE TABLE $TABLE_BOOKS ("
                + "$ID_COL INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$TITLE_COL TEXT,"
                + "$AUTHOR_COL TEXT,"
                + "$DATE_COL TEXT,"
                + "$SERIES_COL TEXT)")
        db.execSQL(createBooksTable)

        val createUsersTable = ("CREATE TABLE $TABLE_USERS ("
                + "$ID_COL INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$USERNAME_COL TEXT,"
                + "$PASSWORD_COL TEXT)")
        db.execSQL(createUsersTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun getWritableDatabase(): SQLiteDatabase {
        return super.getWritableDatabase(DATABASE_PASSWORD.toCharArray())
    }

    fun getReadableDatabase(): SQLiteDatabase {
        return super.getReadableDatabase(DATABASE_PASSWORD.toCharArray())
    }

    fun addNewBook(title: String, author: String, date: String, series: String) {
        val db = getWritableDatabase()
        val values = ContentValues().apply {
            put(TITLE_COL, title)
            put(AUTHOR_COL, author)
            put(DATE_COL, date)
            put(SERIES_COL, series)
        }
        db.insert(TABLE_BOOKS, null, values)
        db.close()
    }

    fun deleteBook(id: Int) {
        val db = getWritableDatabase()
        db.delete(TABLE_BOOKS, "$ID_COL=?", arrayOf(id.toString()))
        db.close()
    }

    fun updateBook(id: Int, title: String, author: String, date: String, series: String) {
        val db = getWritableDatabase()
        val values = ContentValues().apply {
            put(TITLE_COL, title)
            put(AUTHOR_COL, author)
            put(DATE_COL, date)
            put(SERIES_COL, series)
        }
        db.update(TABLE_BOOKS, values, "$ID_COL=?", arrayOf(id.toString()))
        db.close()
    }

    fun getAllBooks(): Cursor {
        val db = getReadableDatabase()
        return db.query(TABLE_BOOKS, null, null, null, null, null, null)
    }

    fun searchBooks(query: String): Cursor {
        val db = getReadableDatabase()
        return db.query(
            TABLE_BOOKS,
            null,
            "$TITLE_COL LIKE ? OR $AUTHOR_COL LIKE ?",
            arrayOf("%$query%", "%$query%"),
            null, null, null
        )
    }

    fun getBook(id: Int): Book? {
        val db = getReadableDatabase()
        val cursor = db.query(
            TABLE_BOOKS,
            null,
            "$ID_COL=?",
            arrayOf(id.toString()),
            null, null, null
        )
        return if (cursor.moveToFirst()) {
            val bookId = cursor.getInt(cursor.getColumnIndexOrThrow(ID_COL))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE_COL))
            val author = cursor.getString(cursor.getColumnIndexOrThrow(AUTHOR_COL))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(DATE_COL))
            val series = cursor.getString(cursor.getColumnIndexOrThrow(SERIES_COL))
            cursor.close()
            Book(bookId, title, author, date, series)
        } else {
            cursor.close()
            null
        }
    }

    fun authenticateUser(username: String, password: String): Boolean {
        val db = getReadableDatabase()
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(ID_COL),
            "$USERNAME_COL=? AND $PASSWORD_COL=?",
            arrayOf(username, password),
            null, null, null
        )
        val isAuthenticated = cursor.count > 0
        cursor.close()
        return isAuthenticated
    }

    fun addUser(username: String, password: String): Boolean {
        val db = getWritableDatabase()
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(ID_COL),
            "$USERNAME_COL=?",
            arrayOf(username),
            null, null, null
        )
        if (cursor.count > 0) {
            cursor.close()
            return false
        }
        cursor.close()
        val values = ContentValues().apply {
            put(USERNAME_COL, username)
            put(PASSWORD_COL, password)
        }
        db.insert(TABLE_USERS, null, values)
        db.close()
        return true
    }

    fun updateUserPassword(username: String, newPassword: String): Boolean {
        val db = getWritableDatabase()
        val values = ContentValues().apply {
            put(PASSWORD_COL, newPassword)
        }
        val rowsUpdated = db.update(TABLE_USERS, values, "$USERNAME_COL=?", arrayOf(username))
        db.close()
        return rowsUpdated > 0
    }
}



