package  com.app.bookingapp

import android.view.View
import android.widget.ProgressBar
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.google.android.material.appbar.CollapsingToolbarLayout
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class IsVisibleToUserMatcher : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description?) {
        description?.appendText("is _actually_ displayed on the screen to the user")
    }

    override fun matchesSafely(item: View?): Boolean {
        return isDisplayed().matches(item)
                && item?.alpha ?: 0f > 0
                && item?.scaleX ?: 0f > 0
                && item?.scaleY ?: 0f > 0

    }
}

class IsLoadingMatcher : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description?) {
        description?.appendText("is loading")
    }

    override fun matchesSafely(item: View?): Boolean {
        return item is ProgressBar && item.visibility == View.VISIBLE

    }
}

class TitleMatcher(private val title: String?) : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description?) {
        description?.appendText("has title $title")
    }

    override fun matchesSafely(item: View?): Boolean {
        return item is CollapsingToolbarLayout && item.title == title
    }
}

fun isVisibleToUser() = IsVisibleToUserMatcher()
fun isLoading() = IsLoadingMatcher()
fun hasTitle(title: String?) = TitleMatcher(title)