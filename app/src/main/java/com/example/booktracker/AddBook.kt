package com.example.booktracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddBook : AppCompatActivity() {
    private lateinit var dbHandler: DBHandler
    private var editingBookId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        dbHandler = DBHandler(this)

        val titleField = findViewById<EditText>(R.id.txtTitleInput)
        val authorField = findViewById<EditText>(R.id.txtAuthorInput)
        val dateField = findViewById<EditText>(R.id.pubYearInput)
        val seriesField = findViewById<EditText>(R.id.txtSeriesInput)
        val addButton = findViewById<Button>(R.id.btnAddBook)
        val editButton = findViewById<Button>(R.id.btnEditBook)
        val deleteButton = findViewById<Button>(R.id.btnDeleteBook)
        val backButton = findViewById<Button>(R.id.btnBacktoMainFromAdd)

        val bookId = intent.getIntExtra("BOOK_ID", -1)
        if (bookId != -1) {
            val book = dbHandler.getBook(bookId)
            if (book != null) {
                editingBookId = book.id
                titleField.setText(book.title)
                authorField.setText(book.author)
                dateField.setText(book.year)
                seriesField.setText(book.seriesName)
            }
        }

        addButton.setOnClickListener {
            val title = titleField.text.toString()
            val author = authorField.text.toString()
            val date = dateField.text.toString()
            val series = seriesField.text.toString()

            if (title.isEmpty() || author.isEmpty() || date.isEmpty() || series.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            dbHandler.addNewBook(title, author, date, series)
            Toast.makeText(this, "Book added: $title by $author", Toast.LENGTH_SHORT).show()

            // Clear fields after adding the book
            titleField.text.clear()
            authorField.text.clear()
            dateField.text.clear()
            seriesField.text.clear()
        }

        editButton.setOnClickListener {
            val id = editingBookId
            val title = titleField.text.toString()
            val author = authorField.text.toString()
            val date = dateField.text.toString()
            val series = seriesField.text.toString()

            if (id == null) {
                Toast.makeText(this, "No book selected to edit", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (title.isEmpty() || author.isEmpty() || date.isEmpty() || series.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            dbHandler.updateBook(id, title, author, date, series)
            Toast.makeText(this, "Book updated: $title by $author", Toast.LENGTH_SHORT).show()

            // Clear fields after editing the book
            titleField.text.clear()
            authorField.text.clear()
            dateField.text.clear()
            seriesField.text.clear()
            editingBookId = null
        }

        deleteButton.setOnClickListener {
            val id = editingBookId

            if (id == null) {
                Toast.makeText(this, "No book selected to delete", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            dbHandler.deleteBook(id)
            Toast.makeText(this, "Book deleted", Toast.LENGTH_SHORT).show()

            // Clear fields after deleting the book
            titleField.text.clear()
            authorField.text.clear()
            dateField.text.clear()
            seriesField.text.clear()
            editingBookId = null
        }

        backButton.setOnClickListener {
            val intent = Intent(this@AddBook, MainMenu::class.java)
            startActivity(intent)
            finish()
        }
    }
}



