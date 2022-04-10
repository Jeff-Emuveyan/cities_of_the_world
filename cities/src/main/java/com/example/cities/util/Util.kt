package com.example.cities.util

import android.content.Context
import android.location.Geocoder
import java.lang.IllegalArgumentException
import java.util.*

fun getAddress(context: Context, lat: Double, lon: Double): String? = try {
    val geoCoder = Geocoder(context, Locale.getDefault())
    val resultCount = 1
    val address = geoCoder.getFromLocation(lat, lon, resultCount)
    "${address.firstOrNull()?.countryName}," +
            " ${address.firstOrNull()?.locality}"
} catch (e: IllegalArgumentException) {
    "N/A"
}