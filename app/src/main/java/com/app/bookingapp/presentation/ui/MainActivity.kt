package com.app.bookingapp.presentation.ui

import android.os.Bundle
import android.util.Log
import com.app.bookingapp.presentation.base.BaseActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.app.bookingapp.R
import com.app.bookingapp.data.ApartmentListImpl
import com.app.bookingapp.data.net.VolleySingleton
import com.app.bookingapp.domain.entity.error.BaseError
import com.app.bookingapp.domain.model.BookApartmentData
import com.app.bookingapp.domain.usecase.GetApartmentUC
import com.app.bookingapp.presentation.base.PBLoaderState
import com.app.bookingapp.presentation.vm.SearchApartmentVM
import com.app.bookingapp.presentation.receiver.ErrorCodeHandler
import kotlin.collections.ArrayList

class MainActivity : BaseActivity() {

    companion object {
        var PERMISSION_REQUEST_LOCATION = 111
    }

    private val _parentVM: SearchApartmentVM by lazy {
        ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SearchApartmentVM(
                    GetApartmentUC(
                        ApartmentListImpl(
                            VolleySingleton.getInstance(
                                applicationContext
                            ).provideRequestQueue()
                        )
                    )
                ) as T
            }
        }).get(SearchApartmentVM::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _parentVM.pgLoaderLD.observe(this, Observer<PBLoaderState> { state ->
            when (state) {
                PBLoaderState.START -> showCustomProgrssDialog()
                PBLoaderState.END -> hideCustomProgrssDialog()
            }
        })

        _parentVM.errorLD.observe(this, Observer<BaseError.ApartmentError> { error ->
            ErrorCodeHandler.showErrorDialog(this, this, error.code, error.message ?: "")
        })
    }
}
