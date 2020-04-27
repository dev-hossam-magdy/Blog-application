package com.example.blogapplication.ui

import android.content.Context
import android.icu.text.CaseMap
import android.widget.Toast
import androidx.annotation.StringRes
import com.afollestad.materialdialogs.MaterialDialog
import com.example.blogapplication.R
import com.google.android.material.dialog.MaterialDialogs
import java.text.SimpleDateFormat
import java.util.*

fun Context.displayDialogMessage(@StringRes titleResurse:Int,msg: String) {
    MaterialDialog(this)
        .show {
            title(titleResurse)
            message(text = msg)
            positiveButton(R.string.text_ok)
        }

}

fun Context.displayToastMessage( msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}


fun Long.convertLongToStringDate(): String{
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    try {
        val date = sdf.format(Date(this))
        return date
    } catch (e: Exception) {
        throw Exception(e)
    }
}


fun String.convertServerStringDateToLong(): Long{
    var stringDate = this.removeRange(this.indexOf("T") until this.length)
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    try {
        val time = sdf.parse(stringDate).time
        return time
    } catch (e: Exception) {
        throw Exception(e)
    }
}