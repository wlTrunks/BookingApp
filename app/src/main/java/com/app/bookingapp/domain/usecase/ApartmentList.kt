package com.app.bookingapp.domain.usecase

import com.app.bookingapp.domain.model.BedRoomFilter
import com.app.bookingapp.domain.usecase.GetApartmentListener


interface ApartmentList {
    fun getApartmentList(bedRoomFilter: BedRoomFilter, dateFrom: String, dateTo: String, listener: GetApartmentListener)
}