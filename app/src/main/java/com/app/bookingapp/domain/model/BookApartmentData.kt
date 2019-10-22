package com.app.bookingapp.domain.model

import java.io.Serializable

data class BookApartmentData(
    val id: String,
    val bedrooms: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val diatance: Float,
    val fromDate: Long,
    val toDate: Long
) : Serializable
