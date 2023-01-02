package com.example.thriftstoresapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.isVisible
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class InfoActivity : AppCompatActivity() {

    /*
    Declare variables to hold the Views.
     */
    private lateinit var storeImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var pricingTextView: TextView
    private lateinit var categoriesTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var latLongTextview: TextView

    /*
    Declare and initialize variables for database and login.
     */
    private val db = Firebase.firestore
    private val currentUser = Firebase.auth.currentUser?.uid.toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        /*
        Some lateinits we promised to make.
         */
        storeImageView = findViewById(R.id.infoImageView)
        titleTextView = findViewById(R.id.infoTitleTextView)
        addressTextView = findViewById(R.id.infoAdressTextView)
        ratingBar = findViewById(R.id.infoRatingBar)
        pricingTextView = findViewById(R.id.infoPricingTextView)
        categoriesTextView = findViewById(R.id.infoOpeningHoursTextView)
        descriptionTextView = findViewById(R.id.infoDescriptionTextView)
        latLongTextview = findViewById(R.id.infoLatLongTextView)


        /*
        Getting the data from the currentPlace to the Views.
         */
        titleTextView.text = LocalData.currentPlace?.title
        addressTextView.text = LocalData.currentPlace?.address
        ratingBar.rating = LocalData.currentPlace?.rating!!
        descriptionTextView.text = LocalData.currentPlace?.description
        latLongTextview.text = "lat: ${LocalData.currentPlace?.latitude}, lng: ${LocalData.currentPlace?.longitude}"

        /*
        Checking if the currentPlace has latitude and longitude. If it does: sets up the map-button
        and makes it visible and clickable.
         */
        if(LocalData.currentPlace!!.latitude != null && LocalData.currentPlace!!.longitude != null) {
            val mapButton = findViewById<Button>(R.id.infoMapButton)
            mapButton.isVisible = true
            mapButton.setOnClickListener {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        /*
        Checks if the currentPlace was added to the database by the current usr. If it was: Sets up
        the delete-button and makes it clickable, allowing the user to delete the currentPlace from
        the database. Also starts PlacesRecyclerViewActivity.
         */
        if(LocalData.currentPlace!!.userUID == currentUser) {
            val deleteButton = findViewById<Button>(R.id.infoDeleteButton)
            deleteButton.isVisible = true

            deleteButton.setOnClickListener {
                val docToDelete = LocalData.currentPlace!!.id
                if(docToDelete != null) {
                    db.collection("places").document(docToDelete).delete().addOnSuccessListener {
                        LocalData.currentPlace = null //resets currentPlace
                        val intent = Intent(this, PlacesRecyclerViewActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }


        /*
        Sets up the back-button. Starts PlacesRecyclerViewActivity.
         */
        val backButton = findViewById<Button>(R.id.infoBackButton)
        backButton.setOnClickListener{
            val intent = Intent(this, PlacesRecyclerViewActivity::class.java)
            LocalData.currentPlace = null //resets currentPlace
            startActivity(intent)
            finish()
        }
    }
}