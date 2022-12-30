package com.example.thriftstoresapp

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.GeoPoint

data class PlaceItem (@DocumentId val id : String? = null,
                      val title: String? = "",
                      val address: String? = "",
                      val description: String? = "",
                      val rating: Float = 0f,
                      val image: Int = R.drawable.ic_android_black_24dp,
                      val latitude: Double? = null,
                      val longitude: Double? = null,
                      val userUID: String? = null)