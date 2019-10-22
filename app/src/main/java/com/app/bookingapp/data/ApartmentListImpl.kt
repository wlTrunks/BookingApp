package com.app.bookingapp.data

import android.util.Log
import com.android.volley.*
import com.app.bookingapp.data.net.connection.ErrorCode
import com.app.bookingapp.domain.usecase.ApartmentList
import com.app.bookingapp.domain.entity.error.BaseError
import com.app.bookingapp.domain.model.ApartmentData
import com.app.bookingapp.domain.model.BedRoomFilter
import com.app.bookingapp.domain.usecase.GetApartmentListener
import org.json.JSONArray
import java.lang.Exception

class ApartmentListImpl(val requestQueue: RequestQueue) :
    ApartmentList {


    override fun getApartmentList(
        bedRoomFilter: BedRoomFilter,
        dateFrom: String,
        dateTo: String,
        listener: GetApartmentListener
    ) {
        val request = ApartmentRequest(dateFrom, dateTo)
        val volleyReqest = request.getRequest(
            Response.Listener {
                try {
                    Log.d("@R>", "Response: ${java.lang.String.format(it.toString())}")
                    val result = mutableListOf<ApartmentData>()
                    val valueArray = JSONArray(it)
                    for (i in 0 until valueArray.length()) {
                        with(valueArray.optJSONObject(i)) {
                            val apartmentData = ApartmentData(
                                optString("id"),
                                optInt("bedrooms"),
                                optString("name"),
                                optDouble("latitude"),
                                optDouble("longitude")
                            )
                            result.add(apartmentData)
                        }
                    }
                    listener.onSuccess(result.filter { it.bedrooms.toString() == bedRoomFilter.count || bedRoomFilter == BedRoomFilter.ALL }.toMutableList())
                } catch (e: Exception) {
                    listener.onError(
                        BaseError.ApartmentError(
                            ErrorCode.EXCEPTION_ERR,
                            ErrorCode.EXCEPTION_MSG
                        )
                    )
                }
            },
            Response.ErrorListener {
                listener.onError(BaseError.ApartmentError(ErrorCode.VOLLY_ERR, "${it.message}"))
            }
        )
        requestQueue.add(volleyReqest)
    }
}