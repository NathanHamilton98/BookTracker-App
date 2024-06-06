package com.example.booktracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val usernameField = findViewById<EditText>(R.id.txtUsername)
        val passwordField = findViewById<EditText>(R.id.txtPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val createButton = findViewById<Button>(R.id.btnCreateAccount)

        loginButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in both username and password fields", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val dbHandler = DBHandler(this)
            try {
                if (dbHandler.authenticateUser(username, password)) {
                    val intent = Intent(this@MainActivity, MainMenu::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Username or Password is incorrect", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error during authentication", e)
                Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG).show()
            }
        }

        createButton.setOnClickListener {
            val attemptedUsername = usernameField.text.toString()
            val attemptedPassword = passwordField.text.toString()

            if (attemptedUsername.isEmpty() || attemptedPassword.isEmpty() ||
                attemptedPassword.length < 6 || !attemptedPassword.any { it.isDigit() } ||
                !attemptedPassword.any { it.isUpperCase() }) {
                Toast.makeText(this, "Password must be at least 6 characters long, contain one digit, and one uppercase letter", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val dbHandler = DBHandler(this)
            try {
                if (dbHandler.addUser(attemptedUsername, attemptedPassword)) {
                    Toast.makeText(this, "Account has been created, you can now log in", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Username already exists", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error during account creation", e)
                Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG).show()
            }
        }
    }
}
