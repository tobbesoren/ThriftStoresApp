package com.example.thriftstoresapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PlacesRecyclerView : AppCompatActivity() {

    val places = mutableListOf<PlaceItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_recycler_view)

        val db = Firebase.firestore

        val recyclerView = findViewById<RecyclerView>(R.id.placesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

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
        db.collection("places").get().addOnSuccessListener { documentSnapShot ->
            for (document in documentSnapShot.documents) {

               val item = document.toObject<PlaceItem>()
               if(item != null) {
                   Log.d("!!!!", "Ohoj")
                   places.add(item)
                   Log.d("!!!!", "${item.id}")
               }
           }
            //Lets the Adapter handle the places.
            val adapter = PlacesRecyclerViewAdapter(this, places)
            recyclerView.adapter = adapter

       }


        val addButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        addButton.setOnClickListener {
            val intent = Intent(this, AddPlace::class.java)
            startActivity(intent)
        }
    }
}