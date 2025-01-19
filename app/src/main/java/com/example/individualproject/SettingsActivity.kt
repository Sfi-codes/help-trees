package com.example.individualproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SettingsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var setNameButton: Button
    private lateinit var username: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        auth = Firebase.auth
        database = Firebase.database.reference

        setNameButton = findViewById(R.id.settings_button)
        username = findViewById(R.id.settings_username)


        setNameButton.setOnClickListener() {
         setUsername(username.text.toString())
        }

    }

    private fun setUsername(username :String) {
        val chosenUserName: HashMap<String, Any> = HashMap()

        if (username.isNotEmpty()) {
            chosenUserName.put("Users/${auth.currentUser!!.uid}/username", username)

            database.updateChildren(chosenUserName)

            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
        else{
            Toast.makeText(this, "Name must not be empty", Toast.LENGTH_SHORT).show()
        }
    }
}