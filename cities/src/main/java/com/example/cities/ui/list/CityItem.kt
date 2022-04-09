package com.example.cities.ui.list

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.cities.R

class CityItem(v: View): RecyclerView.ViewHolder(v) {
    val tvName: TextView = v.findViewById(R.id.tvName)
    val tvLName: TextView = v.findViewById(R.id.tvLocalName)
    val tvLabel: TextView =  v.findViewById(R.id.tvLabel)
    val parentLayout: ConstraintLayout = v.findViewById(R.id.parentLayout)
}