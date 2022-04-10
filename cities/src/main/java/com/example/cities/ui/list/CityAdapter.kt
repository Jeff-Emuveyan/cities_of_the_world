package com.example.cities.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cities.R
import com.example.cities.databinding.RecyclerItemCityBinding
import com.example.core.model.entity.CityEntity

class CityAdapter(var context: Context,
                  var cities: MutableList<CityEntity>,
                  val onItemClicked: (CityEntity)-> Unit) : RecyclerView.Adapter<CityItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityItem {
        return CityItem(RecyclerItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CityItem, position: Int) {
        val city = cities[position]
        holder.bind(city) {
            onItemClicked(it)
        }
    }

    override fun getItemCount(): Int {
        return cities.size
    }
}
