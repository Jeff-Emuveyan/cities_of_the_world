package com.example.core.data.source.local.dao

import androidx.room.*
import com.example.core.model.entity.CityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(cityEntity: CityEntity)

    @Query("SELECT * FROM city ORDER BY id DESC LIMIT :numberOfItems")
    fun getMostRecent(numberOfItems: Int): List<CityEntity>

    @Query("SELECT * FROM city WHERE pageNumber = :pageNumber")
    fun getAllByPageNumber(pageNumber: Int): List<CityEntity>

    @Query("SELECT * FROM city WHERE name LIKE :cityName")
    fun getAllByName(cityName: String): List<CityEntity>

}