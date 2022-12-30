package com.example.thriftstoresapp

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object DataBaseHandler {
    val db = Firebase.firestore
    val auth = Firebase.auth
    val placeList = mutableListOf<PlaceItem>()
    var currentPlace: PlaceItem? = null

    fun createUser() {

    }

    fun logInUser() {

    }

    fun logOutUser() {

    }

    fun loadPlaces() {

    }

    fun setCurrentPlace(documentID: String) {

    }

    fun clearPlaces() {
        
    }
}