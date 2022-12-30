package com.example.thriftstoresapp

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.IOException

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

        val auth = Firebase.auth


        val db = Firebase.firestore

        val saveButton = findViewById<Button>(R.id.saveButton)

        saveButton.setOnClickListener {
            if(nameEdit.text.isNotEmpty() && cityEdit.text.isNotEmpty()) {
                val title = nameEdit.text.toString()
                val address = "${streetEdit.text}\n" +
                        "${postalCodeEdit.text}\n" +
                        "${cityEdit.text}"
                val description = descriptionEdit.text.toString()
                val rating = ratingBar.rating
                val coordinates = getCoordinatesFromAddress(address)

                val place = PlaceItem(
                    title = title,
                    address = address,
                    description = description,
                    rating = rating,
                    image = R.drawable.ic_baseline_panorama_wide_angle_24,
                    latitude = coordinates?.get(0),
                    longitude = coordinates?.get(1),
                    userUID = auth.currentUser?.uid.toString()

                )

                db.collection("places").add(place).addOnSuccessListener {
                    Toast.makeText(this, "Place saved", Toast.LENGTH_SHORT).show()
                    goToPlaces()
                }
            } else {
                Toast.makeText(this, "You must enter a title and an address.", Toast.LENGTH_SHORT).show()
            }

        }

        val cancelButton = findViewById<Button>(R.id.cancelButton)

        cancelButton.setOnClickListener {
            goToPlaces()
        }
    }

    private fun goToPlaces() {
        val intent = Intent(this, PlacesRecyclerView::class.java)
        startActivity(intent)
    }

    private fun getCoordinatesFromAddress(currentAddress: String?): List<Double>? {
        val geocoder = Geocoder(this)
        val addresses : List<Address>?

        val lat: Double
        val long: Double

        try {
            addresses = geocoder.getFromLocationName(currentAddress, 3)
            if(addresses != null) {
                val address = addresses[0]
                lat = address.latitude
                long = address.longitude
                Log.d("!!!!", "Lat: $lat - Long: $long")
                return listOf(lat, long)
            } else {
                return null
            }
        } catch(e: IOException) {
            e.printStackTrace()
        }
        Log.d("!!!!", "$currentAddress")
        return null
    }
}