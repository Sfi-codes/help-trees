package com.example.individualproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.individualproject.models.PostModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar

class AddPostActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        // Get reference of widgets from XML layout

        val enterTitleField = findViewById<View>(R.id.add_post_title) as EditText
        val enterDescriptionField = findViewById<View>(R.id.add_post_description) as EditText
        val enterTypeField = "findViewById<View>(R.id.add_post_description) as EditText"
        val addPostButton = findViewById<View>(R.id.add_post_submit_button) as Button

        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().reference


        addPostButton.setOnClickListener {
            val title = enterTitleField.text.toString()
            val description = enterDescriptionField.text.toString()
            val type = intent.extras?.getString("type").toString()

            val time = Calendar.getInstance().time
            val formatter = SimpleDateFormat("dd-MM-yyyy")
            val date = formatter.format(time)

            val creator = auth.currentUser!!.uid

            createPostInDatabase(title, description, type, date,creator)
        }


    }

    private fun createPostInDatabase(title: String, description: String, type: String, date: String, creator:String) {
//create post
        val post = PostModel(type, title, date, description, creator)
        database.child("Posts").child(title).setValue(post)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Post has been created", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
    }
}
