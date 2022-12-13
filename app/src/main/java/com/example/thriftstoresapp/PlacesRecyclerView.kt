package com.example.thriftstoresapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PlacesRecyclerView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_recycler_view)

        val places = mutableListOf<PlaceItem>(PlaceItem(
            "Röda korset",
            "Storgatan 2 \n444 55\nHorred",
        "Mycket grejor!",
        3.5f,
        R.drawable.ic_android_black_24dp),
            PlaceItem(
                "Erikshjälpen",
                "Storgatan 2 \n444 55\nHorred",
                "Mycket grejor!",
                3.5f,
                R.drawable.ic_android_black_24dp),
            PlaceItem(
                "Dyrt och använt",
                "Storgatan 2 \n444 55\nHorred",
                "Mycket grejor!",
                3.5f,
                R.drawable.ic_android_black_24dp),
            PlaceItem(
                "Pingstkyrkan",
                "Storgatan 2 \n444 55\nHorred",
                "Mycket grejor!",
                3.5f,
                R.drawable.ic_android_black_24dp))

        val addButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        addButton.setOnClickListener {
            val intent = Intent(this, AddPlace::class.java)
            startActivity(intent)
        }


        val recyclerView = findViewById<RecyclerView>(R.id.placesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Lets the Adapter handle the scores.
        val adapter = PlacesRecyclerViewAdapter(this, places)
        recyclerView.adapter = adapter

    }
}