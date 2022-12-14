package com.example.thriftstoresapp

import com.google.firebase.firestore.DocumentId

data class PlaceItem (@DocumentId val id : String? = null,
                      val title: String? = "",
                      val address: String? = "",
                      val description: String? = "",
                      val rating: Float = 0f,
                      val image: Int = R.drawable.ic_android_black_24dp)