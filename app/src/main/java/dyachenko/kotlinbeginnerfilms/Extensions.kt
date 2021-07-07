package dyachenko.kotlinbeginnerfilms

import android.view.Menu
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

const val DATE_TIME_FORMAT = "dd.MMM.yy HH:mm"

fun Date.format(): String = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
    .format(this)

fun String.parseDate(): Date = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
    .parse(this) ?: Date()

fun Menu.hideItem(id: Int) {
    val item = findItem(id)
    item?.let {
        if (it.isVisible) {
            it.isVisible = false
        }
    }
}

fun Menu.hideItems(vararg ids: Int) {
    for (id in ids) {
        hideItem(id)
    }
}

fun FragmentManager.addFragmentWithBackStack(fragment: Fragment) = this.apply {
    beginTransaction()
        .add(R.id.container, fragment)
        .addToBackStack(null)
        .commit()
}

fun View.show() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}

fun View.hide() {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}

fun View.showSnackBar(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length)
        .setAction(actionText, action)
        .show()
}
