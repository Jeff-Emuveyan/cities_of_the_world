package com.example.cities.data.source.remote

import com.example.core.model.dto.CitiesResponse
import kotlinx.coroutines.flow.Flow

interface ICityRemoteDataSource {

    suspend fun getCitiesByPageNumber(pageNumber: Int = 1): CitiesResponse?

    suspend fun getCitiesByCityName(cityName: String): CitiesResponse?
}