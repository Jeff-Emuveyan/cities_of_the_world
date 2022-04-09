package com.example.core.model.dto

data class Pagination(
    val perPage: Int? = null,
    val total: Int? = null,
    val lastPage: Int? = null,
    val currentPage: Int? = null
)