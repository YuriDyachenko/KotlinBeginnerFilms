package dyachenko.kotlinbeginnerfilms

import android.content.pm.PackageManager
import android.view.Menu
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import dyachenko.kotlinbeginnerfilms.view.error.ErrorFragment
import java.io.PrintWriter
import java.io.StringWriter
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

fun Menu.hideAllItems() {
    for (i: Int in 0 until this.size()) {
        this.getItem(i).let {
            if (it.isVisible) {
                it.isVisible = false
            }
        }
    }
}

fun Throwable.printStackTraceToString(): String {
    val stringWriter = StringWriter()
    this.printStackTrace(PrintWriter(stringWriter))
    return stringWriter.toString()
}

fun FragmentManager.addFragmentWithBackStack(fragment: Fragment) = this.apply {
    beginTransaction()
        .add(R.id.container, fragment)
        .addToBackStack(null)
        .commit()
}

fun FragmentActivity.showFragment(fragment: Fragment) {
    this.supportFragmentManager.addFragmentWithBackStack(fragment)
}

fun FragmentActivity.showError(e: Throwable) {
    this.showFragment(ErrorFragment.newInstance(e.printStackTraceToString()))
}

fun Fragment.registerPermissionLauncher(action: () -> Unit, checkAction: () -> Unit, view: View) =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            action()
        } else {
            view.showSnackBar(getString(R.string.permission_error_msg),
                getString(R.string.permission_reload_msg),
                { checkAction() })
        }
    }

fun Fragment.checkPermission(
    request: String,
    action: () -> Unit,
    launcher: ActivityResultLauncher<String>
) {
    context?.let {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(it, request) -> {
                action()
            }
            else -> {
                launcher.launch(request)
            }
        }
    }
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
