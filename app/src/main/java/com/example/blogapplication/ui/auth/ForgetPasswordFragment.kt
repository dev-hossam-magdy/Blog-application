package com.example.blogapplication.ui.auth

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController

import com.example.blogapplication.R
import com.example.blogapplication.ui.DataState
import com.example.blogapplication.ui.DataStateChangesListener
import com.example.blogapplication.ui.Response
import com.example.blogapplication.ui.ResponseType
import com.example.blogapplication.util.Constants
import kotlinx.android.synthetic.main.fragment_forget_password.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.ClassCastException


class ForgetPasswordFragment : BaseAuthFragment() {

    private val TAG = "ForgetPasswordFragment"
    lateinit var webView: WebView
    lateinit var stateChangeListener: DataStateChangesListener
    val webInterfaceCallback: WebAppInterface.OnWebInteractionCallback =
        object :WebAppInterface.OnWebInteractionCallback{
            override fun onSuccess(email: String) {
                Log.e(TAG,"onSuccess: the reset will sanded to this email $email")
                onPasswordResetLinkSend()

            }

            override fun onError(errorMessage: String) {
                Log.e(TAG,"onError:")
                val dataState = DataState.Error<Any>(
                    response = Response(errorMessage, ResponseType.Dialog())
                )
                stateChangeListener.onDataStateChange(dataState)

            }

            override fun onLoading(isLoading: Boolean) {
                Log.e(TAG,"onLoading:")
                GlobalScope.launch(Main) {
                    stateChangeListener.onDataStateChange(
                        dataState = DataState.Loading(isLoading,null)
                    )
                }

            }
        }

    private fun onPasswordResetLinkSend() {
        GlobalScope.launch(Main) {
            parent_view.removeView(webView)
            webView.destroy()
            val animation =TranslateAnimation(
                password_reset_done_container.width.toFloat(),
                0f,
                0f,
                0f
            )
            animation.duration = 500
            password_reset_done_container.startAnimation(animation)
            password_reset_done_container.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_forget_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView = view!!.findViewById(R.id.webview)
        return_to_launcher_fragment.setOnClickListener {
            findNavController().popBackStack()
        }
        loadWebView()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e(TAG, "the view model work ${viewModel.hashCode()}")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            stateChangeListener = context as DataStateChangesListener

        } catch (e: ClassCastException) {
            Log.e(TAG, "$context the context must implemet DataStateChangesListener")
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun loadWebView() {
        stateChangeListener.onDataStateChange(
            DataState.Loading(isLoading = true, cachedData = null)
        )

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                stateChangeListener.onDataStateChange(
                    DataState.Loading(isLoading = false, cachedData = null)
                )
            }
        }

        webView.loadUrl(Constants.PASSWORD_RESET_URL)
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(WebAppInterface(webInterfaceCallback),"AndroidTextListener")


    }

    class WebAppInterface constructor(private val callback: OnWebInteractionCallback) {
        private val TAG ="WebAppInterface"
        @JavascriptInterface
        fun onSuccess(email: String){
            callback.onSuccess(email)
        }
        @JavascriptInterface
        fun onError(errorMessage: String){
            callback.onError(errorMessage)
        }
        @JavascriptInterface
        fun onLoading(isLoading: Boolean){
            callback.onLoading(isLoading)
        }

        interface OnWebInteractionCallback {
            fun onSuccess(email: String)
            fun onError(errorMessage: String)
            fun onLoading(isLoading: Boolean)
        }
    }


}
