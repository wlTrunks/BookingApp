package com.app.bookingapp.presentation.ui

import android.os.IBinder
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.app.bookingapp.R
import com.app.bookingapp.isVisibleToUser
import com.app.bookingapp.presentation.ui.adapter.ApartmentListAdapter

import org.junit.Test
import org.junit.runner.RunWith
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.*
import java.util.concurrent.TimeUnit
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.rule.ActivityTestRule
import com.app.bookingapp.presentation.ui.CustomAssertions.Companion.hasItemCount
import org.junit.Rule
import java.util.concurrent.CountDownLatch
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.*
import android.widget.DatePicker
import androidx.test.espresso.contrib.PickerActions
import com.app.bookingapp.domain.model.BedRoomFilter
import com.app.bookingapp.presentation.Helper
import org.hamcrest.*
import java.util.*


@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    var mActivityRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun recyclerview_filled_with_item() {
        val latch = countDownLatch()
        latch.await(3, TimeUnit.SECONDS)
        onView(withId(R.id.recylerApartment))
            .check(matches(isVisibleToUser()))
            .perform(scrollToPosition<ApartmentListAdapter.MyViewHolder>(0))
        onView(withId(R.id.recylerApartment))
            .check(hasItemCount(10))
    }

    @Test
    fun filter_select_and_clear() {
        val latch = countDownLatch()
        latch.await(3, TimeUnit.SECONDS)
        onView(withId(R.id.spinner)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`(BedRoomFilter.ZERO.count))).perform(
            click()
        )
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString(BedRoomFilter.ZERO.count))))
        onView(withId(R.id.btnSearch)).perform(click())
        onView(withText("Select bedroom")).inRoot(ToastMatcher())
            .check(matches(withText("Select bedroom")))
        val from = CalendarHelper.setDate(1, R.id.ed_fromDate)
        onView(withId(R.id.ed_fromDate)).check(matches(withText(Helper.dateFormatterShow.format(from.time))))
        val to = CalendarHelper.setDate(3, R.id.ed_toDate)
        onView(withId(R.id.ed_toDate)).check(matches(withText(Helper.dateFormatterShow.format(to.time))))
        onView(withId(R.id.tvClearFilter)).perform(click())
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString(BedRoomFilter.ALL.count))))
        onView(withId(R.id.ed_fromDate)).check(matches(withText("")))
        onView(withId(R.id.ed_toDate)).check(matches(withText("")))
    }

    @Test
    fun search_done() {
        val latch = countDownLatch()
        latch.await(3, TimeUnit.SECONDS)
        onView(withId(R.id.spinner)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`(BedRoomFilter.ONE.count))).perform(
            click()
        )
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString(BedRoomFilter.ONE.count))))
        val from = CalendarHelper.setDate(1, R.id.ed_fromDate)
        onView(withId(R.id.ed_fromDate)).check(matches(withText(Helper.dateFormatterShow.format(from.time))))
        val to = CalendarHelper.setDate(3, R.id.ed_toDate)
        onView(withId(R.id.ed_toDate)).check(matches(withText(Helper.dateFormatterShow.format(to.time))))
        onView(withId(R.id.btnSearch)).perform(click())
        onView(withText("DONE")).inRoot(ToastMatcher())
            .check(matches(withText("DONE")))
    }

    @Test
    fun open_apartment() {
        val latch = countDownLatch()
        latch.await(3, TimeUnit.SECONDS)
        onView(withId(R.id.recylerApartment))
            .check(matches(isVisibleToUser()))
            .perform(scrollToPosition<ApartmentListAdapter.MyViewHolder>(0)).perform(click())
        onView(withId(R.id.imageView3))
            .check(matches(isVisibleToUser()))
    }

    private fun countDownLatch(): CountDownLatch {
        val latch = CountDownLatch(2)
        ActivityScenario.launch(MainActivity::class.java).onActivity { _ ->
            latch.countDown()
        }
        return latch
    }
}

object CalendarHelper {
    fun setDate(
        add: Int,
        viewId: Int,
        year: Int? = null,
        month: Int? = null,
        day: Int? = null
    ): Calendar {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, add)
        onView(withId(viewId)).perform(click())
        onView(withClassName(Matchers.equalTo(DatePicker::class.java.name))).perform(
            PickerActions.setDate(
                year ?: calendar.get(Calendar.YEAR),
                month ?: calendar.get(Calendar.MONTH) + 1,
                day ?: calendar.get(Calendar.DAY_OF_MONTH)
            )
        )
        onView(withId(android.R.id.button1)).perform(click())
        return calendar
    }
}

fun matchesDate(year: Int, month: Int, day: Int): Matcher<View> {
    return object : BoundedMatcher<View, DatePicker>(DatePicker::class.java!!) {

        override fun describeTo(description: Description) {
            description.appendText("matches date:")
        }

        override fun matchesSafely(item: DatePicker): Boolean {
            return year == item.getYear() && month == item.getMonth() && day == item.getDayOfMonth()
        }
    }
}

class ToastMatcher : TypeSafeMatcher<Root>() {
    override fun describeTo(description: Description?) {
        description?.appendText("is toast")
    }

    override fun matchesSafely(item: Root?): Boolean {
        val type = item!!.windowLayoutParams.get().type
        if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
            val windowToken: IBinder = item.decorView.windowToken
            val appToken: IBinder = item.decorView.applicationWindowToken
            if (windowToken == appToken) {
                return true
            }
        }
        return false
    }

}

class CustomMatchers {
    companion object {
        fun withItemCount(count: Int): Matcher<View> {
            return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {

                override fun describeTo(description: Description?) {
                    description?.appendText("RecyclerView with item count: $count")
                }

                override fun matchesSafely(item: RecyclerView?): Boolean {
                    return item?.adapter?.itemCount == count
                }
            }
        }
    }
}

class CustomAssertions {
    companion object {
        fun hasItemCount(count: Int): ViewAssertion {
            return RecyclerViewItemCountAssertion(
                count
            )
        }
    }

    private class RecyclerViewItemCountAssertion(private val count: Int) : ViewAssertion {

        override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            if (view !is RecyclerView) {
                throw IllegalStateException("The asserted view is not RecyclerView")
            }

            if (view.adapter == null) {
                throw IllegalStateException("No adapter is assigned to RecyclerView")
            }

            ViewMatchers.assertThat(
                "RecyclerView item count",
                view.adapter!!.itemCount,
                CoreMatchers.equalTo(count)
            )
        }
    }
}