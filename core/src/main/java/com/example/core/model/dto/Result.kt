package com.example.core.model.dto

import com.example.core.model.dto.ui.UIStateType
import com.example.core.model.entity.CityEntity

class Result(val type: UIStateType, val cities: List<CityEntity>? = null)