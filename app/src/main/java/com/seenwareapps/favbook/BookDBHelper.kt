package com.seenwareapps.favbook

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.seenwareapps.favbook.models.Book

class BookDBHelper(
    context: Context?,
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER) {

    companion object {
        private const val DATABASE_NAME = "DB_FAV_BOOK.db"
        private const val DATABASE_VER = 1
        private const val TABLE_NAME = "BOOK_TAB"
        private const val COLUMN_ID = "Id"
        private const val COLUMN_BOOK_NAME = "bookName"
        private const val COLUMN_BOOK_AUTHOR = "bookAuthor"
        private const val COLUMN_BOOK_PAGE = "bookPage"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_BOOK_NAME + " TEXT,"
                + COLUMN_BOOK_AUTHOR + " TEXT,"
                + COLUMN_BOOK_PAGE + " TEXT" + ")")

        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


    fun addBook(bookName: String, bookAuthor: String, bookPage: String){

        // below we are creating
        // a content values variable
        val values = ContentValues()

        // we are inserting our values
        // in the form of key-value pair
        values.put(COLUMN_BOOK_NAME, bookName)
        values.put(COLUMN_BOOK_AUTHOR, bookAuthor)
        values.put(COLUMN_BOOK_PAGE, bookPage)

        // here we are creating a
        // writable variable of
        // our database as we want to
        // insert value in our database
        val db = this.writableDatabase

        // all values are inserted into database
        db.insert(TABLE_NAME, null, values)

        // at last we are
        // closing our database
        db.close()
    }

    @SuppressLint("Range")
    fun getAllBooks(): List<Book> {
        val columns = arrayOf(COLUMN_ID, COLUMN_BOOK_NAME, COLUMN_BOOK_AUTHOR, COLUMN_BOOK_PAGE)
        val books = mutableListOf<Book>()

        // Get a readable instance of the database
        val db = this.readableDatabase

        // Perform the query on the database
        val cursor = db.query(TABLE_NAME, columns, null, null, null, null, null)

        // Iterate through the cursor and add Notes to the list
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            val bookName = cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_NAME))
            val bookAuthor = cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_AUTHOR))
            val bookPage = cursor.getString(cursor.getColumnIndex(COLUMN_BOOK_PAGE))

            books.add(Book(id, bookName, bookAuthor, bookPage))
        }

        // Close the cursor and database
        cursor.close()
        db.close()

        return books
    }

    fun deleteBook(noteId: Int): Boolean {
        // Get a writable instance of the database
        val db = this.writableDatabase

        // Define the WHERE clause to specify which note to delete
        val whereClause = "$COLUMN_ID = ?"

        // Define the values for the WHERE clause
        val whereArgs = arrayOf(noteId.toString())

        // Attempt to delete the note
        val deletedRows = db.delete(TABLE_NAME, whereClause, whereArgs)

        // Close the database
        db.close()

        // Return true if at least one row was affected (note deleted), otherwise false
        return deletedRows > 0
    }

    fun updateBook(id:Int, bookName: String, bookAuthor: String, bookPage: String): Boolean {
        // Get a writable instance of the database
        val db = this.writableDatabase

        // Create a ContentValues object to store the new values
        val values = ContentValues()
        values.put(COLUMN_BOOK_NAME, bookName)
        values.put(COLUMN_BOOK_AUTHOR, bookAuthor)
        values.put(COLUMN_BOOK_PAGE, bookPage)

        // Define the WHERE clause to specify which note to update
        val whereClause = "$COLUMN_ID = ?"

        // Define the values for the WHERE clause
        val whereArgs = arrayOf(id.toString())

        // Attempt to update the note
        val updatedRows = db.update(TABLE_NAME, values, whereClause, whereArgs)

        // Close the database
        db.close()

        // Return true if at least one row was affected (note updated), otherwise false
        return updatedRows > 0
    }

}