package com.example.cities.hilt
import com.example.cities.data.source.remote.CityRemoteDataSource
import com.example.cities.data.source.remote.ICityRemoteDataSource
import com.example.core.model.dto.CitiesResponse
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
abstract class ICityRemoteDataSourceModule {
    @Binds
    abstract fun bind(impl: ICityLocalDataSourceImpl): ICityRemoteDataSource
}

class ICityRemoteDataSourceImpl @Inject constructor(
    val cityRemoteDataSource: CityRemoteDataSource): ICityRemoteDataSource {

    override suspend fun getCitiesByPageNumber(pageNumber: Int): CitiesResponse? {
        return cityRemoteDataSource.getCitiesByPageNumber(pageNumber)
    }

    override suspend fun getCitiesByCityName(cityName: String): CitiesResponse? {
        return cityRemoteDataSource.getCitiesByCityName(cityName)
    }
}