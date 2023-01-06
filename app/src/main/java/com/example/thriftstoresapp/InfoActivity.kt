package com.example.thriftstoresapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class InfoActivity : AppCompatActivity() {

    //Declare variables to hold the Views.
    private lateinit var storeImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var pricingTextView: TextView
    private lateinit var openingHoursTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var latLongTextview: TextView


    //Declare and initialize variables for database and login.
    private val db = Firebase.firestore
    private val currentUser = Firebase.auth.currentUser?.uid.toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)


        //Some lateinits we promised to make.
        storeImageView = findViewById(R.id.infoImageView)
        titleTextView = findViewById(R.id.infoTitleTextView)
        addressTextView = findViewById(R.id.infoAdressTextView)
        ratingBar = findViewById(R.id.infoRatingBar)
        pricingTextView = findViewById(R.id.infoPricingTextView)
        openingHoursTextView = findViewById(R.id.infoOpeningHoursTextView)
        descriptionTextView = findViewById(R.id.infoDescriptionTextView)
        latLongTextview = findViewById(R.id.infoLatLongTextView)

        //Getting the data from the currentPlace to the Views.
        titleTextView.text = LocalData.currentPlace?.title
        addressTextView.text = LocalData.currentPlace?.address
        ratingBar.rating = LocalData.currentPlace?.rating!!
        pricingTextView.text = LocalData.currentPlace?.priceRange
        openingHoursTextView.text = LocalData.currentPlace?.openingHours
        descriptionTextView.text = LocalData.currentPlace?.description
        latLongTextview.text = getString(
            R.string.latLng,
            LocalData.currentPlace?.latitude.toString(),
            LocalData.currentPlace?.longitude.toString()
        )

        //Getting storage reference for image
        val storageReference = FirebaseStorage.getInstance()
            .getReference("images/${LocalData.currentPlace!!.imageFileName}")

        //Setting the image to storeImageView
        storageReference.downloadUrl.addOnSuccessListener { downloadUrl ->
            Glide.with(this)
                .load(downloadUrl)
                .into(storeImageView)
        }

        //This function checks if the PlaceItem has lat and lng: If so, it enables map button
        setUpMapButton()

        //This function checks if the currentUser is the one who created the place. If so, it
        //enables the delete button.
        setUpDeleteButton(storageReference)

        //Sets up the back-button. Starts PlacesRecyclerViewActivity.
        val backButton = findViewById<Button>(R.id.infoBackButton)
        backButton.setOnClickListener{
            LocalData.currentPlace = null //resets currentPlace
            goToPlaces()
        }
    }


    /*
    Checking if the currentPlace has latitude and longitude. If it does: sets up the map-button
    and makes it visible and clickable.
    */
    private fun setUpMapButton() {
        if (LocalData.currentPlace!!.latitude != null &&
            LocalData.currentPlace!!.longitude != null
        ) {
            val mapButton = findViewById<Button>(R.id.infoMapButton)
            mapButton.isVisible = true
            mapButton.setOnClickListener {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }


    /*
    Checks if the currentPlace was added to the database by the current user. If it was: Sets up
    the delete-button and makes it clickable, allowing the user to delete the currentPlace from
    the database, and the corresponding image by calling deletePlace and deleteImage.
    Also calls goToPlaces.
    */
    private fun setUpDeleteButton(storageReference: StorageReference) {
        if (LocalData.currentPlace!!.userUID == currentUser) {
            val deleteButton = findViewById<Button>(R.id.infoDeleteButton)
            deleteButton.isVisible = true
            deleteButton.setOnClickListener {
                val docToDelete = LocalData.currentPlace!!.id

                if (docToDelete != null) {
                    Toast.makeText(this, getString(R.string.deleting),
                        Toast.LENGTH_SHORT).show()
                    deletePlaceAndGoBack(docToDelete)
                    deleteImage(storageReference)
                    LocalData.currentPlace = null //resets currentPlace
                }
            }
        }
    }


    /*
    Deletes the place from the database. Takes documentID as argument (a String).
     */
    private fun deletePlaceAndGoBack(docToDelete: String) {
        db.collection("places").document(docToDelete).delete()
            .addOnSuccessListener {
                LocalData.currentPlace = null //resets currentPlace, just to be safe!
                Toast.makeText(this, getString(R.string.thrift_store_deleted),
                    Toast.LENGTH_SHORT).show()
                goToPlaces()
            }.addOnFailureListener {e ->
                Toast.makeText(this, getString(
                    R.string.could_not_delete_thrift_store,
                    e.toString()
                ), Toast.LENGTH_SHORT).show()
                goToPlaces()
            }
    }


    /*
    Deletes image. Takes the StorageReference to the image as argument.
     */
    private fun deleteImage(imageToDeleteRef: StorageReference) {
        imageToDeleteRef.delete().addOnSuccessListener {
            Toast.makeText(this, getString(R.string.image_deleted),
                Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, getString(R.string.could_not_delete_image),
                Toast.LENGTH_SHORT).show()
        }
    }


    /*
    The goToPlaces-function never lets you down when you want to go to
    PlacesRecyclerViewActivity.
     */
    private fun goToPlaces() {
        val intent = Intent(this, PlacesRecyclerViewActivity::class.java)
        startActivity(intent)
        finish()
    }
}