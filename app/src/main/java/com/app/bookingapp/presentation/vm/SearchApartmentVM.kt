package com.app.bookingapp.presentation.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.bookingapp.domain.entity.error.BaseError
import com.app.bookingapp.domain.model.ApartmentData
import com.app.bookingapp.domain.model.BedRoomFilter
import com.app.bookingapp.domain.usecase.GetApartmentListener
import com.app.bookingapp.domain.usecase.GetApartmentUC
import com.app.bookingapp.presentation.base.PBLoaderState

class SearchApartmentVM(private val usecase: GetApartmentUC) : ViewModel(), GetApartmentListener {

    init {
        usecase.listener = SearchApartmentVM@ this
    }

    private var _isFromSearch = false
    private var _bedroomFilter: BedRoomFilter = BedRoomFilter.ALL

    var dateFrom: String = ""
    var dateTo: String = ""

    val apartmentList: MutableLiveData<MutableList<ApartmentData>> by lazy {
        MutableLiveData<MutableList<ApartmentData>>()
    }

    val pgLoaderLD: MutableLiveData<PBLoaderState> = MutableLiveData()

    val errorLD: MutableLiveData<BaseError.ApartmentError> = MutableLiveData()

    /**
     * Check search filters error
     */
    val checkFilters: MutableLiveData<String> = MutableLiveData()

    override fun onSuccess(list: MutableList<ApartmentData>) {
        pgLoaderLD.postValue(PBLoaderState.END)
        apartmentList.postValue(list)
    }

    override fun onError(error: BaseError.ApartmentError) {
        pgLoaderLD.postValue(PBLoaderState.END)
        errorLD.postValue(error)
    }

    fun requestListApartment() {
        pgLoaderLD.postValue(PBLoaderState.START)
        usecase.params = GetApartmentUC.Params(_bedroomFilter, dateFrom, dateTo)
        usecase.getListApartment()
    }

    fun setBedroomFilter(value: String) {
        _bedroomFilter = when (value) {
            BedRoomFilter.ZERO.count -> BedRoomFilter.ZERO
            BedRoomFilter.ONE.count -> BedRoomFilter.ONE
            BedRoomFilter.TWO.count -> BedRoomFilter.TWO
            BedRoomFilter.THREE.count -> BedRoomFilter.THREE
            BedRoomFilter.ALL.count -> BedRoomFilter.ALL
            else -> BedRoomFilter.ZERO
        }
    }

    fun clearFilters() {
        _bedroomFilter = BedRoomFilter.ALL
        dateFrom = ""
        dateTo = ""
    }

    fun doSearch() {
        if (_bedroomFilter == BedRoomFilter.ZERO) {
            checkFilters.value = "Select bedroom"
        } else {
            if (dateFrom.isEmpty()) {
                checkFilters.value = "Select From Date"
            } else {
                if (dateTo.isEmpty()) {
                    checkFilters.value = "Select To Date"
                } else {
                    // Do Search
                    checkFilters.value = "DONE"
                    _isFromSearch = true
                    requestListApartment()
                }
            }
        }
    }
}