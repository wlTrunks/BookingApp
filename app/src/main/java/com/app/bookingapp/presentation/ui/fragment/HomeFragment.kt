package com.app.bookingapp.presentation.ui.fragment

import androidx.lifecycle.Observer
import android.app.DatePickerDialog
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.app.bookingapp.R
import com.app.bookingapp.presentation.ui.adapter.ApartmentListAdapter
import com.app.bookingapp.presentation.base.BaseActivity
import com.app.bookingapp.domain.model.ApartmentData
import com.app.bookingapp.presentation.Helper
import com.app.bookingapp.presentation.vm.SearchApartmentVM
import com.app.bookingapp.presentation.receiver.ConnectivityReceiver
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private var listApartments = ArrayList<ApartmentData>()
    private lateinit var adapter: ApartmentListAdapter
    val vm: SearchApartmentVM by lazy {
        ViewModelProviders.of(activity!!).get(SearchApartmentVM::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm.checkFilters.observe(this, Observer<String> { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        })
        vm.apartmentList.observe(this, Observer<MutableList<ApartmentData>> { list ->
            proceedListApartment(list)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter(view)

        resetDateFilters()

        vm.requestListApartment()

        setListeners()
    }

    private fun setAdapter(view: View) {
        adapter = ApartmentListAdapter(listApartments) {
            view?.findNavController()?.navigate(R.id.action_homeFragment_to_bookFragment, it)
        }
        recylerApartment.adapter = adapter
    }

    private fun setListeners() {
        ed_fromDate.setOnClickListener { selectDate(ed_fromDate, DATE_FROM) }
        ed_toDate.setOnClickListener { selectDate(ed_toDate, DATE_TO) }
        tvClearFilter.setOnClickListener {
            clearFilters()
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>, p1: View?, p2: Int, p3: Long) {
                vm.setBedroomFilter(p0.getItemAtPosition(p2).toString())
            }
        }
        btnSearch.setOnClickListener { search() }
    }

    private fun clearFilters() {
        recylerApartment.scrollToPosition(0)
        spinner.setSelection(0, false)
        resetDateFilters()
        vm.clearFilters()
    }

    private fun resetDateFilters() {
        ed_fromDate.hint = Helper.dateFormatterShow.format(Calendar.getInstance().timeInMillis)
        ed_toDate.hint = Helper.dateFormatterShow.format(Calendar.getInstance().timeInMillis)
        ed_fromDate.setText("")
        ed_toDate.setText("")
    }

    private fun proceedListApartment(list: MutableList<ApartmentData>) {
        listApartments.clear()
        if (list.size == 0) {
            Toast.makeText(context, "No apartment found", Toast.LENGTH_SHORT).show()
        } else {
            listApartments.addAll(list)
            listApartments.forEach { it.diatance = getDistance(it.latitude, it.longitude) }
            listApartments.sortedWith(Comparator { one, other -> one?.diatance?.compareTo(other?.diatance!!)!! })
        }
        adapter.notifyDataSetChanged()
    }


    private fun search() {
        if (ConnectivityReceiver.isInternet(context!!)) {
            vm.doSearch()
        } else {
            (activity as BaseActivity).noInternetConnection()
        }
    }

    private fun getDistance(latitude: Double, longitude: Double): Float {

        val fromLatLng = Location("FromLocation")
        fromLatLng.latitude = 59.329440
        fromLatLng.longitude = 18.069124

        val toLocation = Location("ToLocation")
        toLocation.latitude = latitude
        toLocation.longitude = longitude

        return fromLatLng.distanceTo(toLocation)
    }

    fun selectDate(editText: EditText, value: String) {


        var fromTime: Long = 0
        var toTime: Long = 0

        val newCalendar = Calendar.getInstance()
        if (value == DATE_FROM) {
            newCalendar.add(Calendar.DATE, 1)// after 1 DAY from today
            fromTime = newCalendar.getTimeInMillis()
        }
        if (value == DATE_TO) {
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
                        val date = Helper.dateFormatterShow.format(newDate.time)
                        editText.setText(date)
                        if (value == DATE_FROM) vm.dateFrom = date
                        if (value == DATE_TO) vm.dateTo = date
                    },
                    newCalendar.get(Calendar.YEAR),
                    newCalendar.get(Calendar.MONTH),
                    newCalendar.get(Calendar.DAY_OF_MONTH)
                )
            }

        if (value == DATE_FROM)
            datePickerDialog?.datePicker?.minDate =
                fromTime//System.currentTimeMillis() + TimeUnit.DAYS.toMillis(180)
        else if (value == DATE_TO)
            datePickerDialog?.datePicker?.minDate = toTime
        else {

        }
        datePickerDialog?.show()
    }

    companion object {
        private const val DATE_FROM = "FROM"
        private const val DATE_TO = "TO"
    }
}
