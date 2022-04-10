package com.example.cities.data.repository

import com.example.cities.data.source.local.CityLocalDataSource
import com.example.cities.data.source.local.ICityLocalDataSource
import com.example.cities.data.source.remote.CityRemoteDataSource
import com.example.cities.data.source.remote.ICityRemoteDataSource
import com.example.core.model.dto.CitiesResponse
import com.example.core.model.dto.City
import com.example.core.model.dto.Query
import com.example.core.model.dto.QueryType.*
import com.example.core.model.dto.Result
import com.example.core.model.dto.ui.UIStateType.*
import com.example.core.model.entity.CityEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CityRepository @Inject constructor (private val localDataSource: CityLocalDataSource,
                                          private val remoteDataSource: CityRemoteDataSource,
                                          private val ioDispatcher: CoroutineDispatcher) {

    fun fetchAndCacheCities(query: Query) = flow {
        val remoteResponse = when(query.queryType) {
            PAGE_NUMBER -> {
                remoteDataSource.getCitiesByPageNumber(query.value as Int)
            }
            CITY_NAME -> {
                remoteDataSource.getCitiesByCityName(query.value as String)
            }
        }
        val result = processResponse(query, remoteResponse)
        emit(result)
    }.flowOn(ioDispatcher)

    suspend fun processResponse(query: Query, remoteResponse: CitiesResponse?) : Result {
        return if (remoteResponse != null) {
            saveAndGetCachedCities(query, remoteResponse)
        } else {
            fallbackToCacheOnNetworkError(query)
        }
    }

    suspend fun saveAndGetCachedCities(query: Query, remoteResponse: CitiesResponse): Result {
        val list = getListFromResponse(remoteResponse)
        if (list.isNullOrEmpty()) return Result(NO_RESULT)

        saveCitiesFromResponse(remoteResponse)
        return getCachedCities(query)
    }

    suspend fun saveCitiesFromResponse(remoteResponse: CitiesResponse) {
        val list = getListFromResponse(remoteResponse) ?: return
        localDataSource.save(list)
    }

    fun getCachedCities(query: Query): Result {
        val list = when(query.queryType) {
            PAGE_NUMBER -> {
                localDataSource.getCitiesByPageNumber(query.value as Int)
            }
            CITY_NAME -> {
                localDataSource.getCitiesByCityName(query.value as String)
            }
        }
        return if (list.isNullOrEmpty()) Result(NO_RESULT) else Result(SUCCESS, list)
    }

    fun getListFromResponse(resultFromRemote: CitiesResponse?): List<CityEntity>? {
        val list = resultFromRemote?.data?.items
        val pageNumber = resultFromRemote?.data?.pagination?.current_page ?: 1
        return when {
            list == null -> null
            list.isEmpty() -> emptyList()
            else -> getCityEntityFromList(list, pageNumber)
        }
    }

    fun getCityEntityFromList(list: List<City?>, pageNumber: Int): List<CityEntity> {
        val result = mutableListOf<CityEntity>()
        list.forEach {
            val cityEntity = it?.toCityEntity(pageNumber)
            if (cityEntity != null) {
                result.add(cityEntity)
            }
        }
        return result
    }

    fun fallbackToCacheOnNetworkError(query: Query): Result {
        val list = when(query.queryType) {
            PAGE_NUMBER -> {
                localDataSource.getCitiesByPageNumber(query.value as Int)
            }
            CITY_NAME -> {
                localDataSource.getCitiesByCityName(query.value as String)
            }
        }
        return if (list.isNullOrEmpty()) Result(NETWORK_ERROR) else Result(SUCCESS, list)
    }
}