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

class AddPlace : AppCompatActivity() {

    /*
    Declaring variables for the Views.
     */
    lateinit var nameEdit: EditText
    lateinit var streetEdit: EditText
    lateinit var postalCodeEdit: EditText
    lateinit var cityEdit: EditText
    lateinit var descriptionEdit: EditText
    lateinit var ratingBar: RatingBar

    /*
    Declaring variables for login and database.
     */
    private val auth = Firebase.auth
    private val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_place)

        /*
        Some lateinit:ing (it IS a word!)
         */
        nameEdit = findViewById(R.id.nameEditText)
        streetEdit = findViewById(R.id.streetEditText)
        postalCodeEdit = findViewById(R.id.postalCodeEditText)
        cityEdit = findViewById(R.id.cityEditText)
        descriptionEdit = findViewById(R.id.descriptionEditText)
        ratingBar = findViewById(R.id.ratingBarEdit)


        /*
        Setting up save-button
         */
        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            /*
            Making sure we at least have a name and an address. If not, have a Toast.
             */
            if(nameEdit.text.isNotEmpty() && cityEdit.text.isNotEmpty()) {
                createPlace()
            } else {
                Toast.makeText(this, "You must enter a title and an address.", Toast.LENGTH_SHORT).show()
            }

        }

        /*
        Setting up cancel-button, for those who had a change of heart.
         */
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        cancelButton.setOnClickListener {
            goToPlaces()
        }
    }

    /*
    Starts PlacesRecyclerViewActivity.
     */
    private fun goToPlaces() {
        val intent = Intent(this, PlacesRecyclerViewActivity::class.java)
        startActivity(intent)
        finish()
    }

    /*
    Calls getCoordinatesFromAddress, creates a PlaceItem from entered data, and tries to save it to
    Firebase.firestore. The Toast comes with info whether the operation was successful or not.
     */
    private fun createPlace() {
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

        db.collection("places").add(place).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                Toast.makeText(this, "Place created", Toast.LENGTH_SHORT).show()
                Log.d("!!!!", "createPlace successful")
                goToPlaces()
            } else {
                Toast.makeText(this, "Place not created ${task.exception}",
                    Toast.LENGTH_SHORT).show()
                Log.d("!!!!", "Place not created ${task.exception}")
            }
        }
    }


    /*
    It seems like this only works on android api 26 or earlier. I should find a real solution!
    Or perhaps it's the emulator acting up again.

    Anyway, it tries to get the coordinates of currentAddress by using Geocoder.
    If it succeeds, it returns the values as a List of Doubles. If not, null is returned.
     */
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
                Toast.makeText(this, "No address found",
                    Toast.LENGTH_SHORT).show()
                Log.d("!!!!", "No address")
                return null
            }
        } catch(e: Exception) {
            Toast.makeText(this, "Geocoder error: $e", Toast.LENGTH_SHORT).show()
            Log.d("!!!!", e.toString())
            e.printStackTrace()
        }
        Log.d("!!!!", "How did we end up here? $currentAddress")
        return null
    }
}