package com.example.individualproject

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.individualproject.models.PostModel
import com.example.individualproject.models.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class PlantedTreesActivity : AppCompatActivity() {

    private lateinit var listview: ListView
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private val commentsList = ArrayList<String?>()
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var title: String
    private lateinit var description: String
    private lateinit var creator: String
    private lateinit var date: String
    private lateinit var type: String
    private lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planted_trees)
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().reference


        title = intent.extras?.getString("title").toString()
        date = intent.extras?.getString("date").toString()
        creator = intent.extras?.getString("creator").toString()
        description = intent.extras?.getString("description").toString()
        type = intent.extras?.getString("type").toString()
        button = findViewById(R.id.post_button)
        listview = findViewById(R.id.comment_list)

        setupAdapter()
        displayComments()

        if(type == "Social"){

            button.text = "Add comment"
            button.setOnClickListener(){
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Enter comment")
// Set up the input
                val input = EditText(this)
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.inputType = InputType.TYPE_CLASS_TEXT
                builder.setView(input)
// Set up the buttons
                builder.setPositiveButton(
                    "OK"
                ) { dialog, which ->
                    database.child("Posts").child(title).child("comments").child(input.text.toString()).setValue("")
                    arrayAdapter.notifyDataSetChanged()
                    val mIntent = intent
                    finish()
                    startActivity(mIntent)}
                builder.setNegativeButton(
                    "Cancel"
                ) { dialog, which -> dialog.cancel() }

                builder.show()

            }
        }
        if(type == "Help Request"){
            button.text = "Sign up"

            button.setOnClickListener(){
                val postListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // Get Post object and use the values to update the UI
                        val user = dataSnapshot.getValue<UserModel>()
                        val name = user!!.username
                        database.child("Posts").child(title).child("comments").child("$name has signed up").setValue("")
                        arrayAdapter.notifyDataSetChanged()
                        val mIntent = intent
                        finish()
                        startActivity(mIntent)
                    }


                    override fun onCancelled(databaseError: DatabaseError) {
                        // Getting Post failed, log a message
                        Log.w(TAG, "loadUsername:onCancelled", databaseError.toException())
                    }
                }
                database.child("Users").child(auth.currentUser!!.uid).addValueEventListener(postListener)
                }
        }

        displayPost(title, date, creator, description, type)
    }

    private fun displayPost(title: String, date: String, creator: String, description: String, type: String) {
        findViewById<TextView>(R.id.post_detailed_date).text = date
        findViewById<TextView>(R.id.post_detailed_description).text  = description
        findViewById<TextView>(R.id.post_detailed_title).text  = title

        if (type != "Notification") {
            database.child("Users").child(creator).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    findViewById<TextView>(R.id.post_detailed_owner).text = snapshot.child("username").value.toString()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    private fun setupAdapter() {
        arrayAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, commentsList)
        listview.adapter = arrayAdapter

    }
    private fun displayComments() {
        commentsList.add("test1")
        val comments: ArrayList<String> = ArrayList()
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val iterator: Iterator<*> = snapshot.child("Posts").child(title).child("comments").children.iterator()
                while (iterator.hasNext()){
                    val next = iterator.next() as DataSnapshot
                    comments.add(next.key.toString())
                }

                commentsList.clear()
                commentsList.addAll(comments)
                arrayAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}