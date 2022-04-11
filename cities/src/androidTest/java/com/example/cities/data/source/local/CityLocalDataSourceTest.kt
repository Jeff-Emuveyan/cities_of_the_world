package com.example.cities.data.source.local

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.core.data.source.local.AppDatabase
import com.example.core.model.entity.CityEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CityLocalDataSourceTest {

    private lateinit var cityLocalDataSource: CityLocalDataSource
    private lateinit var db: AppDatabase
    private val list = listOf(
        CityEntity(id = 1, pageNumber = 44),
        CityEntity(id = 2, pageNumber = 44),
        CityEntity(id = 3, pageNumber = 55),
        CityEntity(id = 4, pageNumber = 55),
        CityEntity(id = 5, pageNumber = 55),
        CityEntity(id = 6, pageNumber = 55))

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        cityLocalDataSource = CityLocalDataSource(db.cityDao(), Dispatchers.IO)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun canSaveCityEntity() = runBlocking {
        cityLocalDataSource.save(list)
        val result = cityLocalDataSource.getCitiesByPageNumber(44)
        Assert.assertEquals(2, result.size)
    }

    @Test
    fun canGetCitiesByPageNumber() = runBlocking {
        cityLocalDataSource.save(list)
        val resultA = cityLocalDataSource.getCitiesByPageNumber(44)
        val resultB = cityLocalDataSource.getCitiesByPageNumber(55)
        Assert.assertEquals(2, resultA.size)
        Assert.assertEquals(4, resultB.size)
    }

    @Test
    fun canGetCitiesByCityName() = runBlocking {
        val list = listOf(
            CityEntity(id = 1, name = "rome"),
            CityEntity(id = 2, name = "rome"),
            CityEntity(id = 3, name = "lagos"),
            CityEntity(id = 4, name = "lagos"),
            CityEntity(id = 5, name = "lagos"),
            CityEntity(id = 6, name = "lagos"))

        cityLocalDataSource.save(list)
        val resultA = cityLocalDataSource.getCitiesByCityName("rome")
        val resultB = cityLocalDataSource.getCitiesByCityName("lagos")
        Assert.assertEquals(2, resultA.size)
        Assert.assertEquals(4, resultB.size)
    }
}