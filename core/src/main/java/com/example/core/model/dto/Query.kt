package com.example.core.model.dto

class Query (val queryType: QueryType, var value: Any)

enum class QueryType {
    PAGE_NUMBER, CITY_NAME
}