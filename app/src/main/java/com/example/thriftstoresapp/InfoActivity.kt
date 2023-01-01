package com.example.thriftstoresapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.isVisible


class InfoActivity : AppCompatActivity() {

    private lateinit var storeImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var pricingTextView: TextView
    private lateinit var categoriesTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var latLongTextview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        storeImageView = findViewById(R.id.infoImageView)
        titleTextView = findViewById(R.id.infoTitleTextView)
        addressTextView = findViewById(R.id.infoAdressTextView)
        ratingBar = findViewById(R.id.infoRatingBar)
        pricingTextView = findViewById(R.id.infoPricingTextView)
        categoriesTextView = findViewById(R.id.infoCategoriesTextView)
        descriptionTextView = findViewById(R.id.infoDescriptionTextView)
        latLongTextview = findViewById(R.id.infoLatLongTextView)


        titleTextView.text = LocalData.currentPlace?.title
        addressTextView.text = LocalData.currentPlace?.address
        ratingBar.rating = LocalData.currentPlace?.rating!!
        descriptionTextView.text = LocalData.currentPlace?.description

        latLongTextview.text = "lat: ${LocalData.currentPlace?.latitude}, lng: ${LocalData.currentPlace?.longitude}"

        if(LocalData.currentPlace!!.latitude != null && LocalData.currentPlace!!.longitude != null) {
            val mapButton = findViewById<Button>(R.id.infoMapButton)
            mapButton.isVisible = true

            mapButton.setOnClickListener {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                finish()
            }
        }


        val backButton = findViewById<Button>(R.id.infoBackButton)
        backButton.setOnClickListener{
            val intent = Intent(this, PlacesRecyclerView::class.java)
            startActivity(intent)
            finish()
        }



    }
}