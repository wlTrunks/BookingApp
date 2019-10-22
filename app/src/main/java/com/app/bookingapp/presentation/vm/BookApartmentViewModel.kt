package com.app.bookingapp.presentation.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.bookingapp.data.ApartmentListImpl
import com.app.bookingapp.domain.entity.error.BaseError
import com.app.bookingapp.domain.model.ApartmentData
import com.app.bookingapp.domain.model.BedRoomFilter
import com.app.bookingapp.domain.model.BookApartmentData
import com.app.bookingapp.domain.usecase.GetApartmentListener
import com.app.bookingapp.domain.usecase.GetApartmentUC
import com.app.bookingapp.presentation.base.PBLoaderState

class BookApartmentViewModel : ViewModel() {

    val apartmentBookList: MutableLiveData<ArrayList<BookApartmentData>> by lazy {
        MutableLiveData<ArrayList<BookApartmentData>>()
    }
}