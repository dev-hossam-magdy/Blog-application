package com.example.blogapplication.base.auth

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.blogapplication.ViewModelProviderFactory
import com.example.blogapplication.ui.auth.AuthViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseAuthFragment : DaggerFragment() {

    @Inject
    lateinit var factory: ViewModelProviderFactory

    lateinit var viewModel: AuthViewModel
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this,factory).get(AuthViewModel::class.java)
        }?: throw Exception("the activity not created")
    }
}