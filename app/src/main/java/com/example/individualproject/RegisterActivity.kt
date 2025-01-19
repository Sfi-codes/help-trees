package com.example.individualproject

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.individualproject.models.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class RegisterActivity : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var registerB: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        email = findViewById(R.id.email)
        registerB = findViewById(R.id.registerButton)
        password = findViewById(R.id.password)
        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        database = Firebase.database.reference
        // [END initialize_auth]

        registerB.setOnClickListener {
            createAccount(email.text.toString(), password.text.toString())
        }
    }

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    val userID  = auth.currentUser!!.uid
                    val user = UserModel("",0)

                    database.child("Users").child(userID).setValue(user)
                    database.child("Posts").child(userID).setValue(user.username)
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    val intent = Intent(this, LoginActivity::class.java)
                    // start your next activity
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "User is registered with that email already",
                        Toast.LENGTH_SHORT,
                    ).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    // start your next activity
                    startActivity(intent)
                }
            }
        // [END create_user_with_email]
    }
}