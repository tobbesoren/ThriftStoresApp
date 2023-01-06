package com.example.thriftstoresapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PlacesRecyclerViewActivity : AppCompatActivity() {

    //Set up the variables for the database and login
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val currentUser = auth.currentUser?.uid.toString()

    //Declare the recyclerView
    private lateinit var recyclerView: RecyclerView

    //Declare and initialize the variables for filtering and sorting the items of the recyclerView.
    //By default, we show all places and sort them by name.
    var sort = 0
    var selectedView = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_recycler_view)

        //Set up recyclerView.
        recyclerView = findViewById(R.id.placesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Loads places into LocalData.placeList.
        loadPlaces()

        //Sets up the log out button.
        val logOutButton = findViewById<Button>(R.id.placesLogOutButton)
        logOutButton.setOnClickListener {
            auth.signOut()
            goToLogIn()
        }

        //Sets up the sort spinner.
        val sortBy = resources.getStringArray(R.array.sortBy)
        val sortBySpinner = findViewById<Spinner>(R.id.sortBySpinner)
        val sortAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortBy)
        sortBySpinner.adapter = sortAdapter

        sortBySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View,
                                        position: Int,
                                        id: Long) {
                sort = position
                loadPlaces()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        //Sets up the filter spinner.
        val viewSelections = resources.getStringArray(R.array.selectView)
        val selectViewSpinner = findViewById<Spinner>(R.id.selectViewSpinner)
        val viewAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, viewSelections)
        selectViewSpinner.adapter = viewAdapter

        selectViewSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View,
                                        position: Int,
                                        id: Long) {
                selectedView = position
                loadPlaces()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        //Sets up the Add-button.
        val addButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        addButton.setOnClickListener {
            goToAddPlace()
        }
    }


    /**
     *Starts AddPlaceActivity
     */
    private fun goToAddPlace() {
        val intent = Intent(this, AddPlace::class.java)
        startActivity(intent)
    }


    /**
     *Starts MainActivity
     */
    private fun goToLogIn() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


    /**
     *Loads places from Firebase.firestore into LocalData.placeList by using makeList. The data is
     *filtered by selectedView. When the data is downloaded, the list is sorted by calling sortList,
     *and setAdapter is called.
     */
    private fun loadPlaces() {
        when(selectedView) {
            0 -> {
                db.collection("places").get().addOnSuccessListener { documentSnapShot ->
                makeList(documentSnapShot)
                    sortList()
                    setAdapter()
                }
            }
            1 -> {
                db.collection("places").whereEqualTo("userUID", currentUser)
                    .get().addOnSuccessListener { documentSnapShot ->
                    makeList(documentSnapShot)
                        sortList()
                        setAdapter()
                }
            }
        }
    }


    /**
     *Makes a list of PlaceItems from the data downloaded from Firebase.firestore.
     */
    private fun makeList(documentSnapShot: QuerySnapshot) {
        LocalData.placeList.clear()
        for (document in documentSnapShot.documents) {
            val item = document.toObject<PlaceItem>()
            if (item != null) {
                Log.d("!!!!", item.toString())
                LocalData.placeList.add(item)
                Log.d("!!!!", "${item.id}")
            }
        }
    }


    /**
     *Sorts the LocalData.placeList by the method in the sort-variable. The value of sort is a string,
     *maybe I should use enum instead?
     */
    private fun sortList() {
        Log.d("!!!!", "sort: $sort, selectedView: $selectedView")
        when(sort) {
            0 -> LocalData.placeList.sortWith(compareBy { it.title?.lowercase() })
            1 -> LocalData.placeList.sortWith(compareByDescending { it.rating })
            2 -> {
                val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm:ss")

                LocalData.placeList.sortByDescending {
                    LocalDate.parse(it.created, dateTimeFormatter)
                }

                LocalData.placeList.sortWith(compareByDescending { it.created })
            }
            //User place not implemented!
            //3 -> LocalData.placeList.sortWith(compareByDescending { it.distance })
        }
    }


    /**
     *Sets the adapter for the recyclerView.
     */
    private fun setAdapter() {
        val adapter = PlacesRecyclerViewAdapter(this, LocalData.placeList)
        recyclerView.adapter = adapter
    }
}