package com.app.bookingapp.domain.usecase

import com.app.bookingapp.domain.entity.error.BaseError
import com.app.bookingapp.domain.model.BedRoomFilter

class GetApartmentUC(
    val repository: ApartmentList
) {
    lateinit var params: Params
    lateinit var listener: GetApartmentListener

    fun getListApartment() {
        try {
            repository.getApartmentList(
                params.bedRoomFilter,
                params.dateFrom,
                params.dateTo,
                listener
            )
        } catch (e: BaseError.ApartmentError) {
            listener.onError(e)
        }
    }
    class Params(val bedRoomFilter: BedRoomFilter, val dateFrom: String, val dateTo: String)
}