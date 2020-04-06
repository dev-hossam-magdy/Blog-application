package com.example.blogapplication.ui.auth

import android.os.Bundle
import android.util.Log
import com.example.blogapplication.R
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class AuthActivity : DaggerAppCompatActivity() {

    private val TAG ="AuthActivity"
    @Inject
    lateinit var string: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        Log.e(TAG,"dagger work... $string")
    }
}
