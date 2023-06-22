package com.challenge.weatherchallenge

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.challenge.weatherchallenge.databinding.LocationItemBinding
import com.challenge.weatherchallenge.model.Location

/**
 * Adapter class for the RecyclerView in the location selection screen.
 * It takes a list of locations and a context as parameters.
 * It also receives a click listener as a lambda function to handle item clicks.
 * The class extends RecyclerView.Adapter<ItemViewHolder>() to bind data to individual items in the RecyclerView.
 * The onCreateViewHolder() method is overridden to inflate the item layout and return an instance of ItemViewHolder.
 * The getItemCount() method returns the total number of locations in the list.
 * The onBindViewHolder() method is overridden to bind the location data to the ViewHolder and set up the item click listener.
 * Inside onBindViewHolder(), the setup() method of the ViewHolder is called to populate the UI with location data.
 * The click listener is set on the root view of the ViewHolder, and when invoked, it calls the click lambda function with the clicked location as the argument.
 */
class Adapter(
    private val locations: List<Location>,
    private val context: Context,
    private val click: ((Location) -> Unit)
) : RecyclerView.Adapter<ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = LocationItemBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val location = locations[position]
        holder.setup(location)
        holder.binding.root.setOnClickListener {
            click.invoke(location)
        }
    }
}


/**
 * ViewHolder class for holding and displaying individual items in the RecyclerView.
 * It takes a binding parameter of type LocationItemBinding to access the views within the item layout.
 * The class extends RecyclerView.ViewHolder(binding.root) to inherit the basic functionality of a ViewHolder.
 * The setup() method is used to bind location data to the views in the item layout.
 * It sets the location name in the location TextView.
 */
class ItemViewHolder(val binding: LocationItemBinding) : ViewHolder(binding.root) {

    /**
     * Binds the location data to the views in the item layout.
     * It sets the location name in the location TextView.
     */
    fun setup(location: Location) {
        binding.location.text = location.locationName()
    }
}
