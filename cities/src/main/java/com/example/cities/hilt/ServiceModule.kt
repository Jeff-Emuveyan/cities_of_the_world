package com.example.cities.hilt
import com.example.core.data.source.remote.Service
import com.example.core.model.dto.CitiesResponse
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun bindService(serviceImpl: ServiceImpl): Service
}

class ServiceImpl @Inject constructor(var retrofit: Retrofit): Service {
    override suspend fun getCitiesByPageNumber(pageNumber: Int): Response<CitiesResponse> {
        return retrofit.create(Service::class.java).getCitiesByPageNumber(pageNumber)
    }

    override suspend fun getCitiesByCityName(cityName: String): Response<CitiesResponse> {
        return retrofit.create(Service::class.java).getCitiesByCityName(cityName)
    }
}