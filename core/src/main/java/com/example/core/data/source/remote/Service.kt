package com.example.core.data.source.remote

import com.example.core.model.dto.CitiesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("city")
    suspend fun getCitiesByPageNumber(@Query("page") pageNumber: Int,
                                      @Query("include") country: String = "country"): Response<CitiesResponse>

    @GET("city")
    suspend fun getCitiesByCityName(@Query("filter[0][name][contains]") cityName: String,
                                    @Query("include") country: String = "country"): Response<CitiesResponse>
}