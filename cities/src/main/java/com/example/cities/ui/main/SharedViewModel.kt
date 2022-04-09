package com.example.cities.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cities.data.repository.CityRepository
import com.example.core.model.dto.ui.Result
import com.example.core.model.dto.ui.UIStateType.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class SharedViewModel @Inject constructor(private val repository: CityRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(Result(DEFAULT))
    val uiState = _uiState.asStateFlow()

    fun getCitiesByPageNumber(pageNumber: Int = 1) = viewModelScope.launch {
        if (_uiState.value.type == LOADING) return@launch
        repository.getAndCacheCitiesByPageNumber(pageNumber).onStart {
            _uiState.value = Result(LOADING)
        }.collect {
            _uiState.value = it
        }
    }
}