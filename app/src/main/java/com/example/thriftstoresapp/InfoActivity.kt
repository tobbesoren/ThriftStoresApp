package com.example.thriftstoresapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class InfoActivity : AppCompatActivity() {

    lateinit var storeImageView: ImageView
    lateinit var titleTextView: TextView
    lateinit var addressTextView: TextView
    lateinit var ratingBar: RatingBar
    lateinit var pricingTextView: TextView
    lateinit var categoriesTextView: TextView
    lateinit var descriptionTextView: TextView
    lateinit var latLongTextview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val db = Firebase.firestore



        storeImageView = findViewById(R.id.infoImageView)
        titleTextView = findViewById(R.id.infoTitleTextView)
        addressTextView = findViewById(R.id.infoAdressTextView)
        ratingBar = findViewById(R.id.infoRatingBar)
        pricingTextView = findViewById(R.id.infoPricingTextView)
        categoriesTextView = findViewById(R.id.infoCategoriesTextView)
        descriptionTextView = findViewById(R.id.infoDescriptionTextView)
        latLongTextview = findViewById(R.id.infoLatLongTextView)

        val itemID = intent.getStringExtra("itemID").toString()
        Log.d("!!!!", "itemID: $itemID")
        val item = db.collection("places")
            .document(itemID)
        item.get().addOnSuccessListener { document ->
            val place = document.toObject<PlaceItem>()
            if (place != null) {
                titleTextView.text = place.title
                addressTextView.text = place.address
                ratingBar.rating = place.rating
                descriptionTextView.text = place.description
            } else {
                Toast.makeText(this, "Couldn't load data", Toast.LENGTH_SHORT).show()
            }

        }

        val backButton = findViewById<Button>(R.id.infoBackButton)
        backButton.setOnClickListener{
            val intent = Intent(this, PlacesRecyclerView::class.java)
            startActivity(intent)
            finish()
        }
        val mapButton = findViewById<Button>(R.id.infoMapButton)
        mapButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}