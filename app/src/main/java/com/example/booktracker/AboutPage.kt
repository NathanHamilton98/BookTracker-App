package com.example.booktracker
//Melvin White & Nathan Hamilton
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AboutPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_page)

        val backButton = findViewById<Button>(R.id.btnBackToMainFromAbout)
        backButton.setOnClickListener {
            val intent = Intent(this@AboutPage, MainMenu::class.java)
            startActivity(intent)
        }
    }
}