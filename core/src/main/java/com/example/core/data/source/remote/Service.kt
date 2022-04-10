package com.example.core.data.source.remote

import com.example.core.model.dto.CitiesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("city")
    suspend fun getCitiesByPageNumber(@Query("page") pageNumber: Int): Response<CitiesResponse>

    @GET("city?filter[0][name][contains]={cityName}")
    suspend fun getCitiesByCityName(cityName: String): Response<CitiesResponse>
}