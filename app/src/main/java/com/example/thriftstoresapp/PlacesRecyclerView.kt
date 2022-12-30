package com.example.thriftstoresapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PlacesRecyclerView : AppCompatActivity() {
    
    private val places = mutableListOf<PlaceItem>()
    private val db = Firebase.firestore
    private val currentUser = Firebase.auth.currentUser?.uid.toString()

    private lateinit var recyclerView: RecyclerView


    var sort = "Sort by name"
    var selectedView = "View all thrift stores"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_recycler_view)



        recyclerView = findViewById(R.id.placesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        loadPlaces(selectedView)


        val logOutButton = findViewById<Button>(R.id.placesLogOutButton)
        logOutButton.setOnClickListener {
            DataBaseHandler.auth.signOut()
            goToLogIn()
        }


        val sortBy = resources.getStringArray(R.array.sortBy)
        val sortBySpinner = findViewById<Spinner>(R.id.sortBySpinner)
        val sortAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortBy)
        sortBySpinner.adapter = sortAdapter

        sortBySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                sort = sortBy[position]
                loadPlaces(selectedView)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        val viewSelections = resources.getStringArray(R.array.selectView)
        val selectViewSpinner = findViewById<Spinner>(R.id.selectViewSpinner)
        val viewAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, viewSelections)
        selectViewSpinner.adapter = viewAdapter

        selectViewSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedView = viewSelections[position]
                loadPlaces(selectedView)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        val addButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        addButton.setOnClickListener {
            goToAddPlace()
        }
    }

    private fun goToAddPlace() {
        val intent = Intent(this, AddPlace::class.java)
        startActivity(intent)
    }

    private fun goToLogIn() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun loadPlaces(selectedView: String) {
        when(selectedView) {
            "View all thrift stores" -> {
                db.collection("places").get().addOnSuccessListener { documentSnapShot ->
                makeList(documentSnapShot)
                    sortList()
                    setAdapter()
                }
            }
            "View my thrift stores" -> {
                db.collection("places").whereEqualTo("userUID", currentUser)
                    .get().addOnSuccessListener { documentSnapShot ->
                    makeList(documentSnapShot)
                        sortList()
                        setAdapter()
                }
            }
        }

    }

    private fun makeList(documentSnapShot: QuerySnapshot) {
        places.clear()
        for (document in documentSnapShot.documents) {
            val item = document.toObject<PlaceItem>()
            if (item != null) {
                Log.d("!!!!", item.toString())
                places.add(item)
                Log.d("!!!!", "${item.id}")
            }
        }
    }

    private fun sortList() {
        Toast.makeText(this, "Sorted by $sort", Toast.LENGTH_SHORT).show()
        when(sort) {
            "Sort by name" -> places.sortWith(compareBy { it.title })
            "Sort by rating" -> places.sortWith(compareByDescending { it.rating })
            "Sort by latest" -> places.sortWith(compareBy { it.created })
            "Sort by distance" -> places.sortWith(compareByDescending { it.distance })
        }

    }

    private fun setAdapter() {
        //Lets the Adapter handle the places.
        val adapter = PlacesRecyclerViewAdapter(this, places)
        recyclerView.adapter = adapter
    }
}