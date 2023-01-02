package com.example.thriftstoresapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.thriftstoresapp.databinding.ActivityMapsBinding

/*
Nothing much going on here, but the default map activity.
Some things to add, if I get the time:
The users location
A button to toggle between All shops and Selected shop
The possibility to click a marker of another shop to view its info
Directions to a store
A more interesting info-window
 */
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



        /*
        Sets up the back-button, to go back to PlacesRecyclerViewActivity. Maybe we should go back
        to InfoActivity instead? We'll see.
         */
        val backButton = findViewById<Button>(R.id.mapsBackButton)
        backButton.setOnClickListener {
            val intent = Intent(this, PlacesRecyclerViewActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val latitude = LocalData.currentPlace?.latitude
        val longitude = LocalData.currentPlace?.longitude
        val shopName = LocalData.currentPlace?.title

        /*
        Adds a marker at the location for the shop and moves the camera there.
         */
        if(latitude != null && longitude != null) {
            val place = LatLng(latitude, longitude)
            mMap.addMarker(MarkerOptions().position(place).title("$shopName"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 15f))
        }
    }
}