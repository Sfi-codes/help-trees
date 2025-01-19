package com.example.individualproject.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.individualproject.AddPostActivity
import com.example.individualproject.HelpRequestActivity
import com.example.individualproject.LearnActivity
import com.example.individualproject.MainActivity
import com.example.individualproject.PlantedTreesActivity
import com.example.individualproject.TreeActivity
import com.example.individualproject.databinding.FragmentHomeBinding
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

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val postList =  ArrayList<PostModel?>()
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var myListAdapter: HomeListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().reference
setupButtons()

        setupAdapter()
        displayLeaderboard()

        return root
    }

    private fun setupButtons() {
        val createButton: ImageView = binding.homeAddPost
        val learnButton: ImageView = binding.homeBook
        val getStartedOnCoordinationButton = binding.homeGetStartedCoordination
        val getStartedOnLearningButton = binding.homeGetStartedLearn

        createButton.setOnClickListener {
            val intent = Intent(context, AddPostActivity::class.java)
            intent.putExtra("type", "Social")
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        learnButton.setOnClickListener {
            val intent = Intent(context, LearnActivity::class.java)
            startActivity(intent)
        }

        getStartedOnCoordinationButton.setOnClickListener {
            val intent = Intent(context, HelpRequestActivity::class.java)
            startActivity(intent)
        }
        getStartedOnLearningButton.setOnClickListener {
            val intent = Intent(context, LearnActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupAdapter() {
        val context = context as MainActivity
        myListAdapter = HomeListAdapter(context, postList)
        binding.homeNewsfeed.adapter = myListAdapter

                binding.homeNewsfeed.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                val post : PostModel? = parent.getItemAtPosition(position) as PostModel?
                val intent = Intent(context, PlantedTreesActivity::class.java)
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
//        postList.add(PostModel("Social","test","","test","test"))
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val iterator: Iterator<*> = snapshot.child("Posts").children.iterator()
                while (iterator.hasNext()){
                    val next = iterator.next() as DataSnapshot
                    val type = next.child("type").value
                    val title = next.child("title").value
                    val date = next.child("date").value
                    val description = next.child("description").value
                    val creator = next.child("creator").value
                    posts.add(PostModel(type.toString(),title.toString(),date.toString(),description.toString(),creator.toString()))
                }

                postList.clear()
                postList.addAll(posts)
                myListAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}