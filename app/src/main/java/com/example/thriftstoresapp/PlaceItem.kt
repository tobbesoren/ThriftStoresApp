package com.example.thriftstoresapp

import com.google.firebase.firestore.DocumentId

/*
Holds the data for the places.
 */
data class PlaceItem (@DocumentId val id : String? = null,
                      val title: String? = "",
                      val address: String? = "",
                      val openingHours: String? = null,
                      val description: String? = "",
                      val priceRange: String? = "",
                      val rating: Float? = null,
                      var imageFileName: String? = "",
                      val latitude: Double? = null,
                      val longitude: Double? = null,
                      val distance: Float? = null,
                      val userUID: String? = null,
                      val created: String? = null)