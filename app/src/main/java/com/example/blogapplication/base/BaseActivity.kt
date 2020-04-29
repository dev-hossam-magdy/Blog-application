package com.example.blogapplication.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.input.InputManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.navigation.NavController
import com.example.blogapplication.R
import com.example.blogapplication.session.SessionManager
import com.example.blogapplication.ui.*
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity(), DataStateChangesListener {

    abstract val TAG: String
    abstract val progressBar:ProgressBar

    @Inject
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

    }

    abstract fun subscribeObservers()


    override fun onDataStateChange(dataState: DataState<*>) {
        dataState?.let {
            GlobalScope.launch(Main) {
                displayProgessBar(it.isLoading)
                it.data?.let { data -> handelStateData(it.response, false) }
                    ?: handelStateData(it.response, true)
            }
        }
    }

    private fun handelStateData(response: Event<Response>?, isErrorEvent: Boolean) {
        Log.e(TAG, "handelStateError: ${response?.getContentIfNotHandled()?.message}")
        response?.getContentIfNotHandled()?.let { response ->
            displayProgessBar(false)

            when (response.responseType) {
                is ResponseType.Dialog -> {
                    Log.e(TAG, "handelStateError: ResponseType.Dialog:")
                    response.message?.let { msg ->
                        if (isErrorEvent)
                            displayDialogMessage(R.string.text_error, msg)
                        else
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


    fun displayProgessBar(isLoading: Boolean) {
        this.progressBar.visibility =
            if (isLoading)
                View.VISIBLE
            else
                View.GONE
    }

    override fun hideKeyboard() {
        currentFocus?.let {
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(it.windowToken,0)
        }
    }
}