package com.example.booktracker

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class BookList : AppCompatActivity() {
    private lateinit var dbHandler: DBHandler
    private lateinit var bookListAdapter: BookListAdapter
    private lateinit var books: MutableList<Book>

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)
        dbHandler = DBHandler(this)
        books = mutableListOf()

        val listView = findViewById<ListView>(R.id.listView)
        bookListAdapter = BookListAdapter(this, books, this::onEditClick, this::onDeleteClick)
        listView.adapter = bookListAdapter

        val backButton = findViewById<Button>(R.id.btnBacktoMainFromList)
        backButton.setOnClickListener {
            val intent = Intent(this@BookList, MainMenu::class.java)
            startActivity(intent)
            finish()
        }

        val searchField = findViewById<EditText>(R.id.userSearch)
        val searchButton = findViewById<Button>(R.id.btnSearch)
        searchButton.setOnClickListener {
            val query = searchField.text.toString()
            searchBooks(query)
        }

        loadBooks()
    }

    private fun loadBooks() {
        val cursor: Cursor = dbHandler.getAllBooks()
        books.clear()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHandler.ID_COL))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.TITLE_COL))
            val author = cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.AUTHOR_COL))
            val seriesName = cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.SERIES_COL))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.DATE_COL))
            books.add(Book(id, title, author, seriesName, date))
        }
        cursor.close()
        bookListAdapter.notifyDataSetChanged()
    }

    private fun searchBooks(query: String) {
        val cursor: Cursor = dbHandler.searchBooks(query)
        books.clear()
        if (cursor.count == 0) {
            Toast.makeText(this, "No books found", Toast.LENGTH_SHORT).show()
            return
        }
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHandler.ID_COL))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.TITLE_COL))
            val author = cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.AUTHOR_COL))
            val seriesName = cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.SERIES_COL))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.DATE_COL))
            books.add(Book(id, title, author, seriesName, date))
        }
        cursor.close()
        bookListAdapter.notifyDataSetChanged()
    }

    private fun onEditClick(book: Book) {
        val intent = Intent(this, AddBook::class.java)
        intent.putExtra("BOOK_ID", book.id)
        startActivity(intent)
    }

    private fun onDeleteClick(book: Book) {
        dbHandler.deleteBook(book.id)
        loadBooks()
        Toast.makeText(this, "Book deleted", Toast.LENGTH_SHORT).show()
    }
}


