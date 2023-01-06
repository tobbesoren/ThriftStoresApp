package com.example.thriftstoresapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage


/*
The Adapter for the PlacesRecyclerView.
 */
class PlacesRecyclerViewAdapter(private val context : Context,
                                private val places : List<PlaceItem>)
    : RecyclerView.Adapter<PlacesRecyclerViewAdapter.ViewHolder>() {

    private var layoutInflater = LayoutInflater.from(context)



    /*
    Returns a ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView)
    }

    /*
    Sets the data to the views. Also, adds setOnClickListener to make the item clickable.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val placeItem = places[position]

        holder.titleTextView.text = placeItem.title
        holder.addressTextView.text = placeItem.address
        holder.descriptionTextView.text = placeItem.description
        holder.ratingView.rating = placeItem.rating!!

        //Using Glide to show image from Firebase storage
        val storageReference = FirebaseStorage.getInstance()
            .getReference("images/${placeItem.imageFileName}")

        storageReference.downloadUrl.addOnSuccessListener { downloadUrl ->
            Glide.with(context)
                .load(downloadUrl)
                .into(holder.placeImageView)
        }

        //When an item is clicked, InfoActivity is started.
        holder.itemView.setOnClickListener {
            val intent = Intent(context, InfoActivity::class.java)
            LocalData.currentPlace = LocalData.placeList[position]
            context.startActivity(intent)
        }

    }

    /*
    Needed for binding ViewHolders. Returns the length of places (a List).
     */
    override fun getItemCount(): Int {
        return places.size
    }

    /*
    Inner class defining the ViewHolder, setting layout Views to variables.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleTextView = itemView.findViewById<TextView>(R.id.titleTextView)!!
        val addressTextView = itemView.findViewById<TextView>(R.id.addressTextView)!!
        val descriptionTextView = itemView.findViewById<TextView>(R.id.shortDescriptiontTextView)!!
        val placeImageView = itemView.findViewById<ImageView>(R.id.placeImageView)!!
        val ratingView = itemView.findViewById<RatingBar>(R.id.ratingBar)!!
    }
}



