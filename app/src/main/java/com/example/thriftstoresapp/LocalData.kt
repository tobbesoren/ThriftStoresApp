package com.example.thriftstoresapp

/**
 *Keeps the list of places at hand, as well as the selected place. This way we don't have to access
 *the database all the time.
 */
object LocalData {

    val placeList = mutableListOf<PlaceItem>()
    var currentPlace: PlaceItem? = null

}