package com.example.blogapplication.ui

import android.app.Activity
import android.content.Context
import android.icu.text.CaseMap
import android.widget.Toast
import androidx.annotation.StringRes
import com.afollestad.materialdialogs.MaterialDialog
import com.example.blogapplication.R
import com.google.android.material.dialog.MaterialDialogs
import java.text.SimpleDateFormat
import java.util.*

fun Activity.displayDialogMessage(@StringRes titleResurse: Int, msg: String) {
    MaterialDialog(this)
        .show {
            title(titleResurse)
            message(text = msg)
            positiveButton(R.string.text_ok)
        }

}

fun Activity.displayToastMessage(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun Activity.displayInfoDialog(message: String) {
    MaterialDialog(this)
        .show {
            title(R.string.text_info)
            message(text = message)
            positiveButton(R.string.text_ok)
        }

}

fun Activity.aryYouSureDialog(message: String, callback: AreYouSureCallback){
    MaterialDialog(this).show {
        title(R.string.are_you_sure)
        message(text = message)
        negativeButton(R.string.text_cancel) {
            callback.cancel()
        }
        positiveButton(R.string.text_yes) {
            callback.proceed()
        }

    }
}

interface AreYouSureCallback{
    fun proceed()
    fun cancel()
}

fun Long.convertLongToStringDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    try {
        val date = sdf.format(Date(this))
        return date
    } catch (e: Exception) {
        throw Exception(e)
    }
}


fun String.convertServerStringDateToLong(): Long {
    var stringDate = this.removeRange(this.indexOf("T") until this.length)
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    try {
        val time = sdf.parse(stringDate).time
        return time
    } catch (e: Exception) {
        throw Exception(e)
    }
}