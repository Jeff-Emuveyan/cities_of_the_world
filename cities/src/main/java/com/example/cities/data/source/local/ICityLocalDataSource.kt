package com.example.cities.data.source.local

import com.example.core.model.dto.CitiesResponse
import com.example.core.model.entity.CityEntity
import kotlinx.coroutines.flow.Flow

interface ICityLocalDataSource {
    
    suspend fun save(list: List<CityEntity>)

    fun getCitiesByPageNumber(pageNumber: Int = 1): List<CityEntity>

    fun getCitiesByCityName(cityName: String): List<CityEntity>
}