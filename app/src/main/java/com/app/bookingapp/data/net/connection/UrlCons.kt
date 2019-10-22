package com.app.bookingapp.data.net.connection

object UrlCons {

    private var baseURL = "https://s3-eu-west-1.amazonaws.com/product.versioning.com/"


    private var apiGetApartments = "apartments.json"
    var GetApartments = baseURL + apiGetApartments
    var tagApartments = "Apartments"

}