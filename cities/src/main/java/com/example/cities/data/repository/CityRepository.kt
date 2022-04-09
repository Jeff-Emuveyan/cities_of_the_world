package com.example.cities.data.repository

import com.example.cities.data.source.local.CityLocalDataSource
import com.example.cities.data.source.local.ICityLocalDataSource
import com.example.cities.data.source.remote.CityRemoteDataSource
import com.example.cities.data.source.remote.ICityRemoteDataSource
import com.example.core.model.dto.CitiesResponse
import com.example.core.model.dto.City
import com.example.core.model.dto.ui.Result
import com.example.core.model.dto.ui.UIStateType.*
import com.example.core.model.entity.CityEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CityRepository @Inject constructor (private val localDataSource: ICityLocalDataSource,
                                          private val remoteDataSource: ICityRemoteDataSource,
                                          private val ioDispatcher: CoroutineDispatcher) {

    fun getAndCacheCitiesByPageNumber(pageNumber: Int) = flow {
        val remoteResponse = remoteDataSource.getCitiesByPageNumber(pageNumber)
        val result = if (remoteResponse != null) {
            saveAndGetCachedCitiesByPageNumber(pageNumber, remoteResponse)
        } else {
            fallbackToCacheOnNetworkError(pageNumber)
        }
        emit(result)
    }.flowOn(ioDispatcher)

    suspend fun saveAndGetCachedCitiesByPageNumber(pageNumber: Int, remoteResponse: CitiesResponse): Result {
        val list = getListFromResponse(remoteResponse)
        if (list.isNullOrEmpty()) return Result(NO_RESULT)

        saveCitiesFromResponse(remoteResponse)
        return getCachedCitiesByPageNumber(pageNumber)
    }

    suspend fun saveCitiesFromResponse(remoteResponse: CitiesResponse) {
        val list = getListFromResponse(remoteResponse) ?: return
        localDataSource.save(list)
    }

    fun getCachedCitiesByPageNumber(pageNumber: Int): Result {
        val list = localDataSource.getCitiesByPageNumber(pageNumber)
        return if (list.isNullOrEmpty()) Result(NO_RESULT) else Result(SUCCESS, list)
    }

    fun getListFromResponse(resultFromRemote: CitiesResponse?): List<CityEntity>? {
        val list = resultFromRemote?.data?.items
        val pageNumber = resultFromRemote?.data?.pagination?.currentPage ?: 1
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

    fun fallbackToCacheOnNetworkError(pageNumber: Int): Result {
        val list = localDataSource.getCitiesByPageNumber(pageNumber)
        return if (list.isNullOrEmpty()) Result(NETWORK_ERROR) else Result(SUCCESS, list)
    }

}