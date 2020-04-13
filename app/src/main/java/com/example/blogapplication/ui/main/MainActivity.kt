package com.example.blogapplication.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import com.example.blogapplication.R
import com.example.blogapplication.base.BaseActivity
import com.example.blogapplication.ui.auth.AuthActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override val progressBar: ProgressBar
        get() = mainProgressBar
    override val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        subscribeObservers()
        logoutBtn.setOnClickListener {
            Log.e(TAG,"appToolbar: :")
            sessionManager.logout()
        }

    }

    override fun subscribeObservers() {
        Log.e(TAG,"subscribeObservers: :")
        sessionManager.cachedToken.observe(this, Observer { authToken->
            Log.e(TAG,"subscribeObservers: sessionManager: authToken:")
            if (authToken == null || authToken.accountPk ==-1 || authToken.token == null){
                Log.e(TAG,"subscribeObservers: sessionManager: authToken: user not authenticated")
                navigateToAuthActivity()
            }

        })
    }

    private fun navigateToAuthActivity() {
        startActivity(Intent(this,AuthActivity::class.java))
        finish()
    }
}
