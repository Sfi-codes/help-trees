package com.example.individualproject.ui.impact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.individualproject.models.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ImpactViewModel : ViewModel() {
    private var auth = Firebase.auth
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var userInfo = database.child("Users").child(auth.currentUser!!.uid)

    private val _text = MutableLiveData<String>().apply {
        var trees = "0"
        userInfo.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val info: UserModel? = snapshot.getValue(UserModel::class.java)
                trees = info!!.planted.toString()
                value = "$trees Trees Planted"
            }
            override fun onCancelled(error: DatabaseError) {}
        })


    }
    val text: LiveData<String> = _text
}