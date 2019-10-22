package com.app.bookingapp.presentation.vm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.bookingapp.domain.model.ApartmentData
import com.app.bookingapp.domain.model.BedRoomFilter
import com.app.bookingapp.domain.usecase.ApartmentList
import com.app.bookingapp.domain.usecase.GetApartmentListener
import com.app.bookingapp.domain.usecase.GetApartmentUC
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchApartmentVMTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var usecase: GetApartmentUC

    private lateinit var vm: SearchApartmentVM
    @Before
    fun setUp() {
        usecase = GetApartmentUC(object : ApartmentList {
            override fun getApartmentList(
                bedRoomFilter: BedRoomFilter,
                dateFrom: String,
                dateTo: String,
                listener: GetApartmentListener
            ) {
                listener.onSuccess(getDumpList().filter { it.bedrooms.toString() == bedRoomFilter.count || bedRoomFilter == BedRoomFilter.ALL }.toMutableList())
            }
        })
        vm = SearchApartmentVM(usecase)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun request_apartmentlist() {
        vm.requestListApartment()
        assertNotNull(vm.apartmentList.value?.size)
        assertTrue(vm.apartmentList.value!!.size == getDumpList().size)
    }

    @Test
    fun request_filter_bedroom() {
        vm.setBedroomFilter(BedRoomFilter.THREE.count)
        vm.requestListApartment()
        assertNotNull(vm.apartmentList.value?.size)
        assertNotNull(vm.apartmentList.value?.size == getDumpList().filter { it.bedrooms.toString() == BedRoomFilter.THREE.count }.toMutableList().size)
    }

    @Test
    fun clear_filters() {
        val d1 = "dateFrom"
        val d2 = "dateTo"
        vm.setBedroomFilter(BedRoomFilter.THREE.count)
        vm.dateFrom = d1
        vm.dateTo = d2
        vm.requestListApartment()
        assertEquals(vm.dateFrom, d1)
        assertEquals(vm.dateTo, d2)
        assertEquals(usecase.params.bedRoomFilter, BedRoomFilter.THREE)
        vm.clearFilters()
        vm.requestListApartment()
        assertEquals(vm.dateFrom, "")
        assertEquals(vm.dateTo, "")
        assertEquals(usecase.params.bedRoomFilter, BedRoomFilter.ALL)
    }

    private fun getDumpList() = mutableListOf(
        ApartmentData("1", 1, "1", 2.0, 2.0, 0f),
        ApartmentData("2", 1, "2", 3.0, 2.0, 0f),
        ApartmentData("3", 2, "3", 3.0, 2.0, 0f),
        ApartmentData("4", 3, "4", 3.0, 2.0, 0f),
        ApartmentData("5", 3, "5", 3.0, 2.0, 0f)
    )
}