package com.example.core.data.source.remote

import com.example.core.model.dto.CitiesResponse
import retrofit2.Response
import retrofit2.http.GET

interface Service {

    @GET("city?page={pageNumber}")
    suspend fun getCitiesByPageNumber(pageNumber: Int): Response<CitiesResponse>

    @GET("city?filter[0][name][contains]={cityName}")
    suspend fun getCitiesByCityName(cityName: String): Response<CitiesResponse>
}