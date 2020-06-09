package com.example.blogapplication.base

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.blogapplication.R
import com.example.blogapplication.session.SessionManager
import com.example.blogapplication.ui.*
import com.example.blogapplication.util.Constants
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity(), DataStateChangesListener,
    UICommunicationListener, RunTimePermissionListener {

    abstract val TAG: String
    abstract val progressBar: ProgressBar

    @Inject
    lateinit var sessionManager: SessionManager


    abstract fun subscribeObservers()


    override fun onDataStateChange(dataState: DataState<*>) {
        dataState.let {
            GlobalScope.launch(Main) {
                displayProgressBar(it.isLoading)
                it.data?.let { data -> handelStateData(it.response, false) }
                    ?: handelStateData(it.response, true)
            }
        }
    }

    private fun handelStateData(response: Event<Response>?, isErrorEvent: Boolean) {
        response?.getContentIfNotHandled()?.let { response ->
            displayProgressBar(false)

            when (response.responseType) {
                is ResponseType.Dialog -> {
                    Log.e(TAG, "handelStateError: ResponseType.Dialog")
                    response.message?.let { msg ->
                        displayDialogMessage(R.string.text_success, msg)
                    }

                }
                is ResponseType.Toast -> {
                    Log.e(TAG, "handelStateError: ResponseType.Toast:")
                    response.message?.let { msg ->
                        displayToastMessage(msg)

                    }

                }
                is ResponseType.None -> {
                    Log.e(TAG, "handelStateError: ResponseType.None:")


                }
            }

        }
    }


    private fun displayProgressBar(isLoading: Boolean) {
        this.progressBar.visibility =
            if (isLoading)
                View.VISIBLE
            else
                View.GONE
    }

    override fun hideKeyboard() {
        currentFocus?.let {
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    override fun onUIMessageReceived(uiMessage: UIMessage) {
        when (uiMessage.uiMessageType) {
            is UIMessageType.AreYouSureDialog ->
                aryYouSureDialog(uiMessage.message, uiMessage.uiMessageType.callback)
            is UIMessageType.Dialog ->
                displayInfoDialog(uiMessage.message)
            is UIMessageType.Toast ->
                displayToastMessage(uiMessage.message)
            is UIMessageType.None ->
                Log.e(TAG, "UIMessageType.None : ${uiMessage.message}")
        }

    }

    override fun isStoragePermissionGranted(): Boolean {
        if (
            (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
            &&
            (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)


        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                Constants.PERMISSION_REQUEST_READ_STORAGE
            )

            return false
        } else
            return true

    }
}