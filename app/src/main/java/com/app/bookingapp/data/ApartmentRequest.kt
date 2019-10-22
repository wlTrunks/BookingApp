package com.app.bookingapp.data

import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.app.bookingapp.data.net.connection.UrlCons

sealed class AppRequest(val url: String, val method: Int, val tagRequest: String) {

    private val _token: String

    init {
        _token = ""
    }

    abstract fun getRequest(
        listener: Response.Listener<String>,
        errorListener: Response.ErrorListener
    ): StringRequest
}

class ApartmentRequest(private val dateFrom: String, private val dateTo: String) :
    AppRequest(UrlCons.GetApartments, Request.Method.GET, UrlCons.tagApartments) {
    override fun getRequest(
        listener: Response.Listener<String>,
        errorListener: Response.ErrorListener
    ): StringRequest =
        object : StringRequest(
            method,
            url,
            listener, errorListener
        ) {
            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }
        }.apply {
            retryPolicy = DefaultRetryPolicy(
                60 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            tag = tagRequest
        }


}