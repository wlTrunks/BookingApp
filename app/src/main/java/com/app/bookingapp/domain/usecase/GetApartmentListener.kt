package com.app.bookingapp.domain.usecase

import com.app.bookingapp.domain.entity.error.BaseError
import com.app.bookingapp.domain.model.ApartmentData

interface GetApartmentListener {
    fun onSuccess(list: MutableList<ApartmentData>)
    fun onError(error: BaseError.ApartmentError)
}