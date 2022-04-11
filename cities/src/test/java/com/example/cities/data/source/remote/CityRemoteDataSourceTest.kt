package com.example.cities.data.source.remote

import com.example.cities.util.dummyResponseBody
import com.example.core.data.source.remote.Service
import com.example.core.model.dto.CitiesResponse
import com.example.core.model.dto.City
import com.example.core.model.dto.Data
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.mockito.Mockito.`when`
import retrofit2.Response

@ExperimentalCoroutinesApi
class CityRemoteDataSourceTest {

    private lateinit var cityRemoteDataSource: CityRemoteDataSource
    private val service: Service = mock(Service::class.java)
    private val testDispatcher = TestCoroutineDispatcher()

    private val list = listOf(City(), City(), City(), City())
    private val data = Data(items = list)
    val response = CitiesResponse(data, 1234)

    @Before
    fun setUp() {
        cityRemoteDataSource = CityRemoteDataSource(service, testDispatcher)
    }

    @Test
    fun `test that remote source can fetch cities by page number successfully`() = runBlockingTest {
        `when`(service.getCitiesByPageNumber(44)).thenReturn(Response.success(response))

        val result = cityRemoteDataSource.getCitiesByPageNumber(44)
        assertEquals(4, result?.data?.items?.size)
    }

    @Test
    fun `test that remote source will return null when fetch cities by page number fails`() = runBlockingTest {
        `when`(service.getCitiesByPageNumber(44)).thenReturn( Response.error(400, dummyResponseBody))

        val result = cityRemoteDataSource.getCitiesByPageNumber(44)
        assertNull(result)
    }

    @Test
    fun `test that remote source can fetch cities by city name successfully`() = runBlockingTest {
        `when`(service.getCitiesByCityName("tokyo")).thenReturn(Response.success(response))

        val result = cityRemoteDataSource.getCitiesByCityName("tokyo")
        assertEquals(4, result?.data?.items?.size)
    }

    @Test
    fun `test that remote source will return null when fetch cities by city name fails`() = runBlockingTest {
        `when`(service.getCitiesByCityName("tokyo")).thenReturn( Response.error(400, dummyResponseBody))

        val result = cityRemoteDataSource.getCitiesByCityName("tokyo")
        assertNull(result)
    }
}