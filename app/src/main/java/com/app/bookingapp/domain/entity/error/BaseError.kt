package com.app.bookingapp.domain.entity.error

sealed class BaseError(val code: Int, message: String = "") : Throwable(message) {
    class ApartmentError(code: Int, message: String) : BaseError(code, message)
}