package com.example.blogapplication.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.blogapplication.R
import com.example.blogapplication.base.auth.BaseAuthFragment
import com.example.blogapplication.ui.auth.state.LoginFields
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : BaseAuthFragment() {
    private val TAG = "LoginFragment"
//    lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container, false)
//        val view = binding.root
        return inflater.inflate(R.layout.fragment_login,container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e(TAG, "the view model work ${viewModel.hashCode()}")
//       binding.vm = viewModel
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer {authViewState->
            authViewState.loginFields?.let { fields->
                fields.loginEmail?.let { inputEmail.setText(it) }
                fields.loginPassword?.let { inputPassword.setText(it) }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setLoginFields(
            LoginFields(inputEmail.text.toString(),inputPassword.text.toString())
        )
    }

}
