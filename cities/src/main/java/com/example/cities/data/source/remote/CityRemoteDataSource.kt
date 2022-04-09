package com.example.cities.data.source.remote

import com.example.core.data.source.remote.Service
import com.example.core.model.dto.CitiesResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class CityRemoteDataSource @Inject constructor (private val service: Service,
                                                private val ioDispatcher: CoroutineDispatcher): ICityRemoteDataSource {

    override suspend fun getCitiesByPageNumber(pageNumber: Int): CitiesResponse? = withContext(ioDispatcher) {
        try {
            val result = service.getCitiesByPageNumber(pageNumber)
            if (result.isSuccessful) result.body() else null
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getCitiesByCityName(cityName: String): CitiesResponse? = withContext(ioDispatcher) {
        try {
            val result = service.getCitiesByCityName(cityName)
            if (result.isSuccessful) result.body() else null
        } catch (e: Exception) {
            null
        }
    }
}