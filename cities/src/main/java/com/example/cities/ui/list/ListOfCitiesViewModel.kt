package com.example.cities.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cities.data.repository.CityRepository
import com.example.core.model.dto.Query
import com.example.core.model.dto.QueryType.*
import com.example.core.model.dto.Result
import com.example.core.model.dto.ui.UIStateType.*
import com.example.core.model.entity.CityEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListOfCitiesViewModel @Inject constructor(private val repository: CityRepository) : ViewModel() {

    private var currentPageNumber = 1

    private val _uiState = MutableStateFlow(Result(DEFAULT))
    val uiState = _uiState.asStateFlow()

    fun getCities(query: Query) = viewModelScope.launch {
        if (_uiState.value.type == LOADING) return@launch

        repository.fetchAndCacheCities(query).onStart {
            _uiState.value = Result(LOADING)
        }.collect {
            handleResult(query, it)
        }
    }

    private fun handleResult(query: Query, result: Result) {
        if (result.type == SUCCESS && (query.value is Int)) {
            currentPageNumber = query.value as Int
        }
        _uiState.value = result
    }

    fun getNextPageNumber() = currentPageNumber + 1
}