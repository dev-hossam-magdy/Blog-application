package com.example.blogapplication.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.blogapplication.R
import com.example.blogapplication.base.auth.BaseAuthFragment
import com.example.blogapplication.ui.auth.state.RegistarationFields
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : BaseAuthFragment() {
    private val TAG = "RegisterFragment"

//    lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_register, container, false)
//        val view = binding
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e(TAG, "the view model work ${viewModel.hashCode()}")
        subscribeObservers()
    }

    private fun subscribeObservers() {

        viewModel.viewState.observe(viewLifecycleOwner, Observer { authViewState ->

            authViewState.registrationFields?.let { fields ->

                fields.registrationConfirmPassword?.let { inputPasswordConfirm.setText(it) }
                fields.registrationEmail?.let { registerInputEmail.setText(it) }
                fields.registrationPassword?.let { inputPassword.setText(it) }
                fields.registrationUsername?.let { inputUsername.setText(it) }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setRegistrationFields(
            RegistarationFields(
                registrationEmail = registerInputEmail.text.toString(),
                registrationPassword = inputPassword.text.toString(),
                registrationConfirmPassword = inputPasswordConfirm.text.toString(),
                registrationUsername = inputUsername.text.toString()
            )
        )
    }

}
