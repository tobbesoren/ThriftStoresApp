package com.example.thriftstoresapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddPlace : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_place)

        val db = Firebase.firestore

        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            //TO DO: Save to firestore
        }
    }
}