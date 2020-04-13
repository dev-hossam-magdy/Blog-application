package com.example.blogapplication.ui

import android.content.Context
import android.icu.text.CaseMap
import android.widget.Toast
import androidx.annotation.StringRes
import com.afollestad.materialdialogs.MaterialDialog
import com.example.blogapplication.R
import com.google.android.material.dialog.MaterialDialogs

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
