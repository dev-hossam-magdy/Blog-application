package com.example.blogapplication.ui.main.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.example.blogapplication.R
import com.example.blogapplication.ui.main.account.state.AccountStateEvent
import com.example.blogapplication.util.SuccessHandling
import kotlinx.android.synthetic.main.fragment_change_password.*

/**
 * A simple [Fragment] subclass.
 */
class ChangePasswordFragment : BaseAccountFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonsAction()
        subscribeObserver()

    }

    private fun subscribeObserver() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            stateChangeListener.onDataStateChange(dataState)

            dataState.response?.let { event ->
                event.peekContent().message?.let {
                    if (it.equals(SuccessHandling.RESPONSE_PASSWORD_UPDATE_SUCCESS)) {
                        findNavController().popBackStack()
                        stateChangeListener.hideKeyboard()
                    }

                }

            }

        })
    }

    private fun setButtonsAction() {
        update_password_button.setOnClickListener {
            viewModel.setStateEvent(
                AccountStateEvent.ChangePasswordEvent(
                    currentPassword = inputCurrentPassword.text.toString(),
                    confirmNewPassword = inputConfirmNewPassword.text.toString(),
                    newPassword = inputNewPassword.text.toString()
                )
            )
        }
    }

}
