package com.example.cities.data.repository

import com.example.cities.data.source.local.CityLocalDataSource
import com.example.cities.data.source.remote.CityRemoteDataSource
import com.example.core.model.dto.*
import com.example.core.model.dto.ui.UIStateType
import com.example.core.model.entity.CityEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class CityRepositoryTest {

    private lateinit var repository: CityRepository
    private val localDataSource: CityLocalDataSource = mock(CityLocalDataSource::class.java)
    private val remoteDataSource: CityRemoteDataSource = mock(CityRemoteDataSource::class.java)
    private val pageNumberQuery = Query(QueryType.PAGE_NUMBER, 44)
    private val cityNameQuery = Query(QueryType.CITY_NAME, "tokyo")
    private val citiesResponse = CitiesResponse(time = 1234)
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        repository = CityRepository(localDataSource, remoteDataSource, testDispatcher)
    }

    @Test
    fun `test that repository can fetch cities using page number from remote source` ():
            Unit = runBlocking {
        repository.fetchAndCacheCities(pageNumberQuery).first()
        verify(remoteDataSource).getCitiesByPageNumber(44)
    }

    @Test
    fun `test that repository can fetch cities using city name from remote source`():
            Unit = runBlocking  {
        repository.fetchAndCacheCities(cityNameQuery).first()
        verify(remoteDataSource).getCitiesByCityName("tokyo")
    }

    @Test
    fun `test that repository will be able to save and return cached data when there is network`():
            Unit = runBlocking {
        val spy = spy(repository)
        spy.processResponse(pageNumberQuery,citiesResponse)
        verify(spy).saveAndGetCachedCities(pageNumberQuery,citiesResponse)
    }

    @Test
    fun `test that repository will be able to process data from cache when there is no network`():
            Unit = runBlocking {
        val spy = spy(repository)
        spy.processResponse(pageNumberQuery,null)
        verify(spy).fallbackToCacheOnNetworkError(pageNumberQuery)
    }

    @Test
    fun `test that repository will return no-result when the service returns and empty list of cities`():
            Unit = runBlocking {
       val result = repository.saveAndGetCachedCities(pageNumberQuery, citiesResponse)
       assertEquals(result.type, UIStateType.NO_RESULT)
    }

    @Test
    fun `test that repository can save cities`(): Unit = runBlocking {
        val list = listOf(City())
        val data = Data(items = list)
        val response = CitiesResponse(data, 1234)

        repository.saveCitiesFromResponse(response)

        verify(localDataSource).save(listOf())
    }

    @Test
    fun `test that repository can get cached cities using page number`() {
        val list = listOf(CityEntity(id = 2),CityEntity(id = 2), CityEntity(id = 2))

        `when`(localDataSource.getCitiesByPageNumber(44)).thenReturn(list)
        val result = repository.getCachedCities(pageNumberQuery)

        assertEquals(3, result.cities?.size)
    }

    @Test
    fun `test that repository can get cached cities using city name`() {
        val list = listOf(CityEntity(id = 2),CityEntity(id = 2), CityEntity(id = 2))

        `when`(localDataSource.getCitiesByCityName("tokyo")).thenReturn(list)
        val result = repository.getCachedCities(cityNameQuery)

        assertEquals(3, result.cities?.size)
    }

    @Test
    fun `test that repository can get list of cities from response`() {
        val list = listOf(City(id = 2),City(id = 2), City(id = 2))
        val data = Data(items = list)
        val response = CitiesResponse(data, 1234)

        val result = repository.getListFromResponse(response)

        assertEquals(3, result?.size)
    }

    @Test
    fun `test that repository can get city entity from list`() {
        val list = listOf(City(id = 1),City(id = 2), City(id = 3), City(id = 4))
        val result = repository.getCityEntityFromList(list, 1)
        assertEquals(4, result.size)
        assertEquals(1, result.first().id)
    }

    @Test
    fun `test that repository can fetch cities using page number from local source` () {
        val list = listOf(CityEntity(id = 1),CityEntity(id = 2), CityEntity(id = 3), CityEntity(id = 4))
        `when`(localDataSource.getCitiesByPageNumber(44)).thenReturn(list)

        val result = repository.fallbackToCacheOnNetworkError(pageNumberQuery)

        assertEquals(UIStateType.SUCCESS, result.type)
        assertEquals(4, result.cities?.size)
    }

    @Test
    fun `test that repository can fetch cities using city name from local source` () {
        val list = listOf(CityEntity(id = 1),CityEntity(id = 2), CityEntity(id = 3), CityEntity(id = 4))
        `when`(localDataSource.getCitiesByCityName("tokyo")).thenReturn(list)

        val result = repository.fallbackToCacheOnNetworkError(cityNameQuery)

        assertEquals(UIStateType.SUCCESS, result.type)
        assertEquals(4, result.cities?.size)
    }
}