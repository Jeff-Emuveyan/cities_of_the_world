package com.example.cities.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cities.R
import com.example.core.model.entity.CityEntity

class CityAdapter(var context: Context,
                  var cities: MutableList<CityEntity>,
                  val onItemClicked: (CityEntity)-> Unit) : RecyclerView.Adapter<CityItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityItem {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.recycler_item_city, parent, false)
        return CityItem(view)
    }

    override fun onBindViewHolder(holder: CityItem, position: Int) {
        val city = cities[position]
        holder.tvName.text = city.name
        holder.tvLName.text = city.localName
        holder.tvLabel.text = city.name?.take(2)
        holder.parentLayout.setOnClickListener {
            onItemClicked(city)
        }
    }

    override fun getItemCount(): Int {
        return cities.size
    }
}
