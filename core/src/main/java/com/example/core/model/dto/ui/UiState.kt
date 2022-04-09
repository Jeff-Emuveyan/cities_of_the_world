package com.example.core.model.dto.ui

import com.example.core.model.entity.CityEntity
import java.lang.Exception

class Result(val type: UIStateType, val cities: List<CityEntity>? = null)

enum class UIStateType{
    DEFAULT, NETWORK_ERROR, SUCCESS, NO_RESULT, LOADING
}