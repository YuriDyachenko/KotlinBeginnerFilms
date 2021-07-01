package dyachenko.kotlinbeginnerfilms

import android.os.Build
import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.io.BufferedReader
import java.util.stream.Collectors

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
    length: Int = Snackbar.LENGTH_LONG
) {
    Snackbar.make(this, textId, length)
        .show()
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
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length)
        .setAction(actionText, action)
        .show()
}

fun BufferedReader.getLines(): String {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        val rawData = StringBuilder(1024)
        var line: String?

        while (this.readLine().also { line = it } != null) {
            rawData.append(line).append("\n")
        }
        this.close()
        rawData.toString()
    } else {
        this.lines().collect(Collectors.joining("\n"))
    }
}