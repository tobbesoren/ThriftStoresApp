package com.example.thriftstoresapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PlacesRecyclerView : AppCompatActivity() {

    private val places = mutableListOf<PlaceItem>()
    private val db = Firebase.firestore
    lateinit var selectedView: String
    lateinit var recyclerView: RecyclerView
    private val currentUser = Firebase.auth.currentUser?.uid.toString()
    val sort = "Name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_recycler_view)

        selectedView = "AllPlaces"
        val sort = "Name"

        recyclerView = findViewById<RecyclerView>(R.id.placesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadPlaces(selectedView)

       /* db.collection("places").addSnapshotListener {snapshot, e ->
            Log.d("!!!!", "Snapshot taken")
            if(snapshot != null) {
                places.clear()
                for (document in snapshot.documents) {
                    Log.d("!!!!", "Snapshot taken")
                    val item = document.toObject<PlaceItem>()
                    if (item != null) {
                        places.add(item)
                    }
                }
            }
        }*/

        val selectButton = findViewById<Button>(R.id.selectButton)
        selectButton.text = "MyPlaces"
        selectButton.setOnClickListener {
            if(selectedView == "AllPlaces") {
                selectedView = "MyPlaces"
                selectButton.text = "AllPlaces"
            } else {
                selectedView = "AllPlaces"
                selectButton.text = "MyPlaces"
            }
            loadPlaces(selectedView)
        }



        val addButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        addButton.setOnClickListener {
            val intent = Intent(this, AddPlace::class.java)
            startActivity(intent)
        }
    }

    private fun loadPlaces(selectedView: String) {
        when(selectedView) {
            "AllPlaces" -> {
                db.collection("places").get().addOnSuccessListener { documentSnapShot ->
                makeList(documentSnapShot)
                    sortList(sort)
                    setAdapter()
                }
            }
            "MyPlaces" -> {
                db.collection("places").whereEqualTo("userUID", currentUser)
                    .get().addOnSuccessListener { documentSnapShot ->
                    makeList(documentSnapShot)
                        sortList(sort)
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
                Log.d("!!!!", "Ohoj")
                places.add(item)
                Log.d("!!!!", "${item.id}")
            }
        }
    }

    private fun sortList(sortBy: String) {
        when(sortBy) {
            "Name" -> places.sortWith(compareBy { it.title })
            "Rating" -> places.sortWith(compareBy { it.rating })
            //"Distance" -> places.sortWith(compareByDescending { it.distance })
        }

    }

    private fun setAdapter() {
        //Lets the Adapter handle the places.
        val adapter = PlacesRecyclerViewAdapter(this, places)
        recyclerView.adapter = adapter
    }
}