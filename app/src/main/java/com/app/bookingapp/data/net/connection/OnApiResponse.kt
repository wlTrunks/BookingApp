package com.app.bookingapp.data.net.connection

import org.json.JSONException



interface OnApiResponse {

    @Throws(JSONException::class)
    fun onSuccess(result: String, type: String)

    fun onFailure(responseCode: Int, responseMessage: String)

}