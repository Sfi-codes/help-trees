package com.example.individualproject.models

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.individualproject.R

class HomeListAdapter(
    private val context: Activity,
    private val postList: ArrayList<PostModel?>
    )
        : ArrayAdapter<PostModel>(context, R.layout.home_listview, postList) {

        override fun getView(position: Int, view: View?, parent: ViewGroup): View {
            val inflater = context.layoutInflater
            val rowView = inflater.inflate(R.layout.home_listview, null, true)

            val type = rowView.findViewById(R.id.leaderboard_rank) as TextView
            type.text = postList[position]!!.type

            val title = rowView.findViewById(R.id.leaderboard_username) as TextView
            title.text = postList[position]!!.title

            val date = rowView.findViewById(R.id.home_post_date) as TextView
            date.text = postList[position]!!.date


            return rowView
        }
    }