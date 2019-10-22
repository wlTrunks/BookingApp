package com.app.bookingapp.presentation.ui.adapter

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.bookingapp.R
import com.app.bookingapp.domain.model.ApartmentData


class ApartmentListAdapter(
    private val offerList: List<ApartmentData>,
    val onItemClick: (Bundle) -> Unit
) :
    RecyclerView.Adapter<ApartmentListAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // create a new view

        // Grid Mode
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recyler_aparments, parent, false)

        var viewHolder = MyViewHolder(view)

        return viewHolder
    }

    override fun getItemCount(): Int = offerList.size ?: 0


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (position < offerList.size) {
            var data = offerList.get(position)



            holder.tvName.setText(data.name)
            holder.tvID.setText("ID: ${data.id}")
            holder.tvBedRooms.setText("Bedroom: ${data.bedrooms}")
            holder.tvLatitude.setText("Lat: ${data.latitude}")
            holder.tvLongitude.setText("Lng: ${data.longitude}")
            holder.tvDistance.setText("Distance: ${data.diatance} (meters)")

            holder.lay_row.setOnClickListener {
                val bundle = Bundle().apply {
                    putSerializable("iApartmentData", data)
                }
                onItemClick.invoke(bundle)
            }
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvID = itemView.findViewById<TextView>(R.id.tvID)
        var tvName = itemView.findViewById<TextView>(R.id.tvName)
        var tvBedRooms = itemView.findViewById<TextView>(R.id.tvBedRooms)
        var tvLatitude = itemView.findViewById<TextView>(R.id.tvLatitude)
        var tvLongitude = itemView.findViewById<TextView>(R.id.tvLongitude)
        var tvDistance = itemView.findViewById<TextView>(R.id.tvDistance)

        var lay_row = itemView.findViewById<ConstraintLayout>(R.id.lay_row)


    }

    fun TextView.showStrikeThrough(show: Boolean) {
        paintFlags =
            if (show) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }


}