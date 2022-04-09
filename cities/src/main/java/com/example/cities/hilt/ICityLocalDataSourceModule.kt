package com.example.cities.hilt
import com.example.cities.data.source.local.CityLocalDataSource
import com.example.cities.data.source.local.ICityLocalDataSource
import com.example.core.data.source.remote.Service
import com.example.core.model.dto.CitiesResponse
import com.example.core.model.entity.CityEntity
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
abstract class ICityLocalDataSourceModule {
    @Binds
    abstract fun bind(impl: ICityLocalDataSourceImpl): ICityLocalDataSource
}

class ICityLocalDataSourceImpl @Inject constructor(
    val cityLocalDataSource: CityLocalDataSource): ICityLocalDataSource {

    override suspend fun save(list: List<CityEntity>) {
        return cityLocalDataSource.save(list)
    }

    override fun getCitiesByPageNumber(pageNumber: Int): List<CityEntity> {
        return cityLocalDataSource.getCitiesByPageNumber(pageNumber)
    }

    override fun getCitiesByCityName(cityName: String): List<CityEntity> {
        return cityLocalDataSource.getCitiesByCityName(cityName)
    }

}