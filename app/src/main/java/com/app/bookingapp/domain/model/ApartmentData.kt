package com.app.bookingapp.domain.model

import java.io.Serializable

data class ApartmentData(
    val id: String,
    val bedrooms: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    var diatance: Float = 0.0f
) : Serializable
