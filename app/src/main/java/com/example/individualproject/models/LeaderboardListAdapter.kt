package com.example.individualproject.models

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.individualproject.R

class LeaderboardListAdapter(
    private val context: Activity,
    private val userList: ArrayList<UserModel?>,
    private  val rankings: ArrayList<Int>
)
: ArrayAdapter<UserModel>(context, R.layout.leaderboard_listview, userList) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.leaderboard_listview, null, true)

        val name = rowView.findViewById(R.id.leaderboard_username) as TextView
        name.text = userList[position]!!.username

        val planted = rowView.findViewById(R.id.leaderboard_planted) as TextView
        planted.text = userList[position]!!.planted.toString()

        val rank = rowView.findViewById(R.id.leaderboard_rank) as TextView
        rank.text = rankings[position].toString()

        return rowView
    }
}