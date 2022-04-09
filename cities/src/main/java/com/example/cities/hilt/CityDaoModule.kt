package com.example.cities.hilt
import com.example.core.data.source.local.AppDatabase
import com.example.core.data.source.local.dao.CityDao
import com.example.core.model.entity.CityEntity
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
abstract class CityDaoModule {
    @Binds
    abstract fun bindService(cityImpl: CityDaoImpl): CityDao
}

class CityDaoImpl @Inject constructor(val db: AppDatabase): CityDao {
    override suspend fun save(cityEntity: CityEntity) {
        return db.cityDao().save(cityEntity)
    }

    override fun getMostRecent(numberOfItems: Int): Flow<List<CityEntity>> {
        return db.cityDao().getMostRecent(numberOfItems)
    }

    override fun getAllByPageNumber(pageNumber: Int): Flow<List<CityEntity>> {
        return db.cityDao().getAllByPageNumber(pageNumber)
    }

    override fun getAllByName(cityName: String): Flow<List<CityEntity>> {
        return db.cityDao().getAllByName(cityName)
    }

}