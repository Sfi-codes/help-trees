package com.example.individualproject.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.individualproject.MainActivity
import com.example.individualproject.databinding.FragmentDashboardBinding
import com.example.individualproject.models.LeaderboardListAdapter
import com.example.individualproject.models.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Collections


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val userList =  ArrayList<UserModel?>()
    private val rankings =  ArrayList<Int>()
    private lateinit var database:DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var myListAdapter: LeaderboardListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textDashboard
//        dashboardViewModel.text.observe(viewLifecycleOwner) {
////            textView.text = it
//        }
auth = Firebase.auth
        database = FirebaseDatabase.getInstance().reference

        setupAdapter()

displayLeaderboard()

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        displayLeaderboard()
//        listView.onItemClickListener =
//            AdapterView.OnItemClickListener { parent, view, position, id ->
//
//                val currentTree : String = parent.getItemAtPosition(position).toString()
//                val intent = Intent(context, PlantedTreesActivity::class.java)
//                intent.putExtra("date", currentTree)
//                startActivity(intent)
//            }
    }

    private fun setupAdapter() {
        val context = context as MainActivity
        myListAdapter = LeaderboardListAdapter(context, userList,rankings)
        binding.leaderboardList.adapter = myListAdapter
//        listView = context.findViewById(R.id.group_list) as ListView
//        arrayAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, groupList)
//        listView.adapter = arrayAdapter
    }
    private fun displayLeaderboard() {
//        userList.add(UserModel("holly",3))
        rankings.add(0)
        val users: ArrayList<UserModel> = ArrayList()
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val iterator: Iterator<*> = snapshot.child("Users").children.iterator()
                while (iterator.hasNext()){
                    val next = iterator.next() as DataSnapshot
                    val user = next.child("username").value
                    val planted = next.child("planted").value
                    users.add(UserModel(user.toString(),planted.toString().toInt()))
                }
                users.add(UserModel("donald",5))
                users.add(UserModel("lisa",20))
                users.add(UserModel("joe",600))
                users.add(UserModel("adam",3))
                users.add(UserModel("grace",3))
                users.add(UserModel("richard",3))
                users.add(UserModel("holly",3))
                Collections.sort(users, Comparator.comparingInt(UserModel::getPlanted))

                userList.clear()
                userList.addAll(users.reversed())

                for (i in 1..userList.size){
                    rankings.add(i)
                }
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