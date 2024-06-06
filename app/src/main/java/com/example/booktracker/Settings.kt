package com.example.booktracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val dbHandler = DBHandler(this)

        val usernameField = findViewById<EditText>(R.id.txtUsernameRequired)
        val currentPasswordField = findViewById<EditText>(R.id.txtCurrentPassword)
        val newPasswordField = findViewById<EditText>(R.id.txtNewPassword)
        val backButton = findViewById<Button>(R.id.btnBacktoMainfromSettings)
        val changePasswordButton = findViewById<Button>(R.id.btnChangePassword)

        backButton.setOnClickListener {
            val intent = Intent(this@Settings, MainMenu::class.java)
            startActivity(intent)
            finish()
        }

        changePasswordButton.setOnClickListener {
            val username = usernameField.text.toString()
            val currentPassword = currentPasswordField.text.toString()
            val newPassword = newPasswordField.text.toString()

            if (username.isEmpty() || currentPassword.isEmpty() || newPassword.isEmpty() ||
                newPassword.length < 6 || !newPassword.any { it.isDigit() } ||
                !newPassword.any { it.isUpperCase() }) {
                Toast.makeText(this, "New password must be at least 6 characters long, contain one digit, and one uppercase letter", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (dbHandler.authenticateUser(username, currentPassword)) {
                if (dbHandler.updateUserPassword(username, newPassword)) {
                    Toast.makeText(this, "Password updated successfully", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Failed to update password", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_LONG).show()
            }
        }
    }
}



