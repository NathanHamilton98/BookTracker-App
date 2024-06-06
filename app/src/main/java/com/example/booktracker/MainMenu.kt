package com.example.booktracker


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        val listButton = findViewById<Button>(R.id.btnList)
        listButton.setOnClickListener {
            val intent = Intent(this@MainMenu, BookList::class.java)
            startActivity(intent)
        }
        val addBookButton = findViewById<Button>(R.id.btnAddBookPage)
        addBookButton.setOnClickListener {
            val intent = Intent(this@MainMenu, AddBook::class.java)
            startActivity(intent)
        }
        val settingsButton = findViewById<Button>(R.id.btnSettings)
        settingsButton.setOnClickListener {
            val intent = Intent(this@MainMenu, Settings::class.java)
            startActivity(intent)
        }
        val aboutPageButton = findViewById<Button>(R.id.btnAboutPage)
        aboutPageButton.setOnClickListener {
            val intent = Intent(this@MainMenu, AboutPage::class.java)
            startActivity(intent)
        }

        val logoutButton = findViewById<Button>(R.id.logoutBtn)
        logoutButton.setOnClickListener {
            val intent = Intent(this@MainMenu, MainActivity::class.java)
            startActivity(intent)
        }
    }
}