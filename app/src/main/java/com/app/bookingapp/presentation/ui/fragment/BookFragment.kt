package com.app.bookingapp.presentation.ui.fragment


import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.app.bookingapp.R
import com.app.bookingapp.domain.model.ApartmentData
import com.app.bookingapp.domain.model.BookApartmentData
import com.app.bookingapp.presentation.Helper
import com.app.bookingapp.presentation.vm.BookApartmentViewModel
import kotlinx.android.synthetic.main.fragment_book.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class BookFragment : Fragment() {
    private val _vm: BookApartmentViewModel by lazy {
        ViewModelProviders.of(this).get(BookApartmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var apatmentData = arguments?.get("iApartmentData") as ApartmentData
        //Log.d(">Data-"," $apatmentData")

        if (apatmentData != null) {
            tvName.setText(apatmentData.name)
            tvID.setText(apatmentData.id)
            tvBedRooms.setText("Bedrooms: ${apatmentData.bedrooms}")
            tvLatitude.setText("Latitude: ${apatmentData.latitude}")
            tvLongitude.setText("Longitude: ${apatmentData.longitude}")
            tvDistance.setText("Distance: ${apatmentData.diatance}")
        }

        btnBookNow.isClickable = true

        ed_fromDate.setOnClickListener { selectDate(ed_fromDate, "FROM") }
        ed_toDate.setOnClickListener { selectDate(ed_toDate, "TO") }
        btnBookNow.setOnClickListener {

            if (ed_fromDate.text.toString().isNotEmpty() && ed_toDate.text.toString().isNotEmpty()) {
                var canBook = true

                var bookAparmentList = _vm.apartmentBookList.value
                if (bookAparmentList != null) {
                    for (j in 0 until bookAparmentList.size) {
                        if (bookAparmentList.get(j).id == apatmentData.id) {
                            canBook = false
                        }
                    }

                }

                if (canBook) {
                    bookApartment(apatmentData)
                } else {
                    Toast.makeText(context, "Can not book", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(context, "Booking Date required", Toast.LENGTH_SHORT).show()
            }
        }


    }


    fun bookApartment(data: ApartmentData) {


        val fromDate = Helper.getTimeMilesFromDate(ed_fromDate.text.toString())
        val toDate = Helper.getTimeMilesFromDate(ed_toDate.text.toString())

        var bookData = BookApartmentData(
            data.id!!, data.bedrooms!!, data.name!!,
            data.latitude!!, data.longitude!!, data.diatance!!,
            fromDate, toDate
        )


        btnBookNow.isClickable = false

        var cartList: ArrayList<BookApartmentData>

        if (_vm.apartmentBookList.value != null) {
            cartList =
                _vm.apartmentBookList.value as ArrayList<BookApartmentData>

        } else {
            cartList = ArrayList<BookApartmentData>()
        }

        cartList.add(bookData)

        _vm.apartmentBookList.value = cartList

        Toast.makeText(context, "Booking DONE", Toast.LENGTH_SHORT).show()

        view?.findNavController()?.navigate(R.id.action_bookFragment_to_homeFragment)
    }


    fun selectDate(editText: EditText, value: String) {


        val dateFormatterShow = SimpleDateFormat("dd-MM-yyyy", Locale.US)

        var fromTime: Long = 0
        var toTime: Long = 0

        val newCalendar = Calendar.getInstance()
        if (value == "FROM") {
            newCalendar.add(Calendar.DATE, 1)// after 1 DAY from today
            fromTime = newCalendar.getTimeInMillis()
        }
        if (value == "TO") {
            newCalendar.add(Calendar.DATE, 2)// after 2 DAY from today
            toTime = newCalendar.getTimeInMillis()
        }

        val datePickerDialog =
            context?.let {
                DatePickerDialog(
                    it,
                    R.style.datepicker,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        val newDate = Calendar.getInstance()
                        newDate.set(year, monthOfYear, dayOfMonth)
                        editText.setText(dateFormatterShow.format(newDate.time))


                    },
                    newCalendar.get(Calendar.YEAR),
                    newCalendar.get(Calendar.MONTH),
                    newCalendar.get(Calendar.DAY_OF_MONTH)
                )
            }

        if (value == "FROM")
            datePickerDialog?.datePicker?.minDate =
                fromTime//System.currentTimeMillis() + TimeUnit.DAYS.toMillis(180)
        else if (value == "TO")
            datePickerDialog?.datePicker?.minDate = toTime
        else {

        }

        datePickerDialog?.show()


    }


}
