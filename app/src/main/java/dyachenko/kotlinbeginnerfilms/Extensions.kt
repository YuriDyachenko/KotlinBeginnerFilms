package dyachenko.kotlinbeginnerfilms

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.util.*

fun Calendar.getSeconds() = this.get(Calendar.SECOND)

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
    textId: Int,
    actionId: Int,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, textId, length)
        .setAction(actionId, action)
        .show()
}

fun View.showSnackBar(
    textId: Int,
    length: Int = Snackbar.LENGTH_SHORT
) {
    Snackbar.make(this, textId, length)
        .setBackgroundTint(Color.GREEN)
        .setTextColor(Color.BLACK)
        .show()
}