package com.example.booktracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView

class BookListAdapter(
    context: Context,
    private val books: List<Book>,
    private val onEditClick: (Book) -> Unit,
    private val onDeleteClick: (Book) -> Unit
) : ArrayAdapter<Book>(context, 0, books) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val book = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.lsit_item_book, parent, false)

        val titleTextView = view.findViewById<TextView>(R.id.bookTitle)
        val authorTextView = view.findViewById<TextView>(R.id.bookAuthor)
        val editButton = view.findViewById<Button>(R.id.btnEditBook)
        val deleteButton = view.findViewById<Button>(R.id.btnDeleteBook)

        titleTextView.text = book?.title
        authorTextView.text = book?.author

        editButton.setOnClickListener {
            book?.let { onEditClick(it) }
        }

        deleteButton.setOnClickListener {
            book?.let { onDeleteClick(it) }
        }

        return view
    }
}


