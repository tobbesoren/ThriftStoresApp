package com.example.thriftstoresapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.thriftstoresapp.databinding.ActivityMainBinding


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
        //holder.placeImageView.setImageResource(placeItem.image)
        holder.ratingView.rating = placeItem.rating!!

        /*
        Testing getting image from Glide
         */
        Glide.with(context)
            .load(placeItem.imageUri)
            .into(holder.placeImageView)


        /*
        When an item is clicked, InfoActivity is started.
         */
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
    Binds the Views to variables.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleTextView = itemView.findViewById<TextView>(R.id.titleTextView)!!
        val addressTextView = itemView.findViewById<TextView>(R.id.addressTextView)!!
        val descriptionTextView = itemView.findViewById<TextView>(R.id.shortDescriptiontTextView)!!
        val placeImageView = itemView.findViewById<ImageView>(R.id.placeImageView)!!
        val ratingView = itemView.findViewById<RatingBar>(R.id.ratingBar)!!
    }
}



