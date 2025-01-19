package com.example.individualproject

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.individualproject.models.PostModel
import com.example.individualproject.models.UserModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar


class TreeActivity : AppCompatActivity() {
    private lateinit var treeList: MutableList<String>
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tree)

        // Get reference of widgets from XML layout
        val listView = findViewById<View>(R.id.planted_ListViewID) as ListView
        val addItem = findViewById<View>(R.id.AddItemBtn) as Button
        val addTrees = findViewById<View>(R.id.plantTree) as Button

        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().reference

        val num: Int = 1000
        val type: String = "Oak trees"

        // Initializing a new String Array
        val trees = arrayOf(
            "$num $type"
        )
        // Create a List from String Array elements
         treeList = ArrayList(listOf(*trees))

        // Create an ArrayAdapter from List
         arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, treeList)

        // DataBind ListView with items from ArrayAdapter
        listView.adapter = arrayAdapter
        addItem.setOnClickListener { // Add new Items to List
            addTree()
    }
        addTrees.setOnClickListener{
            createTrees()
        }

        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
    removeTreeItem(position)
            }

    }

    private fun removeTreeItem(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure you want to remove this?")
        builder.setPositiveButton(
            "OK"
        ) { dialog, which ->
treeList.removeAt(position)
            arrayAdapter.notifyDataSetChanged()
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.cancel() }
        builder.show()

    }

    private fun addTree() {
        var treeType :String = ""
        val builder = AlertDialog.Builder(this)
        builder.setTitle("What did you plant?")

// Set up the input

        val input = EditText(this)
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
// Set up the buttons
        builder.setPositiveButton(
            "OK"
        ) { dialog, which ->
            treeType = input.text.toString()
            addTreeToList(treeType)}
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.cancel() }

        builder.show()

    }

    private fun addTreeToList(treeType: String) {
        var count :Int = 0
        val builder = AlertDialog.Builder(this)
        builder.setTitle("How much did you plant?")

// Set up the input

        val input = EditText(this)
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)
// Set up the buttons
        builder.setPositiveButton(
            "OK"
        ) { dialog, which ->
            count = input.text.toString().toInt()
            treeList.add("$count $treeType")
            /*
                    notifyDataSetChanged ()
                        Notifies the attached observers that the underlying
                        data has been changed and any View reflecting the
                        data set should refresh itself.
                 */
        arrayAdapter.notifyDataSetChanged()}
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.cancel() }

        builder.show()
    }

    private fun createTrees() {
        var total: Int = 0
        val tree: String = ""
        val count: Int = 0

        for(item in treeList){
            val splited: List<String> = item.split(" ")
            total += splited[0].toInt()
        }

        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        val current = formatter.format(time)

        val updates: MutableMap<String, Any> = hashMapOf(
            "Users/${auth.currentUser!!.uid}/planted" to ServerValue.increment(total.toDouble()),

        )
        database.child("Trees").child(current).setValue(total).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                database.updateChildren(updates)
                Toast.makeText(this, "$total trees have been planted", Toast.LENGTH_SHORT).show()
            }
        }

    }
}