package com.challenge.weatherchallenge

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.challenge.weatherchallenge.databinding.LocationItemBinding
import com.challenge.weatherchallenge.model.Location

class Adapter(private val locations: List<Location>, private val context: Context) :
    RecyclerView.Adapter<ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = LocationItemBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return locations.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.setup(locations[position])
    }
}

class ItemViewHolder(private val binding: LocationItemBinding) : ViewHolder(binding.root) {

    fun setup(location: Location) {
        binding.location.text = location.locationName()
    }
}