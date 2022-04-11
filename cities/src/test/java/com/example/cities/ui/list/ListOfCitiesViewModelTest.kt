package com.example.cities.ui.list

import com.example.cities.data.repository.CityRepository
import com.example.cities.data.source.local.CityLocalDataSource
import com.example.cities.data.source.remote.CityRemoteDataSource
import com.example.core.model.dto.Query
import com.example.core.model.dto.QueryType
import com.example.core.model.dto.Result
import com.example.core.model.dto.ui.UIStateType
import com.example.core.model.entity.CityEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.Assert.assertEquals
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class ListOfCitiesViewModelTest {

    private lateinit var listOfCitiesViewModel: ListOfCitiesViewModel
    private val localDataSource: CityLocalDataSource = mock(CityLocalDataSource::class.java)
    private val remoteDataSource: CityRemoteDataSource = mock(CityRemoteDataSource::class.java)
    private val testDispatcher = TestCoroutineDispatcher()
    private val repository = FakeRepository(localDataSource, remoteDataSource, testDispatcher)
    private val pageNumberQuery = Query(QueryType.PAGE_NUMBER, 44)
    val list = listOf(CityEntity(id = 1), CityEntity(id = 2), CityEntity(id = 3), CityEntity(id = 4))

    @Before
    fun setUp() {
        listOfCitiesViewModel = ListOfCitiesViewModel(repository)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `test that the correct ui state is sent after successfully fetching data`() = runBlockingTest {
        repository.whatToReturn(Result(UIStateType.SUCCESS, list))

        listOfCitiesViewModel.getCities(pageNumberQuery)

        val result = listOfCitiesViewModel.uiState.first()
        assertEquals(UIStateType.SUCCESS, result.type)
        assertEquals(4, result.cities?.size)
    }

    @Test
    fun `test that the correct ui state is sent after failure to fetch data`() = runBlockingTest {
        repository.whatToReturn(Result(UIStateType.NETWORK_ERROR))

        listOfCitiesViewModel.getCities(pageNumberQuery)

        val result = listOfCitiesViewModel.uiState.first()
        assertEquals(UIStateType.NETWORK_ERROR, result.type)
        assertEquals(null, result.cities)
    }

    inner class FakeRepository(private val localDataSource: CityLocalDataSource,
                               private val remoteDataSource: CityRemoteDataSource,
                               private val ioDispatcher: CoroutineDispatcher
    ): CityRepository(localDataSource, remoteDataSource, ioDispatcher) {

        private var result = Result(UIStateType.DEFAULT)

        override fun fetchAndCacheCities(query: Query): Flow<Result> = flow {
            emit(result)
        }

        fun whatToReturn(result: Result) {
            this.result = result
        }
    }
}