package com.example.blogapplication.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.blogapplication.R
import com.example.blogapplication.base.auth.BaseAuthFragment


class ForgetPasswordFragment : BaseAuthFragment() {

    private val TAG = "ForgetPasswordFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forget_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e(TAG,"the view model work ${viewModel.hashCode()}")
    }

}
