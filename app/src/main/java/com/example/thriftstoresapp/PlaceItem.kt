package com.example.thriftstoresapp

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.GeoPoint

data class PlaceItem (@DocumentId val id : String? = null,
                      val title: String? = "",
                      val address: String? = "",
                      val openingHours: String? = null,
                      val description: String? = "",
                      val rating: Float? = null,
                      val image: Int = R.drawable.ic_baseline_panorama_wide_angle_24,
                      val latitude: Double? = null,
                      val longitude: Double? = null,
                      val distance: Float? = null,
                      val userUID: String? = null,
                      val created: String? = null)