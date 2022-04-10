package com.example.core.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city")
data class CityEntity(
    val lng: Double? = null,
    val name: String? = null,
    @PrimaryKey val id: Int,
    val localName: String? = null,
    val lat: Double? = null,
    val pageNumber: Int? = null,
    val countryName: String? = null
)