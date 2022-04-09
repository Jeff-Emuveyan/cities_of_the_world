package com.example.core.model.dto

import com.example.core.model.entity.CityEntity

data class City(
    val lng: Double? = null,
    val updatedAt: String? = null,
    val name: String? = null,
    val createdAt: String? = null,
    val id: Int? = null,
    val localName: String? = null,
    val lat: Double? = null,
    val countryId: Int? = null
) {

    fun toCityEntity(pageNumber: Int): CityEntity? {
        return if (id == null) null else CityEntity(lng, name, id, localName, lat, pageNumber)
    }
}