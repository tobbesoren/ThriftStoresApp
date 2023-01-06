package com.example.thriftstoresapp

import android.provider.Settings.Global.getString
import com.google.firebase.firestore.DocumentId

/**
 *Holds the data for the places.
 */
data class PlaceItem (@DocumentId val id : String? = null,
                      val title: String? = "",
                      val address: String? = "",
                      val openingHours: String = "---",
                      val description: String = "---",
                      val priceRange: String = "",
                      val rating: Float = 0.0f,
                      var imageFileName: String = "",
                      val latitude: Double? = null,
                      val longitude: Double? = null,
                      val distance: Float? = null,
                      val userUID: String? = "",
                      val created: String = "")