package com.example.individualproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import com.example.individualproject.models.HomeListAdapter
import com.example.individualproject.models.PostModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HelpRequestActivity : AppCompatActivity() {

    private val postList =  ArrayList<PostModel?>()
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var myListAdapter: HomeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_request)


        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().reference

        val createButton: ImageView = this.findViewById(R.id.home_add_post)

        createButton.setOnClickListener {
            val intent = Intent(this, AddPostActivity::class.java)
            intent.putExtra("type", "Help Request")
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        setupAdapter()
        displayLeaderboard()

    }


private fun setupAdapter() {
    myListAdapter = HomeListAdapter(this, postList)
    val list = this.findViewById<ListView>(R.id.help_request_list)
    list.adapter= myListAdapter

    list.onItemClickListener =
        AdapterView.OnItemClickListener { parent, view, position, id ->

            val post : PostModel? = parent.getItemAtPosition(position) as PostModel?
            val intent = Intent(this, PlantedTreesActivity::class.java)
            intent.putExtra("title", post!!.title)
            intent.putExtra("type", post!!.type)
            intent.putExtra("description", post!!.description)
            intent.putExtra("date", post!!.date)
            intent.putExtra("creator", post!!.creator)
            startActivity(intent)
        }

}
private fun displayLeaderboard() {
    val posts: ArrayList<PostModel> = ArrayList()
    postList.add(PostModel("Help Request","test","","test","test"))
    database.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val iterator: Iterator<*> = snapshot.child("Posts").children.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next() as DataSnapshot
                val type = next.child("type").value
                val title = next.child("title").value
                val date = next.child("date").value
                val description = next.child("description").value
                val creator = next.child("creator").value
if(type.toString() == "Help Request") {
    posts.add(
        PostModel(
            type.toString(),
            title.toString(),
            date.toString(),
            description.toString(),
            creator.toString()
        )
    )
}
            }
            postList.clear()
            postList.addAll(posts)
            myListAdapter.notifyDataSetChanged()
        }
        override fun onCancelled(error: DatabaseError) {}
    })
}
}