package com.example.thriftstoresapp

import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class AddPlace : AppCompatActivity() {

    /*
    Declaring variables for the Views.
     */
    private lateinit var nameEdit: EditText
    private lateinit var addressEdit: EditText
    private lateinit var openingHoursEdit: EditText
    private lateinit var descriptionEdit: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var shopImageView: ImageView

    /*
    Lateinit price range.
     */
    lateinit var priceRange: String

    private lateinit var imageRef: StorageReference

    private lateinit var imageUri: Uri

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
        addressEdit = findViewById(R.id.addressEditText)
        openingHoursEdit = findViewById(R.id.openingHoursEditText)
        descriptionEdit = findViewById(R.id.descriptionEditText)
        ratingBar = findViewById(R.id.ratingBarEdit)
        shopImageView = findViewById(R.id.shopImageView)

        val defaultDrawableResourceId = R.drawable.ic_baseline_panorama_wide_angle_24
        imageUri = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                    packageName + "/" + defaultDrawableResourceId)



        val priceRangeOptions = resources.getStringArray(R.array.priceRange)
        val priceRangeSpinner = findViewById<Spinner>(R.id.priceRangeSpinner)
        val priceRangeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priceRangeOptions)
        priceRangeSpinner.adapter = priceRangeAdapter

        priceRangeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
              priceRange = priceRangeOptions[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }


        /*
        Setting up select image-button.
         */
        val selectImageButton = findViewById<Button>(R.id.selectImageButton)
        selectImageButton.setOnClickListener {
            selectImage()
        }

        /*
        Setting up save-button
         */
        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            /*
            Making sure we at least have a name and an address. If not, have a Toast.
             */
            if(nameEdit.text.isNotEmpty() && addressEdit.text.isNotEmpty()) {
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
    Lets the user select an image for the place.
     */
    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100 && resultCode == RESULT_OK) {
            imageUri = data?.data!!
            shopImageView.setImageURI(imageUri)
        }
    }

    private fun uploadImage() {

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading file...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val fileName = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneOffset.UTC)
            .format(Instant.now())

        val storageReference = FirebaseStorage.getInstance()
            .getReference("images/$fileName")
        imageRef = storageReference

        storageReference.putFile(imageUri).addOnSuccessListener {

            Toast.makeText(
                this, "Successfully uploaded image",
                Toast.LENGTH_SHORT
            ).show()
            if (progressDialog.isShowing) progressDialog.dismiss()


        }.addOnFailureListener {

            if (progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()

        }
        createPlace()
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
        val address = addressEdit.text.toString()
        val openingHours = openingHoursEdit.text.toString()
        val description = descriptionEdit.text.toString()
        val rating = ratingBar.rating



        val coordinates = getCoordinatesFromAddress(address)


        val place = PlaceItem(
            title = title,
            address = address,
            openingHours = openingHours,
            priceRange = priceRange,
            description = description,
            rating = rating,
            //imageRef = imageRef,
            latitude = coordinates?.get(0),
            longitude = coordinates?.get(1),
            userUID = auth.currentUser?.uid.toString(),
            created = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now())
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