package com.example.thriftstoresapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddPlace : AppCompatActivity() {

    lateinit var nameEdit: EditText
    lateinit var streetEdit: EditText
    lateinit var postalCodeEdit: EditText
    lateinit var cityEdit: EditText
    lateinit var descriptionEdit: EditText
    lateinit var ratingBar: RatingBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_place)

        nameEdit = findViewById(R.id.nameEditText)
        streetEdit = findViewById(R.id.streetEditText)
        postalCodeEdit = findViewById(R.id.postalCodeEditText)
        cityEdit = findViewById(R.id.cityEditText)
        descriptionEdit = findViewById(R.id.descriptionEditText)
        ratingBar = findViewById(R.id.ratingBarEdit)

        val db = Firebase.firestore

        val saveButton = findViewById<Button>(R.id.saveButton)

        saveButton.setOnClickListener {
            if(!nameEdit.text.isEmpty() && !cityEdit.text.isEmpty()) {
                val place = PlaceItem(
                    title = nameEdit.text.toString(),
                address = "${streetEdit.text}\n" +
                        "${postalCodeEdit.text}\n" +
                        "${cityEdit.text}",
                description = descriptionEdit.text.toString(),
                rating = ratingBar.rating,
                )
                db.collection("places").add(place).addOnSuccessListener {
                    Toast.makeText(this, "Place saved", Toast.LENGTH_SHORT).show()
                    goToPlaces()
                }
            } else {
                Toast.makeText(this, "You must enter a title and an adress.", Toast.LENGTH_SHORT).show()
            }

        }

        val cancelButton = findViewById<Button>(R.id.cancelButton)

        cancelButton.setOnClickListener {
            goToPlaces()
        }
    }

    fun goToPlaces() {
        val intent = Intent(this, PlacesRecyclerView::class.java)
        startActivity(intent)
    }
}