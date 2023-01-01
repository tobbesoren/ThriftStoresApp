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


class PlacesRecyclerViewAdapter(private val context : Context,
                                private val places : List<PlaceItem>)
    : RecyclerView.Adapter<PlacesRecyclerViewAdapter.ViewHolder>(){

    private var layoutInflater = LayoutInflater.from(context)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = layoutInflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val placeItem = places[position]

        holder.titleTextView.text = placeItem.title
        holder.addressTextView.text = placeItem.address
        holder.descriptionTextView.text = placeItem.description
        //holder.placeImageView.setImageResource(placeItem.image)
        holder.ratingView.rating = placeItem.rating!!
        holder.itemView.setOnClickListener {
            val intent = Intent(context, InfoActivity::class.java)
            LocalData.currentPlace = LocalData.placeList[position]
            //intent.putExtra("itemID", placeItem.id)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return places.size
    }

    inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {

        val titleTextView = itemView.findViewById<TextView>(R.id.titleTextView)!!
        val addressTextView = itemView.findViewById<TextView>(R.id.addressTextView)!!
        val descriptionTextView = itemView.findViewById<TextView>(R.id.shortDescriptiontTextView)!!
        val placeImageView = itemView.findViewById<ImageView>(R.id.placeImageView)!!
        val ratingView = itemView.findViewById<RatingBar>(R.id.ratingBar)!!


        }




    }



